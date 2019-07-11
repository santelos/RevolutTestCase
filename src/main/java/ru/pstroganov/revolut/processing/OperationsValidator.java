package ru.pstroganov.revolut.processing;

import ru.pstroganov.revolut.core.exceptions.BadRequestException;
import ru.pstroganov.revolut.entity.Wallet;

import java.math.BigDecimal;

/**
 * Validator for wallet operations
 * @author pstroganov
 *         Date: 11/07/2019
 */
class OperationsValidator {

    /**
     * Check the amount operation
     * @param wallet    wallet information
     * @param amount    amount to operate
     * @param operation process function
     */
    static void checkAmountOperation(Wallet wallet, BigDecimal amount, WalletAmountOperation operation) {
        // wallet must be active
        if (wallet.getStatus().equals(Wallet.Status.BLOCKED)) {
            throw new BadRequestException("Could not change amount in the wallet ID=\"{0}\". This wallet is blocked", wallet.getId());
        }
        // cannot subtract amount to make the wallet amount negative
        if (operation.apply(wallet.getMoney(), amount).compareTo(new BigDecimal(0)) < 0) {
            throw new BadRequestException("Not enough money to do the operation on wallet with ID: {0}", wallet.getId());
        }
    }

    /**
     * Check the block operation
     * @param wallet wallet information
     */
    static void checkBlockOperation(Wallet wallet) {
        // wallet must not be already blocked
        if (wallet.getStatus().equals(Wallet.Status.BLOCKED)) {
            throw new BadRequestException("Wallet with ID \"{0\" already has BLOCKED status", wallet.getId());
        }
    }

}
