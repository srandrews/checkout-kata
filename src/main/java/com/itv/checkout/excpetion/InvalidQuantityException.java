package com.itv.checkout.excpetion;

public class InvalidQuantityException extends InvalidValueException{
    private static final String INVALID_QUANTITY_EXCEPTION_MESSAGE = "Quantity must be >= 1";

    public InvalidQuantityException() {
        super(INVALID_QUANTITY_EXCEPTION_MESSAGE);
    }

}
