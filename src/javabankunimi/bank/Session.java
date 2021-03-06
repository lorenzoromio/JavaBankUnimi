/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.bank;

import javabankunimi.database.DBConnect;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Session extends Account {
    private static final long duration = 3 * 60 * 1000;
    private Instant creation;
    private Instant expiredTime;
    private List<Transaction> transactions;

    public Session(String username) throws AccountNotFoundException, SQLException {
        super(username);
        creation = Instant.now();
        String query = "select HASHPSW,SALT,TIMESTAMP,SALDO from accounts where username = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(query);
        prepStmt.setString(1, username);
        ResultSet rs = prepStmt.executeQuery();

        if (rs.next()) {
            setHashPsw(rs.getString("HASHPSW"));
            setSalt(rs.getString("SALT"));
            setTimestamp(rs.getString("TIMESTAMP"));
            setSaldo(rs.getDouble("SALDO"));
        } else {
            throw new AccountNotFoundException("Account not in database");
        }

    }

    public static void eraseBalance() throws SQLException {
        DBConnect.eraseBalance();
    }

    public static void deleteAllDatabase() throws SQLException {
        DBConnect.deleteAll();
    }

    public static long getDuration() {
        return duration;
    }

    public Instant getCreation() {
        return creation;
    }

    public Instant getExpiredTime() {
        return expiredTime;
    }

    public void updateCreation() {
        this.creation = Instant.now();
        this.expiredTime = Instant.ofEpochMilli((creation.toEpochMilli() + duration));
    }

    public void isValid() throws TimeoutException {
        long between = ChronoUnit.MILLIS.between(creation, Instant.now());
        System.out.println(between);
        if (between > duration)
            throw new TimeoutException("Sessione Scaduta");
    }

    public void changePassword(char[] oldPsw, char[] newPsw, char[] checkPsw) throws IllegalArgumentException, SQLException, CredentialException {

        updateCreation();

        if (!hash(oldPsw).equals(getHashPsw()))
            throw new CredentialException("Password Errata");

        if (hash(newPsw).equals(getHashPsw()))
            throw new IllegalArgumentException("La nuova password non può essere uguale a quella precedente");

        if (!Arrays.equals(newPsw, checkPsw))
            throw new IllegalArgumentException("Le password non coincidono");

        setSalt(getTimestamp().concat(randomString(10)));
        setPassword(newPsw);

        Arrays.fill(oldPsw, '0');
        Arrays.fill(newPsw, '0');
        Arrays.fill(checkPsw, '0');

    }

    public void deleteAccount(char[] psw, char[] confirmPsw) throws IllegalArgumentException, CredentialException, SQLException {

        updateCreation();

        if (Arrays.equals(psw, confirmPsw))
            throw new IllegalArgumentException("Le password non coincidono");

        if (!getHashPsw().equals(hash(psw)))
            throw new CredentialException("Password Errata");

        String removeAccount = "DELETE FROM accounts WHERE USERNAME = ? and HASHPSW = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(removeAccount);
        prepStmt.setString(1, getUsername());
        prepStmt.setString(2, hash(psw));
        prepStmt.execute();
        Arrays.fill(psw, '0');
        Arrays.fill(confirmPsw, '0');


    }

    public void transfer(String iban, double amount) throws AccountNotFoundException, IllegalArgumentException, SQLException {

        updateCreation();

        if (getSaldo() < amount)
            throw new IllegalArgumentException("Fondi Insufficenti");

        if (amount == 0)
            throw new IllegalArgumentException("Importo non valido");

        if (iban.equals(getIban()))
            throw new AccountNotFoundException("Impossibile eseguire un bonifico \nverso il proprio conto corrente.\nSpecificare un IBAN valido.");


        String checkIfToExist = "select ID from accounts where IBAN = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(checkIfToExist);
        prepStmt.setString(1, iban);
        ResultSet rs = prepStmt.executeQuery();

        if (!rs.next()) {
            throw new AccountNotFoundException("Destinatario non trovato");
        } else {

            String transferMoney = "update accounts set SALDO = SALDO + ? where IBAN = ?";
            prepStmt = DBConnect.getConnection().prepareStatement(transferMoney);
            prepStmt.setDouble(1, amount);
            prepStmt.setString(2, iban);
            prepStmt.executeUpdate();

            setSaldo(getSaldo() - amount);
            new Transaction(this.getIban(), iban, amount, new Date()).push();

            rs.close();
            prepStmt.close();

        }
    }


    public void deposit(Double amount) throws SQLException, IllegalArgumentException {

        updateCreation();

        if (amount <= 0)
            throw new IllegalArgumentException("Importo non valido");

        setSaldo(getSaldo() + amount);
        new Transaction(getIban(), amount, "deposito", new Date()).push();
    }

    public void prelievo(Double amount) throws IllegalArgumentException, SQLException {
        updateCreation();

        if (getSaldo() < amount)
            throw new IllegalArgumentException("Fondi Insufficenti");

        setSaldo(getSaldo() - amount);
        new Transaction(getIban(), amount, "prelievo", new Date()).push();
    }

    public List<Account> showContacts(String search) throws SQLException {
        updateCreation();
        List<Account> accountsList = new ArrayList<>();
        String showContacts = "select USERNAME from accounts where USERNAME <> ? order by COGNOME";

        try {

            PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(showContacts);
            prepStmt.setString(1, getUsername());
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                String[] words = search.split(" ");

                boolean searchMatch = true;
                for (String word : words) {
                    if (!username.toLowerCase().contains(word.toLowerCase())) {
                        searchMatch = false;
                        break;
                    }
                }
                if (searchMatch) {
                    accountsList.add(new Account(username));
                }
            }

        } catch (AccountNotFoundException e) { /*IGNORE*/ }
//        Collections.sort(accountsList);
        return accountsList;
    }

    public List<Transaction> showTransactions() throws SQLException {

//        updateCreation();
        List<Transaction> transactionsList = new ArrayList<>();

        String showTransactions = "select * from transaction where ? IN ( IBAN_FROM, IBAN_DEST ) order by ID desc ";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(showTransactions);
        prepStmt.setString(1, getIban());
        ResultSet rs = prepStmt.executeQuery();

        try {
            while (rs.next()) {
                String ibanFrom = rs.getString("IBAN_FROM");
                Date date = new Date(Long.parseLong(rs.getString("DATE")));
                Double amount = rs.getDouble("AMOUNT");
                String type = rs.getString("TYPE");

                if (type.equals("bonifico")) {
                    String ibanDest = rs.getString("IBAN_DEST");
                    transactionsList.add(new Transaction(ibanFrom, ibanDest, amount, date));
                } else {
                    transactionsList.add(new Transaction(ibanFrom, amount, type, date));
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("NULL POINTER!!!");
            return showTransactions();
        }
        transactions = transactionsList;
        return transactionsList;
    }

    public Double getOutcomes() {
        Double outcomes = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("prelievo") ||
                    (transaction.getType().equals("bonifico") && transaction.getIbanFrom().equals(getIban()))) {
                outcomes += transaction.getAmount();
            }
        }
        return outcomes;
    }

    public Double getIncomes() {
        Double incomes = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("deposito") ||
                    (transaction.getType().equals("bonifico") && transaction.getIbanDest().equals(getIban()))) {
                incomes += transaction.getAmount();
            }
        }
        return incomes;
    }

    @Override
    public Double getSaldo() throws SQLException {
//        updateCreation();
        String update = "select * from accounts where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setString(1, getUsername());
        ResultSet rs = prepStmt.executeQuery();
        Double balance = null;
        if (rs.next())
            balance = rs.getDouble("SALDO");

        super.setSaldo(balance);
        return balance;

    }

    @Override
    protected void setSaldo(Double saldo) throws SQLException {
        updateCreation();
        super.setSaldo(saldo);
        String update = "update accounts set SALDO = ? where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setDouble(1, super.getSaldo());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();

    }

    @Override
    protected void setPassword(char[] psw) throws SQLException {
        updateCreation();
        super.setPassword(psw);
        String update = "update accounts set HASHPSW = ? where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setString(1, super.getHashPsw());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();
    }

    @Override
    public String getHashPsw() throws SQLException {
        String query = "select HASHPSW from accounts where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(query);
        prepStmt.setString(1, getUsername());
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            return rs.getString("HASHPSW");
        } else return null;
    }

    @Override
    protected void setSalt(String salt) throws SQLException {
        updateCreation();
        super.setSalt(salt);
        String update = "update accounts set SALT = ? where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setString(1, super.getSalt());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();
    }

    @Override
    protected void setTimestamp(String timestamp) throws SQLException {
        updateCreation();
        super.setTimestamp(timestamp);
        String update = "update accounts set TIMESTAMP = ? where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setString(1, super.getTimestamp());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();

    }

    @Override
    public String toString() {
        return "Logged as " + getUsername();
    }
}



