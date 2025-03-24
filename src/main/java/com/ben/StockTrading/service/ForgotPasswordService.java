package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.ForgotPassword;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.VeriFicationType;

public interface ForgotPasswordService {

    ForgotPassword createToken(User user, String id,
                               String otp, VeriFicationType veriFicationType,
                               String sendTo);

    ForgotPassword findByEmail(String otp);

    ForgotPassword findByUser(Long userId);

    void deleteToken(ForgotPassword forgotPassword);
}
