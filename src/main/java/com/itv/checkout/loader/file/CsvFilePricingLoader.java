package com.itv.checkout.loader.file;

import com.itv.checkout.domain.StockItem;
import com.itv.checkout.excpetion.DuplicatePricingDataException;
import com.itv.checkout.excpetion.InvalidPricingDataException;
import com.itv.checkout.loader.PricingStrategyFactory;
import com.itv.checkout.loader.file.exception.InvalidPricingDataRowException;
import com.itv.checkout.loader.file.exception.PricingFileNotFoundException;
import com.itv.checkout.loader.PricingLoader;
import com.itv.checkout.loader.file.domain.PricingLine;
import com.itv.checkout.pricing.PricingStrategy;
import com.itv.checkout.utils.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reads pricing data from a CSV file.
 *
 * File must have three columns with the following columns:
 *
 * sku, unit price, special price
 *
 * sku and unit price are mandatory, special price is optional.
 *
 * The first row of the file is assumed to be a header row and is skipped during processing
 */
public class CsvFilePricingLoader implements PricingLoader {

    private static final int MINIMUM_ROW_VALUES = 2;
    private static final int MAXIMUM_ROW_VALUES = 3;
    private static final String CSV_DELIMITER = ",";

    private static final String INVALID_NUMBER_OR_ROWS_MESSAGE = "Invalid number of arguments on row [%s], there should be %d or %d values";
    private static final String FILE_NOT_FOUND_MESSAGE = "File '%s' not found";
    private static final String NOT_A_VALID_FILE_PATH_MESSAGE = "'%s' is not a valid file path";
    private static final String UNIT_PRICE_NOT_AN_INTEGER = "Unit price is not an integer on row %s";
    private static final String MANDATORY_DATA_MISSING_MESSAGE = "Sku and Unit price are mandatory %s";
    private static final String DUPLICATE_SKUS_FOUND_MESSAGE = "Multiple entries found for SKU(s) [%s], please remove duplicates";

    private Path path;

    public CsvFilePricingLoader(String pricingFilePath) {
        path = Paths.get(pricingFilePath);

        if (!Files.exists(path)) {
            URL url = CsvFilePricingLoader.class.getResource(pricingFilePath);

            if (url == null) {
                throw new PricingFileNotFoundException(String.format(FILE_NOT_FOUND_MESSAGE, pricingFilePath));
            }

            try {
                path = Paths.get(url.toURI());
            } catch (URISyntaxException e) {
                throw new PricingFileNotFoundException(String.format(NOT_A_VALID_FILE_PATH_MESSAGE, pricingFilePath));
            }

            if (!Files.exists(path) ) {
                throw new PricingFileNotFoundException(String.format(FILE_NOT_FOUND_MESSAGE, pricingFilePath));
            }
        }

    }

    @Override
    public Map<StockItem, PricingStrategy> getPricingData() {

        List<PricingLine> pricingLines = new ArrayList<>();

        try (Stream<String> lines = Files.lines(path)) {
            lines.skip(1)
                    .map(this::processLine)
                    .forEach(pricingLines::add);
        } catch (IOException ioException) {
            throw new InvalidPricingDataException(ioException);
        }

        checkForDuplicateSkus(pricingLines);

        //Collectors.toMap throws NPE when value is null, so using this as a workaround
        Map<StockItem, PricingStrategy> pricingData = new HashMap<>();
        pricingLines.forEach(pricingLine -> pricingData.put(toStockItem(pricingLine), generatePricingStrategy(pricingLine)));

        return pricingData;
    }

    private void checkForDuplicateSkus(List<PricingLine> pricingLines) {
        List<String> duplicateSkus =  pricingLines.stream()
                .collect(Collectors.toMap(PricingLine::getItem,pricingLine -> 1, Integer::sum))
                .entrySet()
                .stream()
                .filter(stringIntegerEntry -> stringIntegerEntry.getValue()>1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        if (duplicateSkus.size()>0) {
            throw new DuplicatePricingDataException(
                    String.format(DUPLICATE_SKUS_FOUND_MESSAGE, String.join(", ", duplicateSkus)));
        }

    }

    private StockItem toStockItem(PricingLine pricingLine) {

        int unitPrice;

        if (StringUtils.emptyString(pricingLine.getItem()) || StringUtils.emptyString(pricingLine.getUnitPrice())) {
            throw new InvalidPricingDataRowException(String.format(MANDATORY_DATA_MISSING_MESSAGE,pricingLine));
        }

        try {
            unitPrice = Integer.parseInt(pricingLine.getUnitPrice());
        } catch (NumberFormatException nfe) {
            throw new InvalidPricingDataRowException(String.format(UNIT_PRICE_NOT_AN_INTEGER,pricingLine));
        }

        return new StockItem(pricingLine.getItem(), unitPrice);
    }

    private PricingStrategy generatePricingStrategy(PricingLine pricingLine) {
        return PricingStrategyFactory.createPricingStrategy(pricingLine.getSpecialPrice());
    }

    private PricingLine processLine(String line) {
        String[] values = line.split(CSV_DELIMITER);

        if (values.length < MINIMUM_ROW_VALUES || values.length > MAXIMUM_ROW_VALUES) {
            throw new InvalidPricingDataRowException(String.format(INVALID_NUMBER_OR_ROWS_MESSAGE, line,
                    MINIMUM_ROW_VALUES, MAXIMUM_ROW_VALUES));
        }

        String specialPrice = null;

        if (values.length == 3) {
            specialPrice = values[2];
        }

        return new PricingLine(values[0], values[1], specialPrice);
    }
}
