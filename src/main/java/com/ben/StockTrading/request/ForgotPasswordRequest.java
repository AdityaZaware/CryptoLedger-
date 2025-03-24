package com.ben.StockTrading.request;

import com.ben.StockTrading.enums.VeriFicationType;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    private String sendTo;
    private VeriFicationType veriFicationType;
}
