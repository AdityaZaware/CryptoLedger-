package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.TwoFactorAuth;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.VeriFicationType;
import com.ben.StockTrading.repo.UserRepo;
import com.ben.StockTrading.security.JwtProvider;
import com.ben.StockTrading.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }



    @Override
    public User findUserProfileByJwt(String jwt) throws Exception {
        String email = JwtProvider.getEmailFromToken(jwt);

        return findUserProfileByEmail(email);
    }

    @Override
    public User findUserProfileByEmail(String email) throws Exception {
        User user = userRepo.findByEmail(email);

        if(user == null) {
            throw new Exception("User not found");
        }

        return user;
    }

    @Override
    public User findUserById(Long userId) {
        Optional<User> user = userRepo.findById(userId);

        return user.orElse(null);
    }

    @Override
    public User enableTwoFactorAuth(VeriFicationType veriFicationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(veriFicationType);

        user.setTwoFactorAuth(twoFactorAuth);

        return userRepo.save(user);
    }


    @Override
    public User updatePassword(User user, String password) {
        user.setPassword(password);

        return userRepo.save(user);
    }
}
