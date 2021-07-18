package com.itv.checkout.pricing;

import com.itv.checkout.domain.OrderItem;
import com.itv.checkout.domain.StockItem;

import java.util.List;
import java.util.Map;

public interface PricingService {

    /**
     * Add a pricing strategy for the specified stock item. If there is already a pricing
     * strategy for the current stock item, it will be overwritten with the provided
     * pricing strategy
     *
     * @param stockItem The {@link StockItem} that the {@link PricingStrategy} relates to
     * @param pricingStrategy The {@link PricingStrategy} for this {@link StockItem}
     */
    void addStockItemPricingStrategy(StockItem stockItem, PricingStrategy pricingStrategy);


    void setStockItemsAndPricingStrategies(Map<StockItem, PricingStrategy> stockItemsAndPricingStrategies);

    /**
     * For a given SKU, return the stock item
     *
     * @param sku The sku that is to be looked up
     * @return The {@link StockItem} relating to the provided sku
     */
    StockItem getStockItem(String sku);

    /**
     * Adds or replaces a {@link StockItem} to the current PricingService
     * @param stockItem the {@link StockItem} to add
     */
    void addStockItem(StockItem stockItem);

    /**
     * Calcualte the order total for the provided orderItems
     * @param orderItems the {@link OrderItem}s to calculate the total for
     * @return the total for the order
     */
    int calculateOrderTotal(List<OrderItem> orderItems);

    /**
     * Finds the {@link PricingStrategy} for the provided {@link StockItem}
     * @param stockItem the {@link StockItem} to get the {@link PricingStrategy} for
     * @return the {@link PricingStrategy} for stockItem
     */
    PricingStrategy getPricingStrategy(StockItem stockItem);
}
