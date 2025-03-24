package com.ben.StockTrading.repo;

import com.ben.StockTrading.entity.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepo extends JpaRepository<ForgotPassword, String> {

    ForgotPassword findByUserId(Long userId);

    ForgotPassword findForgotPasswordsByEmail(String email);
}
