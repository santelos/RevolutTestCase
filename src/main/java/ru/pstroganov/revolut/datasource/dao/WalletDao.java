package ru.pstroganov.revolut.datasource.dao;

import ru.pstroganov.revolut.core.exceptions.BadRequestException;
import ru.pstroganov.revolut.core.exceptions.CoreException;
import ru.pstroganov.revolut.entity.CurrencyCustom;
import ru.pstroganov.revolut.entity.Wallet;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Database DAO operations for wallet entity
 * @author pstroganov
 *         Date: 08/07/2019
 */
public class WalletDao {

    /** Insert the wallet query */
    private static final String INSERT_WALLET_QUERY = "INSERT INTO wallet(money, currency, status) VALUES (?,?,?)";

    /**
     * Create new wallet in database
     * @param conn   connection to DB
     * @param wallet wallet entity
     * @return new wallet
     */
    public static Wallet insertWallet(Connection conn, Wallet wallet) {
        try (var ps = conn.prepareStatement(INSERT_WALLET_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            ps.setBigDecimal(++i, wallet.getMoney());
            ps.setInt(++i, wallet.getCurrency().getCode());
            ps.setInt(++i, wallet.getStatus().getCode());
            ps.executeUpdate();
            Wallet newWallet;
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newWallet = new Wallet(
                            generatedKeys.getLong(1),
                            wallet.getMoney(),
                            wallet.getCurrency(),
                            wallet.getStatus());
                } else {
                    throw new CoreException("Creating wallet failed, no ID was returned");
                }
            }
            return newWallet;
        } catch (SQLException e) {
            throw new CoreException("DB error during wallet creation for currency: {0}", e, wallet.getCurrency());
        }
    }

    /** Find all wallets query */
    private static final String FIND_ALL_WALLETS_QUERY = "SELECT * FROM wallet";

    /**
     * Find all wallets
     * @param conn connection to DB
     * @return List of wallets
     */
    public static List<Wallet> findWallets(Connection conn) {
        try (var ps = conn.prepareStatement(FIND_ALL_WALLETS_QUERY)) {
            List<Wallet> wallets = new ArrayList<>();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    wallets.add(readFromResultSet(rs));
                }
            }
            return wallets;
        } catch (SQLException e) {
            throw new CoreException("DB error during all wallets selection", e);
        }
    }

    /** Find wallet by id query */
    private static final String FIND_WALLET_BY_ID_QUERY = "SELECT * FROM wallet WHERE id=?";

    /**
     * Wind wallet by an identifier
     * @param conn connection to DB
     * @param id   identifier
     * @return wallet
     */
    public static Wallet findWallet(Connection conn, long id) {
        try (var ps = conn.prepareStatement(FIND_WALLET_BY_ID_QUERY)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return readFromResultSet(rs);
                } else {
                    throw new BadRequestException("There is no wallet for ID: {0}", id);
                }
            }
        } catch (SQLException e) {
            throw new CoreException("DB error during wallet selection for ID: {0}", e, id);
        }
    }

    /** Update wallet status query */
    private static final String CHANGE_WALLET_STATUS_QUERY = "UPDATE wallet SET status=? WHERE id=?";

    /**
     * Change wallet status
     * @param conn   connection to DB
     * @param wallet wallet
     * @param status new status
     */
    public static void changeWalletStatus(Connection conn, Wallet wallet, Wallet.Status status) {
        try (var ps = conn.prepareStatement(CHANGE_WALLET_STATUS_QUERY)) {
            // check if any changes in wallet appears
            changeWalletChanges(conn, wallet);
            int i = 0;
            ps.setInt(++i, status.getCode());
            ps.setLong(++i, wallet.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new CoreException("DB error changing wallet status to {0} for wallet ID: {1}", e, status.getCode(), wallet.getId());
        }
    }

    /** Add money query */
    private static final String ADD_MONEY_QUERY = "UPDATE wallet SET money=? WHERE id=?";

    /**
     * Change amount of the wallet
     * @param conn      connection to DB
     * @param wallet    wallet
     * @param newAmount new amount
     * @return new wallet with amount changed
     */
    public static Wallet changeAmount(Connection conn, Wallet wallet, BigDecimal newAmount) {
        try (var ps = conn.prepareStatement(ADD_MONEY_QUERY)) {
            // check if any changes in wallet appears
            changeWalletChanges(conn, wallet);
            int i = 0;
            ps.setBigDecimal(++i, newAmount);
            ps.setLong(++i, wallet.getId());
            if (ps.executeUpdate() == 0) {
                throw new BadRequestException("Could not add money to wallet. No wallet value affected");
            }
            return new Wallet(wallet.getId(),
                    newAmount,
                    wallet.getCurrency(),
                    wallet.getStatus());
        } catch (SQLException e) {
            throw new CoreException("DB error changing amount in wallet with ID \"{0}\"", e, wallet.getId());
        }
    }

    /**
     * Read a wallet entity from result set
     * @param rs result set
     * @return New wallet entity from database
     * @throws SQLException Error reading wallet info from the database
     */
    private static Wallet readFromResultSet(ResultSet rs) throws SQLException {
        var currencyCode = rs.getInt("currency");
        return new Wallet(
                rs.getLong("id"),
                rs.getBigDecimal("money"),
                CurrencyCustom.valueOf(currencyCode)
                        .orElseThrow(() -> new CoreException("Could not parse currency type for code {0}", currencyCode)),
                Wallet.Status.valueOf(rs.getInt("status"))
        );
    }

    /**
     * Check if wallet was changed after last wallet selection
     * @param conn   connection to DB
     * @param wallet selected wallet
     */
    private static void changeWalletChanges(Connection conn, Wallet wallet) {
        if (!findWallet(conn, wallet.getId()).equals(wallet)) {
            throw new BadRequestException("The requested wallet has been externally changed during this request");
        }
    }

}
