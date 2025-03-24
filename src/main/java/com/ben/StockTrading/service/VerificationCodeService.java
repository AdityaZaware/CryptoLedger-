package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.VerificationCode;
import com.ben.StockTrading.enums.VeriFicationType;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VeriFicationType veriFicationType);

    VerificationCode getVerificationCodeById(Long userId) throws Exception;

    VerificationCode getVerificationCodeByUserId(Long userId);

    void deleteVerificationCode(VerificationCode verificationCode);
}
