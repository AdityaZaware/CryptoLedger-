package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.PaymentOrder;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.PaymentMethod;
import com.ben.StockTrading.response.PaymentResponse;
import com.ben.StockTrading.service.PaymentService;
import com.ben.StockTrading.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentResponse paymentResponse;

        PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

        if (paymentMethod.equals(PaymentMethod.STRIPE)) {
            paymentResponse = paymentService.createStripePaymentLink(user, amount, order.getId());
        } else {
            paymentResponse = paymentService.createRazorPayPaymentLink(user, amount, order.getId());
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

}
