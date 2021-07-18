package com.itv.checkout.excpetion;

public class DuplicatePricingDataException extends RuntimeException{
    public DuplicatePricingDataException(String message) {
        super(message);
    }
}
