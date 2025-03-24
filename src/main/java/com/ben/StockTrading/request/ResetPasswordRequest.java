package com.ben.StockTrading.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String otp;
    private String newPassword;
}
