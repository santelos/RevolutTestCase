package ru.pstroganov.revolut.core.http;

import java.util.Objects;

/**
 * Http http entity
 * @author pstroganov
 *         Date: 06/07/2019
 */
public final class HttpResponse<T> {

    /** Text body of the http */
    private final T body;

    /** Response code */
    private final HttpCode code;

    /**
     * Constructor
     * @param respCode http code
     * @param body     http body
     */
     private HttpResponse(HttpCode respCode, T body) {
        Objects.requireNonNull(respCode);
        Objects.requireNonNull(body);
        this.body = body;
        this.code = respCode;
    }

    public T getBody() {
        return body;
    }

    public HttpCode getRespCode() {
        return code;
    }

    /**
     * Creates a new http response with empty body
     * @return http with empty body
     */
    public static HttpResponse<String> empty() {
        return new HttpResponse<>(HttpCode.NO_CONTENT, "");
    }

    /**
     * Create SUCCESS http response
     * @param body http response body
     * @return SUCCESS http with body response
     */
    public static <T> HttpResponse<T> ok(T body) {
         return new HttpResponse<>(HttpCode.SUCCESS, body);
    }

    /**
     * Create METHOD NOT ALLOWED http response
     * @param body http response body
     * @return METHOD NOT ALLOWED http with body
     */
    public static <T> HttpResponse<T> notAllowed(T body) {
        return new HttpResponse<>(HttpCode.METHOD_NOT_ALLOWED, body);
    }

    /**
     * Create BAD REQUEST http response
     * @param body http response body
     * @return BAD REQUEST http with body
     */
    public static <T> HttpResponse<T> badRequest(T body) {
        return new HttpResponse<>(HttpCode.BAD_REQUEST, body);
    }

    /**
     * Create INTERNAL SERVER ERROR http response
     * @param body http response body
     * @return INTERNAL SERVER ERROR http with body
     */
    public static <T> HttpResponse<T> error(T body) {
        return new HttpResponse<>(HttpCode.INTERNAL_SERVER_ERROR, body);
    }

    /**
     * Create NOT FOUND http response
     * @return NOT FOUND http with body
     */
    public static HttpResponse notFound() {
        return new HttpResponse<>(HttpCode.NOT_FOUND, "");
    }
}
