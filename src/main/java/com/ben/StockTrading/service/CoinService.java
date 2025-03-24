package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinsList(int page) throws Exception;

    String getMarketChart(String coinId, int days) throws Exception;

    String getCoinDetails(String coinId) throws Exception;

    Coin findById(String id);

    String searchCoin(String keyword) throws Exception;

    String getTop50Coins() throws Exception;

    String getTreadingCoins() throws Exception;

}
