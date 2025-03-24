package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.VeriFicationType;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;

public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;

    public User findUserProfileByEmail(String email) throws Exception;

    public User findUserById(Long userId);

    public User enableTwoFactorAuth(VeriFicationType veriFicationType,
                                    String sendTo,
                                    User user);

    public User updatePassword(User user, String password);
}
