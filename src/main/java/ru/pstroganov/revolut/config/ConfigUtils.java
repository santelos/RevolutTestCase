package ru.pstroganov.revolut.config;

import ru.pstroganov.revolut.core.exceptions.CoreException;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Utils for the config package
 * @author pstroganov
 *         Date: 06/07/2019
 */
class ConfigUtils {

    /** Default constructor override */
    private ConfigUtils() {
        throw new IllegalStateException("No ConfigUtils instances for you");
    }

    /**
     * Lookup for the resource in the {@link Properties} or return the default value
     * @param key          key of the resource
     * @param properties   configuration properties
     * @param defaultValue default value if the resource not found
     * @param <T>          type of the resource
     * @return resource or the default value
     */
    @SuppressWarnings("unchecked")
    static <T> T getPropOrDefault(String key, Properties properties, T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }

    /**
     * Creates an instance of a Class vai reflection
     * @param clazz class to instantiate
     * @param <T>   type of the created instance
     * @return instance of a class
     */
    @SuppressWarnings("unchecked")
    static <T> T createHandlerInstance(Class clazz) {
        try {
            return (T) clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CoreException("Error reading the server.properties", e);
        }
    }
}
