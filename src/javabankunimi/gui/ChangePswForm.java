/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.regex.RegexChecker;

import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class ChangePswForm extends MainApp {
    private JPanel changePswPanel;
    private JLabel icon;
    private JPasswordField pswFLD;
    private JPasswordField newPswFLD;
    private JPasswordField confirmNewPswFLD;
    private JButton showHideBTN1;
    private JButton showHideBTN2;
    private JButton showHideBTN3;
    private JButton saveBTN;
    private JButton backBTN;
    private JButton logoutBTN;
    private JLabel dateLBL;
    private JLabel clockLBL;

    public ChangePswForm() {
        session.updateSessionCreation();
        setContentPane(changePswPanel);
        pack();
        setLocation(location);
        setTitle("Change Password - JavaBank");
        setFrameIcon(changePswIconPath);
        setCustomIcon(icon, changePswIconPath);
        setVisible(true);
        getRootPane().setDefaultButton(saveBTN);

        displayClock(clockLBL, dateLBL);
        setCustomIcon(showHideBTN1, hidePswIconPath);
        setCustomIcon(showHideBTN2, hidePswIconPath);
        setCustomIcon(showHideBTN3, hidePswIconPath);
        setHandCursor(backBTN,logoutBTN,saveBTN,showHideBTN1,showHideBTN2,showHideBTN3);

        pswFLD.setEchoChar(echochar);
        newPswFLD.setEchoChar(echochar);
        confirmNewPswFLD.setEchoChar(echochar);

        pswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if (String.valueOf(pswFLD.getPassword()).isEmpty()
                            || session.hash(pswFLD.getPassword()).equals(session.getHashPsw())) {
                        setFieldOnCorrect(pswFLD);
                    } else {
                        setFieldOnError(pswFLD);
                    }
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }
        });

        newPswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (String.valueOf(newPswFLD.getPassword()).isEmpty()) {
                    setFieldOnCorrect(newPswFLD);
                } else {
                    try {
                        RegexChecker.checkValidPassword(newPswFLD.getPassword());
                        setFieldOnCorrect(newPswFLD);
                    } catch (IllegalArgumentException ex) {
                        setFieldOnError(newPswFLD);
                    }
                }
            }
        });

        confirmNewPswFLD.addKeyListener(new KeyAdapter() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public void keyReleased(KeyEvent e) {
                int lenghtPsw1 = newPswFLD.getPassword().length;
                int lenghtPsw2 = confirmNewPswFLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(newPswFLD.getPassword()).substring(0, lenghtPsw2);

                if (String.valueOf(confirmNewPswFLD.getPassword()).isEmpty()
                        || (lenghtPsw2 > lenghtPsw1)
                        || subPsw1.equals(String.valueOf(confirmNewPswFLD.getPassword()))) {
                    setFieldOnCorrect(confirmNewPswFLD);
                } else {
                    setFieldOnError(confirmNewPswFLD);
                }
            }
        });

        showHideBTN1.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                pswFLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN1, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pswFLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN1, hidePswIconPath);
            }
        });

        showHideBTN2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                newPswFLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                newPswFLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN2, hidePswIconPath);
            }
        });

        showHideBTN3.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                confirmNewPswFLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN3, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                confirmNewPswFLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN1, hidePswIconPath);
            }
        });

        saveBTN.addActionListener(e -> defaultAction());

        backBTN.addActionListener(e -> displayHomeForm());

        logoutBTN.addActionListener(e -> logOutAction());

    }


    @Override
    protected void defaultAction() {
        System.out.println("Save BTN pressed");
        try {
            session.changePassword(pswFLD.getPassword(),
                    newPswFLD.getPassword(),
                    confirmNewPswFLD.getPassword());
            JOptionPane.showMessageDialog(getContentPane(), "Modifica effettuata correttamente");
            displayHomeForm();

        } catch (IllegalArgumentException ex) {
            newPswFLD.setText("");
            confirmNewPswFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (CredentialException ex) {
            pswFLD.setText("");
            newPswFLD.setText("");
            confirmNewPswFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);

        } finally {
            setFieldOnCorrect(pswFLD);
            setFieldOnCorrect(newPswFLD);
            setFieldOnCorrect(confirmNewPswFLD);
        }
    }
}