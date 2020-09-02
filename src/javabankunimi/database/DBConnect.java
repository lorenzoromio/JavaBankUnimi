/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.database;

import javabankunimi.gui.MainApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String user = "XVqjqK5y2F";
    private static final String psw = "Pjb9JBb2v3";
    private static final String DBUrl = "jdbc:mysql://remotemysql.com:3306/" + user +
                                        "?autoReconnect=true&useUnicode=yes";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) try {
            Class.forName(driver);
            connection = DriverManager.getConnection(DBUrl, user, psw);

        } catch (ClassNotFoundException ex) {
            new MainApp().SQLExceptionOccurred(new SQLException("Invalid Driver"));
            System.out.println("Error on create Load JDBC Driver: ");
            System.exit(-1);
        }
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }

    public static void deleteAll() throws SQLException, NullPointerException {
        DBConnect.getConnection().createStatement().executeUpdate("TRUNCATE accounts");
        eraseBalance();
        System.out.println("All Database Deleted!");
    }

    public static void eraseBalance() throws SQLException, NullPointerException {
        DBConnect.getConnection().createStatement().executeUpdate("update accounts set SALDO = 0");
        DBConnect.getConnection().createStatement().executeUpdate("TRUNCATE transaction");
        System.out.println("All Balance Deleted!");
    }
}



