package com.itv.checkout;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.loader.PricingLoader;
import com.itv.checkout.pricing.MultibuyPricingStrategy;
import com.itv.checkout.pricing.PricingService;
import com.itv.checkout.pricing.PricingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultCheckoutTest {

    @Mock
    private PricingLoader loader;

    @Mock
    private PricingService pricingService;

    @Test
    void testDefaultCheckout_ensureCorrectTotalIsReturned() {
        int expected = 5421;
        when(pricingService.calculateOrderTotal(any())).thenReturn(expected);
        Checkout checkout = new DefaultCheckout(loader, pricingService);
        assertEquals(expected, checkout.getTotal());
    }

    @Test
    void testDefaultCheckout_shouldGenerateExpectedTotal() {

        int expected = 995;

        Map<StockItem, PricingStrategy> pricingData = new HashMap<>();
        pricingData.put(new StockItem("A", 50),new MultibuyPricingStrategy(3,130));
        pricingData.put(new StockItem("B", 30),new MultibuyPricingStrategy(2,45));
        pricingData.put(new StockItem("C", 20),null);
        pricingData.put(new StockItem("D", 15),null);

        when(loader.getPricingData()).thenReturn(pricingData);

        Checkout checkout = new DefaultCheckout(loader);

        checkout.addOrderLine("A", 2);
        checkout.addOrderLine("B", 6);
        checkout.addOrderLine("C", 12);
        checkout.addOrderLine("A", 12);

        assertEquals(expected, checkout.getTotal());

    }
}