package com.itv.checkout;

public interface Checkout {

    /**
     * Add a order item to the current checkout flow
     * @param sku the sku of the item being added to the checkout flow
     * @param quantity the number of items being added
     */
    void addOrderLine(String sku, int quantity);

    /**
     * Generate the total for all the order items added to the checkout flow
     * @return the order total
     */
    int getTotal();
}
