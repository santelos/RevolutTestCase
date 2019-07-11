package ru.pstroganov.revolut.core.http;

import java.util.Arrays;

/**
 * Supported http methods
 * @author pstroganov
 *         Date: 06/07/2019
 */
public enum HttpMethod {

    /** Get method */
    GET("GET"),

    /** Post method */
    POST("POST"),

    /** Put method */
    PUT("PUT"),

    /** Delete method */
    DELETE("DELETE"),

    /** Unknown method */
    UNKNOWN("unknown");

    /** String representation of the method */
    private final String str;

    /**
     * Constructor
     * @param str string representation
     */
    HttpMethod(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    /**
     * Find method by string representation
     * If not found returns {@link HttpMethod#UNKNOWN}
     * @return http method or {@link HttpMethod#UNKNOWN}
     */
    public static HttpMethod byStr(String str) {
        return Arrays.stream(values())
                .filter(hm -> hm.getStr().equals(str))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
