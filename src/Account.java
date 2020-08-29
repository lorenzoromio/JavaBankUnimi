/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Account {
    private final String nome;
    private final String cognome;
    private final String salt;
    private final String timestamp;
    private final String num_conto;
    private final String iban;
    private final String username;
    private int ID;
    private String hashPsw;
    private Double saldo;


    //Create a new Account
    public Account(String nome, String cognome, String psw) throws NoSuchAlgorithmException, SQLException, InvalidNameException {
        //language=RegExp
        String regex = "^[A-Za-z]+((s)?(('|-|.)?([A-Za-z])+))*$"; //nomi singoli, doppi nomi con spazio, apostrofi
        try {
            checkRegex(nome, regex);
        } catch (IllegalArgumentException e) {
            throw new InvalidNameException("Nome non valido");
        }
        try {
            checkRegex(cognome, regex);
        } catch (IllegalArgumentException e) {
            throw new InvalidNameException("Cognome non valido");

        }

        this.timestamp = String.valueOf(new Date().getTime());
        this.salt = timestamp.concat(random(10));
        try {
            setPassword(psw);
        } catch (TimeoutException e) {
            //
        }

        StringBuilder sb = new StringBuilder();
        String[] nomi = nome.split(" ");
        for (String x : nomi) {
            sb.append(x.substring(0, 1).toUpperCase()).append(x.substring(1));
            if (!x.equals(nomi[nomi.length - 1]))
                sb.append(" ");
        }
        this.nome = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        String[] cognomi = cognome.split(" ");
        for (String x : cognomi) {
            sb2.append(x.substring(0, 1).toUpperCase()).append(x.substring(1));
            if (!x.equals(cognomi[cognomi.length - 1]))
                sb.append(" ");
        }
        this.cognome = sb2.toString();


        this.username = (String.format("%s.%s", this.nome.toLowerCase().replace(" ", ""), this.cognome.toLowerCase().replace(" ", ""))).replaceAll("[-+'^:,]", "");
        this.num_conto = random(7);
        this.iban = String.format("IT%sF%s%s", Bank.getAbi(), Bank.getCab(), this.num_conto);
        this.saldo = 0.0;

    }

    //Create ad Account Obj from Database only by Username
    public Account(String username) throws AccountNotFoundException, SQLException {
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement("select * from accounts where username = ?");

        prepStmt.setString(1, username);
        ResultSet rs = prepStmt.executeQuery();
        if (rs.next()) {
            this.ID = rs.getInt("ID");
            this.username = rs.getString("USERNAME");
            this.nome = rs.getString("NOME");
            this.cognome = rs.getString("COGNOME");
            this.hashPsw = rs.getString("HASHPSW");
            this.salt = rs.getString("SALT");
            this.timestamp = rs.getString("TIMESTAMP");
            this.iban = rs.getString("IBAN");
            this.num_conto = rs.getString("NUM_CONTO");
            this.saldo = rs.getDouble("SALDO");
        } else {
            throw new AccountNotFoundException("Account not in database");
        }


    }

    public static void checkRegex(String string, String regex) throws IllegalArgumentException {

        if (string == null) throw new IllegalArgumentException();

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(string);

        if (!m.matches()) throw new IllegalArgumentException();
    }


    public static void checkValidPassword(String password) throws IllegalArgumentException {
        // Regex to check valid password.

        IllegalArgumentException invalidPasswordException = new IllegalArgumentException("La password deve contenere un carattere minuscolo, uno maiuscolo, " +
                "\nun numero, un carattere speciale e deve essere lunga almeno 8 caratteri");

        String regex = "^(?=.*[0-9])"                     //un numero
                + "(?=.*[a-z])"                      //una lettere minuscola
                + "(?=.*[A-Z])"                      //una lettere maiuscola
                + "(?=.*[!£$%&/()=?^*§°çé\"])"       //un caratteri speciale
                + "(?=\\S+$).{8,20}$";               //lunghezza tra 8 e 20

        try {
            checkRegex(password, regex);
        } catch (IllegalArgumentException ex) {
            throw invalidPasswordException;
        }

    }

    private String random(int length) {
        //Use cryptographically secure random number generator
        Random random = new SecureRandom();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(9));
        }
        return String.valueOf(result);
    }

    public String hash(String psw) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update((psw + this.salt).getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();


    }

    //SETTER
    protected void setPassword(String psw) throws SQLException, TimeoutException {
        checkValidPassword(psw);
        this.hashPsw = hash(psw);

    }

    //GETTER
    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getHashPsw() throws SQLException {
        return hashPsw;
    }

    public String getSalt() {
        return salt;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getNum_conto() {
        return num_conto;
    }

    public String getIban() {
        return iban;
    }

    public Double getSaldo() throws SQLException, TimeoutException {
        return saldo;
    }

    protected void setSaldo(Double saldo) throws SQLException, TimeoutException {
        this.saldo = saldo;

    }


}
