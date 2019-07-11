package ru.pstroganov.revolut.config;

import ru.pstroganov.revolut.core.exceptions.CoreException;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration properties provider
 * @author pstroganov
 *         Date: 07/07/2019
 */
public class PropertiesProvider {

    /** Properties file name */
    private static final String SERVER_PROPERTIES_FILENAME = "server.properties";

    /** Properties */
    private static Properties properties = new Properties();

    /* Static initialization of the properties */
    static {
        try {
            String systemConfigPath = System.getProperty("revolut.config.path");
            if (Objects.nonNull(systemConfigPath)) {
                System.out.println("Use external properties: " + systemConfigPath);
                properties.load(Files.newInputStream(Paths.get(systemConfigPath)));
            } else {
                System.out.println("Use default properties");
                properties.load(PropertiesProvider.class.getClassLoader().getResourceAsStream(SERVER_PROPERTIES_FILENAME));
            }
        }  catch (IOException e) {
            throw new CoreException("Error reading the properties file", e);
        }
    }

    /**
     * Gets a resource as an Object
     * @param key key of the resource
     * @return resource as an Object
     */
    public static Object get(String key) {
        return properties.get(key);
    }

    /**
     * Gets a resource as an {@link Object}
     * @param key        key of the resource
     * @param defaultVal default value of the resource
     * @return resource as an {@link Object} or default
     */
    public static Object get(String key, Object defaultVal) {
        return properties.getOrDefault(key, defaultVal);
    }

    /**
     * Gets a resource as {@link String}
     * @param key key of the resource
     * @return resource as {@link String}
     */
    public static String getAsString(String key) {
        return properties.getProperty(key);
    }

    /**
     * Gets a resource as an {@link String}
     * @param key        key of the resource
     * @param defaultVal default value of the resource
     * @return resource as an {@link String} or default
     */
    public static String getAsString(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }

    /**
     * Gets a resource as {@link Integer}
     * @param key key of the resource
     * @return resource as {@link Integer}
     */
    public static Integer getAsInt(String key) {
        var prop = properties.getProperty(key);
        return prop != null ? Integer.parseInt(prop) : null;
    }

    /**
     * Gets a resource as an {@link Integer}
     * @param key        key of the resource
     * @param defaultVal default value of the resource
     * @return resource as an {@link Integer} or default
     */
    public static Integer getAsInt(String key, Integer defaultVal) {
        var prop = properties.getProperty(key);
        return prop != null ? Integer.parseInt(prop) : defaultVal;
    }

    /**
     * Gets a resource as {@link Boolean}
     * @param key key of the resource
     * @return resource as {@link Boolean}
     */
    public static Boolean getAsBool(String key) {
        var prop = properties.getProperty(key);
        return prop != null ? Boolean.parseBoolean(prop) : null;
    }

    /**
     * Gets a resource as an {@link Boolean}
     * @param key        key of the resource
     * @param defaultVal default value of the resource
     * @return resource as an {@link Boolean} or default
     */
    public static Boolean getAsBool(String key, Boolean defaultVal) {
        var prop = properties.getProperty(key);
        return prop != null ? Boolean.parseBoolean(prop) : defaultVal;
    }
}
