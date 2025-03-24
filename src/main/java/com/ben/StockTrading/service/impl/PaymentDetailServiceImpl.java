package com.ben.StockTrading.service.impl;

import com.ben.StockTrading.entity.PaymentDetails;
import com.ben.StockTrading.entity.User;
import com.ben.StockTrading.repo.PaymentDetailRepo;
import com.ben.StockTrading.service.PaymentDetailService;
import org.springframework.stereotype.Service;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {

    private final PaymentDetailRepo paymentDetailsRepo;

    public PaymentDetailServiceImpl(PaymentDetailRepo paymentDetailsRepo) {
        this.paymentDetailsRepo = paymentDetailsRepo;
    }

    @Override
    public PaymentDetails createPaymentDetails(String accountNumber, String accountHolder, String ifsc, String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();

        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolderName(accountHolder);
        paymentDetails.setBankName(bankName);
        paymentDetails.setIfscCode(ifsc);
        paymentDetails.setUser(user);

        return paymentDetailsRepo.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepo.findByUserId(user.getId());
    }
}
