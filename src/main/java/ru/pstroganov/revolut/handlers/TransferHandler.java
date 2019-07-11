package ru.pstroganov.revolut.handlers;

import com.sun.net.httpserver.HttpExchange;
import ru.pstroganov.revolut.core.annotations.HttpPath;
import ru.pstroganov.revolut.core.http.HttpResponse;
import ru.pstroganov.revolut.entity.TransferInfo;
import ru.pstroganov.revolut.processing.TransferService;

/**
 * Http handler for path "transfer"
 * @author pstroganov
 *         Date: 10/07/2019
 */
@HttpPath("transfer")
public class TransferHandler extends AbstractHandler {

    /**
     * Do transfer from one wallet to another
     * @param exchange http exchange
     * @return completed transfer
     */
    @Override
    protected HttpResponse doPost(HttpExchange exchange) {
        var transferInfo = HandlerUtils.getPayload(exchange, TransferInfo.class);
        return HttpResponse.ok(TransferService.transfer(transferInfo));
    }
}
