/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

@SuppressWarnings("ConstantConditions")
public class DeleteAccountForm extends MainApp {
    private JPanel removePanel;
    private JLabel icon;
    private JPasswordField confirmPswFLD;
    private JPasswordField pswFLD;
    private JButton showHideBTN1;
    private JButton showHideBTN2;
    private JButton deleteBTN;
    private JButton backBTN;
    private JButton logoutBTN;
    private JLabel dateLBL;
    private JLabel clockLBL;
    private JLabel timerLBL;

    public DeleteAccountForm() {
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(removePanel);

        pack();
        setLocation(location);
        setTitle("Delete Account - JavaBank");
        setFrameIcon(deleteAccountIconPath);
        setCustomIcon(icon, deleteAccountIconPath);

        setResizable(false);
        setVisible(true);

        getRootPane().setDefaultButton(deleteBTN);

        displayClock(clockLBL, dateLBL);
        setTimer(timerLBL);
        setCustomIcon(showHideBTN1, hidePswIconPath);
        setCustomIcon(showHideBTN2, hidePswIconPath);
        setHandCursor(backBTN,deleteBTN,logoutBTN,showHideBTN1,showHideBTN2);
        pswFLD.setEchoChar(echochar);
        confirmPswFLD.setEchoChar(echochar);

        pswFLD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if (String.valueOf(pswFLD.getPassword()).isEmpty() || session.hash(pswFLD.getPassword()).equals(session.getHashPsw())) {
                        setFieldOnCorrect(pswFLD);
                    } else {
                        setFieldOnError(pswFLD);
                    }
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }
        });

        confirmPswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                int lenghtPsw1 = pswFLD.getPassword().length;
                int lenghtPsw2 = confirmPswFLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(pswFLD.getPassword()).substring(0, lenghtPsw2);

                if (String.valueOf(confirmPswFLD.getPassword()).isEmpty() || (lenghtPsw2 > lenghtPsw1) || subPsw1.equals(String.valueOf(confirmPswFLD.getPassword()))) {
                    setFieldOnCorrect(confirmPswFLD);
                } else {
                    setFieldOnError(confirmPswFLD);
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
                confirmPswFLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                confirmPswFLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN2, hidePswIconPath);
            }
        });


        deleteBTN.addActionListener(e -> defaultAction());
        logoutBTN.addActionListener(e -> logOutAction());
        backBTN.addActionListener(e -> displayHomeForm());

        pswFLD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                System.out.println("Psw1: " + session.hash(pswFLD.getPassword()));
                try {
                    System.out.println("Psw2: " + session.getHashPsw());
                    System.out.println();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {

                    if (String.valueOf(pswFLD.getPassword()).isEmpty() || session.hash(pswFLD.getPassword()).equals(session.getHashPsw())) {
                        setFieldOnCorrect(pswFLD);
                    } else {
                        setFieldOnError(pswFLD);
                    }
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }


        });
    }

    @Override
    protected void defaultAction() {
        System.out.println("Save BTN pressed");
        int choice = JOptionPane.showConfirmDialog(getContentPane(), "Sei sicuro di voler eliminare il tuo account?");
        if (choice == 0) {

            try {
                session.deleteAccount(pswFLD.getPassword(), confirmPswFLD.getPassword());
                JOptionPane.showMessageDialog(getContentPane(), "Account Eliminato");
                new LoginForm();
                dispose();

            } catch (IllegalArgumentException | CredentialException ex) {
                pswFLD.setText("");
                confirmPswFLD.setText("");
                JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);

            } finally {
                setFieldOnCorrect(pswFLD,confirmPswFLD);
            }
        }
    }
}