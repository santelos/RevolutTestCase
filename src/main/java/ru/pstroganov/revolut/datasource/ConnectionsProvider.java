package ru.pstroganov.revolut.datasource;

import org.apache.commons.io.IOUtils;
import ru.pstroganov.revolut.config.PropertiesProvider;
import ru.pstroganov.revolut.core.exceptions.CoreException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provider of the datasource
 * @author pstroganov
 *         Date: 07/07/2019
 */
public class ConnectionsProvider {

    /** Standard file that initialize database on startup */
    private static final String INIT_SQL_FILENAME = "init_db.sql";

    /** URL of the database connection */
    private static final String URL = PropertiesProvider.getAsString("dbUrl", "jdbc:h2:mem:revolutDB;DB_CLOSE_DELAY=-1;LOCK_MODE=3");

    /** Username of the database connection */
    private static final String USER = PropertiesProvider.getAsString("dbUserName", "changeit");

    /** Password of the database connection */
    private static final String PASS = PropertiesProvider.getAsString("dbUserPass", "changeit");

    /**
     * Provide the connection
     * @return connection to the database
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new CoreException("Error during the connection creation");
        }
    }

    /**
     * Initial function that initializes database
     */
    public static void init() {
        try (var is = ConnectionsProvider.class.getClassLoader().getResourceAsStream(INIT_SQL_FILENAME)) {
            init(IOUtils.toString(is, Charset.defaultCharset()));
        } catch (IOException e) {
            throw new CoreException("InitDB file with name \"{0}\" not found in classpath", e, INIT_SQL_FILENAME);
        }
    }

    /**
     * Initial function that initializes database
     * @param initSqlFile file with startup sql
     */
    public static void init(String initSqlFile) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(initSqlFile)) {
            ps.execute();
            conn.commit();
        } catch (SQLException sqle) {
            throw new CoreException("Error initializing database with initDB file \"{0}\"", sqle, INIT_SQL_FILENAME);
        }
    }

}
