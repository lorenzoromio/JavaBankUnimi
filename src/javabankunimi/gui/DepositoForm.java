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
    private JLabel balance;
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
        if (balance.isVisible()) {
            setCustomIcon(balanceBTN, Icons.SHOWPSW);
        } else {
            setCustomIcon(balanceBTN, Icons.HIDEPSW);
        }

        setHandCursor(balanceBTN, depositBTN, homeBTN, logoutBTN);

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateCreation();
//                session.accountBalanceUpdate();
                balance.setText(euro.format(session.getSaldo()));

                if (balance.isVisible()) {
                    setCustomIcon(balanceBTN, Icons.HIDEPSW);
                    balance.setVisible(false);

                } else {
                    setCustomIcon(balanceBTN, Icons.SHOWPSW);
                    balance.setVisible(true);

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
                    RegexChecker.checkValidAmount(amountFLD.getText());
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

                Double amount = RegexChecker.checkValidAmount(amountFLD.getText());
                session.deposit(amount);
                if (amount == 420.0)
                    playSound(Sounds.WEED);
                else
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