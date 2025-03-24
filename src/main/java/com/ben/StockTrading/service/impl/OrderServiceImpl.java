package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.*;
import com.ben.StockTrading.enums.OrderStatus;
import com.ben.StockTrading.enums.OrderType;
import com.ben.StockTrading.repo.OrderItemRepo;
import com.ben.StockTrading.repo.OrderRepo;
import com.ben.StockTrading.service.AssetService;
import com.ben.StockTrading.service.OrderService;
import com.ben.StockTrading.service.WalletService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final WalletService walletService;
    private final AssetService assetService;

    public OrderServiceImpl(OrderRepo orderRepo, OrderItemRepo orderItemRepo, WalletService walletService, AssetService assetService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.walletService = walletService;
        this.assetService = assetService;
    }

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderType(orderType);
        order.setPrice(new BigDecimal(price));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedDate(LocalDateTime.now());
        order.setOrderItem(orderItem);

        return orderRepo.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepo.findById(orderId);
        return order.orElse(null);
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetName) {
        return orderRepo.findByUserId(userId);
    }

    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {

        if(orderType.equals(OrderType.BUY)) {
            return buyCoin(coin, quantity, user);
        }
        else if(orderType.equals(OrderType.SELL)) {
            return sellCoin(coin, quantity, user);
        }
        throw new Exception("Invalid order type");
    }

    private OrderItem crateOrderItem(
            Coin coin, double quantity,
            double buyPrice, double sellPrice) {

        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepo.save(orderItem);
    }

    @Transactional
    public Order buyCoin(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        double buyPrice = coin.getCurrentPrice();

        OrderItem orderItem = crateOrderItem(coin, quantity, buyPrice, 0);

        Order order = createOrder(user, orderItem, OrderType.BUY);

        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);

        order.setStatus(OrderStatus.COMPLETED);
        order.setOrderType(OrderType.BUY);

        Order savedOrder = orderRepo.save(order);

        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(
                order.getUser().getId(),
                orderItem.getCoin().getId());

        if(oldAsset == null) {
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
        }
        else {
            assetService.updateAsset(oldAsset.getId(), quantity);
        }


        return savedOrder;
    }

    @Transactional
    public Order sellCoin(Coin coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        double sellPrice = coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());

        if(assetToSell != null) {


            double buyPrice = assetToSell.getBuyPrice();

            OrderItem orderItem = crateOrderItem(coin, quantity, buyPrice, sellPrice);

            Order order = createOrder(user, orderItem, OrderType.SELL);

            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {
                order.setStatus(OrderStatus.COMPLETED);
                order.setOrderType(OrderType.SELL);

                Order savedOrder = orderRepo.save(order);
                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);

                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }

                return savedOrder;

            }
            throw new Exception("Insufficient quantity");
        }
        throw new Exception("Asset not found");
    }
}
