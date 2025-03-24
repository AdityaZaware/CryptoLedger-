package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.Order;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.Wallet;
import com.ben.StockTrading.enums.OrderType;
import com.ben.StockTrading.repo.WalletRepo;
import com.ben.StockTrading.service.WalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepo walletRepo;

    public WalletServiceImpl(WalletRepo walletRepo) {
        this.walletRepo = walletRepo;
    }

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepo.findByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepo.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addFundsToWallet(Wallet wallet, Long amount) {
        BigDecimal currentBalance = wallet.getBalance();
        BigDecimal newBalance = currentBalance.add(new BigDecimal(amount));
        wallet.setBalance(newBalance);

        return walletRepo.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long walletId) throws Exception {
        Optional<Wallet> wallet = walletRepo.findById(walletId);

        if(wallet.isPresent()) {
            return wallet.get();
        }
        throw new Exception("Wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiver, Long amount) throws Exception {

        Wallet senderWallet = getUserWallet(sender);

        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("Insufficient funds");
        }

        BigDecimal senderBalance = senderWallet
                .getBalance()
                .subtract(BigDecimal.valueOf(amount));

        senderWallet.setBalance(senderBalance);
        walletRepo.save(senderWallet);

        BigDecimal receiverBalance = receiver
                .getBalance()
                .add(BigDecimal.valueOf(amount));

        receiver.setBalance(receiverBalance);
        walletRepo.save(receiver);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {

        Wallet wallet = getUserWallet(user);

        if(order.getOrderType().equals(OrderType.BUY)) {
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            if(newBalance.compareTo(order.getPrice()) < 0) {
                throw new Exception("Insufficient funds");
            }
            wallet.setBalance(newBalance);
        }
        else {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepo.save(wallet);
        return wallet;
    }
}
