package com.itv.checkout;

import com.itv.checkout.excpetion.InvalidQuantityException;
import com.itv.checkout.excpetion.StockItemNotFoundException;
import com.itv.checkout.loader.PricingLoader;
import com.itv.checkout.loader.file.CsvFilePricingLoader;
import com.itv.checkout.loader.file.exception.InvalidPricingDataRowException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutIT {

    @Test
    void testCheckout_usingConcreteClassesForAllSteps() {

        int expected = 2735;

        PricingLoader pricingLoader = new CsvFilePricingLoader("/pricing-details.csv");

        Checkout checkout = new DefaultCheckout(pricingLoader);

        checkout.addOrderLine("A", 3);
        assertThrows(StockItemNotFoundException.class, () -> checkout.addOrderLine("F", 4));
        checkout.addOrderLine("D", 12);
        checkout.addOrderLine("A", 17);
        assertThrows(InvalidQuantityException.class, () -> checkout.addOrderLine("B", -54));
        checkout.addOrderLine("B", 54);
        checkout.addOrderLine("C", 23);

        assertEquals(expected, checkout.getTotal());

    }

    @Test
    void testCheckout_shouldReturn0WhenNoItemsAdded() {
        int expected = 0;

        PricingLoader pricingLoader = new CsvFilePricingLoader("/pricing-details.csv");

        Checkout checkout = new DefaultCheckout(pricingLoader);

        assertEquals(expected, checkout.getTotal());
    }

    @Test
    void testCheckout_shouldThrowInvalidPricingDataRowExceptionWhenCSVFileHasBadData() {
        PricingLoader pricingLoader = new CsvFilePricingLoader("/pricing-details-invalid-data.csv");
        InvalidPricingDataRowException invalidPricingDataRowException =
                assertThrows(InvalidPricingDataRowException.class, () -> new DefaultCheckout(pricingLoader));
        assertEquals("Unit price is not an integer on row [B, 3sdfg0, 2 for 45]",
                invalidPricingDataRowException.getMessage());
    }

}
