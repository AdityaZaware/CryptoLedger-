package com.ben.StockTrading.repo;

import com.ben.StockTrading.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepo extends JpaRepository<Coin, String> {
}
