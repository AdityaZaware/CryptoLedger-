package com.ben.StockTrading.service;

import com.ben.StockTrading.entity.PaymentDetails;
import com.ben.StockTrading.entity.User;

public interface PaymentDetailService {

    public PaymentDetails createPaymentDetails(String accountNumber,
                                               String accountHolder,
                                               String ifsc,
                                               String bankName,
                                               User user);

    public PaymentDetails getUserPaymentDetails(User user);
}
