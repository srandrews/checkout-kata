package com.itv.checkout.loader;

import com.itv.checkout.excpetion.InvalidSpecialPriceFormatException;
import com.itv.checkout.pricing.MultibuyPricingStrategy;
import com.itv.checkout.pricing.PricingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PricingStrategyFactoryTest {

    @ParameterizedTest
    @CsvSource({
            "3 for 10, 3, 10",
            "5 for 123, 5, 123",
            "12 for 5555, 12, 5555",
    })
    void testPricingStrategyFactory_shouldGenerateAMultibuyPricingStrategy(String specialPriceString, int expectedQty, int expectedPrice) {

        PricingStrategy expected = new MultibuyPricingStrategy(expectedQty, expectedPrice);

        PricingStrategy pricingStrategy = PricingStrategyFactory.createPricingStrategy(specialPriceString);

        assertEquals(expected, pricingStrategy);
    }

    @Test
    void testPricingStrategyFactory_shouldReturnNull() {
        assertNull(PricingStrategyFactory.createPricingStrategy(null));
        assertNull(PricingStrategyFactory.createPricingStrategy(""));
    }

    @ParameterizedTest
    @CsvSource({
            "rubbish string, The specified special price text (rubbish string) does not match the any expected values.",
            "3.5 for 123, The specified special price text (3.5 for 123) does not match the any expected values."
    })
    void testPricingStrategyFactory_shouldThrowInvalidSpecialPriceFormatException(String specialPriceString, String exceptionMessage) {
        InvalidSpecialPriceFormatException invalidSpecialPriceFormatException =
                assertThrows(InvalidSpecialPriceFormatException.class, () -> PricingStrategyFactory.createPricingStrategy(specialPriceString), exceptionMessage);
        assertEquals(exceptionMessage, invalidSpecialPriceFormatException.getMessage());
    }

}