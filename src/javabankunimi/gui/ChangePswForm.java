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
    private JLabel timerLBL;
    private JLabel pswCheckLBL;
    private JLabel newPswCheckLBL;
    private JLabel confirmNewPswCheckLBL;

    public ChangePswForm() {
        session.updateCreation();
        setContentPane(changePswPanel);
        pack();
        setLocation(location);
        setTitle("Change Password - JavaBank");
        setFrameIcon(Icons.CHANGEPSW);
        setCustomIcon(icon, Icons.CHANGEPSW);
        setVisible(true);
        getRootPane().setDefaultButton(saveBTN);

        displayClock(clockLBL, dateLBL);
        setSessionTimer(timerLBL);
        setCustomIcon(showHideBTN1, Icons.HIDEPSW);
        setCustomIcon(showHideBTN2, Icons.HIDEPSW);
        setCustomIcon(showHideBTN3, Icons.HIDEPSW);
        setHandCursor(backBTN, logoutBTN, saveBTN, showHideBTN1, showHideBTN2, showHideBTN3);
        clearFields(pswCheckLBL, newPswCheckLBL, confirmNewPswCheckLBL);

        pswFLD.setEchoChar(Echochar.HIDE);
        newPswFLD.setEchoChar(Echochar.HIDE);
        confirmNewPswFLD.setEchoChar(Echochar.HIDE);
        JOptionPane pswInvalid = new JOptionPane();

        pswFLD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if (pswFLD.getPassword().length == 0
                            || session.hash(pswFLD.getPassword()).equals(session.getHashPsw())) {
                        setFieldOnCorrect(pswFLD);
                        if (pswFLD.getPassword().length != 0) {
                            setCustomIcon(pswCheckLBL, Icons.OK);
                        }
                    } else {
                        setFieldOnError(pswFLD);
                        setCustomIcon(pswCheckLBL, null);
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
                    setCustomIcon(newPswCheckLBL, null);
                } else try {
                    RegexChecker.validatePassword(newPswFLD.getPassword());
                    setFieldOnCorrect(newPswFLD);
                    setCustomIcon(newPswCheckLBL, Icons.OK);
                } catch (IllegalArgumentException ex) {
                    pswInvalid.setMessage(ex.getMessage());
                    setFieldOnError(newPswFLD);
                    setCustomIcon(newPswCheckLBL, null);
                }
            }
        });

        confirmNewPswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                int lenghtPsw1 = newPswFLD.getPassword().length;
                int lenghtPsw2 = confirmNewPswFLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(newPswFLD.getPassword()).substring(0, lenghtPsw2);


                if (String.valueOf(confirmNewPswFLD.getPassword()).isEmpty() || subPsw1.equals(String.valueOf(confirmNewPswFLD.getPassword()))) {

                    setFieldOnCorrect(confirmNewPswFLD);

                    if (lenghtPsw1 == lenghtPsw2 && lenghtPsw2 != 0) {
                        setCustomIcon(confirmNewPswCheckLBL, Icons.OK);
                    } else {
                        setCustomIcon(confirmNewPswCheckLBL, null);
                    }

                } else {
                    setFieldOnError(confirmNewPswFLD);
                    setCustomIcon(confirmNewPswCheckLBL, Icons.X);
                }
            }
        });

        showHideBTN1.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                pswFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(showHideBTN1, Icons.SHOWPSW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pswFLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(showHideBTN1, Icons.HIDEPSW);
            }
        });

        showHideBTN2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                newPswFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(showHideBTN2, Icons.SHOWPSW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                newPswFLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(showHideBTN2, Icons.HIDEPSW);
            }
        });

        showHideBTN3.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                confirmNewPswFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(showHideBTN3, Icons.SHOWPSW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                confirmNewPswFLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(showHideBTN3, Icons.HIDEPSW);
            }
        });

        saveBTN.addActionListener(e -> defaultAction());

        backBTN.addActionListener(e -> displayHomeForm());

        logoutBTN.addActionListener(e -> logOutAction());

    }


    @Override
    protected void defaultAction() {
        try {
            session.changePassword(pswFLD.getPassword(),
                    newPswFLD.getPassword(),
                    confirmNewPswFLD.getPassword());
            JOptionPane.showMessageDialog(getContentPane(), "Modifica effettuata correttamente");
            displayHomeForm();

        } catch (IllegalArgumentException ex) {
            playSound(Sounds.ERROR);
            clearFields(newPswFLD,confirmNewPswFLD);
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (CredentialException ex) {
            playSound(Sounds.ERROR);
            clearFields(pswFLD,newPswFLD,confirmNewPswFLD);
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);

        } finally {
            setFieldOnCorrect(pswFLD, newPswFLD, confirmNewPswFLD);
        }
    }
}