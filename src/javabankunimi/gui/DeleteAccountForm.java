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
    private JLabel pswCheckLBL;
    private JLabel confirmPswCheckLBL;

    public DeleteAccountForm() {
        session.updateCreation();
        System.out.println(session);
        setContentPane(removePanel);

        pack();
        setLocation(location);
        setTitle("Delete Account - JavaBank");
        setFrameIcon(Icons.DELETE_ACCOUNT);
        setCustomIcon(icon, Icons.DELETE_ACCOUNT);

        setResizable(false);
        setVisible(true);

        getRootPane().setDefaultButton(deleteBTN);

        displayClock(clockLBL, dateLBL);
        setSessionTimer(timerLBL);
        setCustomIcon(showHideBTN1, Icons.HIDEPSW);
        setCustomIcon(showHideBTN2, Icons.HIDEPSW);
        setHandCursor(backBTN, deleteBTN, logoutBTN, showHideBTN1, showHideBTN2);
        pswFLD.setEchoChar(Echochar.HIDE);
        confirmPswFLD.setEchoChar(Echochar.HIDE);
        clearFields(pswCheckLBL, confirmPswCheckLBL);

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

        confirmPswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                int lenghtPsw1 = pswFLD.getPassword().length;
                int lenghtPsw2 = confirmPswFLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(pswFLD.getPassword()).substring(0, lenghtPsw2);

                if (confirmPswFLD.getPassword().length == 0 || (lenghtPsw2 > lenghtPsw1) || subPsw1.equals(String.valueOf(confirmPswFLD.getPassword()))) {
                    setFieldOnCorrect(confirmPswFLD);
                    try {
                        if (session.hash(confirmPswFLD.getPassword()).equals(session.getHashPsw())) {
                            setFieldOnCorrect(confirmPswFLD);
                            setCustomIcon(confirmPswCheckLBL, Icons.OK);
                        } else {
                            setCustomIcon(confirmPswCheckLBL, null);
                        }
                    } catch (SQLException ex) {
                        SQLExceptionOccurred(ex);
                    }

                } else {
                    setFieldOnError(confirmPswFLD);
                    setCustomIcon(confirmPswCheckLBL, Icons.X);
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
                confirmPswFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                setCustomIcon(showHideBTN2, Icons.SHOWPSW);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                confirmPswFLD.setEchoChar(Echochar.HIDE);
                setCustomIcon(showHideBTN2, Icons.HIDEPSW);
            }
        });


        deleteBTN.addActionListener(e -> defaultAction());
        logoutBTN.addActionListener(e -> logOutAction());
        backBTN.addActionListener(e -> displayHomeForm());

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
                playSound(Sounds.ERROR);
                pswFLD.setText("");
                confirmPswFLD.setText("");
                JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);

            } finally {
                setFieldOnCorrect(pswFLD, confirmPswFLD);
            }
        }
    }
}