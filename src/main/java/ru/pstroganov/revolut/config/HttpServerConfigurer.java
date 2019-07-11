package ru.pstroganov.revolut.config;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.reflections.Reflections;
import ru.pstroganov.revolut.core.annotations.HttpPath;
import ru.pstroganov.revolut.core.exceptions.CoreException;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class configures the http server
 * Configuration properties must be provided by server.properties in the classpath
 * Scans the classpath underneath the main application class to find the http services
 * @author pstroganov
 *         Date: 06/07/2019
 */
public class HttpServerConfigurer {

    /** Main application class */
    private final Class<?> appClass;

    /**
     * Constructor
     * @param appClass main application class
     */
    private HttpServerConfigurer(Class<?> appClass) {
        this.appClass = appClass;
    }

    /**
     * Static start of hte server
     * @param appClass main application class
     */
    public static void start(Class<?> appClass) {
        new HttpServerConfigurer(appClass).start();
    }

    /**
     * Starts the application
     */
    private void start() {
        try {
            var httpServer = HttpServer.create(new InetSocketAddress(PropertiesProvider.getAsInt("port", 8080)),
                    PropertiesProvider.getAsInt("backlog", 0));
            lookupForContext().forEach(httpServer::createContext);
            httpServer.setExecutor(null);
            httpServer.start();
            // create a pid file to control the process
            Files.writeString(Paths.get("application.pid"), String.valueOf(ProcessHandle.current().pid()));
            System.out.println("Server starts successfully on port: " + httpServer.getAddress().getPort());
        } catch (IOException e) {
            throw new CoreException("Server creation error", e);
        }
    }

    /**
     * Lookup for the {@link HttpPath} annotated classes to add them to the http context
     * @return Map of the context handlers (key - context path, value - instance of a handler)
     */
    private Map<String, HttpHandler> lookupForContext() {
        var reflections = new Reflections(appClass);
        return reflections.getTypesAnnotatedWith(HttpPath.class).stream()
                .peek(c -> System.out.println("Http handler found: " + c.getAnnotation(HttpPath.class).value()))
                .collect(Collectors.toMap(
                        c -> "/" + c.getAnnotation(HttpPath.class).value(),
                        ConfigUtils::createHandlerInstance));
    }

}
