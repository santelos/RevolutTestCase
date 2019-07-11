package ru.pstroganov.revolut.processing;

import ru.pstroganov.revolut.datasource.ConnectionsProvider;
import ru.pstroganov.revolut.datasource.Transaction;
import ru.pstroganov.revolut.datasource.dao.WalletDao;
import ru.pstroganov.revolut.entity.TransferInfo;

import java.math.BigDecimal;

/**
 * Service to process transfer operations
 * @author pstroganov
 *         Date: 11/07/2019
 */
public class TransferService {

    /**
     * Process transfer from one wallet to another
     * @param transferInfo information about the transfer
     * @return completed transfer
     */
    public static TransferInfo transfer(TransferInfo transferInfo) {
        try (var tr = Transaction.start(ConnectionsProvider.getConnection())) {
            // collect info about current wallets
            var walletFrom = WalletDao.findWallet(tr.getConnection(), transferInfo.getFromId());
            var walletTo = WalletDao.findWallet(tr.getConnection(), transferInfo.getToId());
            // validation checks
            OperationsValidator.checkAmountOperation(walletFrom, transferInfo.getAmount(), BigDecimal::subtract);
            OperationsValidator.checkAmountOperation(walletTo, transferInfo.getAmount(), BigDecimal::add);
            // change amount of the seder's wallet
            WalletDao.changeAmount(tr.getConnection(), walletFrom, walletFrom.getMoney().subtract(transferInfo.getAmount()));
            // we need to convert the amount to receiver's currency
            BigDecimal convertedAmount = CurrencyConverter.convert(transferInfo.getAmount(), walletFrom.getCurrency(), walletTo.getCurrency());
            // change amount of the receiver's wallet
            WalletDao.changeAmount(tr.getConnection(), walletTo, walletTo.getMoney().add(convertedAmount));
            tr.commit();
            return transferInfo;
        }
    }

}
