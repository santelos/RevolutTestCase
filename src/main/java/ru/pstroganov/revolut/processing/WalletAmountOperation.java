package ru.pstroganov.revolut.processing;

import java.math.BigDecimal;

/**
 * Functional interface to apply changes on current amount in wallet
 * @author pstroganov
 *         Date: 10/07/2019
 */
@FunctionalInterface
public interface WalletAmountOperation {

    /**
     * Process the current amount of money in wallet with the passed value
     * @param currentValue amount of money that's currently in wallet
     * @param passedValue  value to apply on current value
     * @return result of applying passed value on the current
     */
    BigDecimal apply(BigDecimal currentValue, BigDecimal passedValue);

}
