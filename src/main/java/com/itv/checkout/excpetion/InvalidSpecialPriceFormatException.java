package com.itv.checkout.excpetion;

public class InvalidSpecialPriceFormatException extends RuntimeException{

    private static final String INVALID_SPECIAL_PRICE_FORMAT_MESSAGE = "The specified special price text (%s) does not match the any expected values.";

    public InvalidSpecialPriceFormatException(String specialPriceText) {
        super(String.format(INVALID_SPECIAL_PRICE_FORMAT_MESSAGE, specialPriceText));
    }

}
