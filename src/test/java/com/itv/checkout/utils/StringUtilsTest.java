package com.itv.checkout.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void testStringUtils_checkForEmptyString() {

        assertTrue(StringUtils.emptyString(""));
        assertTrue(StringUtils.emptyString(null));
        assertFalse(StringUtils.emptyString("astring"));
    }

}