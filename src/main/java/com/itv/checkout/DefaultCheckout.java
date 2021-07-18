package com.itv.checkout;

import com.itv.checkout.domain.OrderItem;
import com.itv.checkout.loader.PricingLoader;
import com.itv.checkout.pricing.DefaultPricingService;
import com.itv.checkout.pricing.PricingService;

import java.util.LinkedList;
import java.util.List;

public class DefaultCheckout implements Checkout {

    private final PricingService pricingService;
    private final List<OrderItem> orderItems = new LinkedList<>();

    /**
     * Create a {@link DefaultCheckout} using the provided {@link PricingLoader}, all
     * stock items and pricing strategies will be loaded via this loader
     * @param loader The {@link PricingLoader} to use to retrieve all pricing data
     */
    public DefaultCheckout(PricingLoader loader) {
        this(loader, new DefaultPricingService());
    }

    /**
     * Create a {@link DefaultCheckout} using the provided {@link PricingLoader} and
     * {@link PricingService}, all stock items and pricing strategies will be loaded via
     * this loader and stored in the {@link PricingService}
     * @param loader The {@link PricingLoader} to use to retrieve all pricing data
     */
    public DefaultCheckout(PricingLoader loader, PricingService pricingService) {
        this.pricingService = pricingService;
        pricingService.setStockItemsAndPricingStrategies(loader.getPricingData());
    }

    @Override
    public void addOrderLine(String sku, int quantity) {
        OrderItem orderItem = new OrderItem(pricingService.getStockItem(sku), quantity);
        orderItems.add(orderItem);
    }

    @Override
    public int getTotal() {
        return pricingService.calculateOrderTotal(orderItems);
    }
}