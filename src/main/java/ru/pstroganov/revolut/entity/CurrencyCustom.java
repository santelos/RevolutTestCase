package ru.pstroganov.revolut.entity;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

/**
 * Supported currency
 * @author pstroganov
 *         Date: 07/07/2019
 */
public enum CurrencyCustom {

    /** Pound */
    POUND(0, Currency.getInstance(Locale.UK)),

    /** Usd */
    USD(1, Currency.getInstance(Locale.US));

    /** Currency code */
    private final int code;

    /** Java currency */
    private final Currency currency;

    /**
     * Constructor
     * @param code     currency code
     * @param currency java currency
     */
    CurrencyCustom(int code, Currency currency) {
        this.code = code;
        this.currency = currency;
    }

    public int getCode() {
        return code;
    }

    public Currency getCurrency() {
        return currency;
    }

    /**
     * Lookup for currency by its code
     * @param code code of currency
     * @return optional currency
     */
    public static Optional<CurrencyCustom> valueOf(int code) {
        return Arrays.stream(values())
                .filter(cc -> cc.getCode() == code)
                .findFirst();
    }
}
