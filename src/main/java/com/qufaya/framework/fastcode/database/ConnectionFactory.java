/**
 * 
 */
package com.qufaya.framework.fastcode.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author zhihua
 *
 */
public class ConnectionFactory {

    private static final ConnectionFactory instance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("", e);
        }
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    public Connection createConnection(String host, int port, String database, String user,
            String password) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
    }
}
