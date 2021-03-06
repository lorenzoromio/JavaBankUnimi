/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Bank;
import javabankunimi.database.DBConnect;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.sql.SQLException;

public class LoginForm extends MainApp {

    private JPanel loginPanel;
    private JLabel icon;
    private JTextField userFLD;
    private JPasswordField pswFLD;
    private JButton loginBTN;
    private JButton signupBTN;
    private JButton exitBTN;
    private JLabel clockLBL;
    private JLabel dateLBL;


    public LoginForm() {

        try {
            DBConnect.close();
            //CHIUDE LA CONNESSIONE PRIMA DI EFFETTUARE UN NUOVO LOGIN
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }

        setContentPane(loginPanel);
        pack();

        if (location == null) {
            setLocationRelativeTo(null);        //imposta location in centro allo schermo
        } else {
            setLocation(location);
        }

        setVisible(true);
        setTitle("LoginPage - JavaBank");
        setFrameIcon(Icons.BANK);
        setCustomIcon(Icons.BANK, icon);
        getRootPane().setDefaultButton(loginBTN);               //SELEZIONA IL PULSANTE DI LOGIN
        SwingUtilities.invokeLater(userFLD::requestFocus);       //FOCUS SUL CAMPO USERNAME
        setHandCursor(exitBTN, loginBTN, signupBTN);

        displayClock(clockLBL, dateLBL);


        loginBTN.addActionListener(e -> defaultAction());

        signupBTN.addActionListener(e -> signupForm());

        exitBTN.addActionListener(e -> exitAction());

    }


    @Override
    protected void defaultAction() {

        if (session != null) {
            playSound(Sounds.ERROR);
            JOptionPane.showMessageDialog(getContentPane(), "User already logged in this machine", "Duplicate Session", JOptionPane.ERROR_MESSAGE);
        } else if (userFLD.getText().isEmpty()) {
            playSound(Sounds.ERROR);
            JOptionPane.showMessageDialog(getContentPane(), "Username can't be empty", "Error", JOptionPane.ERROR_MESSAGE);

        } else try {
            timer.cancel();
            session = Bank.login(userFLD.getText(), pswFLD.getPassword());
            location = this.getLocation();

            playSound(Sounds.ACCESS_GRANTED);
            new HomeForm();
            dispose();

        } catch (AccountNotFoundException ex) {
            userFLD.setText("");
            pswFLD.setText("");
            playSound(Sounds.ERROR);
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "Account Not Found", JOptionPane.ERROR_MESSAGE);

        } catch (CredentialException ex) {
            pswFLD.setText("");
            playSound(Sounds.ERROR);
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (SQLException ex) {
            switch (ex.getSQLState()) {
                case "28000":
                case "08001":
                    JOptionPane.showMessageDialog(getContentPane(), "Invalid Credential to Database", "SQL Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case "08S01":
                    JOptionPane.showMessageDialog(getContentPane(), "Database Unreachable", "SQL Error", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    SQLExceptionOccurred(ex);
                    break;
            }
        }
    }

    private void signupForm() {
        timer.cancel();
        location = getLocation();
        new SignUpForm();
        dispose();
    }


}