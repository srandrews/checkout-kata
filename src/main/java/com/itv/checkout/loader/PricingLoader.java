package com.itv.checkout.loader;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.pricing.PricingStrategy;

import java.util.Map;


public interface PricingLoader {
    /**
     * Provides a map of all StockItems and associated {@link PricingStrategy}s.
     * If a {@link StockItem} doesn't have a {@link PricingStrategy} then the value
     * will be null
     *
     * @return map of {@link StockItem}s and associated {@link PricingStrategy}s
     */
    Map<StockItem, PricingStrategy> getPricingData();
}
