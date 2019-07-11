package ru.pstroganov.revolut.core.http;

/**
 * Http codes
 * @author pstroganov
 *         Date: 06/07/2019
 */
public enum HttpCode {

    /** Success */
    SUCCESS(200),

    /** No content */
    NO_CONTENT(204),

    /** Bad request */
    BAD_REQUEST(400),

    /** Not found */
    NOT_FOUND(404),

    /** Method not allowed */
    METHOD_NOT_ALLOWED(405),

    /** Internal server error */
    INTERNAL_SERVER_ERROR(500);

    /** Numeric code */
    private final int code;

    /**
     * Constructor
     * @param code numeric code
     */
    HttpCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
