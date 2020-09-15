/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.bank;

import javabankunimi.database.DBConnect;
import javabankunimi.regex.RegexChecker;

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Account implements Comparable<Account> {
    private final String nome;
    private final String cognome;
    private final String num_conto;
    private final String iban;
    private final String username;
    private String salt;
    private String timestamp;
    private int ID;
    private String hashPsw;
    private Double saldo;

    //Create a new Account
    public Account(String nome, String cognome, char[] psw) throws SQLException, InvalidNameException, IllegalArgumentException {
        try {
            RegexChecker.validateName(nome);
        } catch (InvalidNameException ex) {
            throw new InvalidNameException("Nome non valido");
        }

        try {
            RegexChecker.validateName(cognome);
        } catch (InvalidNameException ex) {
            throw new InvalidNameException("Cognome non valido");
        }

        this.timestamp = String.valueOf(new Date().getTime());
        this.salt = timestamp.concat(randomString(10));
        setPassword(psw);

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
        this.num_conto = randomString(7);
        this.iban = String.format("IT%sF%s%s", Bank.getAbi(), Bank.getCab(), this.num_conto);
        this.saldo = 0.0;

    }

    //Create ad Account Obj from Database only by Username
    public Account(String username) throws AccountNotFoundException, SQLException {

        String query = "select ID,USERNAME,NOME,COGNOME,IBAN,NUM_CONTO from accounts where USERNAME = ?";
        PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(query);
        prepStmt.setString(1, username);
        ResultSet rs = prepStmt.executeQuery();

        if (rs.next()) {
            this.ID = rs.getInt("ID");
            this.username = rs.getString("USERNAME");
            this.nome = rs.getString("NOME");
            this.cognome = rs.getString("COGNOME");
            this.iban = rs.getString("IBAN");
            this.num_conto = rs.getString("NUM_CONTO");
        } else {
            throw new AccountNotFoundException("Account not in database");
        }
    }

    protected String randomString(int length) {
        //Use cryptographically secure randomString number generator
        Random random = new SecureRandom();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(9));
        }
        return String.valueOf(result);
    }

    public String hash(char[] psw) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update((String.valueOf(psw) + this.salt).getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            Arrays.fill(psw, '0');
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    //SETTER
    protected void setPassword(char[] psw) throws SQLException, IllegalArgumentException {
        RegexChecker.validatePassword(psw);
        this.hashPsw = hash(psw);
        Arrays.fill(psw, '0');

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

    protected void setSalt(String salt) throws SQLException {
        this.salt = salt;
    }

    protected void setHashPsw(String hashPsw) {
        this.hashPsw = hashPsw;
    }

    public String getTimestamp() {
        return timestamp;
    }

    protected void setTimestamp(String timestamp) throws SQLException {
        this.timestamp = timestamp;
    }

    public String getNum_conto() {
        return num_conto;
    }

    public String getIban() {
        return iban;
    }

    public Double getSaldo() throws SQLException {
        return saldo;
    }

    protected void setSaldo(Double saldo) throws SQLException {
        this.saldo = saldo;

    }


    @Override
    public String toString() {
        return "Account {\n" +
                "\tnome= " + nome + '\n' +
                "\tcognome= " + cognome + '\n' +
                "\tnum_conto= " + num_conto + '\n' +
                "\tiban= " + iban + '\n' +
                "\tusername= " + username + '\n' +
                "\tsalt= " + salt + '\n' +
                "\ttimestamp= " + timestamp + '\n' +
                "\tID= " + ID + "\n" +
                "\thashPsw= " + hashPsw + "\n" +
                "\tsaldo= " + saldo + "\n" +
                "}\n";
    }

    @Override
    public int compareTo(Account o) {
        return this.getCognome().compareTo(o.getCognome());
    }


}
