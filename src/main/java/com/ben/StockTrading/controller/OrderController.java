package com.ben.StockTrading.controller;

import com.ben.StockTrading.entity.Coin;
import com.ben.StockTrading.entity.Order;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.enums.OrderType;
import com.ben.StockTrading.request.CreateOrderRequest;
import com.ben.StockTrading.service.CoinService;
import com.ben.StockTrading.service.OrderService;
import com.ben.StockTrading.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final CoinService coinService;
    //private WalletTransactionService walletTransactionService;

    public OrderController(OrderService orderService, UserService userService, CoinService coinService) {
        this.orderService = orderService;
        this.userService = userService;
        this.coinService = coinService;
    }

    @PostMapping("/payment")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest request) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        Coin coin = coinService.findById(request.getCoinId());

        Order order = orderService.processOrder(coin, request.getQuantity(), request.getOrderType(), user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) throws Exception {


        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        if(order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        }
        else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType orderType,
            @RequestParam(required = false) String assetName) throws Exception {

        Long userId = userService.findUserProfileByJwt(jwt).getId();

        List<Order> orders = orderService.getAllOrdersOfUser(userId, orderType, assetName);
        return ResponseEntity.ok(orders);
    }


}
