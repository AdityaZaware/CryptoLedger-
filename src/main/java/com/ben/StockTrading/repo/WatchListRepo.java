package com.ben.StockTrading.repo;

import com.ben.StockTrading.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchListRepo extends JpaRepository<WatchList, Long> {

    WatchList findByUserId(Long userId);
}
