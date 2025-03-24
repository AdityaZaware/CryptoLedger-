package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.PaymentOrder;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.PaymentMethod;
import com.ben.StockTrading.enums.PaymentOrderStatus;
import com.ben.StockTrading.repo.PaymentOrderRepo;
import com.ben.StockTrading.response.PaymentResponse;
import com.ben.StockTrading.service.PaymentService;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepo paymentOrderRepo;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value(("${razorpay.api.key}"))
    private String apiKey;

    @Value(("${razorpay.api.secret}"))
    private String apiSecretKey;

    public PaymentServiceImpl(PaymentOrderRepo paymentOrderRepo) {
        this.paymentOrderRepo = paymentOrderRepo;
    }

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();

        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepo.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception {
        return paymentOrderRepo.findById(paymentOrderId)
                .orElseThrow(() -> new Exception("Payment order not found"));
    }

    @Override
    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {

        if(paymentOrder.getStatus() == null) {
            paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        }

        if(paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if(paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {

                RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecretKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);

                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepo.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepo.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {

        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecretKey);

            // Convert amount to paise (Razorpay requires amount in paise)
            Long amountInPaise = amount * 100;

            // ✅ Create payment link request with only required fields
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", amountInPaise);
            paymentLinkRequest.put("currency", "INR");
            paymentLinkRequest.put("accept_partial", false);
            paymentLinkRequest.put("description", "Payment for Service");

            // ✅ Customer details
            JSONObject customer = new JSONObject();
            customer.put("name", user.getUsername());
            customer.put("email", user.getEmail());
            //customer.put("contact", phone); // Required in Razorpay

            paymentLinkRequest.put("customer", customer);

            // ✅ Enable notifications
            JSONObject notify = new JSONObject();
            notify.put("email", true);
            notify.put("sms", true);
            paymentLinkRequest.put("notify", notify);

            // ✅ Use a valid public callback URL (NOT localhost)
            paymentLinkRequest.put("callback_url", "http://localhost:8085/api/wallet?order_id=" + orderId);
            paymentLinkRequest.put("callback_method", "get");

            // ✅ Create the payment link
            PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);

            // ✅ Log and return payment link
            System.out.println("Payment Link Created: " + paymentLink.toString());

            String shortUrl = paymentLink.get("short_url");

            return new PaymentResponse(shortUrl);

        } catch (RazorpayException e) {
            System.err.println("Error creating Razorpay Payment Link: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Payment link creation failed: " + e.getMessage());
        }
    }
    //http://localhost:8085/api/wallet/view

    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addAllPaymentMethodType(Collections.singletonList(SessionCreateParams.PaymentMethodType.CARD))
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8085/api/wallet?order_id=" + orderId)
                .setCancelUrl("http://localhost:8085/api/wallet")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("USD")
                                                .setUnitAmount(amount*100)
                                                .setProductData(SessionCreateParams
                                                        .LineItem
                                                        .PriceData
                                                        .ProductData
                                                        .builder()
                                                        .setName("Stock Trading")
                                                        .build())
                                                .build())
                                .build()).build();

        Session session = Session.create(params);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentUrl(session.getUrl());

        return paymentResponse;
    }
}

