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
        setCustomIcon(icon, Icons.SIGNUP);
        setResizable(false);
        setVisible(true);
        getRootPane().setDefaultButton(signupBTN);

        setHandCursor(signupBTN, exitBTN, backBTN);

        psw1FLD.setEchoChar(Echochar.HIDE);
        psw2FLD.setEchoChar(Echochar.HIDE);

        SwingUtilities.invokeLater(nomeFLD::requestFocus);

        setCustomIcon(showHideBTN1, Icons.HIDEPSW);
        setCustomIcon(showHideBTN2, Icons.HIDEPSW);
        clearFields(nomeCheckLBL, cognomeCheckLBL, psw1CheckLBL, psw2CheckLBL);
        setHandCursor(backBTN, exitBTN, showHideBTN1, showHideBTN2, signupBTN);

        JOptionPane pswInvalid = new JOptionPane();

        nomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (nomeFLD.getText().isEmpty()) {
                    setFieldOnCorrect(nomeFLD);
                    setCustomIcon(nomeCheckLBL, null);
                } else try {
                    RegexChecker.validateName(nomeFLD.getText());
                    setFieldOnCorrect(nomeFLD);
                    setCustomIcon(nomeCheckLBL, Icons.OK);
                } catch (InvalidNameException ex) {
                    setFieldOnError(nomeFLD);
                    setCustomIcon(nomeCheckLBL, Icons.X);
                }
            }
        });

        cognomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (cognomeFLD.getText().isEmpty()) {
                    setFieldOnCorrect(cognomeFLD);
                    setCustomIcon(cognomeCheckLBL, null);
                } else try {
                    RegexChecker.validateName(cognomeFLD.getText());
                    setFieldOnCorrect(cognomeFLD);
                    setCustomIcon(cognomeCheckLBL, Icons.OK);
                } catch (InvalidNameException ex) {
                    setFieldOnError(cognomeFLD);
                    setCustomIcon(cognomeCheckLBL, Icons.X);
                }
            }
        });

        psw1FLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (String.valueOf(psw1FLD.getPassword()).isEmpty()) {
                    setFieldOnCorrect(psw1FLD);
                    setCustomIcon(psw1CheckLBL, null);
                } else try {
                    RegexChecker.validatePassword(psw1FLD.getPassword());
                    setFieldOnCorrect(psw1FLD);
                    setCustomIcon(psw1CheckLBL, Icons.OK);
                } catch (IllegalArgumentException ex) {
                    pswInvalid.setMessage(ex.getMessage());
                    setFieldOnError(psw1FLD);
                    setCustomIcon(psw1CheckLBL, null);
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


                if (String.valueOf(psw2FLD.getPassword()).isEmpty() || subPsw1.equals(String.valueOf(psw2FLD.getPassword()))) {

                    setFieldOnCorrect(psw2FLD);

                    if (lenghtPsw1 == lenghtPsw2 && lenghtPsw2 != 0) {
                        setCustomIcon(psw2CheckLBL, Icons.OK);
                    } else {
                        setCustomIcon(psw2CheckLBL, null);
                    }

                } else {
                    setFieldOnError(psw2FLD);
                    setCustomIcon(psw2CheckLBL, Icons.X);
                }


            }
        });

        showHideBTN1.addMouseListener(new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                psw1FLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(showHideBTN1, Icons.SHOWPSW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw1FLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(showHideBTN1, Icons.HIDEPSW);
            }

        });

        showHideBTN2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                psw2FLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(showHideBTN2, Icons.SHOWPSW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw2FLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(showHideBTN2, Icons.HIDEPSW);
            }

        });

        signupBTN.addActionListener(e -> defaultAction());

        backBTN.addActionListener(e -> displayLoginForm());

        exitBTN.addActionListener(e -> exitAction());

    }


    @Override
    protected void defaultAction() {
        if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || String.valueOf(psw1FLD.getPassword()).isEmpty() || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
            playSound(Sounds.ERROR);
            JOptionPane.showMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
        } else if (String.valueOf(psw1FLD.getPassword()).equals(String.valueOf(psw2FLD.getPassword()))) {
            if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?") == 0) {

                try {
                    Account account = new Account(nomeFLD.getText(), cognomeFLD.getText(), psw1FLD.getPassword());
                    Bank.addAccount(account);
                    if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Login as " + account.getNome() + " " + account.getCognome() + "?") == 0) {
                        session = Bank.login(account.getUsername(), psw1FLD.getPassword());
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
