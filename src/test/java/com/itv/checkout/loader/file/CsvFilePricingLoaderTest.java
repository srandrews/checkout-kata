package com.itv.checkout.loader.file;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.DuplicatePricingDataException;
import com.itv.checkout.loader.file.exception.InvalidPricingDataRowException;
import com.itv.checkout.loader.file.exception.PricingFileNotFoundException;
import com.itv.checkout.pricing.MultibuyPricingStrategy;
import com.itv.checkout.pricing.PricingStrategy;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CsvFilePricingLoaderTest {

    @ParameterizedTest
    @CsvSource({
            "/idontexist.csv, File '/idontexist.csv' not found"
    })
    void testCsvFilePricingLoader_shouldThrowExceptionWhenFileDoesntExist(String filePath, String expectedMessage) {
        PricingFileNotFoundException pricingFileNotFoundException = assertThrows(PricingFileNotFoundException.class, () -> new CsvFilePricingLoader(filePath));
        assertEquals(expectedMessage, pricingFileNotFoundException.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value={
        "/pricing-details-invalid-data-too-many-columns.csv:Invalid number of arguments on row [A,50,3 for 130,23], there should be 2 or 3 values",
        "/pricing-details-invalid-data-too-few-columns.csv:Invalid number of arguments on row [A], there should be 2 or 3 values"
    }, delimiter = ':'  )
    void testCsvFilePricingLoader_shouldThrowInvalidRowData(String filePath, String expectedMessage) {
        CsvFilePricingLoader loader = new CsvFilePricingLoader(filePath);

        InvalidPricingDataRowException invalidPricingDataRowException = assertThrows(InvalidPricingDataRowException.class, loader::getPricingData);
        assertEquals(expectedMessage, invalidPricingDataRowException.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/pricing-details.csv"})
    void testCsvFilePricingLoader_shouldReturnExpectedValues(String filePath) {

        Map<StockItem, PricingStrategy> expected = new HashMap<>();
        expected.put(new StockItem("A", 50),new MultibuyPricingStrategy(3,130));
        expected.put(new StockItem("B", 30),new MultibuyPricingStrategy(2,45));
        expected.put(new StockItem("C", 20),null);
        expected.put(new StockItem("D", 15),null);

        CsvFilePricingLoader loader = new CsvFilePricingLoader(filePath);
        Map<StockItem, PricingStrategy> pricingData = loader.getPricingData();

        assertEquals(expected, pricingData);

    }

    @ParameterizedTest
    @CsvSource(value = {
            "/pricing-details-invalid-data.csv:Unit price is not an integer on row [B, 3sdfg0, 2 for 45]"
    }, delimiterString = ":")
    void testCsvFilePricingLoader_shouldThrowInvalidPricingDataRowException(String filePath, String expectedMessage) {
        CsvFilePricingLoader loader = new CsvFilePricingLoader(filePath);

        InvalidPricingDataRowException invalidPricingDataRowException = assertThrows(InvalidPricingDataRowException.class, loader::getPricingData);
        assertEquals(expectedMessage, invalidPricingDataRowException.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "/pricing-details-missing-data.csv:Sku and Unit price are mandatory [B, , 2 for 45]"
    }, delimiterString = ":")
    void testCsvFilePricingLoader_shouldThrowInvalidPricingDataRowExceptionWhenDataMissing(String filePath, String expectedMessage) {
        CsvFilePricingLoader loader = new CsvFilePricingLoader(filePath);

        InvalidPricingDataRowException invalidPricingDataRowException = assertThrows(InvalidPricingDataRowException.class, loader::getPricingData);
        assertEquals(expectedMessage, invalidPricingDataRowException.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "/pricing-details-with-duplicates.csv:Multiple entries found for SKU(s) [B], please remove duplicates",
            "/pricing-details-with-multiple-duplicates.csv:Multiple entries found for SKU(s) [B, C], please remove duplicates"
    }, delimiterString = ":")
    void testCsvFilePricingLoader_shouldThrowDuplicatePricingDataException(String filePath, String expectedMessage) {
        CsvFilePricingLoader loader = new CsvFilePricingLoader(filePath);

        DuplicatePricingDataException duplicatePricingDataException = assertThrows(DuplicatePricingDataException.class, loader::getPricingData);
        assertEquals(expectedMessage, duplicatePricingDataException.getMessage());
    }

}