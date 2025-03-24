package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.Order;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.Wallet;

public interface WalletService {

    public Wallet getUserWallet(User user);

    public Wallet addFundsToWallet(Wallet wallet, Long amount);

    public Wallet findWalletById(Long walletId) throws Exception;

    public Wallet walletToWalletTransfer(User sender, Wallet receiver, Long amount) throws Exception;

    public Wallet payOrderPayment(Order order, User user) throws Exception;


}
