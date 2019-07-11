package ru.pstroganov.revolut.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.pstroganov.revolut.core.AmountSerializer;

import java.math.BigDecimal;

/**
 * Transfer information entity
 * @author pstroganov
 *         Date: 11/07/2019
 */
public final class TransferInfo {

    /** Identifier of the wallet to transfer from */
    private final long fromId;

    /** Identifier of the wallet to transfer to */
    private final long toId;

    /** Transfer amount */
    @JsonSerialize(using = AmountSerializer.class)
    private final BigDecimal amount;

    /**
     * Constructor
     * @param fromId identifier of the wallet to transfer from
     * @param toId   identifier of the wallet to transfer to
     * @param amount transfer amount
     */
    @JsonCreator
    public TransferInfo(@JsonProperty("fromId") long fromId,
                        @JsonProperty("toId") long toId,
                        @JsonProperty("amount") BigDecimal amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
