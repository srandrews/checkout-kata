package com.itv.checkout.loader;

import com.itv.checkout.excpetion.InvalidSpecialPriceFormatException;
import com.itv.checkout.pricing.MultibuyPricingStrategy;
import com.itv.checkout.pricing.PricingStrategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PricingStrategyFactory {

    private static final String MULTIBUY_PRICING_FORMAT = "^(\\d+)(.for.)(\\d+)";

    public static PricingStrategy createPricingStrategy(String pricingStrategyText) {

        if (pricingStrategyText==null || "".equals(pricingStrategyText)) {
            return null;
        }

        if (pricingStrategyText.matches(MULTIBUY_PRICING_FORMAT)) {
            return generateMultibuyPricingStrategy(pricingStrategyText);
        }

        throw new InvalidSpecialPriceFormatException(pricingStrategyText);

    }

    private static PricingStrategy generateMultibuyPricingStrategy(String pricingStrategyText) {
        Pattern pattern = Pattern.compile(MULTIBUY_PRICING_FORMAT);
        Matcher matcher = pattern.matcher(pricingStrategyText);
        if (matcher.find()) {
            return new MultibuyPricingStrategy(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(3)));
        } else {
            throw new InvalidSpecialPriceFormatException(pricingStrategyText);
        }
    }

}
