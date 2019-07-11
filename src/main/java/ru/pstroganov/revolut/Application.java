package ru.pstroganov.revolut;

import ru.pstroganov.revolut.config.HttpServerConfigurer;
import ru.pstroganov.revolut.datasource.ConnectionsProvider;

/**
 * Main class of the application
 * @author pstroganov
 *         Date: 06/07/2019
 */
public class Application {

    /**
     * Application entry point
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ConnectionsProvider.init();
        HttpServerConfigurer.start(Application.class);
    }

}
