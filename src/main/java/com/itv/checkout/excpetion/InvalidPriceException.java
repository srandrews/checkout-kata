package com.itv.checkout.excpetion;

public class InvalidPriceException extends InvalidValueException{

    private static final String INVALID_PRICE_EXCEPTION_MESSAGE = "Price must be >= 0";

    public InvalidPriceException() {
        super(INVALID_PRICE_EXCEPTION_MESSAGE);
    }

}
