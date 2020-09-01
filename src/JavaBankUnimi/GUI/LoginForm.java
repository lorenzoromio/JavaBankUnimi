/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package JavaBankUnimi.GUI;

import JavaBankUnimi.Bank.Bank;
import JavaBankUnimi.DBConnect;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class LoginForm extends MainApp {
    private static Timer timer;
    private static TimerTask task;
    private JPanel loginPanel;
    private JPanel credentialPanel;
    private JLabel icon;
    private JTextField userFLD;
    private JPasswordField pswFLD;
    private JPanel buttonsPanel;
    private JButton loginBTN;
    private JButton signupBTN;
    private JButton exitBTN;
    private JLabel clockLBL;


    public LoginForm() {
        setContentPane(loginPanel);
        pack();
        if (location == null) {
            setLocationRelativeTo(null);
        } else
            setLocation(location);
        setVisible(true);
        setTitle("LoginPage - JavaBank");
        setFrameIcon(bankIconPath);
        setCustomIcon(icon, bankIconPath);
        getRootPane().setDefaultButton(loginBTN);               //SELEZIONA IL PULSANTE DI LOGIN
        SwingUtilities.invokeLater(userFLD::requestFocus);       //FOCUS SUL CAMPO USERNAME

//        timer = new Timer();
//        task = new TimerTask() {
//            @Override
//            public void run() {
//                String date = sdf.format(new Date());
//                clockLBL.setText(date);
//                System.out.println(date);
//                try {
//                    if(session!=null)
//                    session.isValid();
//                } catch (TimeoutException e) {
//                    System.out.println("aaaaaaaaaaaaa");
//                    sessionExpired();
//                }
//            }
//        };
//        timer.schedule(task,0,1000);


        try {
            DBConnect.close();              //CHIUDE LA CONNESSIONE PRIMA DI EFFETTUARE UN NUOVO LOGIN
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }

        userFLD.setText(this.user);
        pswFLD.setText(this.psw);

        loginBTN.addActionListener(this::login);

        signupBTN.addActionListener(this::signupForm);

        exitBTN.addActionListener(this::exitAction);

    }

    private void login(ActionEvent e) {
        if (session != null) {
            JOptionPane.showMessageDialog(getContentPane(), "User already logged in this machine");
        } else if (userFLD.getText().isEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Username can't be empty");
        } else try {

            session = Bank.login(userFLD.getText(), String.valueOf(pswFLD.getPassword()));
            location = this.getLocation();
//            timer.cancel();
            new HomeForm();
            dispose();

        } catch (AccountNotFoundException ex) {
            userFLD.setText("");
            pswFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (CredentialException ex) {
            pswFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

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
        } catch (TimeoutException timeoutException) {
            sessionExpired();
        }
    }

    private void signupForm(ActionEvent e) {
        location = getLocation();
        new SignUpForm();
        dispose();
    }
}