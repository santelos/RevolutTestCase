package ru.pstroganov.revolut.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.pstroganov.revolut.core.annotations.HttpPath;
import ru.pstroganov.revolut.core.http.HttpResponse;
import ru.pstroganov.revolut.entity.WalletOperation;
import ru.pstroganov.revolut.processing.WalletService;

import java.math.BigDecimal;

/**
 * Http handler on path "wallet/deposit"
 * Handle the money deposit into the wallet
 * @author pstroganov
 *         Date: 10/07/2019
 */
@HttpPath("wallet/deposit")
public class WalletDeposit extends AbstractHandler {

    /**
     * Deposit some amount of money into wallet
     * QueryParams:
     * <pre>
     *     id - wallet ID
     *     amount - amount of money to deposit
     * </pre>
     * @param exchange http exchange
     * @return htp response
     */
    @Override
    protected HttpResponse doPut(HttpExchange exchange) {
        var walletOperation = HandlerUtils.getPayload(exchange, WalletOperation.class);
        return HttpResponse.ok(WalletService.changeAmountInWallet(walletOperation, BigDecimal::add));
    }
}
