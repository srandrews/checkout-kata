package com.itv.checkout.pricing;

import com.itv.checkout.domain.StockItem;

@FunctionalInterface
public interface PricingStrategy {

    /**
     * Generates a total price based on the current PricingStrategy
     * @param stockItem the {@link StockItem} to calcualate the total price
     * @param quantity the quantity of StockItems
     * @return the total price for the quantity of {@link StockItem}s provided
     */
    int getTotalPrice(StockItem stockItem, int quantity);
}
