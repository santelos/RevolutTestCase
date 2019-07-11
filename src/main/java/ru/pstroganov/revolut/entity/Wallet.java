package ru.pstroganov.revolut.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import ru.pstroganov.revolut.core.AmountSerializer;
import ru.pstroganov.revolut.core.exceptions.CoreException;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * User wallet
 * @author pstroganov
 *         Date: 07/07/2019
 */
public final class Wallet {

    /** User identifier */
    private final long id;

    /** Amount of money */
    @JsonSerialize(using = AmountSerializer.class)
    private final BigDecimal money;

    /** Wallet currency */
    private final CurrencyCustom currency;

    /** Wallet status */
    private final Status status;

    /**
     * Constructor
     * @param id       wallet identifier
     * @param money    amount of money on the wallet
     * @param currency currency type
     * @param status   wallet status
     */
    public Wallet(long id, BigDecimal money, CurrencyCustom currency, Status status) {
        this.id = id;
        this.money = money;
        this.currency = currency;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public CurrencyCustom getCurrency() {
        return currency;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return new EqualsBuilder()
                .append(id, wallet.id)
                .append(money, wallet.money)
                .append(currency, wallet.currency)
                .append(status, wallet.status)
                .isEquals();
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", money=" + money +
                ", currency=" + currency +
                ", status=" + status +
                '}';
    }

    /**
     * Statuses for wallet
     */
    public enum Status {

        /** Active status */
        ACTIVE(0),

        /** Blocked status */
        BLOCKED(1);

        /** Status code */
        private final int code;

        /**
         * Constructor
         * @param code status code
         */
        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        /**
         * Find status by its code
         * @param code status code
         * @return found status
         */
        public static Status valueOf(int code) {
            return Arrays.stream(values())
                    .filter(s -> s.getCode() == code)
                    .findFirst()
                    .orElseThrow(() -> new CoreException("Wallet status not found for code: {0}", code));
        }
    }
}
