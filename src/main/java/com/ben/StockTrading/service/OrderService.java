package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.entity.Order;
import com.ben.StockTrading.entity.OrderItem;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.OrderType;

import java.util.List;

public interface OrderService {

    public Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    public Order getOrderById(Long orderId);

    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetName);

    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;

}
