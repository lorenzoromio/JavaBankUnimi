/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import java.sql.*;

public class DBConnect {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String user = "XVqjqK5y2F";
    private static final String psw = "Pjb9JBb2v3";
    private static final String DBUrl = "jdbc:mysql://remotemysql.com:3306/" + user + "?autoReconnect=true&useUnicode=yes";
    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) try {
            Class.forName(driver);
            connection = DriverManager.getConnection(DBUrl, user, psw);
//            System.out.println("create new connection to database");

        } catch (ClassNotFoundException ex) {
            new MainApp().SQLExceptionOccurred(new SQLException("Invalid Driver"));
//            System.exit(-1);
            System.out.println("Error on create Load JDBC Driver: ");
            ex.printStackTrace();

        } catch (SQLException ex) {
            System.out.println(ex);
            throw ex;
        }
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed())
            connection.close();
        connection = null;
    }

    public static void deleteAll() throws SQLException, NullPointerException {
        DBConnect.getConnection().createStatement().executeUpdate("TRUNCATE accounts");
        eraseBalance();
        System.out.println("All Database Deleted!");


    }

    public static void eraseBalance() throws SQLException, NullPointerException {
        DBConnect.getConnection().createStatement().executeUpdate("update accounts set saldo = 0");
        DBConnect.getConnection().createStatement().executeUpdate("TRUNCATE transaction");
        System.out.println("All Balance Deleted!");



    }

    public static void createDBStructure() {
        String transaction = "CREATE TABLE `transaction` (\n" +
                "  `ID` int(11) NOT NULL,\n" +
                "  `TYPE` varchar(20) NOT NULL,\n" +
                "  `DATE` varchar(30) NOT NULL,\n" +
                "  `IBAN_FROM` varchar(20) NOT NULL,\n" +
                "  `USERNAME_FROM` varchar(20) NOT NULL,\n" +
                "  `IBAN_DEST` varchar(20) DEFAULT NULL,\n" +
                "  `USERNAME_DEST` varchar(30) DEFAULT NULL,\n" +
                "  `AMOUNT` double(50,2) NOT NULL)" +
                "   ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;" +
                "   ALTER TABLE `transaction` ADD PRIMARY KEY (`ID`);\n" +
                "   ALTER TABLE `transaction` MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;" +
                "   COMMIT;";


        String accounts =
                "   CREATE TABLE `accounts` (\n" +
                        "  `ID` int(11) NOT NULL,\n" +
                        "  `USERNAME` varchar(50) NOT NULL,\n" +
                        "  `NOME` varchar(25) NOT NULL,\n" +
                        "  `COGNOME` varchar(25) NOT NULL,\n" +
                        "  `HASHPSW` varchar(128) NOT NULL,\n" +
                        "  `SALT` varchar(50) NOT NULL,\n" +
                        "  `TIMESTAMP` varchar(50) NOT NULL,\n" +
                        "  `NUM_CONTO` varchar(5) NOT NULL,\n" +
                        "  `IBAN` varchar(20) NOT NULL,\n" +
                        "  `SALDO` double(50,2) NOT NULL)" +
                        "   ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;" +
                        "   ALTER TABLE `accounts` ADD PRIMARY KEY (`ID`);" +
                        "   ALTER TABLE `accounts` MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1455;" +
                        "   COMMIT;";

        try {
            DBConnect.getConnection().createStatement().executeUpdate(accounts);
        }  catch (SQLException e) {
            e.printStackTrace();
            //testfd
        }

        try {
            DBConnect.getConnection().createStatement().executeUpdate(transaction);
        }  catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void populateDB() {

        try {
            Bank.addAccount(new Account("anna","arnaboldi","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("elena","arnaboldi","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("katia","romagnoli","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("roberto","romio","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("maurizio","romio","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("michelangelo","mazzitelli","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("matteo","malusardi","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("lorenzo","romio","Burton86!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("adele rosalia","romio","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("luca","armaroli","kiara4Lif3!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("miranda","radici","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("edoardo","fiorani","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Bank.addAccount(new Account("andrea","torresi","Password20!"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    }



