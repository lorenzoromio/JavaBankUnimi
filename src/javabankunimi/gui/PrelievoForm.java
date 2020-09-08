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

        session.updateCreation();
        System.out.println(session);
        setContentPane(prelievoPanel);
        pack();
        setLocation(location);
        setTitle("Prelievo - JavaBank");

        setFrameIcon(Icons.MONEY);
        displayClock(clockLBL, dateLBL);
        setSessionTimer(timerLBL);
        setCustomIcon(icon, Icons.MONEY);
        setResizable(false);

        setHandCursor(balanceBTN, homeBTN, logoutBTN, prelievoBTN);

        getRootPane().setDefaultButton(prelievoBTN);
        balance.setText(euro.format(session.getSaldo()));

        if (balance.isVisible()) {
            setCustomIcon(balanceBTN, Icons.SHOWPSW);
        } else {
            setCustomIcon(balanceBTN, Icons.HIDEPSW);
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateCreation();
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
            session.updateCreation();
            if (amountFLD.getText().isEmpty())
                JOptionPane.showMessageDialog(getContentPane(), "Amount field can't be empty");
            else {
                session.prelievo(RegexChecker.checkValidAmount(amountFLD.getText()));
                playSound(Sounds.CASH);
                amountFLD.setText("");
                balance.setText(euro.format(session.getSaldo()));
                JOptionPane.showMessageDialog(getContentPane(), "Prelievo effettuato Correttamente");
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
