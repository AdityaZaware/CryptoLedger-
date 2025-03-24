package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.Wallet;
import com.ben.StockTrading.entity.WalletTransaction;
import com.ben.StockTrading.entity.Withdrawal;
import com.ben.StockTrading.service.UserService;
import com.ben.StockTrading.service.WalletService;
import com.ben.StockTrading.service.WithdrawalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    private final WalletService walletService;

    private final UserService userService;

//    private final WalletTransactionService walletTransactionService;

    public WithdrawalController(WithdrawalService withdrawalService, WalletService walletService, UserService userService) {
        this.withdrawalService = withdrawalService;
        this.walletService = walletService;
        this.userService = userService;
 //       this.walletTransactionService = walletTransactionService;
    }

    @PostMapping("/withdrawalRequest/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
        walletService.addFundsToWallet(wallet, -withdrawal.getAmount());

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @PatchMapping("/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawalRequest(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id,
            @PathVariable boolean accept) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = withdrawalService.processWithdrawal(id, accept);

        Wallet userWallet = walletService.getUserWallet(user);

        if(!accept) {
            walletService.addFundsToWallet(userWallet, withdrawal.getAmount());
        }

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getAllWithdrawalsHistory(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawals = withdrawalService.getUsersWithdrawalHistory(user);

        return new ResponseEntity<>(withdrawals, HttpStatus.OK);
    }

    @GetMapping("/admin/history")
    public ResponseEntity<?> getWithdrawalRequestHistory(
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);

        List<Withdrawal> withdrawals = withdrawalService.getAllWithdrawalRequest();

        return new ResponseEntity<>(withdrawals, HttpStatus.OK);
    }
}
