package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.TwoFactorOtp;
import com.ben.StockTrading.entity.User;

public interface TwoFactorOtpService {

    TwoFactorOtp createOtp(User user, String otp, String jwt);

    TwoFactorOtp findByUser(Long userId);

    TwoFactorOtp findById(String id);

    boolean verifyOtp(TwoFactorOtp twoFactorOtp, String otp);

    void deleteOtp(TwoFactorOtp twoFactorOtp);


}
