/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class Bank {
    private static final String abi = "02";
    private static final String cab = "03";



    public static void addAccount(Account account) throws IllegalArgumentException, SQLException, AccountException {

//        DBConnect db = new DBConnect();
        String checkUser = "select ID from accounts where username = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(checkUser);
        prepStmt.setString(1, account.getUsername());
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            System.out.println("Account already in database");
            throw new AccountException("Account already in database");
        }

        if (rs != null) rs.close();
        prepStmt.close();

        String newAccount = "insert into accounts(USERNAME, NOME, COGNOME, HASHPSW, SALT, TIMESTAMP, IBAN, NUM_CONTO, SALDO) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        prepStmt = DBConnect.getConnection().prepareStatement(newAccount);
        prepStmt.setString(1, account.getUsername());
        prepStmt.setString(2, account.getNome());
        prepStmt.setString(3, account.getCognome());
        prepStmt.setString(4, account.getHashPsw());
        prepStmt.setString(5, account.getSalt());
        prepStmt.setString(6, account.getTimestamp());
        prepStmt.setString(7, account.getIban());
        prepStmt.setString(8, account.getNum_conto());
        try {
            prepStmt.setDouble(9, account.getSaldo());
        } catch (TimeoutException e) {
            //
        }
        prepStmt.execute();
        prepStmt.close();

    }

    public static Session login(String username, String login_psw) throws CredentialException, AccountNotFoundException, SQLException {
        Session logging = new Session(username);
        if (!logging.hash(login_psw).equals(logging.getHashPsw())) {
//            logging = null;
            throw new CredentialException("Invalid Password");
        }
        return logging;
    }

    public static String getAbi() {
        return abi;
    }

    public static String getCab() {
        return cab;
    }


}