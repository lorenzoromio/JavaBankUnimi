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
//    protected final int width = 600;

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

        String fontName = "Times New Roman";
//        UIManager.put("Label.font", new FontUIResource(new Font(fontName, Font.PLAIN, 18)));
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Lucida Console", Font.PLAIN, 18)));
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
//   /
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
//

    //Functions
    protected void sessionExpired() {
        String error_message = "Sei rimasto inattivo per troppo tempo.\n" +
                     "Verrai reindirizzato alla schermata di Login.";

        String title = "Sessione Scaduta";
        JOptionPane.showMessageDialog(getContentPane(),error_message, title,JOptionPane.ERROR_MESSAGE);

//        JOptionPane.showInternalMessageDialog(getContentPane(), "Sessione Scaduta");
        session = null;
        new LoginForm();
        dispose();
    }

    protected void SQLExceptionOccurred(SQLException ex) {
        String error = "SQL State : " + ex.getSQLState() + "\n" +
                       "ErrorCode : " + ex.getErrorCode() + "\n" +
                        ex.getMessage();
        ex.printStackTrace();
//        System.out.println(ex);
        JOptionPane.showMessageDialog(getContentPane(),error,"SQL Error",JOptionPane.ERROR_MESSAGE);

//        JOptionPane.showInternalMessageDialog(getContentPane(), error);
        new LoginForm();
        dispose();
    }

    protected void setButtonIcon(JButton button, String iconPath) {

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


    protected void setLabelIcon(JLabel label, String iconPath) {
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
