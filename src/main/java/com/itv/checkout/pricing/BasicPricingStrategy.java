package com.itv.checkout.pricing;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.InvalidQuantityException;

public class BasicPricingStrategy implements PricingStrategy{

    @Override
    public int getTotalPrice(StockItem stockItem, int quantity) {

        if (quantity<1) {
            throw new InvalidQuantityException();
        }

        return stockItem.getUnitPrice() * quantity;
    }

}
