package com.ben.StockTrading.entity;

import com.ben.StockTrading.enums.VeriFicationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ForgotPassword {

    @Id
    private String id;

    @OneToOne
    private User user;

    private String otp;

    private String email;

    private VeriFicationType veriFicationType;

    private String sendTo;


}
