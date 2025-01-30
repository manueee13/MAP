package com.manueee.systembreach.util.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Gestisce le operazioni sul database del gioco.
 * Fornisce metodi per inizializzare il database e eseguire query.
 */
public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:h2:./systembreach";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public DatabaseManager() {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            initializeDatabase(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Esegue una query SQL sul database.
     * @param query la query SQL da eseguire
     * @return il risultato della query formattato come stringa o null in caso di errore
     */
    public String executeQuery (String query) {
        try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement()) {
                ResultSet resultSet = stmt.executeQuery(query);
                StringBuilder result = new StringBuilder();
                while (resultSet.next()) {
                    int columnCount = resultSet.getMetaData().getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        result.append(resultSet.getMetaData().getColumnName(i))
                            .append(": ")
                            .append(resultSet.getString(i))
                            .append(" | ");
                    }
                    result.append("\n");
                }
                
                return result.toString();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Inizializza il database creando le tabelle necessarie.
     * @param conn la connessione al database
     */
    public void initializeDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS USERS &");
            stmt.execute("CREATE TABLE USERS (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255), password VARCHAR(255))");
            stmt.execute("CREATE TABLE CLIENT (id INT AUTO_INCREMENT PRIMARY KEY, client VARCHAR(255), state VARCHAR(255))");
            stmt.execute("CREATE TABLE PAYMENT (id INT AUTO_INCREMENT PRIMARY KEY, payment FLAOT, type VARCHAR(255))");
            stmt.execute("INSERT INTO USERS ('us378', 'v93g@1!mv')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}