/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.CredentialException;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class DeleteAccountForm extends MainApp {
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

    public DeleteAccountForm() throws TimeoutException {
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
                int lenghtPsw1 = pswFLD.getPassword().length;
                int lenghtPsw2 = confirmPswFLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(pswFLD.getPassword()).substring(0, lenghtPsw2);

                if (subPsw1.equals(String.valueOf(confirmPswFLD.getPassword())) || lenghtPsw2 > lenghtPsw1) {
                    confirmPswFLD.setForeground(pswFLD.getForeground());
                    confirmPswFLD.setBorder(pswFLD.getBorder());
                } else {
                    confirmPswFLD.setForeground(Color.red);
                    confirmPswFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
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


        deleteBTN.addActionListener(this::deleteAccount);
        logoutBTN.addActionListener(this::logOutAction);
        backBTN.addActionListener(this::homeAction);


    }

    private void deleteAccount(ActionEvent e) {
        System.out.println("Save BTN pressed");
        int choice = JOptionPane.showConfirmDialog(getContentPane(), "Sei sicuro di voler eliminare il tuo account?");
        if (choice == 0) {

            try {
                session.deleteAccount(String.valueOf(pswFLD.getPassword()), String.valueOf(confirmPswFLD.getPassword()));
                JOptionPane.showMessageDialog(getContentPane(), "Account Eliminato");
//                    new LoginForm();
                dispose();

            } catch (IllegalArgumentException | CredentialException ex) {
                pswFLD.setText("");
                confirmPswFLD.setText("");
                JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

            } catch (TimeoutException ex) {
                sessionExpired();

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);

            } catch (NoSuchAlgorithmException ex) {
                JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
            }
        }
    }
//
}
