package ru.pstroganov.revolut.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.pstroganov.revolut.core.exceptions.BadRequestException;
import ru.pstroganov.revolut.core.exceptions.CoreException;
import ru.pstroganov.revolut.core.http.HttpCode;
import ru.pstroganov.revolut.core.http.HttpMethod;
import ru.pstroganov.revolut.core.http.HttpResponse;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Abstract implementation of http handler
 * All the http methods returns {@link HttpCode#INTERNAL_SERVER_ERROR} by default
 * If the incoming http method are not supported - returns {@link HttpCode#BAD_REQUEST}
 * @author pstroganov
 *         Date: 06/07/2019
 */
public abstract class AbstractHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // Handle request with exception handling
        HttpResponse response;
        try {
            response = dispatchRequest(exchange);
        } catch (BadRequestException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            response = HttpResponse.badRequest(e.getMessage());
        } catch (CoreException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            response = HttpResponse.error(MessageFormat.format("Internal server error\n", e));
        }

        // Post process the request
        var resultInStr = new ObjectMapper().writeValueAsString(response.getBody());
        exchange.sendResponseHeaders(response.getRespCode().getCode(), resultInStr.length());
        if (!resultInStr.isBlank()) {
            var output = exchange.getResponseBody();
            output.write(resultInStr.getBytes());
            output.flush();
            exchange.close();
        }

        // log the access event
        System.out.println(MessageFormat.format("ACCESS: Method: \"{0}\", Path: \"{1}\", Status: \"{2}({3})\"",
                exchange.getRequestMethod(), exchange.getRequestURI().getPath(), response.getRespCode(), response.getRespCode().getCode()));
    }

    /**
     * Choose which http method should be invoked and invoke it
     * @param exchange http exchange
     * @return response
     */
    private HttpResponse dispatchRequest(HttpExchange exchange) {
        switch (HttpMethod.byStr(exchange.getRequestMethod())) {
            case GET:
                return doGet(exchange);
            case POST:
                return doPost(exchange);
            case PUT:
                return doPut(exchange);
            case DELETE:
                return doDelete(exchange);
            default:
                return HttpResponse.badRequest("Method " + exchange.getRequestMethod() + " not supported");
        }
    }

    /**
     * Handle GET request method
     * @return Http http
     */
    protected HttpResponse doGet(HttpExchange exchange) {
        return HttpResponse.notAllowed("Method not allowed");
    }

    /**
     * Handle POST request method
     * @return Http http
     */
    protected HttpResponse doPost(HttpExchange exchange) {
        return HttpResponse.notAllowed("Method not allowed");
    }

    /**
     * Handle PUT request method
     * @return Http http
     */
    protected HttpResponse doPut(HttpExchange exchange) {
        return HttpResponse.notAllowed("Method not allowed");
    }

    /**
     * Handle DELETE request method
     * @return Http http
     */
    protected HttpResponse doDelete(HttpExchange exchange) {
        return HttpResponse.notAllowed("Method not allowed");
    }
}
