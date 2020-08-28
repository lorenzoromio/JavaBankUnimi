/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeoutException;

//import javax.security.sasl.AuthenticationException;

public class WebApp extends JFrame {
    protected static Session session;
    protected final String deleteAccountIconPath = "icons/deleteAccount.png";
    protected final String changePswIconPath = "icons/changePsw.png";
    protected final String moneyIconPath = "icons/money.png";
    protected final String signUpIconPath = "icons/signUp.png";
    protected final String showPswIconPath = "icons/showpsw.png";
    protected final String hidePswIconPath = "icons/hidepsw.png";
    protected final String refreshIconPath = "icons/refresh.png";
    protected final String bankIconPath = "icons/bank.png";
    protected final String depositoIconPath = "icons/deposito2.png";
    protected final String prelievoIconPath = "icons/prelievo2.png";
    protected final String bonificoInIconPath = "icons/bonificoIn2.png";
    protected final String bonificoOutIconPath = "icons/bonificoOut2.png";
    protected final String nextIconPath = "icons/next.png";
    protected final String prevIconPath = "icons/prev.png";
    protected final int width = 600;
    protected final int height = 600;
    protected final int fldWidth = 190;
    protected final int fldHeight = 30;
    protected final int BTNWidth = 100;
    protected final int BTNHeight = 30;
    protected final int lblWidth = 100;
    protected final int lblHeight = 30;
    protected final DecimalFormat euro = new DecimalFormat("0.00 €");
    protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    protected String user;
    protected String psw;
    protected char echochar = '*';
//    protected char echochar = '\u2022';

    public WebApp(String user, String psw) {
        this.user = user;
        this.psw = psw;
        new LoginForm();
    }

    public WebApp() {
        System.out.println("new WebApp()");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                System.out.println(info.getName());
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("ninbus not avaiable");
        }

//        String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
//        for (int i = 0; i < fonts.length; i++)
//            System.out.println(fonts[i]);

//        String fontName = "Times New Roman";
//        UIManager.put("Label.font", new FontUIResource(new Font(fontName, Font.PLAIN, 18)));
//        UIManager.put("Button.font", new FontUIResource(new Font(fontName, Font.PLAIN, 15)));
//        UIManager.put("TextField.font", new FontUIResource(new Font(fontName, Font.PLAIN, 18)));
//        UIManager.put("PasswordField.font", new FontUIResource(new Font(fontName, Font.PLAIN, 18)));
//        UIManager.put("Button.margin", new Insets(0, 0, 0, 0));

        setFrameIcon(bankIconPath);
//        loginPage();                                                    //CHIAMA LA PAGINA DI LOGIN

        addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {                  // CHIUDE LA CONNESSIONE PRIMA DI CHIUDERE LA FINESTRA
                try {
                    DBConnect.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowActivated(WindowEvent e) {            //CONTROLLA LA VALIDITA' DELLA SESSIONE QUANDO LA FINESTRA SI RIATTIVA
                System.out.println("gain focus");
                if (session != null) try {
                    session.isValid();
                } catch (TimeoutException ex) {
                    sessionExpired();
                }
            }

            @Override
            public void windowDeactivated(WindowEvent e) {          //CHIUDE LA CONNESSIONE QUANDO LA FINESTRA SI DISATTIVA
                try {
                    System.out.println("lost focus");
                    DBConnect.close();
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }
        });
    }


    //    Pages
//    private void loginPage() {
//        try {
//            DBConnect.close();              //CHIUDE LA CONNESSIONE PRIMA DI EFFETTUARE UN NUOVO LOGIN
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        getContentPane().removeAll();
//        repaint();
//        setTitle("LoginPage - JavaBank");
//        setFrameIcon(bankIconPath);
//        JPanel loginPanel = new JPanel();
//
//        add(loginPanel);
//        loginPanel.setLayout(null);
//
////        loginPanel.setLayout(new BorderLayout());
//        loginPanel.setBounds(120,150,350,150);
////        loginPanel.setBackground(Color.gray.brighter().brighter());
//        loginPanel.setBorder(BorderFactory.createLineBorder(Color.black));
////        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//
//
//        JLabel userLBL = new JLabel("Username");
//        JTextField userFLD = new JTextField();
//        JLabel pswLBL = new JLabel("Password");
//        JPasswordField pswFLD = new JPasswordField();
//
//        JButton loginBTN = new JButton("Login");
//        JButton signupBTN = new JButton("Sign Up");
//        JButton exitBTN = new JButton("Exit");
//        getRootPane().setDefaultButton(loginBTN);               //SELEZIONA IL PULSANTE DI LOGIN
//
//        userLBL.setBounds(0, 0, lblWidth, lblHeight);
//        userFLD.setBounds(userLBL.getX() + userLBL.getWidth() + 10, userLBL.getY(), fldWidth, fldHeight);
//        pswLBL.setBounds(userLBL.getX(), userLBL.getY() + userLBL.getHeight() + 5, lblWidth, lblHeight);
//        pswFLD.setBounds(pswLBL.getX() + pswLBL.getWidth() + 10, pswLBL.getY(), fldWidth, fldHeight);
//        pswFLD.setEchoChar(echochar);
//
//        loginBTN.setBounds(userLBL.getX(), pswFLD.getY() + 40, 80, fldHeight);
//        signupBTN.setBounds(loginBTN.getX() + loginBTN.getWidth() + 10, loginBTN.getY(), BTNWidth, BTNHeight);
//        exitBTN.setBounds(signupBTN.getX() + signupBTN.getWidth() + 10, loginBTN.getY(), BTNWidth, BTNHeight);
//
//        loginBTN.setFocusable(false);
//        signupBTN.setFocusable(false);
//        exitBTN.setFocusable(false);
//
//        SwingUtilities.invokeLater(userFLD::requestFocus);       //FOCUS SUL CAMPO USERNAME
//
//        loginPanel.add(userLBL);
//        loginPanel.add(userFLD);
//        loginPanel.add(pswLBL);
//        loginPanel.add(pswFLD);
//        loginPanel.add(loginBTN);
//        loginPanel.add(signupBTN);
//        loginPanel.add(exitBTN);
//        setVisible(true);
//
//        userFLD.setText(user);
//        pswFLD.setText(psw);
//
//        loginBTN.addActionListener((ActionEvent e) -> {
//            if (userFLD.getText().isEmpty())
//                JOptionPane.showInternalMessageDialog(getContentPane(), "Username can't be empty");
//            else try {
//                session = Bank.login(userFLD.getText(), String.valueOf(pswFLD.getPassword()));
//
//                home();
//                JOptionPane.showInternalMessageDialog(getContentPane(), "Logged in Succesfully as " + session.getNome() + " " + session.getCognome());
//
//            } catch (AccountNotFoundException ex) {
//                userFLD.setText("");
//                pswFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (CredentialException ex) {
//                pswFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (SQLException ex) {
//                switch (ex.getSQLState()) {
//                    case "28000":
//                        JOptionPane.showInternalMessageDialog(getContentPane(), "Invalid Credential to Database");
//                        break;
//                    case "08S01":
//                        JOptionPane.showInternalMessageDialog(getContentPane(), "Database Unreachable");
//                        break;
//                    default:
//                        SQLExceptionOccurred(ex);
//                        break;
//                }
//
//            } catch (NoSuchAlgorithmException ex) {
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }
//        });
//
//        signupBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("register BTN pressed");
//            signupPage();
//        });
//
//        exitBTN.addActionListener(this::actionExit);
//    }

//    private void signupPage() {
//        getContentPane().removeAll();
//        repaint();
//        setTitle("SignupPage - JavaBank");
//        setFrameIcon(signUpIconPath);
//
//        JLabel nomeLBL = new JLabel("Nome");
//        JTextField nomeFLD = new JTextField();
//        JLabel cognomeLBL = new JLabel("Cognome");
//        JTextField cognomeFLD = new JTextField();
//        JLabel psw1LBL = new JLabel("Password");
//        JPasswordField psw1FLD = new JPasswordField();
//        JLabel psw2LBL = new JLabel("Confirm");
//        JPasswordField psw2FLD = new JPasswordField();
//        JLabel checkpswLBL = new JLabel("Invalid Password");
//        JButton showHideBTN = new JButton();
//
//        JButton signupBTN = new JButton("Signup");
//        JButton backBTN = new JButton("Back");
//        JButton exitBTN = new JButton("Exit");
//        getRootPane().setDefaultButton(signupBTN);
//
//        nomeLBL.setBounds(10, 20, lblWidth, lblHeight);
//        nomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeLBL.getY(), fldWidth, fldHeight);
//        cognomeLBL.setBounds(nomeLBL.getX(), nomeFLD.getY() + nomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        cognomeFLD.setBounds(nomeFLD.getX(), cognomeLBL.getY(), fldWidth, fldHeight);
//        psw1LBL.setBounds(nomeLBL.getX(), cognomeLBL.getY() + cognomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        psw1FLD.setBounds(nomeFLD.getX(), psw1LBL.getY(), fldWidth, fldHeight);
//        psw2LBL.setBounds(nomeLBL.getX(), psw1LBL.getY() + psw1LBL.getHeight() + 5, lblWidth, lblHeight);
//        psw2FLD.setBounds(nomeFLD.getX(), psw2LBL.getY(), fldWidth, fldHeight);
//        showHideBTN.setBounds(psw1FLD.getX() + psw1FLD.getWidth() + 5, psw1FLD.getY(), BTNHeight, BTNHeight);
//        checkpswLBL.setBounds(showHideBTN.getX() + showHideBTN.getWidth() + 10, psw1FLD.getY(), lblWidth * 2, lblHeight);
//        checkpswLBL.setVisible(false);
//        checkpswLBL.setForeground(Color.red);
//        checkpswLBL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//        signupBTN.setBounds(nomeLBL.getX(), psw2LBL.getY() + psw2LBL.getHeight() + 20, BTNWidth, BTNHeight);
//        backBTN.setBounds(signupBTN.getX() + signupBTN.getWidth() + 10, signupBTN.getY(), BTNWidth, BTNHeight);
//        exitBTN.setBounds(backBTN.getX() + backBTN.getWidth() + 10, backBTN.getY(), BTNWidth, BTNHeight);
//
//        showHideBTN.setFocusable(false);
//        signupBTN.setFocusable(false);
//        backBTN.setFocusable(false);
//        exitBTN.setFocusable(false);
//        showHideBTN.setFocusable(false);
//
//        psw1FLD.setEchoChar(echochar);
//        psw2FLD.setEchoChar(echochar);
//
//        SwingUtilities.invokeLater(nomeFLD::requestFocus);
//
//        setButtonIcon(showHideBTN, hidePswIconPath);
//
//        add(nomeLBL);
//        add(nomeFLD);
//        add(cognomeLBL);
//        add(cognomeFLD);
//        add(psw1LBL);
//        add(psw1FLD);
//        add(psw2LBL);
//        add(psw2FLD);
//        add(signupBTN);
//        add(exitBTN);
//        add(backBTN);
//        add(checkpswLBL);
//        add(showHideBTN);
//        setVisible(true);
//
//
//        JOptionPane pswInvalid = new JOptionPane();
//
//        checkpswLBL.addMouseListener(new MouseListener() {
//
//            final Font font = checkpswLBL.getFont();
//            final Map attributes = font.getAttributes();
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                JOptionPane.showInternalMessageDialog(getContentPane(), pswInvalid.getMessage());
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON); //COSTANTE UNDERLINE_ON =_0
//                checkpswLBL.setFont(font.deriveFont(attributes));
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//                attributes.put(TextAttribute.UNDERLINE, -1); //-1
//                checkpswLBL.setFont(font.deriveFont(attributes));
//            }
//        });
//
//        psw1FLD.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (String.valueOf(psw1FLD.getPassword()).isEmpty())
//                    checkpswLBL.setVisible(false);
//                else try {
//                    Account.checkValidPassword(String.valueOf(psw1FLD.getPassword()));
//                    checkpswLBL.setVisible(false);
//                } catch (IllegalArgumentException ex) {
//                    checkpswLBL.setVisible(true);
//                    pswInvalid.setMessage(ex.getMessage());
//                }
//
//                if (!checkpswLBL.isVisible() || String.valueOf(psw1FLD.getPassword()).isEmpty()) {
//                    psw1FLD.setForeground(new JPasswordField().getForeground());
//                    psw1FLD.setBorder(new JPasswordField().getBorder());
//                } else {
//                    psw1FLD.setForeground(Color.red);
//                    psw1FLD.setBorder(BorderFactory.createLineBorder(Color.red));
//                }
//            }
//        });
//
//        psw2FLD.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (Arrays.equals(psw1FLD.getPassword(), psw2FLD.getPassword()) || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
//                    psw2FLD.setForeground(psw1FLD.getForeground());
//                    psw2FLD.setBorder(psw1FLD.getBorder());
//                } else {
//                    psw2FLD.setForeground(Color.red);
//                    psw2FLD.setBorder(BorderFactory.createLineBorder(Color.red));
//                }
//            }
//        });
//
//        showHideBTN.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                psw1FLD.setEchoChar('\u0000');  //Password Visibile
//                setButtonIcon(showHideBTN, showPswIconPath);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                psw1FLD.setEchoChar(echochar);
////                psw1FLD.setEchoChar('\u2022'); //Dot Echo Char
//                setButtonIcon(showHideBTN, hidePswIconPath);
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });
//
//        signupBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("signup BTN pressed");
//
//            if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || String.valueOf(psw1FLD.getPassword()).isEmpty() || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
//                JOptionPane.showInternalMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
//            } else if (String.valueOf(psw1FLD.getPassword()).equals(String.valueOf(psw2FLD.getPassword()))) {
//                int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?");
//                if (choice == 0) {
//                    try {
//
//                        Bank.addAccount(new Account(nomeFLD.getText(), cognomeFLD.getText(), String.valueOf(psw1FLD.getPassword())));
//                        loginPage();
//
//                    } catch (AccountException ex) {
//                        JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//                        psw1FLD.setText("");
//                        psw2FLD.setText("");
//
//                    } catch (InvalidNameException ex) {
//                        JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//                        nomeFLD.setText("");
//                        cognomeFLD.setText("");
//                        psw1FLD.setText("");
//                        psw2FLD.setText("");
//
//                    } catch (NoSuchAlgorithmException ex) {
//                        JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//                    } catch (SQLException ex) {
//                        SQLExceptionOccurred(ex);
//
//                    }
//
//                }
//
//            } else {
//                System.out.println("Password non corrette");
//                psw1FLD.setText("");
//                psw2FLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), "Password non corrette");
//            }
//        });
//
//        backBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Back BTN pressed");
//            loginPage();
//        });
//
//        exitBTN.addActionListener(this::actionExit);
//
//    }
//
//    private void home() throws SQLException, TimeoutException {
//        session.updateSessionCreation();
//        getContentPane().removeAll();
//        repaint();
//        setTitle("Home - JavaBank");
//        setFrameIcon(bankIconPath);
//
//        JLabel nomeLBL = new JLabel("Nome");
//        JTextField nomeFLD = new JTextField(session.getNome());
//        JLabel cognomeLBL = new JLabel("Cognome");
//        JTextField cognomeFLD = new JTextField(session.getCognome());
//        JLabel ibanLBL = new JLabel("IBAN");
//        JTextField ibanFLD = new JTextField(session.getIban());
//        JLabel balanceLBL = new JLabel("Saldo");
//        JPasswordField balanceFLD = new JPasswordField(euro.format(session.getSaldo()));
//        JButton showHideBTN = new JButton();
//        JButton refreshBTN = new JButton();
//        JLabel incomeLBL = new JLabel("Entrate");
//        JLabel outcomeLBL = new JLabel("Uscite");
//        JPasswordField incomeFLD = new JPasswordField();
//        JPasswordField outcomeFLD = new JPasswordField();
//
//        final char visibleChar = '\u0000';
//        balanceFLD.setEchoChar(visibleChar);
//        incomeFLD.setEchoChar(visibleChar);
//        outcomeFLD.setEchoChar(visibleChar);
//
//        JButton bonificoBTN = new JButton("Bonifico");
//        JButton depositBTN = new JButton("Deposito");
//        JButton prelievoBTN = new JButton("Prelievo");
//        JButton changePswBTN = new JButton("Change Psw");
//        JButton logoutBTN = new JButton("Logout");
//        JButton removeBTN = new JButton("Remove");
//
//        nomeFLD.setEditable(false);
//        cognomeFLD.setEditable(false);
//        ibanFLD.setEditable(false);
//        balanceFLD.setEditable(false);
//        incomeFLD.setEditable(false);
//        outcomeFLD.setEditable(false);
//
//        nomeLBL.setBounds(10, 20, lblWidth, lblHeight);
//        nomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeLBL.getY(), fldWidth, fldHeight);
//        refreshBTN.setBounds(nomeFLD.getX() + nomeFLD.getWidth() + 5, nomeFLD.getY(), BTNHeight, BTNHeight);
//        cognomeLBL.setBounds(nomeLBL.getX(), nomeLBL.getY() + nomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        cognomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeFLD.getY() + nomeFLD.getHeight() + 5, fldWidth, fldHeight);
//        ibanLBL.setBounds(nomeLBL.getX(), cognomeLBL.getY() + cognomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        ibanFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, cognomeFLD.getY() + cognomeFLD.getHeight() + 5, fldWidth, fldHeight);
//        balanceLBL.setBounds(nomeLBL.getX(), ibanLBL.getY() + ibanLBL.getHeight() + 5, lblWidth, lblHeight);
//        balanceFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, ibanFLD.getY() + ibanFLD.getHeight() + 5, fldWidth, fldHeight);
//        showHideBTN.setBounds(balanceFLD.getX() - (BTNHeight + 5), balanceFLD.getY(), BTNHeight, BTNHeight);
//        incomeLBL.setBounds(nomeLBL.getX(), balanceLBL.getY() + balanceLBL.getHeight() + 5, lblWidth, lblHeight);
//        incomeFLD.setBounds(incomeLBL.getX() + incomeLBL.getWidth() + 10, incomeLBL.getY(), fldWidth, fldHeight);
//        outcomeLBL.setBounds(nomeLBL.getX(), incomeLBL.getY() + incomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        outcomeFLD.setBounds(outcomeLBL.getX() + outcomeLBL.getWidth() + 10, outcomeLBL.getY(), fldWidth, fldHeight);
//
//        bonificoBTN.setBounds(350, 20, BTNWidth, BTNHeight);
//        depositBTN.setBounds(bonificoBTN.getX(), bonificoBTN.getY() + bonificoBTN.getHeight() + 5, BTNWidth, BTNHeight);
//        prelievoBTN.setBounds(bonificoBTN.getX(), depositBTN.getY() + depositBTN.getHeight() + 5, BTNWidth, BTNHeight);
//
//        changePswBTN.setBounds(bonificoBTN.getX() + bonificoBTN.getWidth() + 20, bonificoBTN.getY(), BTNWidth, BTNHeight);
//        logoutBTN.setBounds(changePswBTN.getX(), changePswBTN.getY() + changePswBTN.getHeight() + 5, BTNWidth, BTNHeight);
//        removeBTN.setBounds(changePswBTN.getX(), logoutBTN.getY() + logoutBTN.getHeight() + 5, BTNWidth, BTNHeight);
//
//        if (session.getUsername().equals("lorenzo.romio")) {
//            JButton eraseBTN = new JButton("Erase");
//            eraseBTN.setName("Erase");
//            eraseBTN.setBounds(removeBTN.getX(), removeBTN.getY() + removeBTN.getHeight() + 15, BTNWidth, BTNHeight);
//            add(eraseBTN);
//            eraseBTN.addActionListener(e -> {
//                try {
//                    DBConnect.eraseBalance();
//                    home();
//                } catch (SQLException ex) {
//                    SQLExceptionOccurred(ex);
//                } catch (TimeoutException ex) {
//                    sessionExpired();
//                }
//            });
//
//            JButton deleteAll = new JButton("deleteAll");
//            deleteAll.setName("deleteAll");
//            deleteAll.setBounds(eraseBTN.getX(), eraseBTN.getY() + eraseBTN.getHeight() + 5, BTNWidth, BTNHeight);
//            add(deleteAll);
//            deleteAll.addActionListener(e -> {
//                try {
//                    session.updateSessionCreation();
//                    DBConnect.deleteAll();
//                    loginPage();
//                } catch (SQLException ex) {
//                    SQLExceptionOccurred(ex);
//                } catch (TimeoutException ex) {
//                    sessionExpired();
//                }
//            });
//        }
//
//        add(nomeLBL);
//        add(nomeFLD);
//        add(cognomeLBL);
//        add(cognomeFLD);
//        add(ibanLBL);
//        add(ibanFLD);
//        add(balanceLBL);
//        add(balanceFLD);
//        add(incomeLBL);
//        add(incomeFLD);
//        add(outcomeLBL);
//        add(outcomeFLD);
//        add(showHideBTN);
//        add(refreshBTN);
//        add(bonificoBTN);
//        add(depositBTN);
//        add(prelievoBTN);
//        add(changePswBTN);
//        add(logoutBTN);
//        add(removeBTN);
//        setVisible(true);
//
//        List<Transaction> transactions = null;
//        try {
//            transactions = session.showTransactions();
//        } catch (TimeoutException e) {
//            sessionExpired();
//        }
//
//        int num = (transactions != null) ? transactions.size() : 0;
//
//        JTextArea textArea = new JTextArea((int) (num * 3.8 + 1), 1);
//
//        textArea.setEnabled(false);
//        textArea.setBackground(Color.gray.brighter());
//
//        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//        scrollPane.setVisible(num != 0);
//        scrollPane.setBounds(outcomeLBL.getX(), outcomeLBL.getY() + outcomeLBL.getHeight() + 20, 565, 310);
//        add(scrollPane);
//
//        Locale.setDefault(Locale.ITALIAN);
//
//        String iconpath = null;
//        Transaction x;
//        JLabel iconType;
//        JLabel type;
//        JLabel date;
//        JLabel ibanFrom;
//        JLabel ibanDest;
//        JLabel userFrom;
//        JLabel userDest;
//        JLabel amount;
//        String sgn = "";
//
//        for (int i = 0; i < num; i++) {
//            x = transactions.get(i);
//            iconType = new JLabel();
//            type = new JLabel(x.getType().toUpperCase());
//            date = new JLabel();
//            ibanFrom = new JLabel();
//            ibanDest = new JLabel();
//            userFrom = new JLabel();
//            userDest = new JLabel();
//            amount = new JLabel();
//
//
//            if (x.getType().equals("bonifico")) {
//
//                if (x.getIbanFrom().equals(session.getIban())) {
//                    iconpath = bonificoOutIconPath;
//                    sgn = "- ";
//                    amount.setForeground(Color.red);
//                    ibanDest.setText("IBAN " + x.getIbanDest());
//                    userDest.setText("BONIFICO ► " + x.getUsernameDest().toUpperCase().replace(".", " "));
//                    textArea.add(ibanDest);
//                    textArea.add(userDest);
//                } else {
//                    iconpath = bonificoInIconPath;
//                    sgn = "+ ";
//                    amount.setForeground(Color.green.darker());
//                    ibanFrom.setText("IBAN " + x.getIbanFrom());
//                    userFrom.setText("BONIFICO ◄ " + x.getUsernameFrom().toUpperCase().replace(".", " "));
//                    textArea.add(ibanFrom);
//                    textArea.add(userFrom);
//                }
//
//
//            } else {
//                textArea.add(type);
//                switch (x.getType()) {
//                    case "deposito":
//                        iconpath = depositoIconPath;
//                        sgn = "+ ";
//                        amount.setForeground(Color.green.darker());
//                        break;
//                    case "prelievo":
//                        iconpath = prelievoIconPath;
//                        sgn = "- ";
//                        amount.setForeground(Color.red);
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            amount.setText(sgn + euro.format(x.getAmount()));
//            date.setText(sdf.format(x.getDate()));
//            iconType.setBounds(5, 5 + 60 * i, 50, 50);
//            iconType.setForeground(Color.red);
//            amount.setBounds(390, iconType.getY(), 150, 25);
//            amount.setHorizontalAlignment(JLabel.RIGHT);
//            date.setBounds(iconType.getX() + iconType.getWidth() + 10, iconType.getY() + 25, 190, 25);
//            userFrom.setBounds(date.getX(), iconType.getY() + 5, 500, 25);
//            ibanFrom.setBounds(date.getX() + date.getWidth() + 20, date.getY(), 200, 25);
//            userDest.setBounds(userFrom.getBounds());
//            ibanDest.setBounds(ibanFrom.getBounds());
//            type.setBounds(userFrom.getBounds());
//
//            setLabelIcon(iconType, iconpath);
//
//            amount.setFont(new Font(amount.getFont().getName(), Font.BOLD, 20));
//            textArea.add(iconType);
//            textArea.add(amount);
//            textArea.add(date);
//
//            JLabel line = new JLabel();
//            line.setBorder(BorderFactory.createLineBorder(Color.black));
//            line.setBounds(0, iconType.getY() + iconType.getHeight() + 5, width, 1);
//            textArea.add(line);
//        }
//
//        setButtonIcon(showHideBTN, showPswIconPath);
//        setButtonIcon(refreshBTN, refreshIconPath);
//
//        incomeFLD.setText(euro.format(session.getIncomes()));
//        outcomeFLD.setText(euro.format(session.getOutcomes()));
//
//        showHideBTN.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//                if (balanceFLD.getEchoChar() == visibleChar) {
//
//                    balanceFLD.setEchoChar(echochar); //* Echo Char
//                    outcomeFLD.setEchoChar(echochar); //* Echo Char
//                    incomeFLD.setEchoChar(echochar); //* Echo Char
//                    scrollPane.setVisible(false);
//
//                    setButtonIcon(showHideBTN, hidePswIconPath);
//
//                } else {
//                    balanceFLD.setEchoChar(visibleChar);  //Password Visibile
//                    incomeFLD.setEchoChar(visibleChar);  //Password Visibile
//                    outcomeFLD.setEchoChar(visibleChar);  //Password Visibile
//                    scrollPane.setVisible(true);
//
//                    setButtonIcon(showHideBTN, showPswIconPath);
//                    setVisible(true);
//
//
//                }
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//        });
//
//        bonificoBTN.addActionListener((ActionEvent e) -> {
//            try {
//                bonifico();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        refreshBTN.addActionListener(this::homeAction);
//
//        removeBTN.addActionListener((ActionEvent e) -> {
//
//            try {
//                deleteAccount();
//            } catch (TimeoutException ex) {
//                ex.printStackTrace();
//            }
//
//
//        });
//
//        depositBTN.addActionListener((ActionEvent e) -> {
//            try {
//                deposit();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        prelievoBTN.addActionListener((ActionEvent e) -> {
//            try {
//                prelievo();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        logoutBTN.addActionListener(this::actionLogOut);
//
//        changePswBTN.addActionListener((ActionEvent e) -> {
//
//            try {
//                changePassword();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }
//
//        });
//
//
//    }
//
//    private void changePassword() throws TimeoutException {
//        session.updateSessionCreation();
//        getContentPane().removeAll();
//        repaint();
//        setFrameIcon(changePswIconPath);
//        setTitle("Change Password - JavaBank");
//
//        JLabel pswLBL = new JLabel("Attuale");
//        JPasswordField pswFLD = new JPasswordField();
//
//        JLabel newPswLBL = new JLabel("Nuova");
//        JPasswordField newPswFLD = new JPasswordField();
//
//        JLabel chechNewPswLBL = new JLabel("Conferma");
//        JPasswordField checkNewPswFLD = new JPasswordField();
//
//        JLabel checkpswLBL = new JLabel("Invalid Password");
//        JButton showHideBTN1 = new JButton();
//        JButton showHideBTN2 = new JButton();
//
//        JButton saveBTN = new JButton("Save");
//        JButton backBTN = new JButton("Back");
//        JButton logoutBTN = new JButton("Logout");
//        getRootPane().setDefaultButton(saveBTN);
//
//        pswLBL.setBounds(10, 20, lblWidth, lblHeight);
//        pswFLD.setBounds(pswLBL.getX() + pswLBL.getWidth() + 10, pswLBL.getY(), fldWidth, fldHeight);
//        newPswLBL.setBounds(pswLBL.getX(), pswLBL.getY() + pswLBL.getHeight() + 5, lblWidth, lblHeight);
//        newPswFLD.setBounds(newPswLBL.getX() + newPswLBL.getWidth() + 10, newPswLBL.getY(), fldWidth, fldHeight);
//        chechNewPswLBL.setBounds(pswLBL.getX(), newPswFLD.getY() + newPswFLD.getHeight() + 5, lblWidth, lblHeight);
//        checkNewPswFLD.setBounds(chechNewPswLBL.getX() + chechNewPswLBL.getWidth() + 10, chechNewPswLBL.getY(), fldWidth, fldHeight);
//        showHideBTN1.setBounds(newPswFLD.getX() + newPswFLD.getWidth() + 5, newPswFLD.getY(), BTNHeight, BTNHeight);
//        showHideBTN2.setBounds(checkNewPswFLD.getX() + checkNewPswFLD.getWidth() + 5, checkNewPswFLD.getY(), BTNHeight, BTNHeight);
//        checkpswLBL.setBounds(showHideBTN1.getX() + showHideBTN1.getWidth() + 5, newPswFLD.getY(), 100, lblHeight);
//        checkpswLBL.setVisible(false);
//        checkpswLBL.setForeground(Color.red);
//
//        saveBTN.setBounds(pswLBL.getX(), 150, BTNWidth, BTNHeight);
//        backBTN.setBounds(saveBTN.getX() + saveBTN.getWidth() + 10, saveBTN.getY(), BTNWidth, BTNHeight);
//        logoutBTN.setBounds(backBTN.getX() + backBTN.getWidth() + 10, backBTN.getY(), BTNWidth, BTNHeight);
//
//        setButtonIcon(showHideBTN1, hidePswIconPath);
//        setButtonIcon(showHideBTN2, hidePswIconPath);
//
//        pswFLD.setEchoChar(echochar);
//        newPswFLD.setEchoChar(echochar);
//        checkNewPswFLD.setEchoChar(echochar);
//
//        add(pswLBL);
//        add(pswFLD);
//        add(newPswLBL);
//        add(newPswFLD);
//        add(chechNewPswLBL);
//        add(checkNewPswFLD);
//        add(logoutBTN);
//        add(backBTN);
//        add(saveBTN);
//        add(showHideBTN1);
//        add(showHideBTN2);
//        add(checkpswLBL);
//        setVisible(true);
//
//        newPswFLD.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (!String.valueOf(newPswFLD.getPassword()).isEmpty())
//                    try {
//                        Session.checkValidPassword(String.valueOf(newPswFLD.getPassword()));
//                        checkpswLBL.setVisible(false);
//                    } catch (IllegalArgumentException ex) {
//                        checkpswLBL.setVisible(true);
//                    }
//            }
//
//        });
//
//        showHideBTN1.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                newPswFLD.setEchoChar('\u0000');  //Password Visibile
//                setButtonIcon(showHideBTN1, showPswIconPath);
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                newPswFLD.setEchoChar(echochar);
////              newPswFLD.setEchoChar('\u2022'); //Dot Echo Char
//                setButtonIcon(showHideBTN1, hidePswIconPath);
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//        });
//
//        showHideBTN2.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                checkNewPswFLD.setEchoChar('\u0000');  //Password Visibile
//                setButtonIcon(showHideBTN2, showPswIconPath);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                checkNewPswFLD.setEchoChar(echochar);
////                checkNewPswFLD.setEchoChar('\u2022'); //Dot Echo Char
//
//                setButtonIcon(showHideBTN2, hidePswIconPath);
//
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//        });
//
//        saveBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Save BTN pressed");
//            try {
//                session.changePassword(String.valueOf(pswFLD.getPassword()), String.valueOf(newPswFLD.getPassword()), String.valueOf(checkNewPswFLD.getPassword()));
//                JOptionPane.showInternalMessageDialog(getContentPane(), "Modifica effettuata correttamente");
//                home();
//
//            } catch (IllegalArgumentException ex) {
//                newPswFLD.setText("");
//                checkNewPswFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (NoSuchAlgorithmException ex) {
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (CredentialException ex) {
//                pswFLD.setText("");
//                newPswFLD.setText("");
//                checkNewPswFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//
//            }
//        });
//
//        backBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Back BTN pressed");
//            try {
//                home();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }
//        });
//
//        logoutBTN.addActionListener(this::actionLogOut);
//
//
//    }
//
//    private void deleteAccount() throws TimeoutException {
//        session.updateSessionCreation();
//        getContentPane().removeAll();
//        repaint();
//        setTitle("Delete Account - JavaBank");
//        setFrameIcon(deleteAccountIconPath);
//
//        JLabel pswLBL = new JLabel("Password");
//        JPasswordField pswFLD = new JPasswordField();
//
//        JLabel confirmPswLBL = new JLabel("Confirm");
//        JPasswordField confirmPswFLD = new JPasswordField();
//
//        JLabel checkpswLBL = new JLabel("Incorrect Passwords");
//        JButton showHideBTN1 = new JButton();
//        JButton showHideBTN2 = new JButton();
//
//        JButton deleteBTN = new JButton("Delete");
//        JButton backBTN = new JButton("Back");
//        JButton logoutBTN = new JButton("Logout");
//        getRootPane().setDefaultButton(deleteBTN);
//
//        pswLBL.setBounds(10, 20, lblWidth, lblHeight);
//        pswFLD.setBounds(pswLBL.getX() + pswLBL.getWidth() + 10, pswLBL.getY(), fldWidth, fldHeight);
//        confirmPswLBL.setBounds(pswLBL.getX(), pswFLD.getY() + pswFLD.getHeight() + 5, lblWidth, lblHeight);
//        confirmPswFLD.setBounds(confirmPswLBL.getX() + confirmPswLBL.getWidth() + 10, confirmPswLBL.getY(), fldWidth, fldHeight);
//        showHideBTN1.setBounds(pswFLD.getX() + pswFLD.getWidth() + 5, pswFLD.getY(), BTNHeight, BTNHeight);
//        showHideBTN2.setBounds(confirmPswFLD.getX() + confirmPswFLD.getWidth() + 5, confirmPswFLD.getY(), BTNHeight, BTNHeight);
//        showHideBTN2.setMargin(new Insets(0, 0, 0, 0));
//        checkpswLBL.setBounds(showHideBTN1.getX() + showHideBTN1.getWidth() + 5, pswFLD.getY(), 200, lblHeight);
//        checkpswLBL.setVisible(false);
//        checkpswLBL.setForeground(Color.red);
//
//        setButtonIcon(showHideBTN1, hidePswIconPath);
//        setButtonIcon(showHideBTN2, hidePswIconPath);
//        pswFLD.setEchoChar(echochar);
//        confirmPswFLD.setEchoChar(echochar);
//
//        deleteBTN.setBounds(pswLBL.getX(), confirmPswFLD.getY() + confirmPswFLD.getHeight() + 10, BTNWidth, BTNHeight);
//        backBTN.setBounds(deleteBTN.getX() + deleteBTN.getWidth() + 10, deleteBTN.getY(), BTNWidth, BTNHeight);
//        logoutBTN.setBounds(backBTN.getX() + backBTN.getWidth() + 10, backBTN.getY(), BTNWidth, BTNHeight);
//
//        add(pswLBL);
//        add(pswFLD);
//        add(pswLBL);
//        add(pswFLD);
//        add(confirmPswLBL);
//        add(confirmPswFLD);
//        add(logoutBTN);
//        add(backBTN);
//        add(deleteBTN);
//        add(checkpswLBL);
//        add(showHideBTN1);
//        add(showHideBTN2);
//        setVisible(true);
//
//        confirmPswFLD.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                if (Arrays.equals(pswFLD.getPassword(), confirmPswFLD.getPassword()) || String.valueOf(confirmPswFLD.getPassword()).isEmpty()) {
//                    confirmPswFLD.setForeground(new JPasswordField().getForeground());
//                    confirmPswFLD.setBorder(new JPasswordField().getBorder());
//                    pswFLD.setForeground(new JPasswordField().getForeground());
//                    pswFLD.setBorder(new JPasswordField().getBorder());
//                    checkpswLBL.setVisible(false);
//                } else {
//                    confirmPswFLD.setForeground(Color.red);
//                    confirmPswFLD.setBorder(BorderFactory.createLineBorder(Color.red));
//
//                    if (pswFLD.getPassword().length == confirmPswFLD.getPassword().length) {
//                        pswFLD.setForeground(Color.red);
//                        pswFLD.setBorder(BorderFactory.createLineBorder(Color.red));
//                        checkpswLBL.setVisible(true);
//                    }
//                }
//            }
//        });
//
//
//        showHideBTN1.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                pswFLD.setEchoChar('\u0000');  //Password Visibile
//                setButtonIcon(showHideBTN1, showPswIconPath);
//
//
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                pswFLD.setEchoChar(echochar);
////                pswFLD.setEchoChar('\u2022'); //Dot Echo Char
//                setButtonIcon(showHideBTN1, hidePswIconPath);
//
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//
//            }
//        });
//        showHideBTN2.addMouseListener(new MouseListener() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//                confirmPswFLD.setEchoChar('\u0000');  //Password Visibile
//                setButtonIcon(showHideBTN2, showPswIconPath);
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                confirmPswFLD.setEchoChar(echochar);
////                confirmPswFLD.setEchoChar('\u2022'); //Dot Echo Char
//                setButtonIcon(showHideBTN2, hidePswIconPath);
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });
//
//
//        deleteBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Save BTN pressed");
//            int choice = JOptionPane.showConfirmDialog(getContentPane(), "Sei sicuro di voler eliminare il tuo account?");
//            if (choice == 0) {
//
//                try {
//                    session.deleteAccount(String.valueOf(pswFLD.getPassword()), String.valueOf(confirmPswFLD.getPassword()));
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Account Eliminato");
//                    loginPage();
//
//                } catch (IllegalArgumentException | CredentialException ex) {
//                    pswFLD.setText("");
//                    confirmPswFLD.setText("");
//                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//                } catch (TimeoutException ex) {
//                    sessionExpired();
//
//                } catch (SQLException ex) {
//                    SQLExceptionOccurred(ex);
//
//                } catch (NoSuchAlgorithmException ex) {
//                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//                }
//            }
//        });
//
//        backBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Back BTN pressed");
//            try {
//                home();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }
//        });
//
//        logoutBTN.addActionListener(this::actionLogOut);
//
//
//    }
//
//    private void bonifico() throws TimeoutException, SQLException {
//        session.updateSessionCreation();
//        getContentPane().removeAll();
//        repaint();
//        setFrameIcon(moneyIconPath);
//        setTitle("Bonifico - JavaBank");
//
//        JLabel ibanLBL = new JLabel("IBAN");
//        JTextField ibanFLD = new JTextField(30);
//        JLabel amountLBL = new JLabel("Importo");
//        JTextField amountFLD = new JTextField(30);
//
//        JLabel balabceLBL = new JLabel("Saldo");
//        JLabel balance = new JLabel(euro.format(session.getSaldo()));
//        balabceLBL.setVisible(true);
//        balance.setVisible(true);
//        balance.setFont(new Font(balance.getFont().getName(), Font.BOLD, 20));
//
//
//        JButton contactsBTN = new JButton("Rubrica");
//        JButton logoutBTN = new JButton("Logout");
//        JButton transferBTN = new JButton("Transfer");
//        JButton balanceBTN = new JButton();
//        JButton homeBTN = new JButton("Home");
//        getRootPane().setDefaultButton(transferBTN);
//        if (balance.isVisible()) {
//            balanceBTN.setText("Nascondi Saldo");
//        } else {
//            balanceBTN.setText("Mostra Saldo");
//        }
//
//        ibanLBL.setBounds(10, 20, lblWidth, lblHeight);
//        ibanFLD.setBounds(ibanLBL.getX() + ibanLBL.getWidth() + 10, ibanLBL.getY(), fldWidth, fldHeight);
//        amountLBL.setBounds(ibanLBL.getX(), ibanFLD.getY() + ibanFLD.getHeight() + 5, lblWidth, lblHeight);
//        amountFLD.setBounds(amountLBL.getX() + amountLBL.getWidth() + 10, amountLBL.getY(), fldWidth, fldHeight);
//
//        transferBTN.setBounds(ibanLBL.getX(), amountFLD.getY() + amountFLD.getHeight() + 20, BTNWidth, BTNHeight);
//        contactsBTN.setBounds(transferBTN.getX() + transferBTN.getWidth() + 10, transferBTN.getY(), 90, BTNHeight);
//        balanceBTN.setBounds(contactsBTN.getX() + contactsBTN.getWidth() + 10, transferBTN.getY(), 150, BTNHeight);
//        logoutBTN.setBounds(balanceBTN.getX() + balanceBTN.getWidth() + 10, transferBTN.getY(), BTNWidth, BTNHeight);
//        homeBTN.setBounds(logoutBTN.getX(), ibanLBL.getY(), BTNWidth, BTNHeight);
//        balabceLBL.setBounds(ibanFLD.getX() + ibanFLD.getWidth() + 10, ibanFLD.getY(), BTNWidth, BTNHeight);
//        balance.setBounds(balabceLBL.getX(), balabceLBL.getY() + balabceLBL.getHeight() + 10, 300, BTNHeight);
//
//
//        contactsBTN.setFocusable(false);
//        logoutBTN.setFocusable(false);
//        transferBTN.setFocusable(false);
//        balanceBTN.setFocusable(false);
//        homeBTN.setFocusable(false);
//
//        SwingUtilities.invokeLater(amountFLD::requestFocus);
//
//        add(amountFLD);
//        add(amountLBL);
//        add(ibanLBL);
//        add(ibanFLD);
//        add(contactsBTN);
//        add(logoutBTN);
//        add(transferBTN);
//        add(balabceLBL);
//        add(balance);
//        add(balanceBTN);
//        add(homeBTN);
//        setVisible(true);
//
//        //Contact table
//        JLabel searchLBL = new JLabel("Rubrica");
//        JTextField searchFLD = new JTextField();
//        JButton searchBTN = new JButton("Search");
//        JButton clearBTN = new JButton("Clear");
//
//        JLabel nomeLBL = new JLabel("Nome");
//        JTextField nomeFLD = new JTextField();
//        JLabel cognomeLBL = new JLabel("Cognome");
//        JTextField cognomeFLD = new JTextField();
//        JLabel ibanContactLBL = new JLabel("IBAN");
//        JTextField ibanContactFLD = new JTextField();
//
//        JButton prevBTN = new JButton();
//        JButton nextBTN = new JButton();
//        JLabel results = new JLabel("# of #");
//
//        nomeFLD.setEditable(false);
//        cognomeFLD.setEditable(false);
//        ibanContactFLD.setEditable(false);
//
//        searchLBL.setBounds(ibanLBL.getX(), transferBTN.getY() + transferBTN.getHeight() + 100, lblWidth, lblHeight);
//        searchFLD.setBounds(searchLBL.getX() + searchLBL.getWidth() + 10, searchLBL.getY(), fldWidth, fldHeight);
//        searchBTN.setBounds(searchFLD.getX() + searchFLD.getWidth() + 10, searchFLD.getY(), BTNWidth, BTNHeight);
//        clearBTN.setBounds(searchBTN.getX() + searchBTN.getWidth() + 10, searchFLD.getY(), BTNWidth, BTNHeight);
//        nomeLBL.setBounds(searchLBL.getX(), searchLBL.getY() + searchLBL.getHeight() + 10, lblWidth, lblHeight);
//        nomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeLBL.getY(), fldWidth, fldHeight);
//        cognomeLBL.setBounds(nomeLBL.getX(), nomeLBL.getY() + nomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        cognomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeFLD.getY() + nomeFLD.getHeight() + 5, fldWidth, fldHeight);
//        ibanContactLBL.setBounds(nomeLBL.getX(), cognomeLBL.getY() + cognomeLBL.getHeight() + 5, lblWidth, lblHeight);
//        ibanContactFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, cognomeFLD.getY() + cognomeFLD.getHeight() + 5, fldWidth, fldHeight);
//
//        prevBTN.setBounds(nomeLBL.getX() + 80, ibanContactFLD.getY() + ibanContactFLD.getHeight() + 20, 40, 40);
//        results.setBounds(prevBTN.getX() + prevBTN.getWidth() + 5, prevBTN.getY() + 5, BTNWidth, BTNHeight);
//        results.setHorizontalAlignment(SwingConstants.CENTER);
//        nextBTN.setBounds(results.getX() + results.getWidth() + 5, ibanContactFLD.getY() + ibanContactFLD.getHeight() + 20, 40, 40);
//
//
//        add(searchLBL);
//        add(searchFLD);
//        add(searchBTN);
//        add(clearBTN);
//        add(nomeLBL);
//        add(nomeFLD);
//        add(cognomeLBL);
//        add(cognomeFLD);
//        add(ibanContactLBL);
//        add(ibanContactFLD);
//        add(prevBTN);
//        add(nextBTN);
//        add(results);
//        setVisible(true);
//
//        searchBTN.setVisible(false);
//        searchFLD.setVisible(false);
//        searchLBL.setVisible(false);
//        clearBTN.setVisible(false);
//        nomeLBL.setVisible(false);
//        nomeFLD.setVisible(false);
//        cognomeLBL.setVisible(false);
//        cognomeFLD.setVisible(false);
//        ibanContactLBL.setVisible(false);
//        ibanContactFLD.setVisible(false);
//        prevBTN.setVisible(false);
//        results.setVisible(false);
//        nextBTN.setVisible(false);
//
//        searchBTN.setFocusable(false);
//        searchFLD.setFocusable(false);
//        searchLBL.setFocusable(false);
//        clearBTN.setFocusable(false);
//        nomeLBL.setFocusable(false);
//        nomeFLD.setFocusable(false);
//        cognomeLBL.setFocusable(false);
//        cognomeFLD.setFocusable(false);
//        ibanContactLBL.setFocusable(false);
//        prevBTN.setFocusable(false);
//        results.setFocusable(false);
//        nextBTN.setFocusable(false);
//
//        setButtonIcon(nextBTN, nextIconPath);
//        setButtonIcon(prevBTN, prevIconPath);
//
//        var contacts = new Object() {
//            int index = 0;
//            List<Account> list;
//        };
//
//        contactsBTN.addActionListener((ActionEvent e) -> {
//
//            try {
//                contacts.list = session.showContacts();
//
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            }
//
//            boolean flag = !nomeFLD.isVisible();
//
//            searchBTN.setVisible(flag);
//            searchFLD.setVisible(flag);
//            searchLBL.setVisible(flag);
//            clearBTN.setVisible(flag);
//            nomeLBL.setVisible(flag);
//            nomeFLD.setVisible(flag);
//            cognomeLBL.setVisible(flag);
//            cognomeFLD.setVisible(flag);
//            ibanContactLBL.setVisible(flag);
//            ibanContactFLD.setVisible(flag);
//            prevBTN.setVisible(flag);
//            results.setVisible(flag);
//            nextBTN.setVisible(flag);
//
//            searchFLD.setFocusable(true);
//
//            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
//            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
//            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
//            ibanFLD.setText(flag ? ibanContactFLD.getText() : "");
//            results.setText(contacts.index + 1 + " of " + contacts.list.size());
//
//            repaint();
//
//
//        });
//
//        searchFLD.addFocusListener(new FocusListener() {
//            @Override
//            public void focusGained(FocusEvent e) {
//                getRootPane().setDefaultButton(searchBTN);
//            }
//
//            @Override
//            public void focusLost(FocusEvent e) {
//                getRootPane().setDefaultButton(transferBTN);
//            }
//        });
//
//        searchFLD.addKeyListener(new KeyListener() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//
//            }
//        });
//
//        prevBTN.addActionListener((ActionEvent e1) -> {
//            if (contacts.index > 0) contacts.index--;
//            results.setText(contacts.index + 1 + " of " + contacts.list.size());
//
//            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
//            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
//            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
//            ibanFLD.setText(ibanContactFLD.getText());
//        });
//
//        nextBTN.addActionListener((ActionEvent e1) -> {
//            if (contacts.index < contacts.list.size() - 1) contacts.index++;
//            results.setText(contacts.index + 1 + " of " + contacts.list.size());
//
//            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
//            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
//            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
//            ibanFLD.setText(ibanContactFLD.getText());
//        });
//
//        searchBTN.addActionListener((ActionEvent e) -> {          //FUNZIONA
//
//            try {
//                contacts.list = session.showContacts();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//            String[] words = searchFLD.getText().split(" ");
//            for (int i = 0; i < contacts.list.size(); i++) {
//                for (String word : words) {
//                    if (!contacts.list.get(i).getUsername().toLowerCase().contains(word.toLowerCase())) {
//                        contacts.list.remove(i);
//                        i--;
//                        break;
//                    }
//                }
//            }
//
//            contacts.index = 0;
//            if (contacts.list.isEmpty()) {
//                results.setText("No Result");
//                nomeFLD.setText("");
//                cognomeFLD.setText("");
//                ibanContactFLD.setText("");
//                ibanFLD.setText("");
//            } else {
//                results.setText(contacts.index + 1 + " of " + contacts.list.size());
//                nomeFLD.setText(contacts.list.get(contacts.index).getNome());
//                cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
//                ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
//                ibanFLD.setText(ibanContactFLD.getText());
//            }
//
//
//        });
//
//        clearBTN.addActionListener((ActionEvent e) -> {
//            searchFLD.setText("");
////            searchBTN.doClick();
//        });
//
//        balanceBTN.addActionListener((ActionEvent e) -> {
//
//            try {
//                session.updateSessionCreation();
////                session.accountBalanceUpdate();
//                balance.setText(euro.format(session.getSaldo()));
//
//                if (balance.isVisible()) {
//                    balanceBTN.setText("Mostra Saldo");
//                    balabceLBL.setVisible(false);
//                    balance.setVisible(false);
//
//                } else {
//                    balanceBTN.setText("Nascondi Saldo");
//                    balabceLBL.setVisible(true);
//                    balance.setVisible(true);
//                }
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        transferBTN.addActionListener((ActionEvent e) -> {
//            try {
//                Double amount;
//                session.updateSessionCreation();
//                if (amountFLD.getText().isEmpty())
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");
//
//                else {
//                    amount = session.validateAmount(amountFLD.getText());
//                    if (ibanFLD.getText().isEmpty()) {
//                        JOptionPane.showInternalMessageDialog(getContentPane(), "Iban field can't be empty");
//                    } else {
//                        session.transfer(ibanFLD.getText().toUpperCase(), amount);
//                        amountFLD.setText("");
//                        balance.setText(euro.format(session.getSaldo()));
//                        JOptionPane.showInternalMessageDialog(getContentPane(), "Bonifico Effettuato Correttamente");
//                    }
//                }
//
//            } catch (NumberFormatException ex) {
//                amountFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), "Amount can be only numeric");
//
//            } catch (IllegalArgumentException ex) {
//                amountFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (AccountNotFoundException ex) {
//                ibanFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        homeBTN.addActionListener(this::homeAction);
//
//        logoutBTN.addActionListener(this::actionLogOut);
//
//    }
//
//    private void deposit() throws TimeoutException, SQLException {
//        session.updateSessionCreation();
//        getContentPane().removeAll();
//        repaint();
//        setFrameIcon(moneyIconPath);
//
//
//        JPanel depositPanel = new JPanel();
//        setTitle("Deposito - JavaBank");
//        add(depositPanel);
//
//        JLabel amountLBL = new JLabel("Importo");
//        JTextField amountFLD = new JTextField(30);
//
//        JLabel balabceLBL = new JLabel("Saldo");
//        JLabel balance = new JLabel(euro.format(session.getSaldo()));
//        balabceLBL.setVisible(true);
//        balance.setVisible(true);
//        balance.setFont(new Font(balance.getFont().getName(), Font.BOLD, 20));
//
//
////        JButton contactsBTN = new JButton("Contacts");
//        JButton logoutBTN = new JButton("Logout");
//        JButton depositBTN = new JButton("Deposita");
//        JButton balanceBTN = new JButton();
//        JButton homeBTN = new JButton("Account");
//        getRootPane().setDefaultButton(depositBTN);
//
//        amountLBL.setBounds(10, 20, lblWidth, lblHeight);
//        amountFLD.setBounds(amountLBL.getX() + amountLBL.getWidth() + 10, amountLBL.getY(), fldWidth, fldHeight);
//        balabceLBL.setBounds(amountFLD.getX() + amountFLD.getWidth() + 10, amountLBL.getY(), lblWidth, lblHeight);
//        balance.setBounds(balabceLBL.getX(), balabceLBL.getY() + balabceLBL.getHeight() + 5, 300, lblHeight);
//        depositBTN.setBounds(amountLBL.getX(), balance.getY() + balance.getHeight() + 10, BTNWidth, BTNHeight);
//        balanceBTN.setBounds(depositBTN.getX() + depositBTN.getWidth() + 10, depositBTN.getY(), 150, BTNHeight);
//        logoutBTN.setBounds(balanceBTN.getX() + balanceBTN.getWidth() + 10, balanceBTN.getY(), BTNWidth, BTNHeight);
//        homeBTN.setBounds(370, amountLBL.getY(), BTNWidth, BTNHeight);
//
//
//        depositBTN.setFocusable(false);
//        balanceBTN.setFocusable(false);
//        logoutBTN.setFocusable(false);
//        homeBTN.setFocusable(false);
//
//        if (balance.isVisible()) {
//            balanceBTN.setText("Nascondi Saldo");
//        } else {
//            balanceBTN.setText("Mostra Saldo");
//        }
//
//        SwingUtilities.invokeLater(amountFLD::requestFocus);
//
//
//        logoutBTN.addActionListener((ActionEvent e) -> {
////            System.out.println("Logout BTN pressed");
//            loginPage();
//        });
//
//        balanceBTN.addActionListener((ActionEvent e) -> {
//
//            try {
//                session.updateSessionCreation();
////                session.accountBalanceUpdate();
//                balance.setText(euro.format(session.getSaldo()));
//
//                if (balance.isVisible()) {
//
//                    balanceBTN.setText("Mostra Saldo");
//                    balabceLBL.setVisible(false);
//                    balance.setVisible(false);
//
//                } else {
//
//                    balanceBTN.setText("Nascondi Saldo");
//                    balabceLBL.setVisible(true);
//                    balance.setVisible(true);
//
//                }
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//
//        });
//
//        homeBTN.addActionListener((ActionEvent e) -> {
//            try {
//                session.updateSessionCreation();
////                session.accountBalanceUpdate();
//                home();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        depositBTN.addActionListener((ActionEvent e) -> {
//            try {
////                Double amount = 0.0;
//                session.updateSessionCreation();
//                if (amountFLD.getText().isEmpty())
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");
//                else {
////                    amount = ;
//                    session.deposit(session.validateAmount(amountFLD.getText()));
////                    session.deposit(session.validateAmount(amountFLD.getText()));
//                    amountFLD.setText("");
//                    balance.setText(euro.format(session.getSaldo()));
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Deposito Effettuato Correttamente");
//                }
//
//
//            } catch (IllegalArgumentException ex) {
//                amountFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        add(amountFLD);
//        add(amountLBL);
//        add(logoutBTN);
//        add(depositBTN);
//        add(balabceLBL);
//        add(balance);
//        add(balanceBTN);
//        add(homeBTN);
//
//        setVisible(true);
//    }
//
//    private void prelievo() throws TimeoutException, SQLException {
//        session.updateSessionCreation();
//        getContentPane().removeAll();
//        repaint();
//        setFrameIcon(moneyIconPath);
//
//
//        JPanel depositPanel = new JPanel();
//        setTitle("Prelievo - JavaBank");
//        add(depositPanel);
//
//        JLabel amountLBL = new JLabel("Importo");
//        JTextField amountFLD = new JTextField(30);
//
//        JLabel balabceLBL = new JLabel("Saldo");
//        JLabel balance = new JLabel(euro.format(session.getSaldo()));
//        balabceLBL.setVisible(true);
//        balance.setVisible(true);
//        balance.setFont(new Font(balance.getFont().getName(), Font.BOLD, 20));
//
////      JButton contactsBTN = new JButton("Contacts");
//        JButton logoutBTN = new JButton("Logout");
//        JButton prelievoBTN = new JButton("Preleva");
//        JButton balanceBTN = new JButton();
//        JButton homeBTN = new JButton("Home");
//        getRootPane().setDefaultButton(prelievoBTN);
//        if (balance.isVisible()) {
//            balanceBTN.setText("Nascondi Saldo");
//        } else {
//            balanceBTN.setText("Mostra Saldo");
//        }
//
//        amountLBL.setBounds(10, 20, lblWidth, lblHeight);
//        amountFLD.setBounds(amountLBL.getX() + amountLBL.getWidth() + 10, amountLBL.getY(), fldWidth, fldHeight);
//        balabceLBL.setBounds(amountFLD.getX() + amountFLD.getWidth() + 10, amountLBL.getY(), lblWidth, lblHeight);
//        balance.setBounds(balabceLBL.getX(), balabceLBL.getY() + balabceLBL.getHeight() + 5, 300, lblHeight);
//        prelievoBTN.setBounds(amountLBL.getX(), balance.getY() + balance.getHeight() + 10, BTNWidth, BTNHeight);
//        balanceBTN.setBounds(prelievoBTN.getX() + prelievoBTN.getWidth() + 10, prelievoBTN.getY(), 150, BTNHeight);
//        logoutBTN.setBounds(balanceBTN.getX() + balanceBTN.getWidth() + 10, balanceBTN.getY(), BTNWidth, BTNHeight);
//        homeBTN.setBounds(370, amountLBL.getY(), BTNWidth, BTNHeight);
//
//
//        prelievoBTN.setFocusable(false);
//        balanceBTN.setFocusable(false);
//        logoutBTN.setFocusable(false);
//        homeBTN.setFocusable(false);
//
//        SwingUtilities.invokeLater(amountFLD::requestFocus);
//
//        prelievoBTN.addActionListener((ActionEvent e) -> {
//            try {
//                session.updateSessionCreation();
//                if (amountFLD.getText().isEmpty())
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");
//                else {
//                    session.prelievo(session.validateAmount(amountFLD.getText()));
//                    amountFLD.setText("");
//                    balance.setText(euro.format(session.getSaldo()));
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Prelievo Effettuato Correttamente");
//                }
//
//            } catch (IllegalArgumentException ex) {
//                amountFLD.setText("");
//                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        logoutBTN.addActionListener(this::actionLogOut);
//
//        balanceBTN.addActionListener((ActionEvent e) -> {
//
//            try {
//                session.updateSessionCreation();
////                session.accountBalanceUpdate();
//                balance.setText(euro.format(session.getSaldo()));
//
//                if (balance.isVisible()) {
//                    balanceBTN.setText("Mostra Saldo");
//                    balabceLBL.setVisible(false);
//                    balance.setVisible(false);
//
//                } else {
//                    balanceBTN.setText("Nascondi Saldo");
//                    balabceLBL.setVisible(true);
//                    balance.setVisible(true);
//
//                }
//
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//
//        });
//
//        homeBTN.addActionListener((ActionEvent e) -> {
//            try {
//                session.updateSessionCreation();
//                home();
//            } catch (TimeoutException ex) {
//                sessionExpired();
//            } catch (SQLException ex) {
//                SQLExceptionOccurred(ex);
//            }
//
//        });
//
//        add(amountFLD);
//        add(amountLBL);
//        add(logoutBTN);
//        add(prelievoBTN);
//        add(balabceLBL);
//        add(balance);
//        add(balanceBTN);
//        add(homeBTN);
//
//        setVisible(true);
//    }

    //Functions
    protected void sessionExpired() {
        JOptionPane.showInternalMessageDialog(getContentPane(), "Sessione Scaduta");
        session = null;
        new LoginForm();
        dispose();
    }

    protected void SQLExceptionOccurred(SQLException ex) {
        String error = "SQL Error\n" +
                "SQL State : " + ex.getSQLState() +
                "ErrorCode : " + ex.getErrorCode() +
                ex.getMessage();
        ex.printStackTrace();
//        System.out.println(ex);
        JOptionPane.showInternalMessageDialog(getContentPane(), error);
        new LoginForm();
        dispose();
    }

    public void setButtonIcon(JButton button, String iconPath) {

        URL iconUrl;
        Image icon;

        try {
            iconUrl = this.getClass().getResource(iconPath);
            icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
        } catch (Exception e) {
            icon = new ImageIcon(iconPath).getImage();
        }

        try {
            button.setIcon(new ImageIcon(icon.getScaledInstance(button.getWidth() - 5, button.getHeight() - 5, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void setLabelIcon(JLabel label, String iconPath) {
        URL iconUrl;
        Image icon;

        try {
            iconUrl = this.getClass().getResource(iconPath);
            icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
        } catch (Exception e) {
            icon = new ImageIcon(iconPath).getImage();
        }
        try {
            label.setIcon(new ImageIcon(icon.getScaledInstance(label.getWidth() - 5, label.getHeight() - 5, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setFrameIcon(String iconPath) {
        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(iconPath)));
        } catch (NullPointerException e) {
            ImageIcon imageIcon = new ImageIcon(iconPath);
            setIconImage(imageIcon.getImage());
        }
    }


    protected void actionExit(ActionEvent e) {
        System.out.println("Exit BTN pressed");
        int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to exit?");
        if (choice == 0) {
            session = null;
            dispose();
        }
    }

    protected void actionLogOut(ActionEvent e) {

        System.out.println("LogOut BTN pressed");
        int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to LogOut?");
        if (choice == 0) {
            dispose();
            session = null;
            new LoginForm();
        }
    }

    protected void homeAction(ActionEvent e) {
        try {
            new HomeForm();
            dispose();
        } catch (TimeoutException ex) {
            sessionExpired();
        }

    }

}
