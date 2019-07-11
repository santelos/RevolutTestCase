package ru.pstroganov.revolut;

import org.junit.jupiter.api.Test;
import ru.pstroganov.revolut.entity.CurrencyCustom;
import ru.pstroganov.revolut.entity.TransferInfo;
import ru.pstroganov.revolut.entity.Wallet;
import ru.pstroganov.revolut.entity.WalletOperation;
import ru.pstroganov.revolut.processing.CurrencyConverter;
import ru.pstroganov.revolut.processing.TransferService;
import ru.pstroganov.revolut.processing.WalletService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Positive tests for the API
 * @author pstroganov
 *         Date: 11/07/2019
 */
class PositiveTests extends AbstractTest {

    /**
     * Test for wallet creation
     */
    @Test
    void testWalletCreation() {
        var actualWallet = WalletService.createWallet(CurrencyCustom.POUND);
        var expectedWallet = new Wallet(actualWallet.getId(), BigDecimal.ZERO, CurrencyCustom.POUND, Wallet.Status.ACTIVE);
        assertEquals(expectedWallet, actualWallet);
    }

    /**
     * Test searching for wallet
     */
    @Test
    void testGetWallet() {
        var actualWallet = WalletService.createWallet(CurrencyCustom.POUND);
        var expectedWallet = WalletService.findWallet(actualWallet.getId());
        assertEquals(expectedWallet, actualWallet);
    }

    /**
     * Test searching for all wallets
     */
    @Test
    void testGetWallets() {
        Wallet[] actualWallets = new Wallet[2];
        actualWallets[0] = WalletService.createWallet(CurrencyCustom.POUND);
        actualWallets[1] = WalletService.createWallet(CurrencyCustom.USD);
        var expectedWallets = WalletService.findAllWallets();
        assertArrayEquals(expectedWallets.toArray(), actualWallets);
    }

    /**
     * Test for amount in wallet change
     */
    @Test
    void testChangeAmountInWallet() {
        var wallet = WalletService.createWallet(CurrencyCustom.POUND);
        var operationInfo = new WalletOperation(wallet.getId(), BigDecimal.valueOf(50.5));
        var actualWallet = WalletService.changeAmountInWallet(operationInfo, BigDecimal::add);
        var expectedWallet = new Wallet(wallet.getId(), wallet.getMoney().add(BigDecimal.valueOf(50.5)), wallet.getCurrency(), wallet.getStatus());
        assertEquals(expectedWallet, actualWallet);
    }

    /**
     * Test for block the wallet
     */
    @Test
    void testBlockWallet() {
        var wallet = WalletService.createWallet(CurrencyCustom.POUND);
        WalletService.blockWallet(wallet.getId());
        var actualWallet = WalletService.findWallet(wallet.getId());
        var expectedWallet = new Wallet(wallet.getId(), wallet.getMoney(), wallet.getCurrency(), Wallet.Status.BLOCKED);
        assertEquals(expectedWallet, actualWallet);
    }

    /**
     * Test transfer money
     */
    @Test
    void testTransfer() {
        // init two wallets
        var firstWallet = WalletService.createWallet(CurrencyCustom.POUND);
        var secondWallet = WalletService.createWallet(CurrencyCustom.POUND);
        firstWallet = WalletService.changeAmountInWallet(new WalletOperation(firstWallet.getId(), BigDecimal.valueOf(20)), BigDecimal::add);
        secondWallet = WalletService.changeAmountInWallet(new WalletOperation(secondWallet.getId(), BigDecimal.valueOf(20)), BigDecimal::add);

        // do transfer
        var transferInfo = new TransferInfo(firstWallet.getId(), secondWallet.getId(), BigDecimal.valueOf(10));
        TransferService.transfer(transferInfo);

        // configure expected values
        var expectedFirstWallet = new Wallet(firstWallet.getId(), firstWallet.getMoney().subtract(BigDecimal.valueOf(10)),
                firstWallet.getCurrency(), firstWallet.getStatus());
        var expectedSecondWallet = new Wallet(secondWallet.getId(), secondWallet.getMoney().add(BigDecimal.valueOf(10)),
                secondWallet.getCurrency(), secondWallet.getStatus());

        // get actual values
        var actualFirstWallet = WalletService.findWallet(firstWallet.getId());
        var actualSecondWallet = WalletService.findWallet(secondWallet.getId());
        assertArrayEquals(new Wallet[] {expectedFirstWallet, expectedSecondWallet}, new Wallet[] {actualFirstWallet, actualSecondWallet});
    }

    /**
     * Test currency conversion
     */
    @Test
    void testCurrencyConversion() {
        var value = BigDecimal.valueOf(10);
        var actualValue = CurrencyConverter.convert(value, CurrencyCustom.POUND, CurrencyCustom.USD);
        var expectedValue = CurrencyConverter.RatesStub.POUND_USD.getRate().multiply(value);
        assertEquals(expectedValue, actualValue);
    }

}
