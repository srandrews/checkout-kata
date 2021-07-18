package com.itv.checkout.domain;

import com.itv.checkout.excpetion.InvalidQuantityException;

import java.util.Objects;

public class OrderItem {
    private final StockItem stockItem;
    private final int quantity;

    public OrderItem(StockItem stockItem, int quantity) {

        if (quantity<1) {
            throw new InvalidQuantityException();
        }

        this.stockItem = stockItem;
        this.quantity = quantity;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public int getQuantity() {
        return  quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem that = (OrderItem) o;
        return quantity == that.quantity && stockItem.equals(that.stockItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockItem, quantity);
    }
}
