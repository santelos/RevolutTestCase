package ru.pstroganov.revolut.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks the http handlers
 * @author pstroganov
 *         Date: 06/07/2019
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpPath {

    /**
     * Returns http path of the handler
     * @return http path
     */
    String value();

}
