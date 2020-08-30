/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.CredentialException;
import javax.swing.*;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class ChangePswForm extends MainApp {
    private JPanel credentialPanel;
    private JLabel pswLBL;
    private JLabel newPswLBL;
    private JPasswordField pswFLD;
    private JPasswordField checkNewPswFLD;
    private JButton showHideBTN1;
    private JButton showHideBTN2;
    private JLabel checkpswLBL;
    private JPanel buttonsPanel;
    private JButton saveBTN;
    private JButton backBTN;
    private JButton logoutBTN;
    private JLabel icon;
    private JPanel creditsPanel;
    private JLabel chechNewPswLBL;
    private JButton button1;
    private JPanel changePswPanel;
    private JPasswordField newPswFLD;


    public ChangePswForm() throws TimeoutException {
        session.updateSessionCreation();
        setContentPane(changePswPanel);
        pack();
        setLocationRelativeTo(null);
        setTitle("Change Password - JavaBank");

        setFrameIcon(changePswIconPath);
        setLabelIcon(icon, changePswIconPath);
        setResizable(false);
        setVisible(true);
        getRootPane().setDefaultButton(saveBTN);

        setButtonIcon(showHideBTN1, hidePswIconPath);
        setButtonIcon(showHideBTN2, hidePswIconPath);

        pswFLD.setEchoChar(echochar);
        newPswFLD.setEchoChar(echochar);
        checkNewPswFLD.setEchoChar(echochar);

        //todo restyle

        newPswFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!String.valueOf(newPswFLD.getPassword()).isEmpty())
                    try {
                        Session.checkValidPassword(String.valueOf(newPswFLD.getPassword()));
                        checkpswLBL.setVisible(false);
                    } catch (IllegalArgumentException ex) {
                        checkpswLBL.setVisible(true);
                    }
            }

        });

        showHideBTN1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                newPswFLD.setEchoChar('\u0000');  //Password Visibile
                setButtonIcon(showHideBTN1, showPswIconPath);

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                newPswFLD.setEchoChar(echochar);
//              newPswFLD.setEchoChar('\u2022'); //Dot Echo Char
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
                checkNewPswFLD.setEchoChar('\u0000');  //Password Visibile
                setButtonIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                checkNewPswFLD.setEchoChar(echochar);
//                checkNewPswFLD.setEchoChar('\u2022'); //Dot Echo Char

                setButtonIcon(showHideBTN2, hidePswIconPath);


            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        saveBTN.addActionListener(this::changePassword);

        logoutBTN.addActionListener(this::logOutAction);

        backBTN.addActionListener(this::homeAction);

    }

    private void changePassword(ActionEvent e) {
        System.out.println("Save BTN pressed");
        try {
            session.changePassword(String.valueOf(pswFLD.getPassword()), String.valueOf(newPswFLD.getPassword()), String.valueOf(checkNewPswFLD.getPassword()));
            JOptionPane.showMessageDialog(getContentPane(), "Modifica effettuata correttamente");
            homeAction(null);

        } catch (IllegalArgumentException ex) {
            newPswFLD.setText("");
            checkNewPswFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (NoSuchAlgorithmException ex) {
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (CredentialException ex) {
            pswFLD.setText("");
            newPswFLD.setText("");
            checkNewPswFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (TimeoutException ex) {
            sessionExpired();

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);

        }
    }
}
