package com.itv.checkout.pricing;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.InvalidPriceException;
import com.itv.checkout.excpetion.InvalidQuantityException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MultibuyPricingStrategyTest {

    @ParameterizedTest
    @CsvSource({
            "A,50,3,3,130,130",
            "B,30,3,2,45,75",
            "C,30,1,2,45,30",
            "D,30,15,2,45,345",
            "E,20,7,1,10,70"
    })
    void testMultibuyPricing_shouldGenerateExpectedValue(String sku, int unitPrice, int qty, int discountQty,
                                                         int discountPrice, int expected) {
        MultibuyPricingStrategy multibuyPricingStrategy = new MultibuyPricingStrategy(discountQty, discountPrice);

        StockItem stockItem = new StockItem(sku, unitPrice);

        assertEquals(expected, multibuyPricingStrategy.getTotalPrice(stockItem, qty));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void testMultibuyPricing_shouldInvalidQuantityException(int qty) {
        MultibuyPricingStrategy multibuyPricingStrategy = new MultibuyPricingStrategy(1, 1);

        StockItem stockItem = new StockItem("A", 1);

        assertThrows(InvalidQuantityException.class, () -> multibuyPricingStrategy.getTotalPrice(stockItem, qty));

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -12})
    void testMultibuyPricing_shouldThrowInvalidPriceException(int discountPrice) {
        assertThrows(InvalidPriceException.class, () -> new MultibuyPricingStrategy(1, discountPrice));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void testMultibuyPricing_shouldThrowInvalidQuantityException(int discountQty) {
        assertThrows(InvalidQuantityException.class, () -> new MultibuyPricingStrategy(discountQty, 1));
    }

    @Test
    void testMultibuyPricing_objectsShouldBeEqual() {
        MultibuyPricingStrategy pricingStrategyA = new MultibuyPricingStrategy(1,1);
        MultibuyPricingStrategy pricingStrategyB = new MultibuyPricingStrategy(1,1);

        assertEquals(pricingStrategyA, pricingStrategyB);
        assertEquals(pricingStrategyA.hashCode(), pricingStrategyB.hashCode());
    }

    @Test
    void testMultibuyPricing_objectsShouldNotBeEqual() {
        MultibuyPricingStrategy pricingStrategyA = new MultibuyPricingStrategy(1,1);
        MultibuyPricingStrategy pricingStrategyB = new MultibuyPricingStrategy(2,4);

        assertNotEquals(pricingStrategyA, pricingStrategyB);
    }



}