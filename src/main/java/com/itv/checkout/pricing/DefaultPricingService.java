package com.itv.checkout.pricing;

import com.itv.checkout.domain.OrderItem;
import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.StockItemNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultPricingService implements PricingService {

    private static final String STOCKITEM_NOT_FOUND_MESSAGE = "No StockItem exists for SKU %s";

    private final PricingStrategy defaultPricingStrategy;

    private final Map<StockItem, PricingStrategy> stockItemPricingStrategies;

    private final Map<String, StockItem> stockItems;

    public DefaultPricingService() {
        this(new BasicPricingStrategy());
    }

    public DefaultPricingService(PricingStrategy defaultPricingStrategy) {
        this.stockItemPricingStrategies = new HashMap<>();
        this.stockItems = new HashMap<>();
        this.defaultPricingStrategy = defaultPricingStrategy;
    }

    @Override
    public StockItem getStockItem(String sku) {
        return Optional.ofNullable(stockItems.get(sku))
                .orElseThrow(() -> new StockItemNotFoundException(String.format(STOCKITEM_NOT_FOUND_MESSAGE,sku)));
    }

    @Override
    public void addStockItem(StockItem stockItem) {
        stockItems.put(stockItem.getSku(), stockItem);
    }

    @Override
    public void addStockItemPricingStrategy(StockItem stockItem, PricingStrategy pricingStrategy) {
        stockItemPricingStrategies.put(stockItem, pricingStrategy);
    }

    @Override
    public void setStockItemsAndPricingStrategies(Map<StockItem, PricingStrategy> newStockItemsAndPricingStrategies) {
        stockItems.clear();
        stockItemPricingStrategies.clear();

        newStockItemsAndPricingStrategies.keySet().forEach(this::addStockItem);
        newStockItemsAndPricingStrategies
                .entrySet()
                .stream()
                .filter(stockItemPricingStrategyEntry -> stockItemPricingStrategyEntry.getValue()!=null)
                .forEach(stockItemPricingStrategyEntry ->
                        stockItemPricingStrategies.put(stockItemPricingStrategyEntry.getKey(),
                                stockItemPricingStrategyEntry.getValue()));

    }

    @Override
    public PricingStrategy getPricingStrategy(StockItem stockItem) {
        return stockItemPricingStrategies.getOrDefault(stockItem, defaultPricingStrategy);
    }

    @Override
    public int calculateOrderTotal(List<OrderItem> orderItems) {

        if (orderItems == null || orderItems.size() == 0) {
            return 0;
        }

        return aggregateReceiptItemsQuantities(orderItems)
                .entrySet()
                .stream()
                .map(stockItemEntry -> getEntryTotalPrice(stockItemEntry.getKey(), stockItemEntry.getValue()))
                .reduce(0, Integer::sum);
    }

    private int getEntryTotalPrice(StockItem stockItem, int quantity) {
        return getPricingStrategy(stockItem)
                .getTotalPrice(stockItem, quantity);
    }

    private Map<StockItem,Integer> aggregateReceiptItemsQuantities(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .collect(Collectors.toMap(OrderItem::getStockItem, OrderItem::getQuantity, Integer::sum));
    }

}