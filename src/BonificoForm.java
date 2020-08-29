/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.security.auth.login.AccountNotFoundException;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class BonificoForm extends WebApp {
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
    private Contacts contacts = new Contacts();

    ;

    public BonificoForm() throws TimeoutException, SQLException {
//        super();
        session.updateSessionCreation();
        System.out.println(session);
        setContentPane(bonificoPanel);

        pack();
        setLocationRelativeTo(null);
        setTitle("Bonifico - JavaBank");

        setFrameIcon(moneyIconPath);
        setResizable(false);

        getRootPane().setDefaultButton(transferBTN);
        balance.setText(euro.format(session.getSaldo()));

        if (balance.isVisible()) {
            balanceBTN.setText("Nascondi Saldo");
        } else {
            balanceBTN.setText("Mostra Saldo");
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        rubricaPanel.setVisible(false);

        setButtonIcon(nextBTN, nextIconPath);
        setButtonIcon(prevBTN, prevIconPath);

        printContacts();
        ibanFLD.setText("");


        contactsBTN.addActionListener((ActionEvent e) -> {

            boolean flag = !rubricaPanel.isVisible();
            rubricaPanel.setVisible(flag);
            if (flag) {
//                printContacts();
                ibanFLD.setText(ibanContactFLD.getText());
            } else ibanFLD.setText("");

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

        searchFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        prevBTN.addActionListener((ActionEvent e1) -> {
            if (contacts.index > 0) contacts.index--;
            results.setText(contacts.index + 1 + " of " + contacts.list.size());

            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
            ibanFLD.setText(ibanContactFLD.getText());
        });

        nextBTN.addActionListener((ActionEvent e1) -> {
            if (contacts.index < contacts.list.size() - 1) contacts.index++;
            results.setText(contacts.index + 1 + " of " + contacts.list.size());

            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
            ibanFLD.setText(ibanContactFLD.getText());
        });

        searchBTN.addActionListener((ActionEvent e) -> {          //FUNZIONA

            printContacts();


        });

        clearBTN.addActionListener((ActionEvent e) -> {
            searchFLD.setText("");
//            searchBTN.doClick();
        });

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
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

        transferBTN.addActionListener((ActionEvent e) -> {
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

        });

        homeBTN.addActionListener(this::homeAction);

        logoutBTN.addActionListener(this::actionLogOut);
        setVisible(true);

    }

    private void printContacts() {

        try {
            contacts.list = session.showContacts();
        } catch (TimeoutException ex) {
            sessionExpired();

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }

        String[] words = searchFLD.getText().split(" ");
        for (int i = 0; i < contacts.list.size(); i++) {
            for (String word : words) {
                if (!contacts.list.get(i).getUsername().toLowerCase().contains(word.toLowerCase())) {
                    contacts.list.remove(i);
                    i--;
                    break;
                }
            }
        }

        contacts.index = 0;
        if (contacts.list.isEmpty()) {
            results.setText("No Result");
            nomeFLD.setText("");
            cognomeFLD.setText("");
            ibanContactFLD.setText("");
            ibanFLD.setText("");
        } else {
            results.setText(contacts.index + 1 + " of " + contacts.list.size());
            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
            ibanFLD.setText(ibanContactFLD.getText());
        }
    }

    private class Contacts {
        public List<Account> list;
        public int index;

    }

}
