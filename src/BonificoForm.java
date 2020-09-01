/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved. test
 */

import javax.security.auth.login.AccountNotFoundException;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class BonificoForm extends MainApp {
    private JPanel bonificoPanel;
    private JTextField ibanFLD;
    private JTextField amountFLD;
    private JPanel operationPanel;
    private JButton transferBTN;
    private JButton contactsBTN;
    private JButton balanceBTN;
    private JButton logoutBTN;
    private JPanel fieldsPanel;
    private JButton homeBTN;
    private JPanel rubricaPanel;
    private JTextField nomeFLD;
    private JTextField cognomeFLD;
    private JTextField ibanContactFLD;
    private JButton prevBTN;
    private JButton nextBTN;
    private JTextField searchFLD;
    private JButton searchBTN;
    private JButton clearBTN;
    private JLabel searchLBL;
    private JLabel balance;
    private JLabel balanceLBL;
    private JLabel amountLBL;
    private JLabel ibanLBL;
    private JLabel ibanContactLBL;
    private JLabel cognomeLBL;
    private JLabel nomeLBL;
    private JLabel results;
    private JTabbedPane rubricaTab;
    private JPanel buttonsLatoPanel;
    private JPanel balancePanel;
    private JPanel buttonsDownPanel;
    private JPanel creditsPanel;
    private final Contacts contacts = new Contacts();

    public BonificoForm() throws TimeoutException, SQLException {
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(bonificoPanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("Bonifico - JavaBank");

        setFrameIcon(moneyIconPath);
        setResizable(false);

        getRootPane().setDefaultButton(transferBTN);

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        setCustomIcon(nextBTN, nextIconPath);
        setCustomIcon(prevBTN, prevIconPath);

        rubricaPanel.setVisible(false);
        printContacts(searchFLD.getText());
        ibanFLD.setText("");
        balance.setText(euro.format(session.getSaldo()));

        contactsBTN.addActionListener(e -> {

            if (rubricaPanel.isVisible()) {
                rubricaPanel.setVisible(false);
                ibanFLD.setText("");
            } else {
                rubricaPanel.setVisible(true);
                ibanFLD.setText(ibanContactFLD.getText());
            }
        });

        searchFLD.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                getRootPane().setDefaultButton(searchBTN);
            }

            @Override
            public void focusLost(FocusEvent e) {
                getRootPane().setDefaultButton(transferBTN);
            }
        });

        searchFLD.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                printContacts(searchFLD.getText());
            }
        });

        prevBTN.addActionListener(e -> {
            if (contacts.index > 0)
                contacts.index--;
            populateRubrica();
        });

        nextBTN.addActionListener(e -> {
            if (contacts.index < contacts.list.size() - 1)
                contacts.index++;
            populateRubrica();
        });

        searchBTN.addActionListener(e -> printContacts(searchFLD.getText()));

        clearBTN.addActionListener(e -> {
            searchFLD.setText("");
            printContacts(searchFLD.getText());
        });

        balanceBTN.addActionListener(e -> {

            try {
                session.updateSessionCreation();
                balance.setText(euro.format(session.getSaldo()));

                if (balance.isVisible()) {
                    balanceBTN.setText("Mostra Saldo");
                    System.out.println(balanceBTN.getWidth());
                    balanceLBL.setVisible(false);
                    balance.setVisible(false);

                } else {
                    balanceBTN.setText("Nascondi Saldo");
                    System.out.println(balanceBTN.getWidth());
                    balanceLBL.setVisible(true);
                    balance.setVisible(true);
                }

            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        transferBTN.addActionListener(this::bonifico);

        homeBTN.addActionListener(this::homeAction);

        logoutBTN.addActionListener(this::logOutAction);
        setVisible(true);

    }

    private void populateRubrica() {
        results.setText(contacts.index + 1 + " of " + contacts.list.size());
        nomeFLD.setText(contacts.list.get(contacts.index).getNome());
        cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
        ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
        ibanFLD.setText(ibanContactFLD.getText());
    }

    private void printContacts(String search) {

        try {
            contacts.list = session.showContacts(search);
        } catch (TimeoutException ex) {
            sessionExpired();

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }

        contacts.index = 0;
        if (contacts.list.isEmpty()) {
            results.setText("No Result");
            nomeFLD.setText("");
            cognomeFLD.setText("");
            ibanContactFLD.setText("");
            ibanFLD.setText("");
        } else {
            populateRubrica();
        }
    }

    private void bonifico(ActionEvent e) {
        try {
            Double amount;
            session.updateSessionCreation();
            if (amountFLD.getText().isEmpty())
                JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");

            else {
                amount = session.validateAmount(amountFLD.getText());
                if (ibanFLD.getText().isEmpty()) {
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Iban field can't be empty");
                } else {
                    session.transfer(ibanFLD.getText().toUpperCase(), amount);
                    amountFLD.setText("");
                    balance.setText(euro.format(session.getSaldo()));
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Bonifico Effettuato Correttamente");
                }
            }

        } catch (NumberFormatException ex) {
            amountFLD.setText("");
            JOptionPane.showInternalMessageDialog(getContentPane(), "Amount can be only numeric");

        } catch (IllegalArgumentException ex) {
            amountFLD.setText("");
            JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

        } catch (AccountNotFoundException ex) {
            ibanFLD.setText("");
            JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

        } catch (TimeoutException ex) {
            sessionExpired();
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }

    private static class Contacts {
        public List<Account> list;
        public int index;

    }

}
