package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.ForgotPassword;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.VeriFicationType;
import com.ben.StockTrading.repo.ForgotPasswordRepo;
import com.ben.StockTrading.service.ForgotPasswordService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final ForgotPasswordRepo forgotPasswordRepo;

    public ForgotPasswordServiceImpl(ForgotPasswordRepo forgotPasswordRepo) {
        this.forgotPasswordRepo = forgotPasswordRepo;
    }

    @Override
    public ForgotPassword createToken(User user, String id, String otp, VeriFicationType veriFicationType, String sendTo) {
        ForgotPassword forgotPassword = new ForgotPassword();

        forgotPassword.setUser(user);
        forgotPassword.setSendTo(sendTo);
        forgotPassword.setOtp(otp);
        forgotPassword.setVeriFicationType(veriFicationType);
        forgotPassword.setId(id);
        forgotPassword.setEmail(user.getEmail());

        return forgotPasswordRepo.save(forgotPassword);
    }

    @Override
    public ForgotPassword findByEmail(String email) {

        ForgotPassword forgotPassword = forgotPasswordRepo.findForgotPasswordsByEmail(email);

        if(forgotPassword == null) {
            throw new RuntimeException("Invalid OTP");
        }

        return forgotPassword;
    }

    @Override
    public ForgotPassword findByUser(Long userId) {
        return forgotPasswordRepo.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPassword forgotPassword) {
        forgotPasswordRepo.delete(forgotPassword);
    }
}
