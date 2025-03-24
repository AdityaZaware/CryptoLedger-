package com.ben.StockTrading.repo;

import com.ben.StockTrading.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailRepo extends JpaRepository<PaymentDetails, Long> {

    PaymentDetails findByUserId(Long userId);
}
