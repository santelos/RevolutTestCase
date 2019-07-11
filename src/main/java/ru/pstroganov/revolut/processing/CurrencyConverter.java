package ru.pstroganov.revolut.processing;

import ru.pstroganov.revolut.core.exceptions.CoreException;
import ru.pstroganov.revolut.entity.CurrencyCustom;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Class for currency conversion
 * @author pstroganov
 *         Date: 07/07/2019
 */
public class CurrencyConverter {

    /**
     * Convert amount from one currency to another
     * @param amount amount to convert
     * @param from   currency from
     * @param to     currency to
     * @return converted amount
     */
    public static BigDecimal convert(BigDecimal amount, CurrencyCustom from, CurrencyCustom to) {
        return from.equals(to) ? amount : amount.multiply(getRate(from, to));
    }

    /**
     * Find currency rate
     * @param from Currency from
     * @param to   Currency to
     * @return Currency rate
     */
    private static BigDecimal getRate(CurrencyCustom from, CurrencyCustom to) {
        return Arrays.stream(RatesStub.values())
                .filter(rs -> rs.first == from && rs.second == to)
                .map(rs -> rs.rate)
                .findFirst()
                .orElseThrow(() -> new CoreException("Currency rate for \"{0}\" to \"{1}\" not found",
                        from.getCurrency().getDisplayName(), to.getCurrency().getDisplayName()));
    }

    /**
     * Stub for the currency rates
     */
    public enum RatesStub {

        /** POUND to USD*/
        POUND_USD(BigDecimal.valueOf(0.016D), CurrencyCustom.POUND, CurrencyCustom.USD),

        /** USD to POUND */
        USD_POUND(BigDecimal.valueOf(63.81D), CurrencyCustom.USD, CurrencyCustom.POUND);

        /** Currency rate */
        private final BigDecimal rate;

        /** Currency from */
        private final CurrencyCustom first;

        /** Currency to */
        private final CurrencyCustom second;

        /**
         * Constructor
         * @param rate   currency rate
         * @param first  currency from
         * @param second currency to
         */
        RatesStub(BigDecimal rate, CurrencyCustom first, CurrencyCustom second) {
            this.rate = rate;
            this.first = first;
            this.second = second;
        }

        public BigDecimal getRate() {
            return rate;
        }

        public CurrencyCustom getFirst() {
            return first;
        }

        public CurrencyCustom getSecond() {
            return second;
        }
    }

}
