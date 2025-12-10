package sample.application.api.shared.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A class with mathematical utility functions.
 * @author Manoel Campos
 */
public final class MathUtil {
    /** Private constructor to prevent instantiating the class */
    private MathUtil() { throw new UnsupportedOperationException(); }

    /**
     * {@return the average from a set of already summed values}
     * @param sum the sum of the values to calculate the average
     * @param total total number of items to calculate the average
     */
    public static double average(final double sum, final int total) {
        return total == 0 ? 0 : sum/total;
    }

    /**
     * Formats a double value representing a percentage, rounding it to 2 decimal places.
     * @param percent percentage value to be formatted
     * @return the value with 2 decimal places
     */
    public static double formatPercent(final double percent){
        final int decimalPlaces = 2;
        return BigDecimal.valueOf(percent).setScale(decimalPlaces, RoundingMode.HALF_UP).doubleValue();
    }
}
