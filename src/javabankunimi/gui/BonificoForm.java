/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Account;
import javabankunimi.regex.RegexChecker;

import javax.naming.InvalidNameException;
import javax.security.auth.login.AccountNotFoundException;
import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;

public class BonificoForm extends MainApp {
    private final Rubrica rubrica = new Rubrica();
    private JPanel bonificoPanel;
    private JTextField ibanFLD;
    private JTextField amountFLD;
    private JButton transferBTN;
    private JButton contactsBTN;
    private JButton balanceBTN;
    private JButton logoutBTN;
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
    private JPasswordField balance;
    private JLabel results;
    private JLabel dateLBL;
    private JLabel clockLBL;
    private JLabel timerLBL;

    public BonificoForm() throws SQLException {
        session.updateCreation();
        System.out.println(session);
        setContentPane(bonificoPanel);

        pack();
        setLocation(location);
        setTitle("Bonifico - JavaBank");

        setFrameIcon(Icons.MONEY);
        setResizable(false);

        getRootPane().setDefaultButton(transferBTN);

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        displayClock(clockLBL, dateLBL);
        setSessionTimer(timerLBL);
        setCustomIcon(nextBTN, Icons.NEXT);
        setCustomIcon(prevBTN, Icons.PREV);
        setHandCursor(balanceBTN,clearBTN,contactsBTN,homeBTN,logoutBTN,nextBTN,prevBTN,searchBTN,transferBTN);

        setVisible(true);

        rubricaPanel.setVisible(false);

        Runnable search = new Runnable() {
            @Override
            public synchronized void run() {
                printContacts(searchFLD.getText());
            }
        };

        ibanFLD.setText("");
        balance.setText(euro.format(session.getSaldo()));
        balance.setBorder(BorderFactory.createEmptyBorder());

        if (showInfo) {
            balance.setEchoChar(Echochar.SHOW);
            balanceBTN.setText("Nascondi Saldo");
        } else {
            balance.setEchoChar(Echochar.HIDE);
            balanceBTN.setText("Mostra Saldo");
        }

        contactsBTN.addActionListener(e -> {

            if (rubricaPanel.isVisible()) {
                rubricaPanel.setVisible(false);
                ibanFLD.setText("");
            } else {
                backgroundTask(search);
                rubricaPanel.setVisible(true);
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
                backgroundTask(search);
                if (searchFLD.getText().isEmpty()) {
                    setFieldOnCorrect(searchFLD);
                } else try {
                    RegexChecker.validateName(searchFLD.getText());
                    setFieldOnCorrect(searchFLD);
                } catch (InvalidNameException ex) {
                    setFieldOnError(searchFLD);
                }
            }
        });

        prevBTN.addActionListener(e -> {
            rubrica.index--;
            if (rubrica.index < 0)
                rubrica.index = rubrica.list.size() - 1;
            populateRubrica();
        });

        nextBTN.addActionListener(e -> {
            rubrica.index++;
            if (rubrica.index > rubrica.list.size() - 1)
                rubrica.index = 0;
            populateRubrica();
        });

        searchBTN.addActionListener(e -> {
//                    printContacts(searchFLD.getText())
                    backgroundTask(search);
                }
        );

        clearBTN.addActionListener(e -> {
            searchFLD.setText("");
            backgroundTask(search);
//            printContacts(searchFLD.getText());
        });

        balanceBTN.addActionListener(e -> {

            try {
                session.updateCreation();
                balance.setText(euro.format(session.getSaldo()));
                showInfo = !showInfo;


                if (showInfo) {
                    balanceBTN.setText("Nascondi Saldo");
                    balance.setEchoChar(Echochar.SHOW);
//                    balanceLBL.setVisible(false);
//                    balance.setVisible(false);

                } else {
                    balanceBTN.setText("Mostra Saldo");
                    balance.setEchoChar(Echochar.HIDE);
//                    balanceLBL.setVisible(true);
//                    balance.setVisible(true);
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
                    RegexChecker.validateAmount(amountFLD.getText());
                    setFieldOnCorrect(amountFLD);
                } catch (IllegalArgumentException ex) {
                    setFieldOnError(amountFLD);
                }
            }
        });

        transferBTN.addActionListener(e -> defaultAction());

        homeBTN.addActionListener(e1 -> displayHomeForm());

        logoutBTN.addActionListener(e -> logOutAction());

    }


    private void populateRubrica() {
        results.setText(rubrica.index + 1 + " of " + rubrica.list.size());
        nomeFLD.setText(rubrica.list.get(rubrica.index).getNome());
        cognomeFLD.setText(rubrica.list.get(rubrica.index).getCognome());
        ibanContactFLD.setText(rubrica.list.get(rubrica.index).getIban());
        ibanFLD.setText(ibanContactFLD.getText());
    }

    private void printContacts(String search) {

        try {
            rubrica.list = session.showContacts(search);
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }

        rubrica.index = 0;
        if (rubrica.list.isEmpty()) {
            results.setText("No Result");
            nomeFLD.setText("");
            cognomeFLD.setText("");
            ibanContactFLD.setText("");
            ibanFLD.setText("");
        } else {
            populateRubrica();
        }
    }

    @Override
    protected void defaultAction() {
        try {
            session.updateCreation();
            if (amountFLD.getText().isEmpty()) {
                playSound(Sounds.ERROR);
                JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");
            } else {
                if (ibanFLD.getText().isEmpty()) {
                    playSound(Sounds.ERROR);
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Iban field can't be empty");
                } else {
                    session.transfer(ibanFLD.getText().toUpperCase(), RegexChecker.validateAmount(amountFLD.getText()));
                    playSound(Sounds.CASH);
                    amountFLD.setText("");
                    balance.setText(euro.format(session.getSaldo()));
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Bonifico Effettuato Correttamente");
                }
            }

        } catch (NumberFormatException ex) {
            playSound(Sounds.ERROR);
            amountFLD.setText("");
            JOptionPane.showInternalMessageDialog(getContentPane(), "Amount can be only numeric");

        } catch (IllegalArgumentException ex) {
            playSound(Sounds.ERROR);
            amountFLD.setText("");
            JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

        } catch (AccountNotFoundException ex) {
            playSound(Sounds.ERROR);
            ibanFLD.setText("");
            JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

        } catch (SQLException ex) {
            playSound(Sounds.ERROR);
            SQLExceptionOccurred(ex);
        }
    }

    private static class Rubrica {
        public List<Account> list;
        public int index;

    }

}
