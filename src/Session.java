import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.sasl.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Session extends Account {
    private final long duration = 60;
    private Instant sessionCreation;
    private ArrayList<Transaction> transactions;

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

    public void changePassword(String oldPsw, String newPsw, String checkPsw) throws TimeoutException, AuthenticationException, IllegalArgumentException, SQLException, AccountException, NoSuchAlgorithmException {

        updateSessionCreation();

        if (!getHashPsw().equals(hash(oldPsw)))
            throw new AuthenticationException("Password Errata");

        if (getHashPsw().equals(hash(newPsw)))
            throw new IllegalArgumentException("La nuova password non può essere uguale a quella precedente");

        if (!newPsw.equals(checkPsw))
            throw new IllegalArgumentException("Le password non coincidono");

        setPassword(newPsw);

    }

    public void deleteAccount(String psw, String confirmPsw) throws TimeoutException, AuthenticationException, SQLException, NoSuchAlgorithmException {

        updateSessionCreation();

        if (!psw.equals(confirmPsw))
            throw new AuthenticationException("Le password non coincidono");

        if (!getHashPsw().equals(hash(psw)))
            throw new AuthenticationException("Password Errata");

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


        String checkIfToExist = "select * from accounts where IBAN = ?";
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

            if (rs != null) rs.close();
            if (prepStmt != null) prepStmt.close();

        } else {
            throw new AccountNotFoundException("Destinatario non trovato");
        }
    }

    public Double validateAmount(String amount) throws IllegalArgumentException {

        // Regex to check valid password.
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Importo non valido");

        if (amount == null) throw illegalArgumentException;
        amount = amount.replace(",", ".");

        String regex = "^\\$?[0-9]+(\\.([0-9]{1,2}))?$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(amount);

        if (!m.matches()) throw illegalArgumentException;

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

    public ArrayList<Account> showContacts() throws TimeoutException, SQLException {

        updateSessionCreation();
        ArrayList<Account> accountsList = new ArrayList<>();

        String showContacts = "select * from accounts order by cognome";
        try {
            ResultSet rs = DBConnect.getConnection().createStatement().executeQuery(showContacts);
            while (rs.next()) {
                String username = rs.getString("USERNAME");
                if (!username.equals(getUsername())) {
                    accountsList.add(new Account(username));
                }
            }

        } catch (AccountNotFoundException e) {
//           IGNORE, EXCEPTION IMPOSSIBILE
        }

        return accountsList;

    }

    public ArrayList<Transaction> showTransactions() throws TimeoutException, SQLException {

        updateSessionCreation();
        ArrayList<Transaction> transactionsList = new ArrayList<>();
        String showTransactions = "select * from transaction order by ID desc";
        ResultSet rs = DBConnect.getConnection().createStatement().executeQuery(showTransactions);

        while (rs.next()) {
            String ibanFrom = rs.getString("IBAN_FROM");
            String ibanDest = rs.getString("IBAN_DEST");
            Date date = new Date(Long.parseLong(rs.getString("DATE")));
////            System.out.println(date.getTime());
////            System.out.println(new Date(date.getTime()));

            if (getIban().equals(ibanFrom) || getIban().equals(ibanDest)) {
                Double amount = rs.getDouble("AMOUNT");
                String type = rs.getString("TYPE");

                if (type.equals("bonifico")) {
                    transactionsList.add(new Transaction(ibanFrom, ibanDest, amount, date));
                } else {
                    transactionsList.add(new Transaction(ibanFrom, amount, type, date));
                }
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
    public void setSaldo(Double saldo) throws SQLException {
        super.setSaldo(saldo);
        String update = "update accounts set saldo = ? where username = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(update);
        prepStmt.setDouble(1, super.getSaldo());
        prepStmt.setString(2, getUsername());
        prepStmt.executeUpdate();

    }

    //
    @Override
    public void setPassword(String psw) throws NoSuchAlgorithmException, SQLException, AccountException {
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



