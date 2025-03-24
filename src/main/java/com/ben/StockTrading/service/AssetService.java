package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.Asset;
import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.entity.User;

import java.util.List;

public interface AssetService {

    public Asset createAsset(User user, Coin coin, double quantity);

    public Asset getAssetById(Long assetId) throws Exception;

    public Asset getAssetByUserId(Long userId, Long assetId) throws Exception;

    public List<Asset> getUsersAssets(Long userId);

    public Asset updateAsset(Long assetId, double quantity) throws Exception;

    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) throws Exception;

    public void deleteAsset(Long assetId);
}
