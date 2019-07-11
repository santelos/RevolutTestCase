package ru.pstroganov.revolut.core.exceptions;

/**
 * Exception thrown when request is identified as BAD_REQUEST
 * @author pstroganov
 *         Date: 08/07/2019
 */
public class BadRequestException extends CoreException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Object... params) {
        super(message, params);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }
}
