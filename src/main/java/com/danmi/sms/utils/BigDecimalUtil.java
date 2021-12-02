package com.danmi.sms.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class BigDecimalUtil {

    /**
     * 除法：保留n位有效数字
     *
     * @param dividend
     * @param divisor
     * @param precision
     * @return
     */
    public static BigDecimal divideRound(BigDecimal dividend, BigDecimal divisor, Integer precision) {
        if (Objects.isNull(dividend) || Objects.isNull(divisor) || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        // n位有效数字
        return dividend.divide(divisor, new MathContext(precision, RoundingMode.HALF_UP));
    }

    /**
     * 乘法：保留n位有效数字
     *
     * @param multiplier
     * @param multiplicator
     * @param precision
     * @return
     */
    public static BigDecimal multiplyRound(BigDecimal multiplier, BigDecimal multiplicator, Integer precision) {
        // n位有效数字
        return multiplier.multiply(multiplicator, new MathContext(precision, RoundingMode.HALF_UP));
    }

    /**
     * 保留n位有效数字
     *
     * @param num
     * @param precision
     * @return
     */
    public static BigDecimal effectiveRound(BigDecimal num, Integer precision) {
        // n位有效数字
        return num.divide(BigDecimal.ONE, new MathContext(precision, RoundingMode.HALF_UP));
    }
}
