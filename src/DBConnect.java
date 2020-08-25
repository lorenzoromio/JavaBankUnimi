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
            System.out.println("create new connection to database");

        } catch (ClassNotFoundException ex) {
            System.out.println("Error on create Load JDBC Driver: " + ex);
        } catch (SQLException ex) {
            System.out.println(ex);
            throw ex;
        }
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null && !connection.isClosed())
            connection.close();
        System.out.println("Call DBConnect.close()");
        connection = null;
        System.out.println(connection);


    }

    public static void deleteAll() throws SQLException, NullPointerException {
        String delete = "delete from accounts";
        PreparedStatement preparedStmt = getConnection().prepareStatement(delete);
        preparedStmt.executeUpdate();
        System.out.println("All Database Deleted!");
        if (preparedStmt != null) preparedStmt.close();

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
        } catch (SQLSyntaxErrorException ex) {
            System.out.println(ex);
        } catch (SQLException e) {
            e.printStackTrace();
            //testfd
        }

        try {
            DBConnect.getConnection().createStatement().executeUpdate(transaction);
        } catch (SQLSyntaxErrorException ex) {
            System.out.println(ex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void populateDB() {

        String accounts = "INSERT INTO `accounts` (`ID`, `USERNAME`, `NOME`, `COGNOME`, `HASHPSW`, `SALT`, `TIMESTAMP`, `NUM_CONTO`, `IBAN`, `SALDO`) VALUES\n" +
                "(1432, 'anna.arnaboldi', 'Anna', 'Arnaboldi', '8b09d5e19f7616e556a3089daf1511462067248e5cc41568432b1e56ac99fc9a1c78df56f590f4c15aa826cfcec39e66640eabd81a75b3355f27f21822524a2b', '15977092284391085384383', '1597709228439', '081', 'IT0203081', 0.00),\n" +
                "(1435, 'lia.romio', 'Lia', 'Romio', '718f23631882295f5beefa1e37e1de19fbff0aedb16444d146e31bed90b321f25804ffc0fed950fd60f4d331bcabddb0cc8719dd0d4e703a92460e5e7883eb91', '15977094780452141114234', '1597709478045', '882', 'IT0203882', 0.00),\n" +
                "(1438, 'michelangelo.mazzitelli', 'Michelangelo', 'Mazzitelli', 'aa287c718aa608351bf58b6a6a261fc577a7ded6662d90328ad0ba14eb01a8f087a2d963d2f527b558103c5702bc8284bd6fe2a226fb095ea66afa9b3a3d906a', '15977186644575158524602', '1597718664457', '024', 'IT0203024', 0.00),\n" +
                "(1439, 'maurizio.mazzitelli', 'Maurizio', 'Mazzitelli', '8b5b84e42211c9b2c040d033ca0df45361bb6fea0fd4f9a2ab9e2bdaf365ed1da917da2dd2b0fb5bac8c4e301347edb23e2017a2e54bb14a2d9149bd78f729fd', '15977186848725472774573', '1597718684872', '507', 'IT0203507', 0.00),\n" +
                "(1440, 'emanuele.arnaboldi', 'Emanuele', 'Arnaboldi', 'dd9d21f3d1c06a97cdbea018af7411f5ad44b9f343cffca7b0ca2cb2448337d997ad0a6b90b6815f8d9eb3187b34eb13a22693adb4d9fd6a38460e818caf2c24', '15977187152373338662647', '1597718715237', '032', 'IT0203032', 0.00),\n" +
                "(1441, 'maurizio.romio', 'Maurizio', 'Romio', '255d68b8d4d32d3bd012f28eeeb298a98019af30dc1153ac8a919e643c84e6a36e6544d8d402f20e3f5bf6cfd85cb07028c7cefce93845d059c4217866e6fba9', '15977187747746771833337', '1597718774774', '381', 'IT0203381', 0.00),\n" +
                "(1446, 'roberto.romio', 'Roberto', 'Romio', '0216517ef294e2735d55eb58c44ea4d1dee525efdca4a958017b373a8660be8ed3fa27b07173884ba166d13621f3b7300cd4184a5d37b476b4e0843c42e696e1', '15977220606215058231363', '1597722060621', '242', 'IT0203242', 0.00),\n" +
                "(1447, 'katia.romagnoli', 'Katia', 'Romagnoli', '3cc2e5718f66f1674a6eb05ed004659b14ca185f9ea843479ed76bb44e5d0529b2b79c9fdd4662bf74dc76d2d27d69c731ca935124a1a2d761c758f91fa42c16', '15977220610663270581732', '1597722061066', '245', 'IT0203245', 0.00),\n" +
                "(1448, 'luca.cecchini', 'Luca', 'Cecchini', '43fe4d32c1a995bb440218fe482ded95187ac032dd40a20349f6515bf1e5b9844d3470cc6f16106d05ae00d5bc5a7b39d397f55b016ff9cda3525c2653aceab4', '15977533461140568521777', '1597753346114', '764', 'IT0203764', 0.00),\n" +
                "(1449, 'leonardo.colombo', 'Leonardo', 'Colombo', '51edb210fc99200025a1b2197c00a50b1136bf7f181332dfdf4cb41dfdfe334d588cb9fa25c19682addb386739f13a94524eb5fbbf6c5766a2425c33927e685e', '15977533634493842506008', '1597753363449', '205', 'IT0203205', 0.00),\n" +
                "(1450, 'luca.armaroli', 'Luca', 'Armaroli', '79398fb5c3d76e7a7b4c7b31838d439e60e74746d63e7f10b43637e9df64b6c371f02f7fd3d8aa0c19342556ff869d69276fb2e6172ae706d697eddd0d783808', '15977533935856311304364', '1597753393585', '031', 'IT0203031', 0.00),\n" +
                "(1453, 'lorenzo.romio', 'Lorenzo', 'Romio', '14ffb7c7acabb6c2bd7212d6e8ba87de45a405fb38fff9704c85437b2780a10107e1bbbe514d6a1bd8910032102b5dd8bbe14e86992f7df51f90c6134f8c61b4', '15977696891590478452747', '1597769689159', '778', 'IT0203778', 0.00);\n";

        try {
            DBConnect.getConnection().createStatement().executeUpdate(accounts);
        } catch (SQLSyntaxErrorException ex) {
            System.out.println(ex);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}


