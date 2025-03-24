package com.ben.StockTrading.request;

import com.ben.StockTrading.enums.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
