/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Account;

import javax.security.auth.login.CredentialException;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
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

        pswFLD.setEchoChar(echochar);
        newPswFLD.setEchoChar(echochar);
        confirmNewPswFLD.setEchoChar(echochar);

        pswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if (String.valueOf(pswFLD.getPassword()).isEmpty() || session.hash(String.valueOf(pswFLD.getPassword())).equals(session.getHashPsw())) {
                        pswFLD.setForeground(new JPasswordField().getForeground());
                        pswFLD.setBorder(new JPasswordField().getBorder());
                    } else {
                        pswFLD.setForeground(Color.red);
                        pswFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
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
                    newPswFLD.setForeground(new JPasswordField().getForeground());
                    newPswFLD.setBorder(new JPasswordField().getBorder());
                } else {
                    try {
                        Account.checkValidPassword(String.valueOf(newPswFLD.getPassword()));
                        newPswFLD.setForeground(new JPasswordField().getForeground());
                        newPswFLD.setBorder(new JPasswordField().getBorder());
                    } catch (IllegalArgumentException ex) {
                        newPswFLD.setForeground(Color.red);
                        newPswFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
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

                if (String.valueOf(confirmNewPswFLD.getPassword()).isEmpty() || (lenghtPsw2 > lenghtPsw1) || subPsw1.equals(String.valueOf(confirmNewPswFLD.getPassword()))) {
                    confirmNewPswFLD.setForeground(new JPasswordField().getForeground());
                    confirmNewPswFLD.setBorder(new JPasswordField().getBorder());
                } else {
                    confirmNewPswFLD.setForeground(Color.red);
                    confirmNewPswFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
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

        saveBTN.addActionListener(this::changePassword);

        backBTN.addActionListener(this::homeAction);

        logoutBTN.addActionListener(this::logOutAction);

    }

    private void changePassword(ActionEvent e) {
        System.out.println("Save BTN pressed");
        try {
            session.changePassword(String.valueOf(pswFLD.getPassword()), String.valueOf(newPswFLD.getPassword()), String.valueOf(confirmNewPswFLD.getPassword()));
            JOptionPane.showMessageDialog(getContentPane(), "Modifica effettuata correttamente");
            homeAction(null);

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
            pswFLD.setForeground(new JPasswordField().getForeground());
            pswFLD.setBorder(new JPasswordField().getBorder());
            newPswFLD.setForeground(new JPasswordField().getForeground());
            newPswFLD.setBorder(new JPasswordField().getBorder());
            confirmNewPswFLD.setForeground(new JPasswordField().getForeground());
            confirmNewPswFLD.setBorder(new JPasswordField().getBorder());
        }
    }
}