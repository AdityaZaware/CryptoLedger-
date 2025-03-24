package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.VerificationCode;
import com.ben.StockTrading.enums.VeriFicationType;
import com.ben.StockTrading.repo.VerificationCodeRepo;
import com.ben.StockTrading.service.VerificationCodeService;
import com.ben.StockTrading.utils.OtpUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepo verificationCodeRepo;

    public VerificationCodeServiceImpl(VerificationCodeRepo verificationCodeRepo) {
        this.verificationCodeRepo = verificationCodeRepo;
    }


    @Override
    public VerificationCode sendVerificationCode(User user, VeriFicationType veriFicationType) {

        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVeriFicationType(veriFicationType);

        verificationCode1.setUser(user);

        return verificationCodeRepo.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long userId) throws Exception {

        Optional<VerificationCode> verificationCode =
                verificationCodeRepo.findById(userId);

        if (verificationCode.isPresent()) {
            return verificationCode.get();
        }

        throw new Exception("Verification code not found");
    }

    @Override
    public VerificationCode getVerificationCodeByUserId(Long userId) {
        return verificationCodeRepo.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepo.delete(verificationCode);
    }
}
