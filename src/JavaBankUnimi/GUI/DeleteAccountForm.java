/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package JavaBankUnimi.GUI;
import javax.security.auth.login.CredentialException;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

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

    public DeleteAccountForm() throws TimeoutException {
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
        setCustomIcon(showHideBTN1, hidePswIconPath);
        setCustomIcon(showHideBTN2, hidePswIconPath);
        pswFLD.setEchoChar(echochar);
        confirmPswFLD.setEchoChar(echochar);

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

        confirmPswFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {

                int lenghtPsw1 = pswFLD.getPassword().length;
                int lenghtPsw2 = confirmPswFLD.getPassword().length;

                if (lenghtPsw2 > lenghtPsw1) lenghtPsw2 = lenghtPsw1;

                String subPsw1 = String.valueOf(pswFLD.getPassword()).substring(0, lenghtPsw2);

                if (String.valueOf(confirmPswFLD.getPassword()).isEmpty() || (lenghtPsw2 > lenghtPsw1) || subPsw1.equals(String.valueOf(confirmPswFLD.getPassword()))) {
                    confirmPswFLD.setForeground(new JPasswordField().getForeground());
                    confirmPswFLD.setBorder(new JPasswordField().getBorder());
                } else {
                    confirmPswFLD.setForeground(Color.red);
                    confirmPswFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
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


        deleteBTN.addActionListener(this::deleteAccount);
        logoutBTN.addActionListener(this::logOutAction);
        backBTN.addActionListener(this::homeAction);

        pswFLD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                System.out.println("Psw1: " + session.hash(String.valueOf(pswFLD.getPassword())));
                try {
                    System.out.println("Psw2: " + session.getHashPsw());
                    System.out.println();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

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
    }

    private void deleteAccount(ActionEvent e) {
        System.out.println("Save BTN pressed");
        int choice = JOptionPane.showConfirmDialog(getContentPane(), "Sei sicuro di voler eliminare il tuo account?");
        if (choice == 0) {

            try {
                session.deleteAccount(String.valueOf(pswFLD.getPassword()), String.valueOf(confirmPswFLD.getPassword()));
                JOptionPane.showMessageDialog(getContentPane(), "Account Eliminato");
                new LoginForm();
                dispose();

            } catch (IllegalArgumentException | CredentialException ex) {
                pswFLD.setText("");
                confirmPswFLD.setText("");
                JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

            } catch (TimeoutException ex) {
                sessionExpired();

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);

            } finally {

                pswFLD.setForeground(new JPasswordField().getForeground());
                pswFLD.setBorder(new JPasswordField().getBorder());
                confirmPswFLD.setForeground(new JPasswordField().getForeground());
                confirmPswFLD.setBorder(new JPasswordField().getBorder());

            }
        }
    }
}