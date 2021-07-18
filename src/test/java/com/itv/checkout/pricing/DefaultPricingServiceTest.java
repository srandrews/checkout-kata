package com.itv.checkout.pricing;

import com.itv.checkout.domain.OrderItem;
import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.StockItemNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DefaultPricingServiceTest {

    private final PricingStrategy defaultPricingStrategy = (stockItem, quantity) -> stockItem.getUnitPrice() * quantity;

    private final PricingStrategy quantityBasedStrategy = (stockItem, quantity) -> quantity * 2;

    private PricingService pricingService;

    private final StockItem itemA = new StockItem("A", 12);
    private final StockItem itemB = new StockItem("B", 32);
    private final StockItem itemC = new StockItem("C", 1);
    private final StockItem itemD = new StockItem("D", 3);
    private final StockItem itemE = new StockItem("E", 21);

    private List<OrderItem> orderItems;

    @BeforeEach
    void setup() {
        orderItems = new LinkedList<>();
        pricingService = new DefaultPricingService(defaultPricingStrategy);
    }

    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingDefaultPricingAndSendingEmptyList() {
        int expected = 0;

        assertEquals(expected, pricingService.calculateOrderTotal(orderItems));
    }
    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingDefaultPricingAndNull() {
        int expected = 0;

        assertEquals(expected, pricingService.calculateOrderTotal(null));
    }


    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingDefaultPricingAndOnlyUsingEachStockItemOnce() {

        int expected = 219;

        orderItems.add(new OrderItem(itemA,3));
        orderItems.add(new OrderItem(itemB,1));
        orderItems.add(new OrderItem(itemC,7));
        orderItems.add(new OrderItem(itemD,20));
        orderItems.add(new OrderItem(itemE,4));

        assertEquals(expected, pricingService.calculateOrderTotal(orderItems));

    }

    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingDefaultPricingAndMultipleEntriesForOneStockItem() {

        int expected = 120;

        orderItems.add(new OrderItem(itemA,3));
        orderItems.add(new OrderItem(itemA,7));

        assertEquals(expected, pricingService.calculateOrderTotal(orderItems));

    }

    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingDefaultPricingAndMultipleEntriesForManyStockItems() {

        int expected = 528;

        orderItems.add(new OrderItem(itemA,6));
        orderItems.add(new OrderItem(itemB,3));
        orderItems.add(new OrderItem(itemC,7));
        orderItems.add(new OrderItem(itemD,20));
        orderItems.add(new OrderItem(itemE,4));
        orderItems.add(new OrderItem(itemC,23));
        orderItems.add(new OrderItem(itemA,3));
        orderItems.add(new OrderItem(itemD,50));

        assertEquals(expected, pricingService.calculateOrderTotal(orderItems));

    }

    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingSpecificPricingStrategyForOneStockItem() {

        pricingService.addStockItemPricingStrategy(itemA, quantityBasedStrategy);

        int expected = 142;

        orderItems.add(new OrderItem(itemA,6));
        orderItems.add(new OrderItem(itemA,65));

        assertEquals(expected, pricingService.calculateOrderTotal(orderItems));

    }

    @Test
    void testPricingService_shouldReturnExpectedTotalWhenUsingSpecialPricingForSomeStockItems() {

        int expected = 398;

        pricingService.addStockItemPricingStrategy(itemA, quantityBasedStrategy);
        pricingService.addStockItemPricingStrategy(itemC, quantityBasedStrategy);
        pricingService.addStockItemPricingStrategy(itemD, quantityBasedStrategy);

        orderItems.add(new OrderItem(itemA,6));
        orderItems.add(new OrderItem(itemB,3));
        orderItems.add(new OrderItem(itemC,7));
        orderItems.add(new OrderItem(itemD,20));
        orderItems.add(new OrderItem(itemE,4));
        orderItems.add(new OrderItem(itemC,23));
        orderItems.add(new OrderItem(itemA,3));
        orderItems.add(new OrderItem(itemD,50));

        assertEquals(expected, pricingService.calculateOrderTotal(orderItems));

    }

    @ParameterizedTest
    @CsvSource({"A,10","B,23","C,56"})
    void testPricingService_shouldAddStockItemToListAndReturnStockItem(String sku, int unitPrice) {

        StockItem stockItem = new StockItem(sku, unitPrice);

        pricingService.addStockItem(stockItem);

        assertEquals(stockItem, pricingService.getStockItem(sku));
    }

    @ParameterizedTest
    @CsvSource({
            "T,No StockItem exists for SKU T",
            "R,No StockItem exists for SKU R"
    })
    void testPricingService_shouldThrowStockItemNotFoundException(String sku, String expectedMessage) {
        StockItemNotFoundException stockItemNotFoundException =
                assertThrows(StockItemNotFoundException.class, () -> pricingService.getStockItem(sku));
        assertEquals(expectedMessage, stockItemNotFoundException.getMessage());
    }



    @Test
    void testPricingService_shouldReturnCorrectStockItemWhenStockItemIsReplaced() {

        String sku = "A";

        StockItem firstStockItem = new StockItem(sku, 20);
        StockItem secondStockItem = new StockItem(sku, 230);

        pricingService.addStockItem(firstStockItem);
        pricingService.addStockItem(secondStockItem);

        assertEquals(secondStockItem, pricingService.getStockItem(sku));
    }

    @Test
    void testPricingService_CheckAllStockItemsAreAdded() {

        List<StockItem> stockItems = new ArrayList<>();
        stockItems.add(new StockItem("A", 50));
        stockItems.add(new StockItem("B", 30));
        stockItems.add(new StockItem("C", 20));
        stockItems.add(new StockItem("D", 15));


        Map<StockItem, PricingStrategy> stockItemPricingStrategies = new HashMap<>();
        stockItems.forEach(stockItem -> stockItemPricingStrategies.put(stockItem, null));

        pricingService.setStockItemsAndPricingStrategies(stockItemPricingStrategies);

        stockItems.forEach(stockItem -> assertEquals(stockItem, pricingService.getStockItem(stockItem.getSku())));

    }

    @Test
    void testPricingService_CheckPricingStrategiesAreAddedCorrectly() {

        StockItem stockItemA = new StockItem("A", 50);
        StockItem stockItemB = new StockItem("B", 30);

        Map<StockItem, PricingStrategy> stockItemPricingStrategies = new HashMap<>();
        stockItemPricingStrategies.put(stockItemA, quantityBasedStrategy);
        stockItemPricingStrategies.put(stockItemB, null);

        pricingService.setStockItemsAndPricingStrategies(stockItemPricingStrategies);

        assertEquals(quantityBasedStrategy, pricingService.getPricingStrategy(stockItemA));
        assertEquals(defaultPricingStrategy, pricingService.getPricingStrategy(stockItemB));

    }

}