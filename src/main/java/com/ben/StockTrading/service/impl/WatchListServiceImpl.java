package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.WatchList;
import com.ben.StockTrading.repo.WatchListRepo;
import com.ben.StockTrading.service.WatchListService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements WatchListService {

    private final WatchListRepo watchListRepo;

    public WatchListServiceImpl(WatchListRepo watchListRepo) {
        this.watchListRepo = watchListRepo;
    }

    @Override
    public WatchList findUserWatchList(Long userId) throws Exception {
        WatchList watchList = watchListRepo.findByUserId(userId);

        if (watchList == null) {
            throw new Exception("Watchlist not found");
        }
        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepo.save(watchList);
    }

    @Override
    public WatchList findById(Long watchListId) {
        Optional<WatchList> watchList = watchListRepo.findById(watchListId);
        if (watchList.isEmpty()) {
            throw new RuntimeException("Watchlist not found");
        }
        return watchList.get();
    }

    @Override
    public Coin addItemToWatchLis(Coin coin, User user) throws Exception {

        WatchList watchList = findUserWatchList(user.getId());

        if(watchList.getCoins().contains(coin)) {
            watchList.getCoins().remove(coin);
        }
        else {
            watchList.getCoins().add(coin);
        }

        watchListRepo.save(watchList);

        return coin;
    }
}
