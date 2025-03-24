package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.PaymentDetails;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.service.PaymentDetailService;
import com.ben.StockTrading.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paymentdetail")
public class PaymentDetailController {

    private final PaymentDetailService paymentDetailService;
    private final UserService userService;

    public PaymentDetailController(PaymentDetailService paymentDetailService, UserService userService) {
        this.paymentDetailService = paymentDetailService;
        this.userService = userService;
    }

    @PostMapping("/paymentdetail")
    public ResponseEntity<PaymentDetails> addPaymentDetail(
            @RequestBody PaymentDetails paymentDetails,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        PaymentDetails paymentDetails1 = paymentDetailService.createPaymentDetails(
                paymentDetails.getAccountNumber(),
                paymentDetails.getAccountHolderName(),
                paymentDetails.getIfscCode(),
                paymentDetails.getBankName(),
                user
        );

        return new ResponseEntity<>(paymentDetails1, HttpStatus.OK);
    }

    @GetMapping("/paymentdetail")
    public ResponseEntity<PaymentDetails> getPaymentDetail(
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailService.getUserPaymentDetails(user);

        return new ResponseEntity<>(paymentDetails, HttpStatus.OK);
    }



}
