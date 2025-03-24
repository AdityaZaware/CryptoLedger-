package com.ben.StockTrading.entity;

import com.ben.StockTrading.enums.VeriFicationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    @OneToOne
    private User user;

    private String email;

    private String mobile;

    private VeriFicationType veriFicationType;

}
