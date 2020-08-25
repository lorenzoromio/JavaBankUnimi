/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Transaction {
    private final String type;
    private final Date date;
    //    private final String timestamp;
    private final String ibanFrom;
    private final String usernameFrom;
    private final Double amount;
    private String ibanDest = null;
    private String usernameDest = null;


    public Transaction(String ibanFrom, String ibanDest, Double amount, Date date) throws SQLException {
        this.type = "bonifico"; //BONIFICO
        this.ibanFrom = ibanFrom;
        this.ibanDest = ibanDest;
        this.usernameFrom = setUsername(ibanFrom);
        this.usernameDest = setUsername(ibanDest);
        this.amount = amount;
        this.date = date;
    }

    public Transaction(String ibanFrom, Double amount, String type, Date date) throws SQLException {
        this.type = type; //prelievo o versamento
        this.ibanFrom = ibanFrom;
        this.amount = amount;
        this.date = date;
        this.usernameFrom = setUsername(ibanFrom);
    }

    private String setUsername(String iban) throws SQLException {
        DBConnect db = new DBConnect();
        String query = "select username from accounts where iban = ?";
        PreparedStatement prepStmt = db.getConnection().prepareStatement(query);
        prepStmt.setString(1, iban);
        ResultSet rs = prepStmt.executeQuery();
        String result = null;
        if (rs.next()) {
            result = rs.getString("USERNAME");
        }
        if (rs != null) rs.close();
        if (prepStmt != null) prepStmt.close();
        return result;
    }

    public void push() throws SQLException {
        try {//
            String query = "INSERT INTO `transaction` (`TYPE`, `DATE`, `IBAN_FROM`,`USERNAME_FROM`, `IBAN_DEST`,`USERNAME_DEST`, `AMOUNT`) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement prepStmt = DBConnect.getConnection().prepareStatement(query);
            prepStmt.setString(1, this.type);
            prepStmt.setString(2, String.valueOf(this.date.getTime()));
            prepStmt.setString(3, this.ibanFrom);
            prepStmt.setString(4, this.usernameFrom);
            prepStmt.setString(5, this.ibanDest);
            prepStmt.setString(6, this.usernameDest);
            prepStmt.setDouble(7, this.amount);
            prepStmt.execute();
            if (prepStmt != null) prepStmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "\n\ttype         = " + type +
                "\n\tdate         = " + date +
                "\n\tibanFrom     = " + ibanFrom +
                "\n\tibanDest     = " + ibanDest +
                "\n\tusernameFrom = " + usernameFrom +
                "\n\tusernameDest = " + usernameDest +
                "\n\tamount       = " + amount +
                "\t}\n--------------------------------------------------";
    }


    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getIbanFrom() {
        return ibanFrom;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public String getIbanDest() {
        return ibanDest;
    }

    public String getUsernameDest() {
        return usernameDest;
    }

    public Double getAmount() {
        return amount;
    }
}





