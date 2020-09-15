/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Account;
import javabankunimi.bank.Bank;
import javabankunimi.regex.RegexChecker;

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Arrays;

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
    private JLabel nomeCheckLBL;
    private JLabel cognomeCheckLBL;
    private JLabel psw1CheckLBL;
    private JLabel psw2CheckLBL;

    public SignUpForm() {

        System.out.println(session);
        setContentPane(signupPanel);

        pack();
        setLocation(location);
        setTitle("SignUP - JavaBank");
        displayClock(clockLBL, dateLBL);
        setFrameIcon(Icons.SIGNUP);
        setCustomIcon(Icons.SIGNUP, icon);
        setResizable(false);
        setVisible(true);
        getRootPane().setDefaultButton(signupBTN);

        setHandCursor(signupBTN, exitBTN, backBTN);

        psw1FLD.setEchoChar(Echochar.HIDE);
        psw2FLD.setEchoChar(Echochar.HIDE);

        SwingUtilities.invokeLater(nomeFLD::requestFocus);

        setCustomIcon(Icons.HIDEPSW, showHideBTN1);
        setCustomIcon(Icons.HIDEPSW, showHideBTN2);
        clearFields(nomeCheckLBL, cognomeCheckLBL, psw1CheckLBL, psw2CheckLBL);
        setHandCursor(backBTN, exitBTN, showHideBTN1, showHideBTN2, signupBTN);

        JOptionPane pswInvalid = new JOptionPane();

        nomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (nomeFLD.getText().isEmpty()) {
                    setFieldOnCorrect(nomeFLD);
                    setCustomIcon(null, nomeCheckLBL);
                } else try {
                    RegexChecker.validateName(nomeFLD.getText());
                    setFieldOnCorrect(nomeFLD);
                    setCustomIcon(Icons.OK, nomeCheckLBL);
                } catch (InvalidNameException ex) {
                    setFieldOnError(nomeFLD);
                    setCustomIcon(Icons.X, nomeCheckLBL);
                }
            }
        });

        cognomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (cognomeFLD.getText().isEmpty()) {
                    setFieldOnCorrect(cognomeFLD);
                    setCustomIcon(null, cognomeCheckLBL);
                } else try {
                    RegexChecker.validateName(cognomeFLD.getText());
                    setFieldOnCorrect(cognomeFLD);
                    setCustomIcon(Icons.OK, cognomeCheckLBL);
                } catch (InvalidNameException ex) {
                    setFieldOnError(cognomeFLD);
                    setCustomIcon(Icons.X, cognomeCheckLBL);
                }
            }
        });

        psw1FLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (psw1FLD.getPassword().length == 0) {
                    setFieldOnCorrect(psw1FLD);
                    setCustomIcon(null, psw1CheckLBL);
                } else try {
                    RegexChecker.validatePassword(psw1FLD.getPassword());
                    setFieldOnCorrect(psw1FLD);
                    setCustomIcon(Icons.OK, psw1CheckLBL);
                } catch (IllegalArgumentException ex) {
                    pswInvalid.setMessage(ex.getMessage());
                    setFieldOnError(psw1FLD);
                    setCustomIcon(null, psw1CheckLBL);
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


                if (psw2FLD.getPassword().length == 0 || subPsw1.equals(String.valueOf(psw2FLD.getPassword()))) {

                    setFieldOnCorrect(psw2FLD);

                    if (lenghtPsw1 == lenghtPsw2 && lenghtPsw2 != 0) {
                        setCustomIcon(Icons.OK, psw2CheckLBL);
                    } else {
                        setCustomIcon(null, psw2CheckLBL);
                    }

                } else {
                    setFieldOnError(psw2FLD);
                    setCustomIcon(Icons.X, psw2CheckLBL);
                }


            }
        });

        showHideBTN1.addMouseListener(new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                psw1FLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(Icons.SHOWPSW, showHideBTN1);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw1FLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(Icons.HIDEPSW, showHideBTN1);
            }

        });

        showHideBTN2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                psw2FLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(Icons.SHOWPSW, showHideBTN2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw2FLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(Icons.HIDEPSW, showHideBTN2);
            }

        });

        signupBTN.addActionListener(e -> defaultAction());

        backBTN.addActionListener(e -> displayLoginForm());

        exitBTN.addActionListener(e -> exitAction());

    }


    @Override
    protected void defaultAction() {
        if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || psw1FLD.getPassword().length == 0 || psw2FLD.getPassword().length == 0 ) {
            playSound(Sounds.ERROR);
            JOptionPane.showMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
        } else if (Arrays.equals(psw1FLD.getPassword(), psw2FLD.getPassword())) {
            if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?") == 0) {

                try {
                    Account account = new Account(nomeFLD.getText(), cognomeFLD.getText(), psw1FLD.getPassword());
                    Bank.addAccount(account);
                    if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Login as " + account.getNome() + " " + account.getCognome() + "?") == 0) {
                        session = Bank.login(account.getUsername(), psw1FLD.getPassword());
                        playSound(Sounds.WELCOME);
                        displayHomeForm();
                    } else {
                        displayLoginForm();
                        clearFields(nomeFLD, cognomeFLD, psw1FLD, psw2FLD);
                    }

                } catch (IllegalArgumentException ex) {
                    playSound(Sounds.ERROR);
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearFields(psw1FLD, psw2FLD);

                } catch (InvalidNameException ex) {
                    playSound(Sounds.ERROR);
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearFields(nomeFLD, cognomeFLD);

                } catch (AccountException ex) {
                    playSound(Sounds.ERROR);
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearFields(nomeFLD, cognomeFLD, psw1FLD, psw2FLD);

                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);

                } catch (CredentialException ex) {
                    //
                }
            }

        } else {
            playSound(Sounds.ERROR);
            clearFields(psw1FLD, psw2FLD);
            JOptionPane.showMessageDialog(getContentPane(), "Password non corrette");
        }
    }

    private void displayLoginForm() {
        timer.cancel();
        new LoginForm();
        dispose();
    }
}
