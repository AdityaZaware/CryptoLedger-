package com.ben.StockTrading.utils;

import java.util.Random;

public class OtpUtils {

    public static String generateOtp() {
        int optLength = 6;

        Random random = new Random();

        StringBuilder opt = new StringBuilder(optLength);

        for (int i = 0; i < optLength; i++) {
            opt.append(random.nextInt(10));
        }
        return opt.toString();
    }
}
