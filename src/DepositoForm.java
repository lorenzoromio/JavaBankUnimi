/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class DepositoForm extends WebApp {


    private JPanel depositoPanel;
    private JPanel operationPanel;
    private JPanel fieldsPanel;
    private JLabel amountLBL;
    private JTextField amountFLD;
    private JPanel buttonsDownPanel;
    private JButton depositBTN;
    private JButton balanceBTN;
    private JButton contactsBTN;
    private JPanel buttonsLatoPanel;
    private JButton homeBTN;
    private JButton logoutBTN;
    private JPanel balancePanel;
    private JLabel balanceLBL;
    private JLabel balance;


    public DepositoForm() throws TimeoutException, SQLException {
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(depositoPanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("Deposito - JavaBank");
        setFrameIcon(moneyIconPath);
        setResizable(false);

        getRootPane().setDefaultButton(depositBTN);
        balance.setText(euro.format(session.getSaldo()));

        if (balance.isVisible()) {
            balanceBTN.setText("Nascondi Saldo");
        } else {
            balanceBTN.setText("Mostra Saldo");
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                balance.setText(euro.format(session.getSaldo()));

                if (balance.isVisible()) {

                    balanceBTN.setText("Mostra Saldo");
                    balanceLBL.setVisible(false);
                    balance.setVisible(false);

                } else {

                    balanceBTN.setText("Nascondi Saldo");
                    balanceLBL.setVisible(true);
                    balance.setVisible(true);

                }

            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }


        });

        homeBTN.addActionListener(this::homeAction);

        logoutBTN.addActionListener(this::actionLogOut);

        depositBTN.addActionListener((ActionEvent e) -> {
            try {
//                Double amount = 0.0;
                session.updateSessionCreation();
                if (amountFLD.getText().isEmpty())
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");
                else {
//                    amount = ;
                    session.deposit(session.validateAmount(amountFLD.getText()));
//                    session.deposit(session.validateAmount(amountFLD.getText()));
                    amountFLD.setText("");
                    balance.setText(euro.format(session.getSaldo()));
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Deposito Effettuato Correttamente");
                }


            } catch (IllegalArgumentException ex) {
                amountFLD.setText("");
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        setVisible(true);
    }
}
