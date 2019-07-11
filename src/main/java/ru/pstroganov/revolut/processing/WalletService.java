package ru.pstroganov.revolut.processing;

import ru.pstroganov.revolut.datasource.ConnectionsProvider;
import ru.pstroganov.revolut.datasource.Transaction;
import ru.pstroganov.revolut.datasource.dao.WalletDao;
import ru.pstroganov.revolut.entity.CurrencyCustom;
import ru.pstroganov.revolut.entity.Wallet;
import ru.pstroganov.revolut.entity.WalletOperation;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service to process wallet operations
 * @author pstroganov
 *         Date: 08/07/2019
 */
public class WalletService {

    /**
     * Find all wallets
     * @return list of wallets
     */
    public static List<Wallet> findAllWallets() {
        return WalletDao.findWallets(ConnectionsProvider.getConnection());
    }

    /**
     * Get wallet by id
     * @param id identifier
     * @return found wallet
     */
    public static Wallet findWallet(long id) {
        return WalletDao.findWallet(ConnectionsProvider.getConnection(), id);
    }

    /**
     * Create a wallet and put it in database
     * @param currencyType currency type
     * @return created wallet
     */
    public static Wallet createWallet(CurrencyCustom currencyType) {
        try (var tr = Transaction.start(ConnectionsProvider.getConnection())) {
            var wallet = new Wallet(-1,  BigDecimal.ZERO, currencyType, Wallet.Status.ACTIVE);
            var newWallet = WalletDao.insertWallet(tr.getConnection(), wallet);
            tr.commit();
            return newWallet;
        }
    }

    /**
     * Adds amount to the wallet
     * @param info      values to operate in operation
     * @param operation function to apply on current wallet amount
     * @return new wallet with amount added
     */
    public static Wallet changeAmountInWallet(WalletOperation info, WalletAmountOperation operation) {
        try (var tr = Transaction.start(ConnectionsProvider.getConnection())) {
            var wallet = WalletDao.findWallet(tr.getConnection(), info.getWalletId());
            OperationsValidator.checkAmountOperation(wallet, info.getAmount(), operation);
            BigDecimal newAmount = operation.apply(wallet.getMoney(), info.getAmount());
            var newWallet = WalletDao.changeAmount(tr.getConnection(), wallet, newAmount);
            tr.commit();
            return newWallet;
        }
    }

    /**
     * Block wallet with id
     * If wallet is already blocked return BAD_REQUEST
     * @param id id
     */
    public static void blockWallet(long id) {
        try (var tr = Transaction.start(ConnectionsProvider.getConnection())) {
            var wallet = WalletDao.findWallet(tr.getConnection(), id);
            OperationsValidator.checkBlockOperation(wallet);
            WalletDao.changeWalletStatus(tr.getConnection(), wallet, Wallet.Status.BLOCKED);
            tr.commit();
        }
    }

}
