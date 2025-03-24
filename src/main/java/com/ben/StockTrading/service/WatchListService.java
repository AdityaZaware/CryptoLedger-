package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.entity.WatchList;

public interface WatchListService {

    public WatchList findUserWatchList(Long userId) throws Exception;

    public WatchList createWatchList(User user);

    public WatchList findById(Long watchListId);

    public Coin addItemToWatchLis(Coin coin, User user) throws Exception;

}
