/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public class PrelievoForm extends MainApp {


    private JPanel prelievoPanel;
    private JTextField amountFLD;
    private JButton prelievoBTN;
    private JButton balanceBTN;
    private JButton homeBTN;
    private JButton logoutBTN;
    private JPanel credentialPanel;
    private JLabel amountLBL;
    private JPanel buttonsPanel;
    private JLabel icon;
    private JPanel creditsPanel;
    private JLabel balanceLBL;
    private JLabel balance;


    public PrelievoForm() throws TimeoutException, SQLException {
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(prelievoPanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("Prelievo - JavaBank");

        setFrameIcon(moneyIconPath);
        setLabelIcon(icon, moneyIconPath);
        setResizable(false);

        getRootPane().setDefaultButton(prelievoBTN);
        balance.setText(euro.format(session.getSaldo()));

        if (balance.isVisible()) {
            setButtonIcon(balanceBTN,showPswIconPath);
        } else {
            setButtonIcon(balanceBTN,hidePswIconPath);
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
                balance.setText(euro.format(session.getSaldo()));

                if (balance.isVisible()) {
                    setButtonIcon(balanceBTN,hidePswIconPath);
                    balance.setVisible(false);

                } else {
                    setButtonIcon(balanceBTN,showPswIconPath);
                    balance.setVisible(true);

                }

            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }


        });

        homeBTN.addActionListener(this::homeAction);

        logoutBTN.addActionListener(this::logOutAction);

        prelievoBTN.addActionListener(this::prelievo);

        setVisible(true);
    }

    private void prelievo(ActionEvent e) {
        try {
            session.updateSessionCreation();
            if (amountFLD.getText().isEmpty())
                JOptionPane.showMessageDialog(getContentPane(), "Amount field can't be empty");
            else {
                session.prelievo(session.validateAmount(amountFLD.getText()));
                amountFLD.setText("");
                balance.setText(euro.format(session.getSaldo()));
                JOptionPane.showMessageDialog(getContentPane(), "Prelievo effettuato Correttamente");
            }


        } catch (IllegalArgumentException ex) {
            amountFLD.setText("");
            JOptionPane.showMessageDialog(getContentPane(), ex.getMessage());

        } catch (TimeoutException ex) {
            sessionExpired();
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }
}
