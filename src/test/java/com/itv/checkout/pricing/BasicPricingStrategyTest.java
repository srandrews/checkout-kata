package com.itv.checkout.pricing;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.InvalidQuantityException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class BasicPricingStrategyTest {

    private final BasicPricingStrategy basicPricingStrategy = new BasicPricingStrategy();

    @ParameterizedTest
    @CsvSource({
            "A,123,2,246",
            "B,12,4,48",
            "C,0,123,0"
    })
    void testBasicPricing_shouldGenerateExpectedValue(String sku, int unitPrice, int qty, int expected) {
        StockItem stockItem = new StockItem(sku, unitPrice);

        assertEquals(expected, basicPricingStrategy.getTotalPrice(stockItem, qty));
    }

    @ParameterizedTest
    @CsvSource({
            "A,12,-1",
            "B,134,0"
    })
    void testBasicPricing_shouldThrowInvalidQuantityException(String sku, int unitPrice, int qty) {
        StockItem stockItem = new StockItem(sku, unitPrice);

        assertThrows(InvalidQuantityException.class, () -> basicPricingStrategy.getTotalPrice(stockItem, qty));
    }



}