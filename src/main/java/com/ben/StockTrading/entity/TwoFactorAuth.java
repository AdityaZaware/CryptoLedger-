package com.ben.StockTrading.entity;

import com.ben.StockTrading.enums.VeriFicationType;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnabled = false;

    private VeriFicationType sendTo;
}
