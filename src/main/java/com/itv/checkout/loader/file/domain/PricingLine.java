package com.itv.checkout.loader.file.domain;

public class PricingLine {

    private final String item;
    private final String unitPrice;
    private final String specialPrice;

    public PricingLine(String item, String unitPrice, String specialPrice) {
        this.item = item;
        this.unitPrice = unitPrice;
        this.specialPrice = specialPrice;
    }

    public String getItem() {
        return item;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    @Override
    public String toString() {
        return "[" + item + ", " + unitPrice +", " + specialPrice +"]";
    }
}
