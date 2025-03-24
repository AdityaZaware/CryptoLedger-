package com.ben.StockTrading.entity;

import com.ben.StockTrading.enums.OrderStatus;
import com.ben.StockTrading.enums.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private OrderType orderType;

    @Column(nullable = false)
    private BigDecimal price;

    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(nullable = false)
    private OrderStatus status;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "order")
    private OrderItem orderItem;
}
