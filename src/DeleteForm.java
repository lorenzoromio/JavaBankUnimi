/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class DeleteForm extends WebApp {
    private JPasswordField confirmPswFLD;
    private JPanel removePanel;
    private JPanel credentialPanel;
    private JPasswordField pswFLD;
    private JPanel buttonsPanel;
    private JButton deleteBTN;
    private JButton backBTN;
    private JButton logoutBTN;
    private JLabel icon;
    private JPanel creditsPanel;
    private JButton showHideBTN1;
    private JButton showHideBTN2;
    private JLabel pswLBL;
    private JLabel confirmPswLBL;
    private JLabel checkpswLBL;

    public DeleteForm() throws TimeoutException {
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(removePanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("Delete Account - JavaBank");
        setFrameIcon(deleteAccountIconPath);
        setLabelIcon(icon, deleteAccountIconPath);

        setResizable(false);
        setVisible(true);

        getRootPane().setDefaultButton(deleteBTN);
        checkpswLBL.setVisible(false);
        checkpswLBL.setForeground(Color.red);
        setButtonIcon(showHideBTN1, hidePswIconPath);
        setButtonIcon(showHideBTN2, hidePswIconPath);
        pswFLD.setEchoChar(echochar);
        confirmPswFLD.setEchoChar(echochar);


        confirmPswFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Arrays.equals(pswFLD.getPassword(), confirmPswFLD.getPassword()) || String.valueOf(confirmPswFLD.getPassword()).isEmpty()) {
                    confirmPswFLD.setForeground(new JPasswordField().getForeground());
                    confirmPswFLD.setBorder(new JPasswordField().getBorder());
                    pswFLD.setForeground(new JPasswordField().getForeground());
                    pswFLD.setBorder(new JPasswordField().getBorder());
                    checkpswLBL.setVisible(false);
                } else {
                    confirmPswFLD.setForeground(Color.red);
                    confirmPswFLD.setBorder(BorderFactory.createLineBorder(Color.red));

                    if (pswFLD.getPassword().length == confirmPswFLD.getPassword().length) {
                        pswFLD.setForeground(Color.red);
                        pswFLD.setBorder(BorderFactory.createLineBorder(Color.red));
                        checkpswLBL.setVisible(true);
                    }
                }
            }
        });


        showHideBTN1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                pswFLD.setEchoChar('\u0000');  //Password Visibile
                setButtonIcon(showHideBTN1, showPswIconPath);


            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pswFLD.setEchoChar(echochar);
//                pswFLD.setEchoChar('\u2022'); //Dot Echo Char
                setButtonIcon(showHideBTN1, hidePswIconPath);

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        showHideBTN2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                confirmPswFLD.setEchoChar('\u0000');  //Password Visibile
                setButtonIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                confirmPswFLD.setEchoChar(echochar);
//                confirmPswFLD.setEchoChar('\u2022'); //Dot Echo Char
                setButtonIcon(showHideBTN2, hidePswIconPath);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });


        deleteBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Save BTN pressed");
            int choice = JOptionPane.showConfirmDialog(getContentPane(), "Sei sicuro di voler eliminare il tuo account?");
            if (choice == 0) {

                try {
                    session.deleteAccount(String.valueOf(pswFLD.getPassword()), String.valueOf(confirmPswFLD.getPassword()));
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Account Eliminato");
//                    new LoginForm();
                    dispose();

                } catch (IllegalArgumentException | CredentialException ex) {
                    pswFLD.setText("");
                    confirmPswFLD.setText("");
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

                } catch (TimeoutException ex) {
                    sessionExpired();

                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);

                } catch (NoSuchAlgorithmException ex) {
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
                }
            }
        });

//        backBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Back BTN pressed");
//            try {
//                homeAction(null);
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }
//        });

        logoutBTN.addActionListener(this::actionLogOut);
        backBTN.addActionListener(this::homeAction);


    }
//
}
