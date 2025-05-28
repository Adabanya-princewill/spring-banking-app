package com.example.dto.springapp.utils;

import java.time.Year;

public class AccountUtils {

    public static String ACCOUNT_EXIST_CODE = "001";
    public static String ACCOUNT_EXIST_MESSAGE = "User already has an account.";

    public static String ACCOUNT_SUCCESS_CODE = "002";
    public static String ACCOUNT_SUCCESS_MESSAGE = "User account created successful!";

    public static String ACCOUNT_GET_CODE = "003";
    public static String ACCOUNT_GET_MESSAGE = "successful!";

    public static String ACCOUNT_DOES_NOT_EXIT_CODE = "004";
    public static String ACCOUNT_DOES_NOT_EXIT_MESSAGE = "User does not exist";


    public static String generateAccountNumber(){
        // account number = current year + randomSixDigits;

        Year currentYear = Year.now();
        int min = 100_000;
        int max = 999_999;
        int randomSixDigits = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randomSixDigits);
        return year.concat(randomNumber);
    }
}
