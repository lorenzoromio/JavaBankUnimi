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

public class PrelievoForm extends MainApp {

    private JPanel prelievoPanel;
    private JTextField amountFLD;
    private JButton prelievoBTN;
    private JButton balanceBTN;
    private JButton homeBTN;
    private JButton logoutBTN;
    private JLabel icon;
    private JLabel balance;
    private JLabel dateLBL;
    private JLabel clockLBL;
    private JLabel timerLBL;

    public PrelievoForm() throws SQLException {

        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(prelievoPanel);
        pack();
        setLocation(location);
        setTitle("Prelievo - JavaBank");

        setFrameIcon(moneyIconPath);
        displayClock(clockLBL, dateLBL);
        setTimer(timerLBL);
        setCustomIcon(icon, moneyIconPath);
        setResizable(false);

        setHandCursor(balanceBTN, homeBTN, logoutBTN, prelievoBTN);

        getRootPane().setDefaultButton(prelievoBTN);
        balance.setText(euro.format(session.getSaldo()));

        if (balance.isVisible()) {
            setCustomIcon(balanceBTN, showPswIconPath);
        } else {
            setCustomIcon(balanceBTN, hidePswIconPath);
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
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

        homeBTN.addActionListener(e1 -> displayHomeForm());

        logoutBTN.addActionListener(e -> logOutAction());

        prelievoBTN.addActionListener(e -> defaultAction());

        setVisible(true);
    }

    @Override
    protected void defaultAction() {
        try {
            session.updateSessionCreation();
            if (amountFLD.getText().isEmpty())
                JOptionPane.showMessageDialog(getContentPane(), "Amount field can't be empty");
            else {
                session.prelievo(RegexChecker.checkValidAmount(amountFLD.getText()));
                playSound(cashSound);
                amountFLD.setText("");
                balance.setText(euro.format(session.getSaldo()));
                JOptionPane.showMessageDialog(getContentPane(), "Prelievo effettuato Correttamente");
            }


        } catch (IllegalArgumentException ex) {
            amountFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }
}
