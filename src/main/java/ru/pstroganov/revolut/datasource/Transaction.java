package ru.pstroganov.revolut.datasource;

import ru.pstroganov.revolut.core.exceptions.CoreException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Transaction wrapper
 * @author pstroganov
 *         Date: 10/07/2019
 */
public class Transaction implements AutoCloseable {

    /** Connection shared within transaction */
    private final Connection connection;

    /** Transaction state */
    private State state = State.ACTIVE;

    /**
     * Constructor
     * @param connection connection within transaction
     */
    private Transaction(Connection connection) {
        this.connection = connection;
    }

    /**
     * Starts a transaction
     * @param connection connection
     * @return transaction instance
     */
    public static Transaction start(Connection connection) {
        try {
            connection.setAutoCommit(false);
            return new Transaction(connection);
        } catch (SQLException e) {
            throw new CoreException("Error starting the transaction");
        }
    }

    /**
     * Commit the transaction
     */
    public void commit() {
        if (state.equals(State.COMMITTED) || state.equals(State.CLOSED)) {
            return;
        }
        state = State.COMMITTED;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            if (state.equals(State.COMMITTED)) {
                connection.commit();
            } else {
                connection.rollback();
            }
            connection.close();
            state = State.CLOSED;
        } catch (SQLException e) {
            state = State.CLOSED;
            throw new CoreException("Error during transaction closing", e);
        }
    }

    /**
     * Transaction states
     */
    private enum State {
        /** Active transaction */
        ACTIVE,

        /** Committed transaction */
        COMMITTED,

        /** Closed transaction */
        CLOSED
    }
}
