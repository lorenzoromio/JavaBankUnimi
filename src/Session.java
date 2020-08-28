/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Session extends Account {
    private final long duration = 60 * 3;
    private Instant sessionCreation;
    private List<Transaction> transactions;

    public Session(String username) throws AccountNotFoundException, SQLException {
        super(username);
        sessionCreation = Instant.now();
    }

    public void isValid() throws TimeoutException {
        if (ChronoUnit.MILLIS.between(sessionCreation, Instant.now()) > duration * 1000)
            throw new TimeoutException("Sessione Scaduta");
    }

    public void updateSessionCreation() throws TimeoutException {
        isValid();
        this.sessionCreation = Instant.now();
    }

    public void changePassword(String oldPsw, String newPsw, String checkPsw) throws TimeoutException, IllegalArgumentException, SQLException, NoSuchAlgorithmException, CredentialException {

        updateSessionCreation();

        if (!getHashPsw().equals(hash(oldPsw)))
            throw new CredentialException("Password Errata");

        if (getHashPsw().equals(hash(newPsw)))
            throw new IllegalArgumentException("La nuova password non può essere uguale a quella precedente");

        if (!newPsw.equals(checkPsw))
            throw new IllegalArgumentException("Le password non coincidono");

        setPassword(newPsw);

    }

    public void deleteAccount(String psw, String confirmPsw) throws TimeoutException, IllegalArgumentException, CredentialException, SQLException, NoSuchAlgorithmException {

        updateSessionCreation();

        if (!psw.equals(confirmPsw))
            throw new IllegalArgumentException("Le password non coincidono");

        if (!getHashPsw().equals(hash(psw)))
            throw new CredentialException("Password Errata");

        String removeAccount = "DELETE FROM accounts WHERE USERNAME = ? and HASHPSW = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(removeAccount);
        prepStmt.setString(1, getUsername());
        prepStmt.setString(2, hash(psw));
        prepStmt.execute();

    }

    public void transfer(String iban, double amount) throws AccountNotFoundException, IllegalArgumentException, TimeoutException, SQLException {

        updateSessionCreation();

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

        if (rs.next()) {

            String transferMoney = "update accounts set saldo = saldo + ? where iban = ?";
            prepStmt = DBConnect.getConnection().prepareStatement(transferMoney);
            prepStmt.setDouble(1, amount);
            prepStmt.setString(2, iban);
            prepStmt.executeUpdate();

            setSaldo(getSaldo() - amount);
            new Transaction(this.getIban(), iban, amount, new Date()).push();

            rs.close();
            prepStmt.close();

        } else {
            throw new AccountNotFoundException("Destinatario non trovato");
        }
    }

    public Double validateAmount(String amount) throws IllegalArgumentException {

        // Regex to check valid password.
        IllegalArgumentException validateAmountException = new IllegalArgumentException("Importo non valido");

        String regex = "^\\$?[0-9]+(\\.([0-9]{1,2}))?$";
        amount = amount.replace(",", ".");
        try {
            checkRegex(amount, regex);
        } catch (IllegalArgumentException e) {
            throw validateAmountException;
        }

        return Double.parseDouble(amount);
    }


    public void deposit(Double amount) throws TimeoutException, SQLException, IllegalArgumentException {

        updateSessionCreation();

        if (amount <= 0)
            throw new IllegalArgumentException("Importo non valido");

        setSaldo(getSaldo() + amount);
        new Transaction(getIban(), amount, "deposito", new Date()).push();
    }

    public void prelievo(Double amount) throws IllegalArgumentException, TimeoutException, SQLException {
        updateSessionCreation();

        if (getSaldo() < amount)
            throw new IllegalArgumentException("Fondi Insufficenti");

        setSaldo(getSaldo() - amount);
        new Transaction(getIban(), amount, "prelievo", new Date()).push();
    }

    public List<Account> showContacts() throws TimeoutException, SQLException {
        updateSessionCreation();

        List<Account> accountsList = new ArrayList<>();
        String showContacts = "select username from accounts where username <> ? order by cognome";
        try {

            PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(showContacts);
            prepStmt.setString(1, getUsername());
            ResultSet rs = prepStmt.executeQuery();
            Instant check = Instant.now();
            while (rs.next()) {
                String username1 = "USERNAME";
                String username = rs.getString(username1);
                accountsList.add(new Account(username));
            }
            System.out.println("ShowContacts :" + ChronoUnit.MILLIS.between(check, Instant.now()) + "ms for");

        } catch (AccountNotFoundException e) {
//           IGNORE, EXCEPTION IMPOSSIBILE
        }


        return accountsList;

    }


    public List<Transaction> showTransactions() throws TimeoutException, SQLException {

        updateSessionCreation();
        List<Transaction> transactionsList = new ArrayList<>();

        String showTransactions = "select * from transaction where ? IN ( IBAN_FROM, IBAN_DEST ) order by ID desc ";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(showTransactions);
        prepStmt.setString(1, getIban());
        ResultSet rs = prepStmt.executeQuery();

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

        transactions = transactionsList;

        return transactionsList;

    }

    public Double getOutcomes() {
        Double outcomes = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("prelievo")) {
                outcomes += transaction.getAmount();
            }

            if (transaction.getType().equals("bonifico") && transaction.getIbanFrom().equals(getIban())) {
                outcomes += transaction.getAmount();
            }
        }
        return outcomes;
    }

    public Double getIncomes() {
////        System.out.println("get outcome");
        Double incomes = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getType().equals("deposito")) {
                incomes += transaction.getAmount();
            }

            if (transaction.getType().equals("bonifico") && transaction.getIbanDest().equals(getIban())) {
                incomes += transaction.getAmount();
            }
        }
        return incomes;
    }

    @Override
    public Double getSaldo() throws SQLException {
        String update = "select * from accounts where username = ?";
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
        super.setSaldo(saldo);
        String update = "update accounts set saldo = ? where username = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setDouble(1, super.getSaldo());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();

    }

    //
    @Override
    protected void setPassword(String psw) throws NoSuchAlgorithmException, SQLException {
        super.setPassword(psw);
//        System.out.println("UPDATE PSW ON DATABASE");
        String update = "update accounts set HASHPSW = ? where username = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setString(1, super.getHashPsw());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();
    }

    //
    @Override
    public String getHashPsw() throws SQLException {
        String query = "select hashpsw from accounts where username = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(query);
        prepStmt.setString(1, getUsername());
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            return rs.getString("HASHPSW");
        } else return null;
    }
}



