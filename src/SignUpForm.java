/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class SignUpForm extends MainApp {
    private JPanel signupPanel;
    private JPanel credentialPanel;
    private JTextField nomeFLD;
    private JPanel buttonsPanel;
    private JButton signupBTN;
    private JButton backBTN;
    private JButton exitBTN;
    private JLabel icon;
    private JTextField cognomeFLD;
    private JPasswordField psw1FLD;
    private JPasswordField psw2FLD;
    private JButton showHideBTN1;
    private JButton showHideBTN2;
    private JLabel psw1LBL;
    private JLabel psw2LBL;
    private JPanel creditsPanel;

    public SignUpForm() {

        System.out.println(session);
        setContentPane(signupPanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("SignUP - JavaBank");

        setFrameIcon(signUpIconPath);
        setCustomIcon(icon, signUpIconPath);
        setResizable(false);
        setVisible(true);
        getRootPane().setDefaultButton(signupBTN);

        showHideBTN1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideBTN2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        psw1FLD.setEchoChar(echochar);
        psw2FLD.setEchoChar(echochar);

        SwingUtilities.invokeLater(nomeFLD::requestFocus);

        setCustomIcon(showHideBTN1, hidePswIconPath);
        setCustomIcon(showHideBTN2, hidePswIconPath);

        JOptionPane pswInvalid = new JOptionPane();

        nomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (nomeFLD.getText().isEmpty()) {
                    nomeFLD.setForeground(new JPasswordField().getForeground());
                    nomeFLD.setBorder(new JPasswordField().getBorder());
                } else try {
                    Account.checkValidName(nomeFLD.getText());
                    nomeFLD.setForeground(new JPasswordField().getForeground());
                    nomeFLD.setBorder(new JPasswordField().getBorder());
                } catch (InvalidNameException ex) {
                    nomeFLD.setForeground(Color.red);
                    nomeFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
                }
            }
        });

        cognomeFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (cognomeFLD.getText().isEmpty()) {
                    cognomeFLD.setForeground(new JPasswordField().getForeground());
                    cognomeFLD.setBorder(new JPasswordField().getBorder());
                } else try {
                    Account.checkValidName(cognomeFLD.getText());
                    cognomeFLD.setForeground(new JPasswordField().getForeground());
                    cognomeFLD.setBorder(new JPasswordField().getBorder());
                } catch (InvalidNameException ex) {
                    cognomeFLD.setForeground(Color.red);
                    cognomeFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
                }
            }
        });

        psw1FLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (String.valueOf(psw1FLD.getPassword()).isEmpty()) {
                    psw1FLD.setForeground(new JPasswordField().getForeground());
                    psw1FLD.setBorder(new JPasswordField().getBorder());
                } else try {
                    Account.checkValidPassword(String.valueOf(psw1FLD.getPassword()));
                    psw1FLD.setForeground(new JPasswordField().getForeground());
                    psw1FLD.setBorder(new JPasswordField().getBorder());
                } catch (IllegalArgumentException ex) {
                    pswInvalid.setMessage(ex.getMessage());
                    psw1FLD.setForeground(Color.red);
                    psw1FLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
                }
            }
        });

        psw2FLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int lenghtPsw1 = psw1FLD.getPassword().length;
                int lenghtPsw2 = psw2FLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(psw1FLD.getPassword()).substring(0, lenghtPsw2);

                if (subPsw1.equals(String.valueOf(psw2FLD.getPassword())) || lenghtPsw2 > lenghtPsw1) {
                    psw2FLD.setForeground(psw1FLD.getForeground());
                    psw2FLD.setBorder(psw1FLD.getBorder());
                } else {
                    psw2FLD.setForeground(Color.red);
                    psw2FLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
                }
            }
        });

        showHideBTN1.addMouseListener(new MouseAdapter() {


            @Override
            public void mousePressed(MouseEvent e) {
                psw1FLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN1, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw1FLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN1, hidePswIconPath);
            }

        });

        showHideBTN2.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                psw2FLD.setEchoChar('\u0000');  //Password Visibile
                setCustomIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw2FLD.setEchoChar(echochar);
                setCustomIcon(showHideBTN2, hidePswIconPath);
            }

        });

        signupBTN.addActionListener(this::signUpAction);

        backBTN.addActionListener(this::logInAction);

        exitBTN.addActionListener(this::exitAction);

    }

    private void signUpAction(ActionEvent e) {
        if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || String.valueOf(psw1FLD.getPassword()).isEmpty() || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
        } else if (String.valueOf(psw1FLD.getPassword()).equals(String.valueOf(psw2FLD.getPassword()))) {
            if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?") == 0) {

                try {
                    Account account = new Account(nomeFLD.getText(), cognomeFLD.getText(), String.valueOf(psw1FLD.getPassword()));
                    Bank.addAccount(account);
                    if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Login as " + account.getNome() + " " + account.getCognome() + "?") == 0) {
                        session = Bank.login(account.getUsername(), String.valueOf(psw1FLD.getPassword()));
                        homeAction(null);
                    } else {
                        //logInAction(null);
                        clearNamesFields();
                        clearPasswordFields();
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearPasswordFields();

                } catch (InvalidNameException ex) {
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearNamesFields();

                } catch (AccountException ex) {
                    JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                    clearNamesFields();
                    clearPasswordFields();

                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);

                } catch (CredentialException ex) {
                    //
                }
            }

        } else {
            clearPasswordFields();
            JOptionPane.showMessageDialog(getContentPane(), "Password non corrette");
        }
    }

    private void clearNamesFields() {
        nomeFLD.setText("");
        cognomeFLD.setText("");
        nomeFLD.setForeground(new JLabel().getForeground());
        cognomeFLD.setForeground(new JLabel().getForeground());
    }

    private void clearPasswordFields() {
        psw1FLD.setText("");
        psw2FLD.setText("");
        psw1FLD.setForeground(new JPasswordField().getForeground());
        psw2FLD.setForeground(new JPasswordField().getForeground());
    }

    private void logInAction(ActionEvent e) {
        new LoginForm();
        dispose();
    }
}
