package ru.pstroganov.revolut.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.pstroganov.revolut.core.annotations.HttpPath;
import ru.pstroganov.revolut.core.http.HttpResponse;
import ru.pstroganov.revolut.entity.WalletOperation;
import ru.pstroganov.revolut.processing.WalletService;

import java.math.BigDecimal;

/**
 * Http handler for path "wallet/withdraw"
 * Handle withdraw from the wallet
 * @author pstroganov
 *         Date: 10/07/2019
 */
@HttpPath("wallet/withdraw")
public class WalletWithdraw extends AbstractHandler {

    @Override
    protected HttpResponse doPut(HttpExchange exchange) {
        var walletOperation = HandlerUtils.getPayload(exchange, WalletOperation.class);
        return HttpResponse.ok(WalletService.changeAmountInWallet(walletOperation, BigDecimal::subtract));
    }
}
