package ru.pstroganov.revolut.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.pstroganov.revolut.core.annotations.HttpPath;
import ru.pstroganov.revolut.core.http.HttpResponse;
import ru.pstroganov.revolut.processing.WalletService;

/**
 * Http handler on path "wallet/list"
 * @author pstroganov
 *         Date: 10/07/2019
 */
@HttpPath("wallet/list")
public class WalletListHandler extends AbstractHandler {

    /**
     * Request to find all stored wallets
     * @param exchange http exchange
     * @return list of all stored wallets
     */
    @Override
    protected HttpResponse doGet(HttpExchange exchange) {
        return HttpResponse.ok(WalletService.findAllWallets());
    }
}
