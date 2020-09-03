/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Account;
import javabankunimi.bank.Bank;
import javabankunimi.bank.RegexChecker;

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SignUpForm extends MainApp {
    private JPanel signupPanel;
    private JTextField nomeFLD;
    private JButton signupBTN;
    private JButton backBTN;
    private JButton exitBTN;
    private JLabel icon;
    private JTextField cognomeFLD;
    private JPasswordField psw1FLD;
    private JPasswordField psw2FLD;
    private JButton showHideBTN1;
    private JButton showHideBTN2;
    private JLabel dateLBL;
    private JLabel clockLBL;

    public SignUpForm() {

        System.out.println(session);
        setContentPane(signupPanel);

        pack();
        setLocation(location);
        setTitle("SignUP - JavaBank");
        displayClock(clockLBL, dateLBL);
        setFrameIcon(signUpIconPath);
        setCustomIcon(icon, signUpIconPath);
        setResizable(false);
        setVisible(true);
        getRootPane().setDefaultButton(signupBTN);

        setHandCursor(signupBTN, exitBTN, backBTN);

        psw1FLD.setEchoChar(echochar);
        psw2FLD.setEchoChar(echochar);

        SwingUtilities.invokeLater(nomeFLD::requestFocus);

        setCustomIcon(showHideBTN1, hidePswIconPath);
        setCustomIcon(showHideBTN2, hidePswIconPath);
        setHandCursor(backBTN, exitBTN, showHideBTN1, showHideBTN2, signupBTN);

        JOptionPane pswInvalid = new JOptionPane();

        nomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (nomeFLD.getText().isEmpty()) {
                    setFieldOnCorrect(nomeFLD);
                } else try {
                    RegexChecker.checkValidName(nomeFLD.getText());
                    setFieldOnCorrect(nomeFLD);
                } catch (InvalidNameException ex) {
                    setFieldOnError(nomeFLD);
                }
            }
        });

        cognomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (cognomeFLD.getText().isEmpty()) {
                    setFieldOnCorrect(cognomeFLD);
                } else try {
                    RegexChecker.checkValidName(cognomeFLD.getText());
                    setFieldOnCorrect(cognomeFLD);
                } catch (InvalidNameException ex) {
                    setFieldOnError(cognomeFLD);
                }
            }
        });

        psw1FLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (String.valueOf(psw1FLD.getPassword()).isEmpty()) {
                    setFieldOnCorrect(psw1FLD);
                } else try {
                    RegexChecker.checkValidPassword(String.valueOf(psw1FLD.getPassword()));
                    setFieldOnCorrect(psw1FLD);
                } catch (IllegalArgumentException ex) {
                    pswInvalid.setMessage(ex.getMessage());
                    setFieldOnError(psw1FLD);
                }
            }
        });

        psw2FLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                int lenghtPsw1 = psw1FLD.getPassword().length;
                int lenghtPsw2 = psw2FLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(psw1FLD.getPassword()).substring(0, lenghtPsw2);

                if (subPsw1.equals(String.valueOf(psw2FLD.getPassword())) || lenghtPsw2 > lenghtPsw1) {
                    setFieldOnCorrect(psw2FLD);
                } else {
                    setFieldOnError(psw2FLD);
                }
            }
        });

        showHideBTN1.addMouseListener(new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                psw1FLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN1, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw1FLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN1, hidePswIconPath);
            }

        });

        showHideBTN2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                psw2FLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw2FLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN2, hidePswIconPath);
            }

        });

        signupBTN.addActionListener(this::signUpAction);

        backBTN.addActionListener(this::logInAction);

        exitBTN.addActionListener(this::exitAction);

    }


    private void signUpAction(ActionEvent e) {
        if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || String.valueOf(psw1FLD.getPassword()).isEmpty() || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
        } else if (String.valueOf(psw1FLD.getPassword()).equals(String.valueOf(psw2FLD.getPassword()))) {
            if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?") == 0) {

                try {
                    Account account = new Account(nomeFLD.getText(), cognomeFLD.getText(), String.valueOf(psw1FLD.getPassword()));
                    Bank.addAccount(account);
                    if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Login as " + account.getNome() + " " + account.getCognome() + "?") == 0) {
                        session = Bank.login(account.getUsername(), String.valueOf(psw1FLD.getPassword()));
                        homeAction(null);
                    } else {
                        logInAction(null);
                        clearFields(nomeFLD, cognomeFLD, psw1FLD, psw2FLD);

                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearFields(psw1FLD, psw2FLD);

                } catch (InvalidNameException ex) {
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearFields(nomeFLD, cognomeFLD);

                } catch (AccountException ex) {
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearFields(nomeFLD, cognomeFLD, psw1FLD, psw2FLD);


                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);

                } catch (CredentialException ex) {
                    //
                }
            }

        } else {
            clearFields(psw1FLD, psw2FLD);

            JOptionPane.showMessageDialog(getContentPane(), "Password non corrette");
        }
    }

    private void clearNamesFields() {
        nomeFLD.setText("");
        cognomeFLD.setText("");
        nomeFLD.setForeground(new JLabel().getForeground());
        cognomeFLD.setForeground(new JLabel().getForeground());
    }

    private void clearPasswordFields() {
        psw1FLD.setText("");
        psw2FLD.setText("");
        psw1FLD.setForeground(new JPasswordField().getForeground());
        psw2FLD.setForeground(new JPasswordField().getForeground());
    }

    private void logInAction(ActionEvent e) {
        timer.cancel();
        new LoginForm();
        dispose();
    }
}
