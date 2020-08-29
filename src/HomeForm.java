 /*
  * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
  */


 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.sql.SQLException;
 import java.util.List;
 import java.util.Locale;
 import java.util.concurrent.TimeoutException;

 public class HomeForm extends WebApp {

     private JPanel homePanel;
     private JPanel buttonsPanel;
     private JTextField nomeFLD;
     private JTextField cognomeFLD;
     private JTextField ibanFLD;
     private JPasswordField balanceFLD;
     private JPasswordField incomeFLD;
     private JPasswordField outcomeFLD;
     private JButton bonificoBTN;
     private JButton changePswBTN;
     private JButton depositBTN;
     private JButton prelievoBTN;
     private JButton logoutBTN;
     private JButton removeBTN;
     private JButton eraseBTN;
     private JButton deleteAll;
     private JButton refreshBTN;
     private JButton showHideBTN;
     private JScrollPane scrollPane;
     private JLabel outcomeLBL;
     private JLabel incomeLBL;
     private JLabel balanceLBL;
     private JLabel ibanLBL;
     private JLabel cognomeLBL;
     private JLabel nomeLBL;
     private JPanel informationPanel;
     private JTextArea transictionArea;
     private JPanel areaTransaction;

     public HomeForm() throws TimeoutException {
//        super();
         session.updateSessionCreation();
         System.out.println(session);
         setContentPane(homePanel);

         pack();
         setLocationRelativeTo(null); //appare in centro allo schermo

         setTitle("Home - JavaBank");
         setFrameIcon(bankIconPath);

         final char visibleChar = '\u0000';
         balanceFLD.setEchoChar(visibleChar);
         incomeFLD.setEchoChar(visibleChar);
         outcomeFLD.setEchoChar(visibleChar);


         try {
             setValue();
         } catch (SQLException e) {
             e.printStackTrace();
         }
         eraseBTN.setVisible(false);
         deleteAll.setVisible(false);

         if (session.getUsername().equals("lorenzo.romio")) {
             eraseBTN.setVisible(true);
             deleteAll.setVisible(true);

             eraseBTN.addActionListener(e -> {
                 try {
                     DBConnect.eraseBalance();
                     setValue();
                     repaint();
                 } catch (SQLException ex) {
                     SQLExceptionOccurred(ex);
                 } catch (TimeoutException ex) {
                     sessionExpired();
                 }
             });

             deleteAll.addActionListener(e -> {

                 try {
                     session.updateSessionCreation();
                     DBConnect.deleteAll();
                     dispose();
                     new LoginForm();
                 } catch (SQLException ex) {
                     SQLExceptionOccurred(ex);
                 } catch (TimeoutException ex) {
                     sessionExpired();
                 }
             });
         }
         Locale.setDefault(Locale.ITALIAN);

         setButtonIcon(showHideBTN, showPswIconPath);
         setButtonIcon(refreshBTN, refreshIconPath);

         setVisible(true);


         showHideBTN.addMouseListener(new MouseListener() {
             @Override
             public void mouseClicked(MouseEvent e) {

                 if (balanceFLD.getEchoChar() == visibleChar) {

                     balanceFLD.setEchoChar(echochar); //* Echo Char
                     outcomeFLD.setEchoChar(echochar); //* Echo Char
                     incomeFLD.setEchoChar(echochar); //* Echo Char
                     scrollPane.setVisible(false);

                     setButtonIcon(showHideBTN, hidePswIconPath);

                 } else {
                     balanceFLD.setEchoChar(visibleChar);  //Password Visibile
                     incomeFLD.setEchoChar(visibleChar);  //Password Visibile
                     outcomeFLD.setEchoChar(visibleChar);  //Password Visibile
                     scrollPane.setVisible(true);

                     setButtonIcon(showHideBTN, showPswIconPath);


                 }
             }

             @Override
             public void mousePressed(MouseEvent e) {

             }

             @Override
             public void mouseReleased(MouseEvent e) {

             }

             @Override
             public void mouseEntered(MouseEvent e) {

             }

             @Override
             public void mouseExited(MouseEvent e) {

             }
         });

         bonificoBTN.addActionListener((ActionEvent e) -> {
             try {
                 new BonificoForm();
                 dispose();

             } catch (TimeoutException ex) {
                 sessionExpired();
             } catch (SQLException ex) {
                 SQLExceptionOccurred(ex);
             }
         });

         refreshBTN.addActionListener(e -> {

             try {
                 setValue();
             } catch (TimeoutException ex) {
                 ex.printStackTrace();
             } catch (SQLException ex) {
                 ex.printStackTrace();
             }

             repaint();


         });

         removeBTN.addActionListener((ActionEvent e) -> {

             try {
                 new DeleteForm();
                 dispose();

             } catch (TimeoutException ex) {
                 sessionExpired();
             }


         });

         depositBTN.addActionListener((ActionEvent e) -> {
             try {
                 new DepositoForm();
                 dispose();

             } catch (TimeoutException ex) {
                 sessionExpired();
             } catch (SQLException ex) {
                 SQLExceptionOccurred(ex);
             }

         });

         prelievoBTN.addActionListener((ActionEvent e) -> {
             try {
                 new PrelievoForm();
                 dispose();

             } catch (TimeoutException ex) {
                 sessionExpired();
             } catch (SQLException ex) {
                 SQLExceptionOccurred(ex);
             }

         });

         logoutBTN.addActionListener(this::actionLogOut);

         changePswBTN.addActionListener((ActionEvent e) -> {

//            try {
//                changePassword();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }

         });

         setVisible(true);

     }

     private void setValue() throws TimeoutException, SQLException {
         printTransaction();
         nomeFLD.setText(session.getNome());
         cognomeFLD.setText(session.getCognome());
         ibanFLD.setText(session.getIban());
         balanceFLD.setText(euro.format(session.getSaldo()));
         incomeFLD.setText(euro.format(session.getIncomes()));
         outcomeFLD.setText(euro.format(session.getOutcomes()));
         repaint();


     }


     private void printTransaction() {
         transictionArea.removeAll();

         List<Transaction> transactions = null;
         try {
             transactions = WebApp.session.showTransactions();
         } catch (TimeoutException e) {
             sessionExpired();
         } catch (SQLException e) {
             SQLExceptionOccurred((SQLException) e);
         }

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

             if (x.getType().equals("bonifico")) {

                 if (x.getIbanFrom().equals(session.getIban())) {
                     iconpath = bonificoOutIconPath;
                     sgn = "- ";
                     amount.setForeground(Color.red);
                     ibanDest.setText("IBAN " + x.getIbanDest());
                     userDest.setText("BONIFICO ► " + x.getUsernameDest().toUpperCase().replace(".", " "));
                     transictionArea.add(ibanDest);
                     transictionArea.add(userDest);
                 } else {
                     iconpath = bonificoInIconPath;
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
                         iconpath = depositoIconPath;
                         sgn = "+ ";
                         amount.setForeground(Color.green.darker());
                         break;
                     case "prelievo":
                         iconpath = prelievoIconPath;
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

             setLabelIcon(iconType, iconpath);

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
     }


 }

