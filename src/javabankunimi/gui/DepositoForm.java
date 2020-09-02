/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.RegexChecker;

import javax.swing.*;
import java.awt.event.ActionEvent;
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


    public DepositoForm() throws SQLException {
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(depositoPanel);

        pack();
        setLocation(location);
        setTitle("Deposito - JavaBank");
        setFrameIcon(moneyIconPath);
        setCustomIcon(icon, moneyIconPath);
        setResizable(false);

        getRootPane().setDefaultButton(depositBTN);
        balance.setText(euro.format(session.getSaldo()));

        displayClock(clockLBL, dateLBL);
        if (balance.isVisible()) {
            setCustomIcon(balanceBTN, showPswIconPath);
        } else {
            setCustomIcon(balanceBTN, hidePswIconPath);
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                balance.setText(euro.format(session.getSaldo()));

                if (balance.isVisible()) {
                    setCustomIcon(balanceBTN, hidePswIconPath);
                    balance.setVisible(false);

                } else {
                    setCustomIcon(balanceBTN, showPswIconPath);
                    balance.setVisible(true);

                }

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }


        });

        homeBTN.addActionListener(this::homeAction);

        logoutBTN.addActionListener(this::logOutAction);

        depositBTN.addActionListener(this::deposito);

        setVisible(true);
    }

    private void deposito(ActionEvent e) {
        try {
//                Double amount = 0.0;
            session.updateSessionCreation();
            if (amountFLD.getText().isEmpty())
                JOptionPane.showMessageDialog(getContentPane(), "Amount field can't be empty");
            else {

                session.deposit(RegexChecker.checkValidAmount(amountFLD.getText()));
                amountFLD.setText("");
                balance.setText(euro.format(session.getSaldo()));
                JOptionPane.showMessageDialog(getContentPane(), "Deposito Effettuato Correttamente");
            }


        } catch (IllegalArgumentException ex) {
            amountFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }
}