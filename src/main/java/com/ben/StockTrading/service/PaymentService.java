package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.PaymentOrder;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.PaymentMethod;
import com.ben.StockTrading.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {

    public PaymentOrder createOrder(User user, Long amount , PaymentMethod paymentMethod);

    public PaymentOrder getPaymentOrderById(Long paymentOrderId) throws Exception;

    public Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    public PaymentResponse createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;

    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
