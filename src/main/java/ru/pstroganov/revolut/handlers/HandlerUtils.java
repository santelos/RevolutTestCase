package ru.pstroganov.revolut.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import ru.pstroganov.revolut.core.exceptions.BadRequestException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Util class for handlers
 * @author pstroganov
 *         Date: 08/07/2019
 */
class HandlerUtils {

    /**
     * Parse URL to find a query params
     * @param query URL
     * @return mapped query params
     */
    static Map<String, String> getQueryParams(String query) {
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            result.put(entry[0], entry.length > 1 ? entry[1] : "");
        }
        return result;
    }

    /**
     * Lookup for required query property and take it as {@link Long} value
     * @param queryParams provided query parameters
     * @param key         key
     * @return required query property as {@link Long} value
     */
    static Long getFromQueryParamsAsLong(Map<String, String> queryParams, String key) {
        String param = queryParams.get(key);
        if (param == null) {
            throw new BadRequestException("Requested query param \"{0}\" is not provided", key);
        }
        try {
            return Long.parseLong(param);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Requested query param \"{0}\" is not an Long", e, key);
        }
    }

    /**
     * Lookup for required query property and take it as {@link Integer} value
     * @param queryParams provided query parameters
     * @param key         key
     * @return required query property as {@link Integer} value
     */
    static Integer getFromQueryParamsAsInt(Map<String, String> queryParams, String key) {
        String param = queryParams.get(key);
        if (param == null) {
            throw new BadRequestException("Requested query param \"{0}\" is not provided", key);
        }
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Requested query param \"{0}\" is not an Integer", e, key);
        }
    }

    /**
     * Parse request body to get
     * @param exchange http exchange
     * @param clazz    class to parse request body to
     * @param <T>      entity type
     * @return parsed http request body
     */
    static <T> T getPayload(HttpExchange exchange, Class<? extends T> clazz) {
        if (exchange.getRequestHeaders().getFirst("Content-Type").equals("application/json")) {
            try {
                return new ObjectMapper().readValue(exchange.getRequestBody(), clazz);
            } catch (IOException e) {
                throw new BadRequestException("Unable to parse the request payload", e);
            }
        } else {
            throw new BadRequestException("Only application/json content type supported for request body");
        }
    }

}
