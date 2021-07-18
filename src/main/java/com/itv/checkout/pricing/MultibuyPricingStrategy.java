package com.itv.checkout.pricing;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.InvalidPriceException;
import com.itv.checkout.excpetion.InvalidQuantityException;

import java.util.Objects;

public class MultibuyPricingStrategy implements PricingStrategy{

    private final int discountQty;

    private final int discountPrice;

    public MultibuyPricingStrategy(int discountQty, int discountPrice) {

        if ( discountPrice < 0 ) {
            throw new InvalidPriceException();
        }

        if ( discountQty < 1 ) {
            throw new InvalidQuantityException();
        }

        this.discountQty = discountQty;
        this.discountPrice = discountPrice;
    }

    @Override
    public int getTotalPrice(StockItem stockItem, int quantity) {

        if (quantity<1) {
            throw new InvalidQuantityException();
        }

        return ((quantity / discountQty) * discountPrice)
                + ((quantity % discountQty) * stockItem.getUnitPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MultibuyPricingStrategy that = (MultibuyPricingStrategy) o;
        return discountQty == that.discountQty && discountPrice == that.discountPrice;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discountQty, discountPrice);
    }
}
