/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Session;
import javabankunimi.bank.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class HomeForm extends MainApp {


    private JPanel homePanel;
    private JTextField nomeFLD;
    private JTextField cognomeFLD;
    private JTextField ibanFLD;
    private JPasswordField balanceFLD;
    private JPasswordField incomeFLD;
    private JPasswordField outcomeFLD;
    private JButton bonificoBTN;
    private JButton depositBTN;
    private JButton prelievoBTN;
    private JButton changePswBTN;
    private JButton logoutBTN;
    private JButton removeBTN;
    private JButton eraseBTN;
    private JButton deleteAllBTN;
    private JButton refreshBTN;
    private JButton showHideBTN;
    private JScrollPane scrollPane;
    private JTextArea transictionArea;
    private JLabel dateLBL;
    private JLabel clockLBL;
    private JTextField timestampFLD;
    private JLabel timerLBL;
    private List<Transaction> transactions;

    public HomeForm() {
        session.updateCreation();
        System.out.println(session);
        setContentPane(homePanel);

        pack();
        System.out.println(location);
        setLocation(location);
        setTitle("Home - JavaBank");
        setFrameIcon(Icons.BANK);
        displayClock(clockLBL, dateLBL);
        setSessionTimer(timerLBL);

        if (showInfo) {
            setCustomIcon(showHideBTN, Icons.HIDEPSW);
            balanceFLD.setEchoChar(Echochar.SHOW);
            incomeFLD.setEchoChar(Echochar.SHOW);
            outcomeFLD.setEchoChar(Echochar.SHOW);
        } else {
            setCustomIcon(showHideBTN, Icons.SHOWPSW);
            balanceFLD.setEchoChar(Echochar.HIDE);
            incomeFLD.setEchoChar(Echochar.HIDE);
            outcomeFLD.setEchoChar(Echochar.HIDE);
        }

        Runnable setValue = new Runnable() {
            @Override
            public synchronized void run() {
                setValue();
            }
        };

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                backgroundTask(new Runnable() {
                    @Override
                    public synchronized void run() {
                        try {
                            if (session.showTransactions().size() > transactions.size()) {
                                playSound(Sounds.NOTIFICATION);
                                setValue();
                            }
                        } catch (SQLException exception) {
//                            SQLExceptionOccurred(exception);
                        }
                    }
                });

            }
        }, 1000, 1000);


        eraseBTN.setVisible(false);
        deleteAllBTN.setVisible(false);

        if (session.getUsername().equals("lorenzo.romio")) {
            eraseBTN.setVisible(true);
            deleteAllBTN.setVisible(true);

            eraseBTN.addActionListener(this::eraseBalance);
            deleteAllBTN.addActionListener(this::deleteAllDatabase);
        }

        setCustomIcon(refreshBTN, Icons.REFRESH);
        setHandCursor(bonificoBTN, changePswBTN, deleteAllBTN, depositBTN, eraseBTN, logoutBTN, prelievoBTN, refreshBTN, removeBTN, showHideBTN);

//        backgroundTask(setValue);
        setValue();
        setVisible(true);

//
        showHideBTN.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showInfo = !showInfo;
                transictionArea.setVisible(showInfo);

                if (showInfo) {
                    setCustomIcon(showHideBTN, Icons.HIDEPSW);
                    balanceFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                    incomeFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                    outcomeFLD.setEchoChar(Echochar.SHOW);  //Password Visibile
                    backgroundTask(setValue);
//                    setValue();
                } else {
                    setCustomIcon(showHideBTN, Icons.SHOWPSW);
                    balanceFLD.setEchoChar(Echochar.HIDE); //* Echo Char
                    outcomeFLD.setEchoChar(Echochar.HIDE); //* Echo Char
                    incomeFLD.setEchoChar(Echochar.HIDE); //* Echo Char

                }
            }


        });

        bonificoBTN.addActionListener(e -> defaultAction());

        refreshBTN.addActionListener(e -> {
                    playSound(Sounds.REFRESH);
                    session.updateCreation();
                    backgroundTask(setValue);
//                        setValue()
                }
        );

        removeBTN.addActionListener(e -> deleteAccountForm());

        depositBTN.addActionListener(e -> depositoForm());

        prelievoBTN.addActionListener(e -> prelievoForm());

        logoutBTN.addActionListener(e -> logOutAction());

        changePswBTN.addActionListener(e -> changePswForm());


    }

    private void setValue() {

        try {
            // print transaction
            transictionArea.removeAll();

            transactions = session.showTransactions();

            int num = (transactions != null) ? transactions.size() : 0;
            transictionArea.setRows((int) (num * 3.8 + 1));
            transictionArea.repaint();

            String iconpath = null;
            Transaction x;
            JLabel iconType;
            JLabel type;
            JLabel date;
            JLabel ibanFrom;
            JLabel ibanDest;
            JLabel userFrom;
            JLabel userDest;
            JLabel amount;
            String sgn = "";

            for (int i = 0; i < num; i++) {
                x = transactions.get(i);
                iconType = new JLabel();
                type = new JLabel(x.getType().toUpperCase());
                date = new JLabel();
                ibanFrom = new JLabel();
                ibanDest = new JLabel();
                userFrom = new JLabel();
                userDest = new JLabel();
                amount = new JLabel();

                if (x.getType().equals("bonifico")) {                  //controllo se il bonifico è in uscita o in entrata

                    if (x.getIbanFrom().equals(session.getIban())) {
                        iconpath = Icons.BONIFICO_OUT;
                        sgn = "- ";
                        amount.setForeground(Color.red);
                        ibanDest.setText("IBAN " + x.getIbanDest());
                        userDest.setText("BONIFICO ► " + x.getUsernameDest().toUpperCase().replace(".", " "));
                        transictionArea.add(ibanDest);
                        transictionArea.add(userDest);
                    } else {
                        iconpath = Icons.BONIFICO_IN;
                        sgn = "+ ";
                        amount.setForeground(Color.green.darker());
                        ibanFrom.setText("IBAN " + x.getIbanFrom());
                        userFrom.setText("BONIFICO ◄ " + x.getUsernameFrom().toUpperCase().replace(".", " "));
                        transictionArea.add(ibanFrom);
                        transictionArea.add(userFrom);
                    }

                } else {
                    transictionArea.add(type);
                    switch (x.getType()) {
                        case "deposito":
                            iconpath = Icons.DEPOSITO;
                            sgn = "+ ";
                            amount.setForeground(Color.green.darker());
                            break;
                        case "prelievo":
                            iconpath = Icons.PRELIEVO;
                            sgn = "- ";
                            amount.setForeground(Color.red);
                            break;
                        default:
                            break;
                    }
                }

                amount.setText(sgn + euro.format(x.getAmount()));
                date.setText(sdf.format(x.getDate()));
                iconType.setBounds(5, 5 + 60 * i, 50, 50);
                iconType.setForeground(Color.red);

                amount.setBounds(380, iconType.getY(), 150, 25);
                amount.setHorizontalAlignment(JLabel.RIGHT);
                date.setBounds(iconType.getX() + iconType.getWidth() + 10, iconType.getY() + 25, 190, 25);
                userFrom.setBounds(date.getX(), iconType.getY() + 5, 500, 25);
                ibanFrom.setBounds(date.getX() + date.getWidth() + 20, date.getY(), 200, 25);
                userDest.setBounds(userFrom.getBounds());
                ibanDest.setBounds(ibanFrom.getBounds());
                type.setBounds(userFrom.getBounds());

                setCustomIcon(iconType, iconpath);

                amount.setFont(new Font(amount.getFont().getName(), Font.BOLD, 20));
                transictionArea.add(iconType);
                transictionArea.add(amount);
                transictionArea.add(date);

                JLabel line = new JLabel();
                line.setBorder(BorderFactory.createLineBorder(Color.black));
                line.setBounds(0, iconType.getY() + iconType.getHeight() + 5, getWidth(), 1);
                transictionArea.add(line);
            }

            scrollPane.setVisible(false);
            scrollPane.setVisible(true);

            transictionArea.setVisible(showInfo);

            //set value

            nomeFLD.setText(session.getNome());
            cognomeFLD.setText(session.getCognome());
            ibanFLD.setText(session.getIban());
            balanceFLD.setText(euro.format(session.getSaldo()));
            incomeFLD.setText(euro.format(session.getIncomes()));
            outcomeFLD.setText(euro.format(session.getOutcomes()));
            timestampFLD.setText(sdf.format(new Date(Long.parseLong(session.getTimestamp()))));
            repaint();

        } catch (SQLException e) {
            SQLExceptionOccurred(e);
        }


    }


    private void eraseBalance(ActionEvent e) {
        try {
            Session.eraseBalance();
            setValue();
            repaint();
            JOptionPane.showMessageDialog(getContentPane(), "All transaction was deleted!");
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }

    private void deleteAllDatabase(ActionEvent e) {
        try {
            timer.cancel();
            session.updateCreation();
            Session.deleteAllDatabase();
            dispose();
            new LoginForm();
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }

    private void changePswForm() {
        timer.cancel();
        location = getLocation();
        new ChangePswForm();

        dispose();

    }

    private void prelievoForm() {
        try {
            timer.cancel();
            location = getLocation();
            new PrelievoForm();
        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        } finally {
            dispose();
        }

    }

    private void depositoForm() {
        try {

            timer.cancel();
            location = this.getLocation();
            new DepositoForm();

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        } finally {
            dispose();
        }

    }

    private void deleteAccountForm() {

        timer.cancel();
        location = getLocation();
        new DeleteAccountForm();
        dispose();


    }

    @Override
    protected void defaultAction() {
        try {
            timer.cancel();
            location = getLocation();
            new BonificoForm();
            dispose();

        } catch (SQLException ex) {
            SQLExceptionOccurred(ex);
        }
    }
}