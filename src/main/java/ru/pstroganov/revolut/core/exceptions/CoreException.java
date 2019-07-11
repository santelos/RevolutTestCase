package ru.pstroganov.revolut.core.exceptions;

import java.text.MessageFormat;

/**
 * Base exception thrown when request goes wrong and 500 code must appear
 * @author pstroganov
 *         Date: 27/06/2019
 */
public class CoreException extends RuntimeException {

    /**
     * Simple exception text
     * @param message message text
     */
    public CoreException(String message) {
        super(message);
    }

    /**
     * Exception message is formatted by {@link MessageFormat}
     * @param pattern formatter pattern
     * @param params  formatter params
     */
    public CoreException(String pattern, Object... params) {
        super(MessageFormat.format(pattern, params));
    }

    /**
     * Exception with cause
     * @param message message text
     * @param cause   cause of this exception
     */
    public CoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception message is formatted by {@link MessageFormat} and with a cause
     * @param message formatter pattern
     * @param cause   cause of this exception
     * @param params  formatter params
     */
    public CoreException(String message, Throwable cause, Object... params) {
        super(MessageFormat.format(message, params), cause);
    }
}
