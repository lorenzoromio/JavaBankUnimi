/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountException;
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


    public SignUpForm() {

        System.out.println(session);
        setContentPane(signupPanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("SignUP - JavaBank");

        setFrameIcon(signUpIconPath);
        setLabelIcon(icon, signUpIconPath);
        setResizable(false);
        setVisible(true);
        getRootPane().setDefaultButton(signupBTN);

//        checkpswLBL.setVisible(false);
//        checkpswLBL.setForeground(Color.red);
//        matchPswLBL.setVisible(false);
//        matchPswLBL.setForeground(Color.red);
//
//        checkpswLBL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideBTN1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showHideBTN2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        exitBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBTN.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        psw1FLD.setEchoChar(echochar);
        psw2FLD.setEchoChar(echochar);

        SwingUtilities.invokeLater(nomeFLD::requestFocus);

        setButtonIcon(showHideBTN1, hidePswIconPath);
        setButtonIcon(showHideBTN2, hidePswIconPath);


        JOptionPane pswInvalid = new JOptionPane();

//        psw1FLD.addMouseListener(new MouseListener() {
//
////            final Font font = checkpswLBL.getFont();
////            final Map attributes = font.getAttributes();
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                JOptionPane.showMessageDialog(getContentPane(), pswInvalid.getMessage());
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
////                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON); //COSTANTE UNDERLINE_ON =_0
////                checkpswLBL.setFont(font.deriveFont(attributes));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
////                attributes.put(TextAttribute.UNDERLINE, -1); //-1
////                checkpswLBL.setFont(font.deriveFont(attributes));
//            }
//        });

        nomeFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (nomeFLD.getText().isEmpty()) {
                    nomeFLD.setForeground(new JPasswordField().getForeground());
                    nomeFLD.setBorder(new JPasswordField().getBorder());
                } else try {
                    Account.checkNome(nomeFLD.getText());
                    nomeFLD.setForeground(new JPasswordField().getForeground());
                    nomeFLD.setBorder(new JPasswordField().getBorder());
                } catch (InvalidNameException ex) {
                    nomeFLD.setForeground(Color.red);
                    nomeFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.red));
                }
            }
        });

        cognomeFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (cognomeFLD.getText().isEmpty()) {
                    cognomeFLD.setForeground(new JPasswordField().getForeground());
                    cognomeFLD.setBorder(new JPasswordField().getBorder());
                } else try {
                    Account.checkNome(cognomeFLD.getText());
                    cognomeFLD.setForeground(new JPasswordField().getForeground());
                    cognomeFLD.setBorder(new JPasswordField().getBorder());
                } catch (InvalidNameException ex) {
                    cognomeFLD.setForeground(Color.red);
                    cognomeFLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.red));
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
                    psw1FLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.red));
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
                    psw2FLD.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED,Color.red,Color.red));
                }
            }
        });

        showHideBTN1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                psw1FLD.setEchoChar('\u0000');  //Password Visibile
                setButtonIcon(showHideBTN1, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw1FLD.setEchoChar(echochar);
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
                psw2FLD.setEchoChar('\u0000');  //Password Visibile
                setButtonIcon(showHideBTN2, showPswIconPath);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw2FLD.setEchoChar(echochar);
                setButtonIcon(showHideBTN2, hidePswIconPath);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        signupBTN.addActionListener((ActionEvent e) -> {
            if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || String.valueOf(psw1FLD.getPassword()).isEmpty() || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
                JOptionPane.showMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
            } else if (String.valueOf(psw1FLD.getPassword()).equals(String.valueOf(psw2FLD.getPassword()))) {
                int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?");
                if (choice == 0) {

                    try {
                        Bank.addAccount(new Account(nomeFLD.getText(), cognomeFLD.getText(), String.valueOf(psw1FLD.getPassword())));
                        new LoginForm();
                        dispose();

                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                        psw1FLD.setText("");
                        psw2FLD.setText("");
                        psw1FLD.setForeground(new JPasswordField().getForeground());
                        psw2FLD.setForeground(new JPasswordField().getForeground());

                    } catch (InvalidNameException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                        nomeFLD.setText("");
                        cognomeFLD.setText("");
                        nomeFLD.setForeground(new JLabel().getForeground());
                        cognomeFLD.setForeground(new JLabel().getForeground());

                    } catch (AccountException ex) {
                        JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());
                        nomeFLD.setText("");
                        cognomeFLD.setText("");
                        psw1FLD.setText("");
                        psw2FLD.setText("");
                        nomeFLD.setForeground(new JLabel().getForeground());
                        cognomeFLD.setForeground(new JLabel().getForeground());
                        psw1FLD.setForeground(new JPasswordField().getForeground());
                        psw2FLD.setForeground(new JPasswordField().getForeground());

                    } catch (SQLException ex) {
                        SQLExceptionOccurred(ex);
                    }
                }

            } else {
                psw1FLD.setText("");
                psw2FLD.setText("");
                psw1FLD.setForeground(new JPasswordField().getForeground());
                psw2FLD.setForeground(new JPasswordField().getForeground());
                JOptionPane.showMessageDialog(getContentPane(), "Password non corrette");
            }
        });

        backBTN.addActionListener((ActionEvent e) -> {
            new LoginForm();
            dispose();
        });

        exitBTN.addActionListener(this::exitAction);

    }
}
