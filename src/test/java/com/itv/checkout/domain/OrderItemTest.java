package com.itv.checkout.domain;

import com.itv.checkout.excpetion.InvalidQuantityException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    void testReceiptItem_shouldThroughInvalidQuantityExceptionWhenQuantityBelowOne() {
        assertThrows(InvalidQuantityException.class, () -> new OrderItem(new StockItem("A", 1), -1));
    }


}