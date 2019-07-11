package ru.pstroganov.revolut;

import org.junit.jupiter.api.Test;
import ru.pstroganov.revolut.core.exceptions.BadRequestException;
import ru.pstroganov.revolut.entity.CurrencyCustom;
import ru.pstroganov.revolut.entity.TransferInfo;
import ru.pstroganov.revolut.entity.Wallet;
import ru.pstroganov.revolut.entity.WalletOperation;
import ru.pstroganov.revolut.processing.TransferService;
import ru.pstroganov.revolut.processing.WalletService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Negative tests
 * @author pstroganov
 *         Date: 12/07/2019
 */
class NegativeTests extends AbstractTest {

    /**
     * Test that wallet amount cannot become negative
     */
    @Test
    void testWalletAmountToNegative() {
        assertThrows(BadRequestException.class, () -> {
            var wallet = WalletService.createWallet(CurrencyCustom.POUND);
            var operation = new WalletOperation(wallet.getId(), BigDecimal.valueOf(10));
            WalletService.changeAmountInWallet(operation, BigDecimal::subtract);
        });
    }

    /**
     * Test that no amount operation available on blocked wallet
     */
    @Test
    void testBlockedWalletOperation() {
        assertThrows(BadRequestException.class, () -> {
            var wallet = WalletService.createWallet(CurrencyCustom.POUND);
            WalletService.blockWallet(wallet.getId());
            var operation = new WalletOperation(wallet.getId(), BigDecimal.valueOf(10));
            WalletService.changeAmountInWallet(operation, BigDecimal::add);
        });
    }

    /**
     * Test that wallet info won't be changed if the transfer fails
     */
    @Test
    void testTransferFailureDoesNotAffect() {
        // init two wallets
        var expectedFirstWallet = WalletService.createWallet(CurrencyCustom.POUND);
        var expectedSecondWallet = WalletService.createWallet(CurrencyCustom.POUND);

        // do transfer
        try {
            var transferInfo = new TransferInfo(expectedFirstWallet.getId(), expectedSecondWallet.getId(), BigDecimal.valueOf(10));
            TransferService.transfer(transferInfo);
        } catch (Exception ignored) {}

        // get actual values
        var actualFirstWallet = WalletService.findWallet(expectedFirstWallet.getId());
        var actualSecondWallet = WalletService.findWallet(expectedSecondWallet.getId());
        assertArrayEquals(new Wallet[] {expectedFirstWallet, expectedSecondWallet}, new Wallet[] {actualFirstWallet, actualSecondWallet});
    }

}
