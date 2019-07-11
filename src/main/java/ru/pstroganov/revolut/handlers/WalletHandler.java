package ru.pstroganov.revolut.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.pstroganov.revolut.core.annotations.HttpPath;
import ru.pstroganov.revolut.core.exceptions.BadRequestException;
import ru.pstroganov.revolut.core.http.HttpResponse;
import ru.pstroganov.revolut.entity.CurrencyCustom;
import ru.pstroganov.revolut.entity.Wallet;
import ru.pstroganov.revolut.processing.WalletService;

import java.util.Map;

import static ru.pstroganov.revolut.handlers.HandlerUtils.*;

/**
 * Wallet http handler
 * @author pstroganov
 *         Date: 07/07/2019
 */
@HttpPath("wallet")
public class WalletHandler extends AbstractHandler {

    /**
     * Get the wallet info
     * Query params:
     * <pre>
     *      id - identifier of the wallet
     * </>
     * @param exchange http exchange
     * @return response
     */
    @Override
    protected HttpResponse<Wallet> doGet(HttpExchange exchange) {
        Map<String, String> queryParams = getQueryParams(exchange.getRequestURI().getQuery());
        long id = getFromQueryParamsAsLong(queryParams, "id");
        Wallet wallet = WalletService.findWallet(id);
        return HttpResponse.ok(wallet);
    }

    /**
     * Create the wallet
     * @param exchange http exchange
     * @return response
     */
    @Override
    protected HttpResponse doPost(HttpExchange exchange) {
        Map<String, String> queryParams = getQueryParams(exchange.getRequestURI().getQuery());
        int currencyCode = getFromQueryParamsAsInt(queryParams, "currency");
        CurrencyCustom currency = CurrencyCustom.valueOf(currencyCode)
                .orElseThrow(() -> new BadRequestException("Could not parse currency with code {0}", currencyCode));
        return HttpResponse.ok(WalletService.createWallet(currency));
    }

    /**
     * Delete the wallet
     * @param exchange http exchange
     * @return response
     */
    @Override
    protected HttpResponse doDelete(HttpExchange exchange) {
        Map<String, String> queryParams = getQueryParams(exchange.getRequestURI().getQuery());
        long id = getFromQueryParamsAsLong(queryParams, "id");
        WalletService.blockWallet(id);
        return HttpResponse.empty();
    }
}
