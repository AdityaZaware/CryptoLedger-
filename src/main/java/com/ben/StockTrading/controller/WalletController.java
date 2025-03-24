package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.*;
import com.ben.StockTrading.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService walletService;
    private final UserService userService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public WalletController(WalletService walletService, UserService userService, OrderService orderService, PaymentService paymentService) {
        this.walletService = walletService;
        this.userService = userService;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping()
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PutMapping("/transfer/{walletId}")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction request) throws Exception {

        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);

        Wallet walletTransfer = walletService.walletToWalletTransfer(senderUser, receiverWallet, request.getAmount());

        return new ResponseEntity<>(walletTransfer, HttpStatus.OK);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
            ) throws Exception {

        User senderUser = userService.findUserProfileByJwt(jwt);

        Order order = new Order();

        Wallet wallet = walletService.payOrderPayment(order, senderUser);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addMoneyToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "oder_id") Long orderId,
            @RequestParam(name = "payment_id") String paymentId
    ) throws Exception {

        User senderUser = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(senderUser);

        PaymentOrder order = paymentService.getPaymentOrderById(orderId);

        Boolean status = paymentService.ProceedPaymentOrder(order, paymentId);

        if(wallet.getBalance() == null) {
            wallet.setBalance(BigDecimal.valueOf(0));
        }

        if (status) {
            walletService.addFundsToWallet(wallet, order.getAmount());
        }


        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }
}
