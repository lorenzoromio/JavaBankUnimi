import javax.imageio.ImageIO;
import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.sasl.AuthenticationException;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class WebApp extends JFrame {
    private final String showPswIconPath = "icons/showpsw.png";
    private final String hidePswIconPath = "icons/hidepsw.png";
    private final String refreshIconPath = "icons/refresh.png";
    private final String bankIconPath = "icons/bank.png";
    private final String depositoIconPath = "icons/deposito2.png";
    private final String prelievoIconPath = "icons/prelievo2.png";
    private final String bonificoInIconPath = "icons/bonificoIn.png";
    private final String bonificoOutIconPath = "icons/bonificoOut.png";
    private final String nextIconPath = "icons/next.png";
    private final String prevIconPath = "icons/prev.png";
    private final int width = 600;
    private final int height = 600;
    private final int fldWidth = 190;
    private final int fldHeight = 30;
    private final int BTNWidth = 100;
    private final int BTNHeight = 30;
    private final int lblWidth = 100;
    private final int lblHeight = 30;
    private DecimalFormat € = new DecimalFormat("0.00 €");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    private Session session;
    private String user;
    private String psw;


    public WebApp() {
        new WebApp("", "");
    }

    public WebApp(String user, String psw) {
        this.user = user;
        this.psw = psw;
        setSize(width, height);
        setResizable(false);

        UIManager.put("Label.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 15)));
        UIManager.put("Button.font", new FontUIResource(new Font("Verdana", Font.PLAIN, 15)));
        UIManager.put("TextField.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 14)));
        UIManager.put("PasswordField.font", new FontUIResource(new Font("Dialog", Font.PLAIN, 15)));
        UIManager.put("Button.margin", new Insets(0, 0, 0, 0));


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        try {
////            setIconImage(ImageIO.read(new File(bankIconPath)));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        URL iconUrl = this.getClass().getResource(bankIconPath);
        Toolkit tk = this.getToolkit();
        setIconImage(tk.getImage(iconUrl));

//        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(bankIconPath)));


        setLayout(null);

        loginPage();                                                    //CHIAMA LA PAGINA DI LOGIN

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {                  // CHIUDE LA CONNESSIONE PRIMA DI CHIUDERE LA FINESTRA
                try {
                    DBConnect.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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

            @Override
            public void windowActivated(WindowEvent e) {            //CONTROLLA LA VALIDITA' DELLA SESSIONE QUANDO LA FINESTRA SI RIATTIVA
                if (session != null) {
                    try {
                        session.isValid();
                    } catch (TimeoutException ex) {
                        sessionExpired();
                    }
                }
            }

            @Override
            public void windowDeactivated(WindowEvent e) {          //CHIUDE LA CONNESSIONE QUANDO LA FINESTRA SI DISATTIVA
                try {
                    DBConnect.close();
                } catch (SQLException ex) {
                }
            }
        });


    }

//    Pages

    private void loginPage() {
        try {
            DBConnect.close();              //CHIUDE LA CONNESSIONE PRIMA DI EFFETTUARE UN NUOVO LOGIN
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        session = null;
        getContentPane().removeAll();
        repaint();

        JPanel loginPanel = new JPanel();
        setTitle("LoginPage - JavaBank");
        add(loginPanel);
        setLayout(null);

        JLabel userLBL = new JLabel("Username");
        JTextField userFLD = new JTextField();
        JLabel pswLBL = new JLabel("Password");
        JPasswordField pswFLD = new JPasswordField();
        JButton showHideBTN = new JButton();

        JButton loginBTN = new JButton("Login");
        JButton signupBTN = new JButton("Sign Up");
        JButton exitBTN = new JButton("Exit");
        getRootPane().setDefaultButton(loginBTN);               //SELEZIONA IL PULSANTE DI LOGIN


        try {
            Image icon = ImageIO.read(new File(hidePswIconPath));


            showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(BTNWidth, BTNHeight, Image.SCALE_SMOOTH)));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        userLBL.setBounds(10, 20, lblWidth, lblHeight);
        userFLD.setBounds(userLBL.getX() + userLBL.getWidth() + 10, userLBL.getY(), fldWidth, fldHeight);
        pswLBL.setBounds(userLBL.getX(), userLBL.getY() + userLBL.getHeight() + 5, lblWidth, lblHeight);
        pswFLD.setBounds(pswLBL.getX() + pswLBL.getWidth() + 10, pswLBL.getY(), fldWidth, fldHeight);
        showHideBTN.setBounds(pswFLD.getX() + pswFLD.getWidth() + 5, pswFLD.getY(), BTNHeight, BTNHeight);

        loginBTN.setBounds(userLBL.getX(), pswFLD.getY() + 40, 80, fldHeight);
        signupBTN.setBounds(loginBTN.getX() + loginBTN.getWidth() + 10, loginBTN.getY(), BTNWidth, BTNHeight);
        exitBTN.setBounds(signupBTN.getX() + signupBTN.getWidth() + 10, loginBTN.getY(), BTNWidth, BTNHeight);

        loginBTN.setFocusable(false);
        signupBTN.setFocusable(false);
        exitBTN.setFocusable(false);

        SwingUtilities.invokeLater(() -> userFLD.requestFocus());       //FOCUS SUL CAMPO USERNAME

        add(userLBL);
        add(userFLD);
        add(pswLBL);
        add(pswFLD);
        add(loginBTN);
        add(signupBTN);
        add(exitBTN);

        setVisible(true);

        userFLD.setText(user);
        pswFLD.setText(psw);

        showHideBTN.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                pswFLD.setEchoChar('\u0000');  //Password Visibile

//                try {
////                    Image icon = ImageIO.read(new File(showPswIconPath));
////                    showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(BTNWidth, BTNHeight, Image.SCALE_SMOOTH)));
////                    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(bankIconPath)));
////
////
////                } catch (IOException ex) {
////                    ex.printStackTrace();
////                }

                URL iconUrl = this.getClass().getResource(showPswIconPath);
                Toolkit tk = Toolkit.getDefaultToolkit();
                Image icon = tk.getImage(iconUrl);
                showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(showHideBTN.getWidth(), showHideBTN.getHeight(), Image.SCALE_SMOOTH)));

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pswFLD.setEchoChar('\u2022'); //Dot Echo Char
//                try {
//                    Image icon = ImageIO.read(new File(hidePswIconPath));
//                    showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(BTNWidth, BTNHeight, Image.SCALE_SMOOTH)));
//
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }

                URL iconUrl = this.getClass().getResource(hidePswIconPath);
                Toolkit tk = Toolkit.getDefaultToolkit();
                Image icon = tk.getImage(iconUrl);
                showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(showHideBTN.getWidth(), showHideBTN.getHeight(), Image.SCALE_SMOOTH)));

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        loginBTN.addActionListener((ActionEvent e) -> {
            if (userFLD.getText().isEmpty())
                JOptionPane.showInternalMessageDialog(getContentPane(), "Username can't be empty");
            else {
                try {
                    session = Bank.login(userFLD.getText(), String.valueOf(pswFLD.getPassword()));
//                    System.out.println("Logged As '" + userFLD.getText() + "'");
//                    JOptionPane.showInternalMessageDialog(getContentPane(), "Logged in Succesfully as " + session.getNome() + " " + session.getCognome());
                    home();

                } catch (AccountNotFoundException ex) {
                    userFLD.setText("");
                    pswFLD.setText("");
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

                } catch (AuthenticationException ex) {
                    pswFLD.setText("");
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

                } catch (SQLException ex) {
                    switch (ex.getSQLState()) {
                        case "28000":
                            JOptionPane.showInternalMessageDialog(getContentPane(), "Invalid Credential to Database");
                            break;
                        case "08S01":
                            JOptionPane.showInternalMessageDialog(getContentPane(), "Database Unreachable");
                            break;
                        default:
                            SQLExceptionOccurred(ex);
                            break;
                    }

                } catch (NoSuchAlgorithmException ex) {
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
                } catch (TimeoutException ex) {
                    sessionExpired();
                }
            }

        });

        signupBTN.addActionListener((ActionEvent e) -> {
            System.out.println("register BTN pressed");
            signupPage();
        });

        exitBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Exit BTN pressed");
            int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to exit?");
            if (choice == 0) {
                dispose();
            }
        });

        JButton erase = new JButton("Erase");
        erase.setName("Erase");
        erase.setBounds(width / 2, height / 2, BTNWidth, BTNHeight);
        add(erase);
        erase.addActionListener(e -> {
            try {
                DBConnect.eraseBalance();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }
        });


    }

    private void signupPage() {
        getContentPane().removeAll();
        repaint();
        JPanel signupPanel = new JPanel();
        setTitle("SignupPage - JavaBank");
        add(signupPanel);

        JLabel nomeLBL = new JLabel("Nome");
        JTextField nomeFLD = new JTextField();
        JLabel cognomeLBL = new JLabel("Cognome");
        JTextField cognomeFLD = new JTextField();
        JLabel psw1LBL = new JLabel("Password");
        JPasswordField psw1FLD = new JPasswordField();
        JLabel psw2LBL = new JLabel("Confirm");
        JPasswordField psw2FLD = new JPasswordField();
        JLabel checkpswLBL = new JLabel("Invalid Password");
        JButton showHideBTN = new JButton();


        JButton signupBTN = new JButton("Signup");
        JButton backBTN = new JButton("Back");
        JButton exitBTN = new JButton("Exit");
        getRootPane().setDefaultButton(signupBTN);

        try {
            Image icon = ImageIO.read(new File(hidePswIconPath));
            showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        nomeLBL.setBounds(10, 20, lblWidth, lblHeight);
        nomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeLBL.getY(), fldWidth, fldHeight);
        cognomeLBL.setBounds(nomeLBL.getX(), nomeFLD.getY() + nomeLBL.getHeight() + 5, lblWidth, lblHeight);
        cognomeFLD.setBounds(nomeFLD.getX(), cognomeLBL.getY(), fldWidth, fldHeight);
        psw1LBL.setBounds(nomeLBL.getX(), cognomeLBL.getY() + cognomeLBL.getHeight() + 5, lblWidth, lblHeight);
        psw1FLD.setBounds(nomeFLD.getX(), psw1LBL.getY(), fldWidth, fldHeight);
        psw2LBL.setBounds(nomeLBL.getX(), psw1LBL.getY() + psw1LBL.getHeight() + 5, lblWidth, lblHeight);
        psw2FLD.setBounds(nomeFLD.getX(), psw2LBL.getY(), fldWidth, fldHeight);
        showHideBTN.setBounds(psw1FLD.getX() + psw1FLD.getWidth() + 5, psw1FLD.getY(), BTNHeight, BTNHeight);
        checkpswLBL.setBounds(showHideBTN.getX() + showHideBTN.getWidth() + 10, psw1FLD.getY(), lblWidth * 2, lblHeight);
        checkpswLBL.setVisible(false);
        checkpswLBL.setForeground(Color.red);
        checkpswLBL.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        signupBTN.setBounds(nomeLBL.getX(), psw2LBL.getY() + psw2LBL.getHeight() + 20, BTNWidth, BTNHeight);
        backBTN.setBounds(signupBTN.getX() + signupBTN.getWidth() + 10, signupBTN.getY(), BTNWidth, BTNHeight);
        exitBTN.setBounds(backBTN.getX() + backBTN.getWidth() + 10, backBTN.getY(), BTNWidth, BTNHeight);


        showHideBTN.setFocusable(false);
        signupBTN.setFocusable(false);
        backBTN.setFocusable(false);
        exitBTN.setFocusable(false);
        showHideBTN.setFocusable(false);

        SwingUtilities.invokeLater(() -> nomeFLD.requestFocus());


        add(nomeLBL);
        add(nomeFLD);
        add(cognomeLBL);
        add(cognomeFLD);
        add(psw1LBL);
        add(psw1FLD);
        add(psw2LBL);
        add(psw2FLD);
        add(signupBTN);
        add(exitBTN);
        add(backBTN);
        add(checkpswLBL);
        add(showHideBTN);

        setVisible(true);

        JOptionPane pswInvalid = new JOptionPane();

        checkpswLBL.addMouseListener(new MouseListener() {

            final Font font = checkpswLBL.getFont();
            final Map attributes = font.getAttributes();

            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(getContentPane(), pswInvalid.getMessage());

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON); //COSTANTE UNDERLINE_ON =_0
                checkpswLBL.setFont(font.deriveFont(attributes));
            }

            @Override
            public void mouseExited(MouseEvent e) {

                attributes.put(TextAttribute.UNDERLINE, -1); //-1
                checkpswLBL.setFont(font.deriveFont(attributes));

            }
        });

        psw1FLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (String.valueOf(psw1FLD.getPassword()).isEmpty()) {
                    checkpswLBL.setVisible(false);

                } else {
                    try {
                        Account.checkValidPassword(String.valueOf(psw1FLD.getPassword()));
                        checkpswLBL.setVisible(false);
                    } catch (AccountException ex) {
                        checkpswLBL.setVisible(true);
                        pswInvalid.setMessage(ex.getMessage());
                    }
                }

                if (!checkpswLBL.isVisible() || String.valueOf(psw1FLD.getPassword()).isEmpty()) {
                    psw1FLD.setForeground(new JPasswordField().getForeground());
                    psw1FLD.setBorder(new JPasswordField().getBorder());

                } else {
                    psw1FLD.setForeground(Color.red);
                    psw1FLD.setBorder(BorderFactory.createLineBorder(Color.red));
                }
            }

        });

        psw2FLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Arrays.equals(psw1FLD.getPassword(), psw2FLD.getPassword()) || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
                    psw2FLD.setForeground(psw1FLD.getForeground());
                    psw2FLD.setBorder(psw1FLD.getBorder());
                } else {
                    psw2FLD.setForeground(Color.red);
                    psw2FLD.setBorder(BorderFactory.createLineBorder(Color.red));
                }
            }
        });

        showHideBTN.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                psw1FLD.setEchoChar('\u0000');  //Password Visibile
                try {
                    Image icon = ImageIO.read(new File(showPswIconPath));
                    showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                psw1FLD.setEchoChar('\u2022'); //Dot Echo Char
                try {
                    Image icon = ImageIO.read(new File(hidePswIconPath));
                    showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        signupBTN.addActionListener((ActionEvent e) -> {
            System.out.println("signup BTN pressed");

            if (nomeFLD.getText().isEmpty() || cognomeFLD.getText().isEmpty() || String.valueOf(psw1FLD.getPassword()).isEmpty() || String.valueOf(psw2FLD.getPassword()).isEmpty()) {
                JOptionPane.showInternalMessageDialog(getContentPane(), "Tutti i campi devono essere compilati");
            } else if (String.valueOf(psw1FLD.getPassword()).equals(String.valueOf(psw2FLD.getPassword()))) {
                int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to Sign Up?");
                if (choice == 0) {
                    try {
                        Bank.addAccount(new Account(nomeFLD.getText(), cognomeFLD.getText(), String.valueOf(psw1FLD.getPassword())));
                        loginPage();

                    } catch (AccountException ex) {
                        JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
                        psw1FLD.setText("");
                        psw2FLD.setText("");

                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
                        nomeFLD.setText("");
                        cognomeFLD.setText("");
                        psw1FLD.setText("");
                        psw2FLD.setText("");

                    } catch (NoSuchAlgorithmException ex) {
                        JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

                    } catch (SQLException ex) {
                        SQLExceptionOccurred(ex);

                    }

                }

            } else {
                System.out.println("Password non corrette");
                psw1FLD.setText("");
                psw2FLD.setText("");
                JOptionPane.showInternalMessageDialog(getContentPane(), "Password non corrette");
            }
        });

        backBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Back BTN pressed");
            loginPage();
        });

        exitBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Exit BTN pressed");
            int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to exit?");
            if (choice == 0) {
                dispose();
            }
        });


    }

    private void home() throws SQLException, TimeoutException {
        session.updateSessionCreation();
        getContentPane().removeAll();
        repaint();
        JPanel homePanel = new JPanel();
        setTitle("Home - JavaBank");
        add(homePanel);
        setVisible(true);

        JLabel nomeLBL = new JLabel("Nome");
        JTextField nomeFLD = new JTextField(session.getNome());
        JLabel cognomeLBL = new JLabel("Cognome");
        JTextField cognomeFLD = new JTextField(session.getCognome());
        JLabel ibanLBL = new JLabel("IBAN");
        JTextField ibanFLD = new JTextField(session.getIban());
        JLabel balanceLBL = new JLabel("Saldo");
        JPasswordField balanceFLD = new JPasswordField(€.format(session.getSaldo()));
        JButton showHideBTN = new JButton();
        JButton refreshBTN = new JButton();
        JLabel incomeLBL = new JLabel("Entrate");
        JLabel outcomeLBL = new JLabel("Uscite");
        JPasswordField incomeFLD = new JPasswordField();
        JPasswordField outcomeFLD = new JPasswordField();

        balanceFLD.setEchoChar('\u0000');
        incomeFLD.setEchoChar('\u0000');
        outcomeFLD.setEchoChar('\u0000');

//        JButton backBTN = new JButton("Back");
//        JButton exitBTN = new JButton("Exit");
        JButton bonificoBTN = new JButton("Bonifico");
        JButton depositBTN = new JButton("Deposito");
        JButton prelievoBTN = new JButton("Prelievo");
        JButton changePswBTN = new JButton("Change Psw");
        JButton logoutBTN = new JButton("Logout");
        JButton removeBTN = new JButton("Remove");

        nomeFLD.setEditable(false);
        cognomeFLD.setEditable(false);
        ibanFLD.setEditable(false);
        balanceFLD.setEditable(false);
        incomeFLD.setEditable(false);
        outcomeFLD.setEditable(false);

        nomeLBL.setBounds(10, 20, lblWidth, lblHeight);
        nomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeLBL.getY(), fldWidth, fldHeight);
        refreshBTN.setBounds(nomeFLD.getX() + nomeFLD.getWidth() + 5, nomeFLD.getY(), BTNHeight, BTNHeight);
        cognomeLBL.setBounds(nomeLBL.getX(), nomeLBL.getY() + nomeLBL.getHeight() + 5, lblWidth, lblHeight);
        cognomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeFLD.getY() + nomeFLD.getHeight() + 5, fldWidth, fldHeight);
        ibanLBL.setBounds(nomeLBL.getX(), cognomeLBL.getY() + cognomeLBL.getHeight() + 5, lblWidth, lblHeight);
        ibanFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, cognomeFLD.getY() + cognomeFLD.getHeight() + 5, fldWidth, fldHeight);
        balanceLBL.setBounds(nomeLBL.getX(), ibanLBL.getY() + ibanLBL.getHeight() + 5, lblWidth, lblHeight);
        balanceFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, ibanFLD.getY() + ibanFLD.getHeight() + 5, fldWidth, fldHeight);
        showHideBTN.setBounds(balanceFLD.getX() - (BTNHeight + 5), balanceFLD.getY(), BTNHeight, BTNHeight);
        incomeLBL.setBounds(nomeLBL.getX(), balanceLBL.getY() + balanceLBL.getHeight() + 5, lblWidth, lblHeight);
        incomeFLD.setBounds(incomeLBL.getX() + incomeLBL.getWidth() + 10, incomeLBL.getY(), fldWidth, fldHeight);
        outcomeLBL.setBounds(nomeLBL.getX(), incomeLBL.getY() + incomeLBL.getHeight() + 5, lblWidth, lblHeight);
        outcomeFLD.setBounds(outcomeLBL.getX() + outcomeLBL.getWidth() + 10, outcomeLBL.getY(), fldWidth, fldHeight);

        bonificoBTN.setBounds(350, 20, BTNWidth, BTNHeight);
        depositBTN.setBounds(bonificoBTN.getX(), bonificoBTN.getY() + bonificoBTN.getHeight() + 5, BTNWidth, BTNHeight);
        prelievoBTN.setBounds(bonificoBTN.getX(), depositBTN.getY() + depositBTN.getHeight() + 5, BTNWidth, BTNHeight);

        changePswBTN.setBounds(bonificoBTN.getX() + bonificoBTN.getWidth() + 20, bonificoBTN.getY(), BTNWidth, BTNHeight);
        logoutBTN.setBounds(changePswBTN.getX(), changePswBTN.getY() + changePswBTN.getHeight() + 5, BTNWidth, BTNHeight);
        removeBTN.setBounds(changePswBTN.getX(), logoutBTN.getY() + logoutBTN.getHeight() + 5, BTNWidth, BTNHeight);


        add(nomeLBL);
        add(nomeFLD);
        add(cognomeLBL);
        add(cognomeFLD);
        add(ibanLBL);
        add(ibanFLD);
        add(balanceLBL);
        add(balanceFLD);
        add(incomeLBL);
        add(incomeFLD);
        add(outcomeLBL);
        add(outcomeFLD);
        add(showHideBTN);
        add(refreshBTN);
        add(bonificoBTN);
        add(depositBTN);
        add(prelievoBTN);
        add(changePswBTN);
        add(logoutBTN);
        add(removeBTN);

        ArrayList<Transaction> transactions = null;
        try {
            transactions = session.showTransactions();
        } catch (TimeoutException e) {
            sessionExpired();
        }

        int num = (transactions != null) ? transactions.size() : 0;

        JTextArea textArea = new JTextArea((int) (num * 3.8), 1);

        textArea.setEnabled(false);
        textArea.setBackground(Color.gray.brighter());

        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVisible(num != 0);

        scrollPane.setBounds(outcomeLBL.getX(), outcomeLBL.getY() + outcomeLBL.getHeight() + 20, 565, 310);
        add(scrollPane);

        Locale.setDefault(Locale.ITALIAN);

        String iconpath = null;

        for (int i = 0; i < num; i++) {
            Transaction x = transactions.get(i);
            JLabel iconType = new JLabel();
            JLabel type = new JLabel(x.getType().toUpperCase());
            JLabel date = new JLabel();
            JLabel ibanFrom = new JLabel();
            JLabel ibanDest = new JLabel();
            JLabel userFrom = new JLabel();
            JLabel userDest = new JLabel();
            JLabel amount = new JLabel();
            String sgn = "";


            if (x.getType().equals("bonifico")) {

                if (x.getIbanFrom().equals(session.getIban())) {
                    iconpath = bonificoOutIconPath;
                    sgn = "- ";
                    amount.setForeground(Color.red);
                    ibanDest.setText("IBAN " + x.getIbanDest());
                    userDest.setText("BONIFICO ► " + x.getUsernameDest().toUpperCase().replace(".", " "));
                    textArea.add(ibanDest);
                    textArea.add(userDest);
                }

                if (x.getIbanDest().equals(session.getIban())) {
                    iconpath = bonificoInIconPath;
                    sgn = "+ ";
                    amount.setForeground(Color.green.darker());
                    ibanFrom.setText("IBAN " + x.getIbanFrom());
                    userFrom.setText("BONIFICO ◄ " + x.getUsernameFrom().toUpperCase().replace(".", " "));
                    textArea.add(ibanFrom);
                    textArea.add(userFrom);
                }


            } else {
                textArea.add(type);
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

            amount.setText(sgn + €.format(x.getAmount()));
            date.setText(sdf.format(x.getDate()));
//            ibanDest.setText(x.getIbanDest());
//            ibanFrom.setText(x.getIbanFrom());
//            userFrom.setText(x.getUsernameFrom());
//            userDest.setText(x.getUsernameDest());

            iconType.setBounds(10, 10 + 60 * i, 40, 40);
            iconType.setForeground(Color.red);
            amount.setBounds(390, iconType.getY(), 150, 25);
//            amount.setAlignmentX(JLabel.RIGHT);
//            amount.setBorder(BorderFactory.createLineBorder(Color.black));
            amount.setHorizontalAlignment(JLabel.RIGHT);
            date.setBounds(iconType.getX() + iconType.getWidth() + 10, iconType.getY() + 20, 180, 25);
            userFrom.setBounds(date.getX(), iconType.getY(), 230, 25);
            ibanFrom.setBounds(date.getX() + date.getWidth() + 20, date.getY(), 150, 25);
            userDest.setBounds(userFrom.getBounds());
            ibanDest.setBounds(ibanFrom.getBounds());
            type.setBounds(userFrom.getBounds());
//            ibanFrom.setBounds(date.getX(),type.getY(),150,25);
//            ibanFrom.setBounds(date.getX(),type.getY(),150,25);


            try {
                Image icon = ImageIO.read(new File(iconpath));
                iconType.setIcon(new ImageIcon(icon.getScaledInstance(iconType.getWidth(), iconType.getHeight(), Image.SCALE_SMOOTH)));

            } catch (IOException ex) {
                ex.printStackTrace();
            }

//            type.setEditable(false);
//            amount.setEditable(false);
//
//            date.setEditable(false);


            amount.setFont(new Font(amount.getFont().getName(), Font.BOLD, 20));
//            g.drawLine(0, type.getY() + type.getHeight(), 500, type.getY() + type.getHeight());
            textArea.add(iconType);
            textArea.add(amount);
            textArea.add(date);
//            JLabel line = new JLabel();
//            line.setBorder(BorderFactory.createLineBorder(Color.black));
//            line.setBounds(0,type.getY()+type.getHeight()+10,width,1);
//            textArea.add(line);

            JLabel line = new JLabel();
            line.setBorder(BorderFactory.createLineBorder(Color.black));
            line.setBounds(0, iconType.getY() - 11, width, 1);
            textArea.add(line);
        }


        try {
            Image icon = ImageIO.read(new File(hidePswIconPath));
            showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(showHideBTN.getWidth(), showHideBTN.getHeight(), Image.SCALE_SMOOTH)));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

//        try {
//            Image icon = ImageIO.read(new File(refreshIconPath));
//            refreshBTN.setIcon(new ImageIcon(icon.getScaledInstance(refreshBTN.getWidth(), refreshBTN.getHeight(), Image.SCALE_SMOOTH)));
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }

        URL iconUrl = this.getClass().getResource(refreshIconPath);
        System.out.println(iconUrl);
        Image icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
        refreshBTN.setIcon(new ImageIcon(icon.getScaledInstance(refreshBTN.getWidth(), refreshBTN.getHeight(), Image.SCALE_SMOOTH)));


        incomeFLD.setText(€.format(session.getIncomes()));
        outcomeFLD.setText(€.format(session.getOutcomes()));


        setVisible(true);

        showHideBTN.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (balanceFLD.getEchoChar() == '\u0000') {
                    balanceFLD.setEchoChar('x'); //Dot Echo Char
                    outcomeFLD.setEchoChar('x'); //Dot Echo Char
                    incomeFLD.setEchoChar('x'); //Dot Echo Char
                    scrollPane.setVisible(false);

                    try {
                        Image icon = ImageIO.read(new File(hidePswIconPath));
                        showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                } else {
                    balanceFLD.setEchoChar('\u0000');  //Password Visibile
                    incomeFLD.setEchoChar('\u0000');  //Password Visibile
                    outcomeFLD.setEchoChar('\u0000');  //Password Visibile
                    scrollPane.setVisible(true);

                    try {
                        Image icon = ImageIO.read(new File(showPswIconPath));
                        showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
//                balanceFLD.setEchoChar('\u0000');  //Password Visibile
//                try {
//                    Image icon = ImageIO.read(new File(showpswpath));
//                    showHideBTN.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));
//
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }

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
                bonifico();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        refreshBTN.addActionListener((ActionEvent e) -> {

            try {
//                incomeFLD.setText(€.format(session.getIncomes()));
//                outcomeFLD.setText(€.format(session.getOutcomes()));
//                balanceFLD.setText(€.format(session.getSaldo()));

                home();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        removeBTN.addActionListener((ActionEvent e) -> {

            try {
                deleteAccount();
            } catch (TimeoutException ex) {
                ex.printStackTrace();
            }


        });

        depositBTN.addActionListener((ActionEvent e) -> {
            try {
                deposit();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        prelievoBTN.addActionListener((ActionEvent e) -> {
            try {
                prelievo();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

//        backBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Back BTN pressed");
//
//            new LoginPage;
//        });
//
//        exitBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Exit BTN pressed");
//            dispose();
//        });

        logoutBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Exit BTN pressed");
            loginPage();
        });

        changePswBTN.addActionListener((ActionEvent e) -> {

            try {
                changePassword();
            } catch (TimeoutException ex) {
                sessionExpired();
            }

        });


    }

    private void changePassword() throws TimeoutException {
        session.updateSessionCreation();
        getContentPane().removeAll();
        repaint();
        JPanel pswPanel = new JPanel();
        setTitle("Change Password - JavaBank");
        add(pswPanel);

        JLabel pswLBL = new JLabel("Attuale");
        JPasswordField pswFLD = new JPasswordField();

        JLabel newPswLBL = new JLabel("Nuova");
        JPasswordField newPswFLD = new JPasswordField();

        JLabel chechNewPswLBL = new JLabel("Conferma");
        JPasswordField checkNewPswFLD = new JPasswordField();

        JLabel checkpswLBL = new JLabel("Invalid Password");
        JButton showHideBTN1 = new JButton();
        JButton showHideBTN2 = new JButton();

        JButton saveBTN = new JButton("Save");
        JButton backBTN = new JButton("Back");
        JButton logoutBTN = new JButton("Logout");
        getRootPane().setDefaultButton(saveBTN);

        try {
            Image icon = ImageIO.read(new File(hidePswIconPath));
            showHideBTN1.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));
            showHideBTN2.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

        } catch (IOException e) {
            e.printStackTrace();
        }

        pswLBL.setBounds(10, 20, lblWidth, lblHeight);
        pswFLD.setBounds(pswLBL.getX() + pswLBL.getWidth() + 10, pswLBL.getY(), fldWidth, fldHeight);
        newPswLBL.setBounds(pswLBL.getX(), pswLBL.getY() + pswLBL.getHeight() + 5, lblWidth, lblHeight);
        newPswFLD.setBounds(newPswLBL.getX() + newPswLBL.getWidth() + 10, newPswLBL.getY(), fldWidth, fldHeight);
        chechNewPswLBL.setBounds(pswLBL.getX(), newPswFLD.getY() + newPswFLD.getHeight() + 5, lblWidth, lblHeight);
        checkNewPswFLD.setBounds(chechNewPswLBL.getX() + chechNewPswLBL.getWidth() + 10, chechNewPswLBL.getY(), fldWidth, fldHeight);
        showHideBTN1.setBounds(newPswFLD.getX() + newPswFLD.getWidth() + 5, newPswFLD.getY(), BTNHeight, BTNHeight);
        showHideBTN2.setBounds(checkNewPswFLD.getX() + checkNewPswFLD.getWidth() + 5, checkNewPswFLD.getY(), BTNHeight, BTNHeight);
        checkpswLBL.setBounds(showHideBTN1.getX() + showHideBTN1.getWidth() + 5, newPswFLD.getY(), 100, lblHeight);
        checkpswLBL.setVisible(false);
        checkpswLBL.setForeground(Color.red);

        saveBTN.setBounds(pswLBL.getX(), 150, BTNWidth, BTNHeight);
        backBTN.setBounds(saveBTN.getX() + saveBTN.getWidth() + 10, saveBTN.getY(), BTNWidth, BTNHeight);
        logoutBTN.setBounds(backBTN.getX() + backBTN.getWidth() + 10, backBTN.getY(), BTNWidth, BTNHeight);


        newPswFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!String.valueOf(newPswFLD.getPassword()).isEmpty())
                    try {
                        Session.checkValidPassword(String.valueOf(newPswFLD.getPassword()));
                        checkpswLBL.setVisible(false);
                    } catch (AccountException ex) {
                        checkpswLBL.setVisible(true);
                    }
            }

        });

        showHideBTN1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                newPswFLD.setEchoChar('\u0000');  //Password Visibile
                try {
                    Image icon = ImageIO.read(new File(showPswIconPath));
                    showHideBTN1.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                newPswFLD.setEchoChar('\u2022'); //Dot Echo Char
                try {
                    Image icon = ImageIO.read(new File(hidePswIconPath));
                    showHideBTN1.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        showHideBTN2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                checkNewPswFLD.setEchoChar('\u0000');  //Password Visibile
                try {
                    Image icon = ImageIO.read(new File(showPswIconPath));
                    showHideBTN2.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                checkNewPswFLD.setEchoChar('\u2022'); //Dot Echo Char
                try {
                    Image icon = ImageIO.read(new File(hidePswIconPath));
                    showHideBTN2.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        saveBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Save BTN pressed");
            try {
                session.changePassword(String.valueOf(pswFLD.getPassword()), String.valueOf(newPswFLD.getPassword()), String.valueOf(checkNewPswFLD.getPassword()));
                JOptionPane.showInternalMessageDialog(getContentPane(), "Modifica effettuata correttamente");
                home();

            } catch (IllegalArgumentException ex) {
                newPswFLD.setText("");
                checkNewPswFLD.setText("");
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            } catch (AccountException | NoSuchAlgorithmException ex) {
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            } catch (AuthenticationException ex) {
                pswFLD.setText("");
                newPswFLD.setText("");
                checkNewPswFLD.setText("");
                JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

            } catch (TimeoutException ex) {
                sessionExpired();

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);

            }
        });

        backBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Back BTN pressed");
            try {
                home();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            } catch (TimeoutException ex) {
                sessionExpired();
            }
        });

        logoutBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Exit BTN pressed");
            loginPage();
        });


        add(pswLBL);
        add(pswFLD);
        add(newPswLBL);
        add(newPswFLD);
        add(chechNewPswLBL);
        add(checkNewPswFLD);
        add(logoutBTN);
        add(backBTN);
        add(saveBTN);
        add(showHideBTN1);
        add(showHideBTN2);
        add(checkpswLBL);

        setVisible(true);

    }

    private void deleteAccount() throws TimeoutException {
        session.updateSessionCreation();
        getContentPane().removeAll();
        repaint();
        JPanel deletePanel = new JPanel();
        setTitle("Delete Account - JavaBank");
        add(deletePanel);


        JLabel pswLBL = new JLabel("Password");
        JPasswordField pswFLD = new JPasswordField();

        JLabel confirmPswLBL = new JLabel("Confirm");
        JPasswordField confirmPswFLD = new JPasswordField();

        JLabel checkpswLBL = new JLabel("Invalid Password");
        JButton showHideBTN1 = new JButton();
        JButton showHideBTN2 = new JButton();

        JButton deleteBTN = new JButton("Delete");
        JButton backBTN = new JButton("Back");
        JButton logoutBTN = new JButton("Logout");
        getRootPane().setDefaultButton(deleteBTN);

        try {
            Image icon = ImageIO.read(new File(hidePswIconPath));
            showHideBTN1.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
            showHideBTN2.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

        } catch (IOException e) {
            e.printStackTrace();
        }


        pswLBL.setBounds(10, 20, lblWidth, lblHeight);
        pswFLD.setBounds(pswLBL.getX() + pswLBL.getWidth() + 10, pswLBL.getY(), fldWidth, fldHeight);
        confirmPswLBL.setBounds(pswLBL.getX(), pswFLD.getY() + pswFLD.getHeight() + 5, lblWidth, lblHeight);
        confirmPswFLD.setBounds(confirmPswLBL.getX() + confirmPswLBL.getWidth() + 10, confirmPswLBL.getY(), fldWidth, fldHeight);
        showHideBTN1.setBounds(pswFLD.getX() + pswFLD.getWidth() + 5, pswFLD.getY(), BTNHeight, BTNHeight);
        showHideBTN2.setBounds(confirmPswFLD.getX() + confirmPswFLD.getWidth() + 5, confirmPswFLD.getY(), BTNHeight, BTNHeight);
        showHideBTN2.setMargin(new Insets(0, 0, 0, 0));
        checkpswLBL.setBounds(showHideBTN1.getX() + showHideBTN1.getWidth() + 5, pswFLD.getY(), 100, lblHeight);
        checkpswLBL.setVisible(false);
        checkpswLBL.setForeground(Color.red);

        deleteBTN.setBounds(pswLBL.getX(), confirmPswFLD.getY() + confirmPswFLD.getHeight() + 10, BTNWidth, BTNHeight);
        backBTN.setBounds(deleteBTN.getX() + deleteBTN.getWidth() + 10, deleteBTN.getY(), BTNWidth, BTNHeight);
        logoutBTN.setBounds(backBTN.getX() + backBTN.getWidth() + 10, backBTN.getY(), BTNWidth, BTNHeight);


//        pswFLD.addKeyListener(new KeyListener() {
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
//                if (!String.valueOf(pswFLD.getPassword()).isEmpty())
//                    try {
//                        session.checkValidPassword(String.valueOf(pswFLD.getPassword()));
//                        checkpswLBL.setVisible(false);
//                    } catch (AccountException ex) {
//                        checkpswLBL.setVisible(true);
//                    }
//            }
//
//        });

        confirmPswFLD.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Arrays.equals(pswFLD.getPassword(), confirmPswFLD.getPassword()) || String.valueOf(confirmPswFLD.getPassword()).isEmpty()) {
                    confirmPswFLD.setForeground(new JPasswordField().getForeground());
                    confirmPswFLD.setBorder(new JPasswordField().getBorder());
                    pswFLD.setForeground(new JPasswordField().getForeground());
                    pswFLD.setBorder(new JPasswordField().getBorder());
                } else {
                    confirmPswFLD.setForeground(Color.red);
                    confirmPswFLD.setBorder(BorderFactory.createLineBorder(Color.red));

                    if (pswFLD.getPassword().length == confirmPswFLD.getPassword().length) {
                        pswFLD.setForeground(Color.red);
                        pswFLD.setBorder(BorderFactory.createLineBorder(Color.red));
                    }
                }
            }
        });


        showHideBTN1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                pswFLD.setEchoChar('\u0000');  //Password Visibile
                try {
                    Image icon = ImageIO.read(new File(showPswIconPath));
                    showHideBTN1.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pswFLD.setEchoChar('\u2022'); //Dot Echo Char
                try {
                    Image icon = ImageIO.read(new File(hidePswIconPath));
                    showHideBTN1.setIcon(new ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        showHideBTN2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                confirmPswFLD.setEchoChar('\u0000');  //Password Visibile
                try {
                    Image icon = ImageIO.read(new File(showPswIconPath));
                    showHideBTN2.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                confirmPswFLD.setEchoChar('\u2022'); //Dot Echo Char
                try {
                    Image icon = ImageIO.read(new File(hidePswIconPath));
                    showHideBTN2.setIcon(new ImageIcon(icon.getScaledInstance(BTNHeight, BTNHeight, Image.SCALE_SMOOTH)));

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


        deleteBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Save BTN pressed");
            int choice = JOptionPane.showConfirmDialog(getContentPane(), "Sei sicuro di voler eliminare il tuo account?");
            if (choice == 0) {

                try {
                    session.deleteAccount(String.valueOf(pswFLD.getPassword()), String.valueOf(confirmPswFLD.getPassword()));
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Account Eliminato");
                    loginPage();

                } catch (IllegalArgumentException ex) {
                    System.out.println(ex);
                    pswFLD.setText("");
                    confirmPswFLD.setText("");
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());


                } catch (AuthenticationException ex) {
                    System.out.println(ex);
                    pswFLD.setText("");
                    pswFLD.setText("");
                    confirmPswFLD.setText("");
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());

                } catch (TimeoutException ex) {
                    System.out.println(ex);
                    sessionExpired();

                } catch (SQLException ex) {
                    System.out.println(ex);
                    SQLExceptionOccurred(ex);

                } catch (NoSuchAlgorithmException ex) {
                    JOptionPane.showInternalMessageDialog(getContentPane(), ex.getMessage());
                }
            }
        });

        backBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Back BTN pressed");
            try {
                home();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            } catch (TimeoutException ex) {
                sessionExpired();
            }
        });

        logoutBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Exit BTN pressed");
            loginPage();
        });


        add(pswLBL);
        add(pswFLD);
        add(pswLBL);
        add(pswFLD);
        add(confirmPswLBL);
        add(confirmPswFLD);
        add(logoutBTN);
        add(backBTN);
        add(deleteBTN);
//        add(showHideBTN1);
//        add(showHideBTN2);
        add(checkpswLBL);

        setVisible(true);

    }

    private void bonifico() throws TimeoutException, SQLException {
        session.updateSessionCreation();
        getContentPane().removeAll();
        repaint();

//        JPanel bonificoPanel = new JPanel();
        setTitle("Bonifico - JavaBank");
//        add(bonificoPanel);

        JLabel ibanLBL = new JLabel("IBAN");
        JTextField ibanFLD = new JTextField(30);
        JLabel amountLBL = new JLabel("Importo");
        JTextField amountFLD = new JTextField(30);

        JLabel balabceLBL = new JLabel("Saldo");
        JLabel balance = new JLabel(€.format(session.getSaldo()));
        balabceLBL.setVisible(true);
        balance.setVisible(true);
        balance.setFont(new Font(balance.getFont().getName(), Font.BOLD, 20));


        JButton contactsBTN = new JButton("Rubrica");
        JButton logoutBTN = new JButton("Logout");
        JButton transferBTN = new JButton("Transfer");
        JButton balanceBTN = new JButton();
        JButton homeBTN = new JButton("Home");
        getRootPane().setDefaultButton(transferBTN);
        if (balance.isVisible()) {
            balanceBTN.setText("Nascondi Saldo");
        } else {
            balanceBTN.setText("Mostra Saldo");
        }

        ibanLBL.setBounds(10, 20, lblWidth, lblHeight);
        ibanFLD.setBounds(ibanLBL.getX() + ibanLBL.getWidth() + 10, ibanLBL.getY(), fldWidth, fldHeight);
        amountLBL.setBounds(ibanLBL.getX(), ibanFLD.getY() + ibanFLD.getHeight() + 5, lblWidth, lblHeight);
        amountFLD.setBounds(amountLBL.getX() + amountLBL.getWidth() + 10, amountLBL.getY(), fldWidth, fldHeight);

        transferBTN.setBounds(ibanLBL.getX(), amountFLD.getY() + amountFLD.getHeight() + 20, BTNWidth, BTNHeight);
        contactsBTN.setBounds(transferBTN.getX() + transferBTN.getWidth() + 10, transferBTN.getY(), 90, BTNHeight);
        balanceBTN.setBounds(contactsBTN.getX() + contactsBTN.getWidth() + 10, transferBTN.getY(), 150, BTNHeight);
        logoutBTN.setBounds(balanceBTN.getX() + balanceBTN.getWidth() + 10, transferBTN.getY(), BTNWidth, BTNHeight);
        homeBTN.setBounds(logoutBTN.getX(), ibanLBL.getY(), BTNWidth, BTNHeight);
        balabceLBL.setBounds(ibanFLD.getX() + ibanFLD.getWidth() + 10, ibanFLD.getY(), BTNWidth, BTNHeight);
        balance.setBounds(balabceLBL.getX(), balabceLBL.getY() + balabceLBL.getHeight() + 10, 300, BTNHeight);


        contactsBTN.setFocusable(false);
        logoutBTN.setFocusable(false);
        transferBTN.setFocusable(false);
        balanceBTN.setFocusable(false);
        homeBTN.setFocusable(false);

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        add(amountFLD);
        add(amountLBL);
        add(ibanLBL);
        add(ibanFLD);
        add(contactsBTN);
        add(logoutBTN);
        add(transferBTN);
        add(balabceLBL);
        add(balance);
        add(balanceBTN);
        add(homeBTN);

        //Contact table
        JLabel searchLBL = new JLabel("Rubrica");
        JTextField searchFLD = new JTextField();
        JButton searchBTN = new JButton("Search");
        JButton clearBTN = new JButton("Clear");

        JLabel nomeLBL = new JLabel("Nome");
        JTextField nomeFLD = new JTextField();
        JLabel cognomeLBL = new JLabel("Cognome");
        JTextField cognomeFLD = new JTextField();
        JLabel ibanContactLBL = new JLabel("IBAN");
        JTextField ibanContactFLD = new JTextField();

        JButton prevBTN = new JButton();
        JButton nextBTN = new JButton();
        JLabel results = new JLabel("# of #");

        nomeFLD.setEditable(false);
        cognomeFLD.setEditable(false);
        ibanContactFLD.setEditable(false);

        searchLBL.setBounds(ibanLBL.getX(), transferBTN.getY() + transferBTN.getHeight() + 100, lblWidth, lblHeight);
        searchFLD.setBounds(searchLBL.getX() + searchLBL.getWidth() + 10, searchLBL.getY(), fldWidth, fldHeight);
        searchBTN.setBounds(searchFLD.getX() + searchFLD.getWidth() + 10, searchFLD.getY(), BTNWidth, BTNHeight);
        clearBTN.setBounds(searchBTN.getX() + searchBTN.getWidth() + 10, searchFLD.getY(), BTNWidth, BTNHeight);
        nomeLBL.setBounds(searchLBL.getX(), searchLBL.getY() + searchLBL.getHeight() + 10, lblWidth, lblHeight);
        nomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeLBL.getY(), fldWidth, fldHeight);
        cognomeLBL.setBounds(nomeLBL.getX(), nomeLBL.getY() + nomeLBL.getHeight() + 5, lblWidth, lblHeight);
        cognomeFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, nomeFLD.getY() + nomeFLD.getHeight() + 5, fldWidth, fldHeight);
        ibanContactLBL.setBounds(nomeLBL.getX(), cognomeLBL.getY() + cognomeLBL.getHeight() + 5, lblWidth, lblHeight);
        ibanContactFLD.setBounds(nomeLBL.getX() + nomeLBL.getWidth() + 10, cognomeFLD.getY() + cognomeFLD.getHeight() + 5, fldWidth, fldHeight);

        prevBTN.setBounds(nomeLBL.getX() + 80, ibanContactFLD.getY() + ibanContactFLD.getHeight() + 20, 40, 40);
        results.setBounds(prevBTN.getX() + prevBTN.getWidth() + 5, prevBTN.getY() + 5, BTNWidth, BTNHeight);
        results.setHorizontalAlignment(SwingConstants.CENTER);
        nextBTN.setBounds(results.getX() + results.getWidth() + 5, ibanContactFLD.getY() + ibanContactFLD.getHeight() + 20, 40, 40);


        add(searchLBL);
        add(searchFLD);
        add(searchBTN);
        add(clearBTN);
        add(nomeLBL);
        add(nomeFLD);
        add(cognomeLBL);
        add(cognomeFLD);
        add(ibanContactLBL);
        add(ibanContactFLD);
        add(prevBTN);
        add(nextBTN);
        add(results);

        searchBTN.setVisible(false);
        searchFLD.setVisible(false);
        searchLBL.setVisible(false);
        clearBTN.setVisible(false);
        nomeLBL.setVisible(false);
        nomeFLD.setVisible(false);
        cognomeLBL.setVisible(false);
        cognomeFLD.setVisible(false);
        ibanContactLBL.setVisible(false);
        ibanContactFLD.setVisible(false);
        prevBTN.setVisible(false);
        results.setVisible(false);
        nextBTN.setVisible(false);

        searchBTN.setFocusable(false);
        searchFLD.setFocusable(false);
        searchLBL.setFocusable(false);
        clearBTN.setFocusable(false);
        nomeLBL.setFocusable(false);
        nomeFLD.setFocusable(false);
        cognomeLBL.setFocusable(false);
        cognomeFLD.setFocusable(false);
        ibanContactLBL.setFocusable(false);
        prevBTN.setFocusable(false);
        results.setFocusable(false);
        nextBTN.setFocusable(false);

        try {
            Image icon = ImageIO.read(new File(nextIconPath));
            nextBTN.setIcon(new ImageIcon(icon.getScaledInstance(nextBTN.getWidth() - 5, nextBTN.getHeight() - 5, Image.SCALE_SMOOTH)));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            Image icon = ImageIO.read(new File(prevIconPath));
            prevBTN.setIcon(new ImageIcon(icon.getScaledInstance(prevBTN.getWidth() - 5, prevBTN.getHeight() - 5, Image.SCALE_SMOOTH)));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        setVisible(true);

        var contacts = new Object() {
            int index = 0;
            ArrayList<Account> list;
        };

        contactsBTN.addActionListener((ActionEvent e) -> {

            try {
                contacts.list = session.showContacts();

            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);

            } catch (TimeoutException ex) {
                sessionExpired();
            }

            boolean flag = !nomeFLD.isVisible();


            searchBTN.setVisible(flag);
            searchFLD.setVisible(flag);
            searchLBL.setVisible(flag);
            clearBTN.setVisible(flag);
            nomeLBL.setVisible(flag);
            nomeFLD.setVisible(flag);
            cognomeLBL.setVisible(flag);
            cognomeFLD.setVisible(flag);
            ibanContactLBL.setVisible(flag);
            ibanContactFLD.setVisible(flag);
            prevBTN.setVisible(flag);
            results.setVisible(flag);
            nextBTN.setVisible(flag);
            searchFLD.setFocusable(true);

            nomeFLD.setText(contacts.list.get(contacts.index).getNome());
            cognomeFLD.setText(contacts.list.get(contacts.index).getCognome());
            ibanContactFLD.setText(contacts.list.get(contacts.index).getIban());
            ibanFLD.setText(flag ? ibanContactFLD.getText() : "");
            results.setText(contacts.index + 1 + " of " + contacts.list.size());

            repaint();


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


        });

        clearBTN.addActionListener((ActionEvent e) -> {
            searchFLD.setText("");
//            searchBTN.doClick();
        });

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                balance.setText(€.format(session.getSaldo()));

                if (balance.isVisible()) {
                    balanceBTN.setText("Mostra Saldo");
                    balabceLBL.setVisible(false);
                    balance.setVisible(false);

                } else {
                    balanceBTN.setText("Nascondi Saldo");
                    balabceLBL.setVisible(true);
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
//                    amount = Double.parseDouble(amountFLD.getText());
                    if (ibanFLD.getText().isEmpty()) {
                        JOptionPane.showInternalMessageDialog(getContentPane(), "Iban field can't be empty");
                    } else {
                        session.transfer(ibanFLD.getText().toUpperCase(), amount);
                        amountFLD.setText("");
                        balance.setText(€.format(session.getSaldo()));

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

        homeBTN.addActionListener((ActionEvent e) -> {
            try {
//                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                home();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        logoutBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Logout BTN pressed");

            loginPage();
        });


    }

    private void deposit() throws TimeoutException, SQLException {
        session.updateSessionCreation();
        getContentPane().removeAll();
        repaint();

        JPanel depositPanel = new JPanel();
        setTitle("Deposit - JavaBank");
        add(depositPanel);

        JLabel amountLBL = new JLabel("Importo");
        JTextField amountFLD = new JTextField(30);

        JLabel balabceLBL = new JLabel("Saldo");
        JLabel balance = new JLabel(€.format(session.getSaldo()));
        balabceLBL.setVisible(true);
        balance.setVisible(true);
        balance.setFont(new Font(balance.getFont().getName(), Font.BOLD, 20));


//        JButton contactsBTN = new JButton("Contacts");
        JButton logoutBTN = new JButton("Logout");
        JButton depositBTN = new JButton("Deposita");
        JButton balanceBTN = new JButton();
        JButton homeBTN = new JButton("Account");
        getRootPane().setDefaultButton(depositBTN);

        amountLBL.setBounds(10, 20, lblWidth, lblHeight);
        amountFLD.setBounds(amountLBL.getX() + amountLBL.getWidth() + 10, amountLBL.getY(), fldWidth, fldHeight);
        balabceLBL.setBounds(amountFLD.getX() + amountFLD.getWidth() + 10, amountLBL.getY(), lblWidth, lblHeight);
        balance.setBounds(balabceLBL.getX(), balabceLBL.getY() + balabceLBL.getHeight() + 5, 300, lblHeight);
        depositBTN.setBounds(amountLBL.getX(), balance.getY() + balance.getHeight() + 10, BTNWidth, BTNHeight);
        balanceBTN.setBounds(depositBTN.getX() + depositBTN.getWidth() + 10, depositBTN.getY(), 150, BTNHeight);
        logoutBTN.setBounds(balanceBTN.getX() + balanceBTN.getWidth() + 10, balanceBTN.getY(), BTNWidth, BTNHeight);
        homeBTN.setBounds(370, amountLBL.getY(), BTNWidth, BTNHeight);


        depositBTN.setFocusable(false);
        balanceBTN.setFocusable(false);
        logoutBTN.setFocusable(false);
        homeBTN.setFocusable(false);

        if (balance.isVisible()) {
            balanceBTN.setText("Nascondi Saldo");
        } else {
            balanceBTN.setText("Mostra Saldo");
        }

        SwingUtilities.invokeLater(amountFLD::requestFocus);


        logoutBTN.addActionListener((ActionEvent e) -> {
//            System.out.println("Logout BTN pressed");
            loginPage();
        });

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                balance.setText(€.format(session.getSaldo()));

                if (balance.isVisible()) {

                    balanceBTN.setText("Mostra Saldo");
                    balabceLBL.setVisible(false);
                    balance.setVisible(false);

                } else {

                    balanceBTN.setText("Nascondi Saldo");
                    balabceLBL.setVisible(true);
                    balance.setVisible(true);

                }

            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }


        });

        homeBTN.addActionListener((ActionEvent e) -> {
            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                home();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

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
                    balance.setText(€.format(session.getSaldo()));
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

        add(amountFLD);
        add(amountLBL);
        add(logoutBTN);
        add(depositBTN);
        add(balabceLBL);
        add(balance);
        add(balanceBTN);
        add(homeBTN);

        setVisible(true);
    }

    private void prelievo() throws TimeoutException, SQLException {
        session.updateSessionCreation();
        getContentPane().removeAll();
        repaint();

        JPanel depositPanel = new JPanel();
        setTitle("prelievo - JavaBank");
        add(depositPanel);

        JLabel amountLBL = new JLabel("Importo");
        JTextField amountFLD = new JTextField(30);

        JLabel balabceLBL = new JLabel("Saldo");
        JLabel balance = new JLabel(€.format(session.getSaldo()));
        balabceLBL.setVisible(true);
        balance.setVisible(true);
        balance.setFont(new Font(balance.getFont().getName(), Font.BOLD, 20));

//      JButton contactsBTN = new JButton("Contacts");
        JButton logoutBTN = new JButton("Logout");
        JButton prelievoBTN = new JButton("Preleva");
        JButton balanceBTN = new JButton();
        JButton homeBTN = new JButton("Home");
        getRootPane().setDefaultButton(prelievoBTN);
        if (balance.isVisible()) {
            balanceBTN.setText("Nascondi Saldo");
        } else {
            balanceBTN.setText("Mostra Saldo");
        }

        amountLBL.setBounds(10, 20, lblWidth, lblHeight);
        amountFLD.setBounds(amountLBL.getX() + amountLBL.getWidth() + 10, amountLBL.getY(), fldWidth, fldHeight);
        balabceLBL.setBounds(amountFLD.getX() + amountFLD.getWidth() + 10, amountLBL.getY(), lblWidth, lblHeight);
        balance.setBounds(balabceLBL.getX(), balabceLBL.getY() + balabceLBL.getHeight() + 5, 300, lblHeight);
        prelievoBTN.setBounds(amountLBL.getX(), balance.getY() + balance.getHeight() + 10, BTNWidth, BTNHeight);
        balanceBTN.setBounds(prelievoBTN.getX() + prelievoBTN.getWidth() + 10, prelievoBTN.getY(), 150, BTNHeight);
        logoutBTN.setBounds(balanceBTN.getX() + balanceBTN.getWidth() + 10, balanceBTN.getY(), BTNWidth, BTNHeight);
        homeBTN.setBounds(370, amountLBL.getY(), BTNWidth, BTNHeight);


        prelievoBTN.setFocusable(false);
        balanceBTN.setFocusable(false);
        logoutBTN.setFocusable(false);
        homeBTN.setFocusable(false);

        SwingUtilities.invokeLater(amountFLD::requestFocus);

        prelievoBTN.addActionListener((ActionEvent e) -> {
            try {
                session.updateSessionCreation();
                if (amountFLD.getText().isEmpty())
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Amount field can't be empty");
                else {
                    session.prelievo(session.validateAmount(amountFLD.getText()));
                    amountFLD.setText("");
                    balance.setText(€.format(session.getSaldo()));
                    JOptionPane.showInternalMessageDialog(getContentPane(), "Prelievo Effettuato Correttamente");
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

        logoutBTN.addActionListener((ActionEvent e) -> {
            System.out.println("Logout BTN pressed");
            loginPage();
        });

        balanceBTN.addActionListener((ActionEvent e) -> {

            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                balance.setText(€.format(session.getSaldo()));

                if (balance.isVisible()) {
                    balanceBTN.setText("Mostra Saldo");
                    balabceLBL.setVisible(false);
                    balance.setVisible(false);

                } else {
                    balanceBTN.setText("Nascondi Saldo");
                    balabceLBL.setVisible(true);
                    balance.setVisible(true);

                }

            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }


        });

        homeBTN.addActionListener((ActionEvent e) -> {
            try {
                session.updateSessionCreation();
//                session.accountBalanceUpdate();
                home();
            } catch (TimeoutException ex) {
                sessionExpired();
            } catch (SQLException ex) {
                SQLExceptionOccurred(ex);
            }

        });

        add(amountFLD);
        add(amountLBL);
        add(logoutBTN);
        add(prelievoBTN);
        add(balabceLBL);
        add(balance);
        add(balanceBTN);
        add(homeBTN);

        setVisible(true);
    }


    //Functions
    private void sessionExpired() {
        JOptionPane.showInternalMessageDialog(getContentPane(), "Sessione Scaduta");
        loginPage();
    }

    private void SQLExceptionOccurred(SQLException ex) {
        String error = "SQL Error\n" +
                "SQL State : " + ex.getSQLState() +
                "ErrorCode : " + ex.getErrorCode() +
                ex.getMessage();
        ex.printStackTrace();
        System.out.println(ex);
        JOptionPane.showMessageDialog(getContentPane(), error);
        loginPage();
    }

}
