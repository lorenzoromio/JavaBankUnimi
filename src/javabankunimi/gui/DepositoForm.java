/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.regex.RegexChecker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;

public class DepositoForm extends MainApp {


    private JPanel depositoPanel;
    private JLabel icon;
    private JTextField amountFLD;
    private JPasswordField balance;
    private JButton depositBTN;
    private JButton balanceBTN;
    private JButton homeBTN;
    private JButton logoutBTN;
    private JLabel dateLBL;
    private JLabel clockLBL;
    private JLabel timerLBL;


    public DepositoForm() throws SQLException {
        session.updateCreation();
        System.out.println(session);
        setContentPane(depositoPanel);

        pack();
        setLocation(location);
        setTitle("Deposito - JavaBank");
        setFrameIcon(Icons.MONEY);
        setCustomIcon(icon, Icons.MONEY);
        setResizable(false);

        getRootPane().setDefaultButton(depositBTN);
        balance.setText(euro.format(session.getSaldo()));

        displayClock(clockLBL, dateLBL);
        setSessionTimer(timerLBL);

        if (showInfo) {
            setCustomIcon(balanceBTN, Icons.SHOWPSW);
            balance.setEchoChar(Echochar.SHOW);
        } else {
            setCustomIcon(balanceBTN, Icons.HIDEPSW);
            balance.setEchoChar(Echochar.HIDE);

        }

        setHandCursor(balanceBTN, depositBTN, homeBTN, logoutBTN);
        balance.setBorder(BorderFactory.createEmptyBorder());

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateCreation();
                balance.setText(euro.format(session.getSaldo()));
                showInfo = !showInfo;
                if (showInfo) {
                    setCustomIcon(balanceBTN, Icons.SHOWPSW);
                    balance.setEchoChar(Echochar.SHOW);

                } else {
                    setCustomIcon(balanceBTN, Icons.HIDEPSW);
                    balance.setEchoChar(Echochar.HIDE);

                }

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }


        });

        homeBTN.addActionListener(e1 -> displayHomeForm());

        logoutBTN.addActionListener(e -> logOutAction());

        depositBTN.addActionListener(e -> defaultAction());

        amountFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (amountFLD.getText().isEmpty()) {
                    setFieldOnCorrect(amountFLD);
                } else try {
                    RegexChecker.validateAmount(amountFLD.getText());
                    setFieldOnCorrect(amountFLD);
                } catch (IllegalArgumentException ex) {
                    setFieldOnError(amountFLD);
                }
            }
        });

        setVisible(true);
    }

    @Override
    protected void defaultAction() {
        try {
            session.updateCreation();
            if (amountFLD.getText().isEmpty())
                JOptionPane.showMessageDialog(getContentPane(), "Amount field can't be empty");
            else {

                Double amount = RegexChecker.validateAmount(amountFLD.getText());
                session.deposit(amount);
                playSound(Sounds.CASH);
                amountFLD.setText("");
                balance.setText(euro.format(session.getSaldo()));
                JOptionPane.showMessageDialog(getContentPane(), "Deposito Effettuato Correttamente");
            }


        } catch (IllegalArgumentException ex) {
            playSound(Sounds.ERROR);
            amountFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }
}