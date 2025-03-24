package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    public Withdrawal requestWithdrawal(Long amount, User user);

    public Withdrawal processWithdrawal(Long withdrawalId, boolean accept);

    public List<Withdrawal> getUsersWithdrawalHistory(User user);

    public List<Withdrawal> getAllWithdrawalRequest();
}
