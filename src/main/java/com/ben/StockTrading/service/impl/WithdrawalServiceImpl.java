package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.Withdrawal;
import com.ben.StockTrading.enums.WithdrawalStatus;
import com.ben.StockTrading.repo.WithdrawalRepo;
import com.ben.StockTrading.service.WithdrawalService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private final WithdrawalRepo withdrawalRepo;

    public WithdrawalServiceImpl(WithdrawalRepo withdrawalRepo) {
        this.withdrawalRepo = withdrawalRepo;
    }

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = new Withdrawal();

        withdrawal.setAmount(amount);
        withdrawal.setUser(user);
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawalRepo.save(withdrawal);
    }

    @Override
    public Withdrawal processWithdrawal(Long withdrawalId, boolean accept) {
        Optional<Withdrawal> withdrawal = withdrawalRepo.findById(withdrawalId);

        if(withdrawal.isEmpty()) {
            throw new RuntimeException("Withdrawal not found");
        }

        Withdrawal withdrawal1 = withdrawal.get();

        withdrawal1.setDate(LocalDateTime.now());

        if(accept) {
            withdrawal1.setStatus(WithdrawalStatus.SUCCESS);
        } else {
            withdrawal1.setStatus(WithdrawalStatus.REJECTED);
        }
        return withdrawalRepo.save(withdrawal1);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {
        return withdrawalRepo.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepo.findAll();
    }
}
