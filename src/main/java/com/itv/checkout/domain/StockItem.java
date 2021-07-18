package com.itv.checkout.domain;

import java.util.Objects;

public class StockItem {
    private final String sku;

    private final int unitPrice;

    public StockItem(String sku, int unitPrice) {
        this.sku = sku;
        this.unitPrice = unitPrice;
    }

    public String getSku() {
        return this.sku;
    }

    public int getUnitPrice() {
        return this.unitPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockItem stockItem = (StockItem) o;
        return unitPrice == stockItem.unitPrice && sku.equals(stockItem.sku);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sku, unitPrice);
    }
}
