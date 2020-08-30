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


public class MainApp extends JFrame {
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

    protected final DecimalFormat euro = new DecimalFormat("0.00 €");
    protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    protected String user;
    protected String psw;
    protected char echochar = '*';

    public MainApp(String user, String psw) {
        this.user = user;
        this.psw = psw;
        new LoginForm();
    }

    public MainApp() {
        System.out.println("new MainApp()");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("ninbus not avaiable");
        }


        String fontName = "Times New Roman";
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Lucida Console", Font.PLAIN, 16)));

        setFrameIcon(bankIconPath);
        addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {              // CHIUDE LA CONNESSIONE PRIMA DI CHIUDERE LA FINESTRA
                try {
                    DBConnect.close();
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }

            @Override
            public void windowActivated(WindowEvent e) {            //CONTROLLA LA VALIDITA' DELLA SESSIONE QUANDO LA FINESTRA SI RIATTIVA
                System.out.println("gain focus");
                if (session != null) try {
                    session.updateSessionCreation();
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



    //Functions
    protected void sessionExpired() {
        String error_message = "Sei rimasto inattivo per troppo tempo.\n" +
                     "Verrai reindirizzato alla schermata di Login.";

        String title = "Sessione Scaduta";
        dispose();
        JOptionPane.showMessageDialog(getContentPane(),error_message, title,JOptionPane.ERROR_MESSAGE);
        session = null;
        new LoginForm();

    }

    protected void SQLExceptionOccurred(SQLException ex) {
        String error = "SQL State : " + ex.getSQLState() + "\n" +
                       "ErrorCode : " + ex.getErrorCode() + "\n" +
                        ex.getMessage();
        ex.printStackTrace();
        JOptionPane.showMessageDialog(getContentPane(),error,"SQL Error",JOptionPane.ERROR_MESSAGE);

        dispose();
        new LoginForm();

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


    protected void exitAction(ActionEvent e) {
        System.out.println("Exit BTN pressed");
        int choice = JOptionPane.showConfirmDialog(getContentPane(), "Do you want to exit?");
        if (choice == 0) {
            session = null;
            dispose();
        }
    }

    protected void logOutAction(ActionEvent e) {

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
