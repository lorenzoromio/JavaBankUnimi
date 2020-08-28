/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class LoginForm extends WebApp {
    private JTextField userFLD;
    private JPasswordField pswFLD;
    private JButton loginBTN;
    private JButton signupBTN;
    private JButton exitBTN;
    private JPanel loginPanel;
    private JLabel icon;
    private JPanel credentialPanel;
    private JPanel buttonsPanel;

    public LoginForm() {
        new LoginForm("", "");
    }

    public LoginForm(String user, String psw) {
        super();
        this.user = user;
        this.psw = psw;
        try {
            DBConnect.close();              //CHIUDE LA CONNESSIONE PRIMA DI EFFETTUARE UN NUOVO LOGIN
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        setContentPane(loginPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        setTitle("LoginPage - JavaBank");
        setFrameIcon(bankIconPath);
        setLabelIcon(icon,bankIconPath);
        getRootPane().setDefaultButton(loginBTN);               //SELEZIONA IL PULSANTE DI LOGIN
        SwingUtilities.invokeLater(userFLD::requestFocus);       //FOCUS SUL CAMPO USERNAME

        userFLD.setText(this.user);
        pswFLD.setText(this.psw);

        loginBTN.addActionListener((ActionEvent e) -> {
            if (userFLD.getText().isEmpty())
                JOptionPane.showInternalMessageDialog(getContentPane(), "Username can't be empty");
            else try {
                session = Bank.login(userFLD.getText(), String.valueOf(pswFLD.getPassword()));
                new HomeForm();
                dispose();

//                JOptionPane.showInternalMessageDialog(getContentPane(), "Logged in Succesfully as " + session.getNome() + " " + session.getCognome());

            } catch (AccountNotFoundException ex) {
                userFLD.setText("");
                pswFLD.setText("");
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            } catch (CredentialException ex) {
                pswFLD.setText("");
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            } catch (SQLException ex) {
                switch (ex.getSQLState()) {
                    case "28000":
                        JOptionPane.showInternalMessageDialog(getContentPane(), "Invalid Credential to Database");
                        break;
                    case "08S01":
                        JOptionPane.showInternalMessageDialog(getContentPane(), "Database Unreachable");
                        break;
                    default:
                        SQLExceptionOccurred(ex);
                        break;
                }

            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            }  catch (TimeoutException ex) {
                sessionExpired();
            }
        });

        signupBTN.addActionListener((ActionEvent e) -> {
            System.out.println("register BTN pressed");
//            signupPage();
        });

        exitBTN.addActionListener(this::actionExit);
    }

}
