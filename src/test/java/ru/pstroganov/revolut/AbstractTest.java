package ru.pstroganov.revolut;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.pstroganov.revolut.datasource.ConnectionsProvider;
import ru.pstroganov.revolut.datasource.Transaction;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Abstract test class
 * @author pstroganov
 *         Date: 12/07/2019
 */
public abstract class AbstractTest {

    /**
     * Init tests
     */
    @BeforeAll
    static void init() {
        try (var is = PositiveTests.class.getResourceAsStream("init_db.sql")) {
            ConnectionsProvider.init(IOUtils.toString(is, Charset.defaultCharset()));
        } catch (IOException e) {
            throw new RuntimeException("Init db file reading error", e);
        }
    }

    /**
     * Cleanup database after each test
     * @throws Exception error during cleanup
     */
    @BeforeEach
    void refreshDataBase() throws Exception {
        try (var tr = Transaction.start(ConnectionsProvider.getConnection())) {
            try (var ps = tr.getConnection().prepareStatement("TRUNCATE TABLE wallet; " +
                    "ALTER SEQUENCE wallet_ids RESTART WITH 1")) {
                ps.executeUpdate();
            }
        }
    }

}
