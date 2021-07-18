package com.itv.checkout.excpetion;

public class StockItemNotFoundException extends RuntimeException{
    public StockItemNotFoundException(String message) {
        super(message);
    }
}
