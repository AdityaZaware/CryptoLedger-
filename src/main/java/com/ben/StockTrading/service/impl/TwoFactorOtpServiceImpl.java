package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.TwoFactorOtp;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.repo.TwoFactorOtpRepo;
import com.ben.StockTrading.service.TwoFactorOtpService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    private final TwoFactorOtpRepo twoFactorOtpRepo;

    public TwoFactorOtpServiceImpl(TwoFactorOtpRepo twoFactorOtpRepo) {
        this.twoFactorOtpRepo = twoFactorOtpRepo;
    }


    @Override
    public TwoFactorOtp createOtp(User user, String otp, String jwt) {
        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();

        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setId(id);
        twoFactorOtp.setUser(user);
        twoFactorOtp.setJwt(jwt);
        TwoFactorOtp saved = twoFactorOtpRepo.save(twoFactorOtp);

        return saved;
    }

    @Override
    public TwoFactorOtp findByUser(Long userId) {
        TwoFactorOtp twoFactorOtp = twoFactorOtpRepo.findByUserId(userId);
        return twoFactorOtp;
    }

    @Override
    public TwoFactorOtp findById(String id) {
        Optional<TwoFactorOtp> twoFactorOtp = twoFactorOtpRepo.findById(id);
        return twoFactorOtp.orElse(null);
    }

    @Override
    public boolean verifyOtp(TwoFactorOtp twoFactorOtp, String otp) {
        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepo.delete(twoFactorOtp);
    }
}





