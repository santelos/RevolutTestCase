package ru.pstroganov.revolut.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.pstroganov.revolut.core.AmountSerializer;

import java.math.BigDecimal;

/**
 * Simple amount operation for single wallet
 * @author pstroganov
 *         Date: 11/07/2019
 */
public final class WalletOperation {

    /** Wallet identifier */
    private final long walletId;

    /** change amount */
    @JsonSerialize(using = AmountSerializer.class)
    private final BigDecimal amount;

    /**
     * Constructor
     * @param walletId Wallet identifier
     * @param amount   change amount
     */
    @JsonCreator
    public WalletOperation(@JsonProperty("walletId") long walletId,
                           @JsonProperty("amount") BigDecimal amount) {
        this.walletId = walletId;
        this.amount = amount;
    }

    public long getWalletId() {
        return walletId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
