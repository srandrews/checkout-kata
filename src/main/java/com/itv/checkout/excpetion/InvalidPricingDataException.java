package com.itv.checkout.excpetion;

public class InvalidPricingDataException extends RuntimeException{
    public InvalidPricingDataException(Throwable cause){
        super(cause);
    }
}
