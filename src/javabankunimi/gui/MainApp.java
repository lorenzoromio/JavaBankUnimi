/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Session;
import javabankunimi.database.DBConnect;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public abstract class MainApp extends JFrame {
    protected static Point location;
    protected static Session session;
    protected final Timer timer = new Timer();
    protected final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
    protected final DecimalFormat euro = new DecimalFormat("0.00 €");
    protected final String bankIconPath = "icons/bank.png";
    protected final String nextIconPath = "icons/next.png";
    protected final String prevIconPath = "icons/prev.png";
    protected final String moneyIconPath = "icons/money.png";
    protected final String signUpIconPath = "icons/signUp.png";
    protected final String showPswIconPath = "icons/showpsw.png";
    protected final String hidePswIconPath = "icons/hidepsw.png";
    protected final String refreshIconPath = "icons/refresh.png";
    protected final String depositoIconPath = "icons/deposito2.png";
    protected final String prelievoIconPath = "icons/prelievo2.png";
    protected final String changePswIconPath = "icons/changePsw.png";
    protected final String bonificoInIconPath = "icons/bonificoIn2.png";
    protected final String bonificoOutIconPath = "icons/bonificoOut2.png";
    protected final String deleteAccountIconPath = "icons/deleteAccount.png";
    protected final char echochar = '*';

    public MainApp() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFrameIcon(bankIconPath);
        System.out.println("Location: " + location);

        Locale.setDefault(Locale.ITALIAN);
        UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Lucida Console", Font.PLAIN, 16)));

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Ninbus not avaiable");
        }


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {              // CLOSE CONNECTION BEFORE CLOSING WINDOW
                try {
                    DBConnect.close();
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }

            @Override
            public void windowActivated(WindowEvent e) {            //UPDATE SESSION CREATION WHEN USER FOCUS WINDOW
                System.out.println("gain focus");

                if (session != null) {
                    session.updateSessionCreation();
                }
            }

            @Override
            public void windowDeactivated(WindowEvent e) {          //CLOSE CONNECTION WHEN WINDOW DEACTIVATE
                try {
                    System.out.println("lost focus");
                    DBConnect.close();
                } catch (SQLException ex) {
                    SQLExceptionOccurred(ex);
                }
            }
        });

        backgroundCheckValidSession();
    }

    public static void main(String[] args) {
        new LoginForm();
    }

    protected void displayClock(JLabel clockLBL, JLabel dateLBL) {

        TimerTask displayClock = new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                Date now = new Date();
                clockLBL.setText(time.format(now));
                dateLBL.setText(date.format(now));
//                System.out.println(sdf.format(now));
            }
        };
        timer.schedule(displayClock, 0, 1000);
    }

    protected void setFieldOnError(JTextField... fields) {
        for (JTextField field : fields) {
            field.setForeground(Color.red);
            field.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.red, Color.red));
        }
    }

    protected void setFieldOnCorrect(JTextField... fields) {
        for (JTextField field : fields) {
            field.setForeground(new JPasswordField().getForeground());
            field.setBorder(new JPasswordField().getBorder());
        }
    }

    protected void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
            field.setForeground(new JLabel().getForeground());
        }
    }

    protected void setFrameIcon(String iconPath) {

        try {
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/" + iconPath)));
        } catch (Exception ex) {
            ImageIcon imageIcon = new ImageIcon(iconPath);
            setIconImage(imageIcon.getImage());
        }
    }

    protected void setCustomIcon(JButton button, String iconPath) {
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        int margin = 5;

        Image icon;
        try {
            URL iconUrl = this.getClass().getResource("/" + iconPath);
            icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
        } catch (Exception e) {
            icon = new ImageIcon(iconPath).getImage();
        }

        try {
            button.setIcon(new ImageIcon(icon.getScaledInstance(button.getWidth() - margin, button.getHeight() - margin, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setCustomIcon(JLabel label, String iconPath) {
        Image icon;

        try {
            URL iconUrl = this.getClass().getResource("/" + iconPath);
            icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
        } catch (Exception ex) {
            icon = new ImageIcon(iconPath).getImage();
        }
        try {
            label.setIcon(new ImageIcon(icon.getScaledInstance(label.getWidth() - 5, label.getHeight() - 5, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void setHandCursor(JButton... buttons) {
        for (JButton button : buttons) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    private void backgroundCheckValidSession() {
        timer.schedule(new TimerTask() {
            @Override
            public synchronized void run() {
                System.out.println("session timer running");
                if (session != null) {
                    try {
                        session.isValid();
                    } catch (TimeoutException e) {
                        sessionExpired();
                    }
                }
            }
        }, Session.duration, 1000);
    }

    protected void backgroundTask(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join(150);
        } catch (InterruptedException interruptedException) {
            System.out.println("interrupt");
            interruptedException.printStackTrace();
        }
        System.out.println("ID: " + thread.getId());
        System.out.println("Count : " + Thread.activeCount());
        System.out.println();
    }

    private void sessionExpired() {
        session = null;
        location = this.getLocation();
        while (!isFocused()) {
            //wait for user focus
        }
        String error_message = "Sei rimasto inattivo per troppo tempo.\n" +
                "Verrai reindirizzato alla schermata di Login.";

        String title = "Sessione Scaduta";
        JOptionPane.showMessageDialog(getContentPane(), error_message, title, JOptionPane.ERROR_MESSAGE);
        dispose();
        timer.cancel();
        System.out.println("open new login");
        new LoginForm();
    }

    public void SQLExceptionOccurred(SQLException ex) {
        String error = "SQL State : " + ex.getSQLState() + "\n" +
                "ErrorCode : " + ex.getErrorCode() + "\n" +
                ex.getMessage();

        JOptionPane.showMessageDialog(getContentPane(), error, "SQL Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void exitAction() {
        timer.cancel();
        location = getLocation();
        if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to exit?") == 0) {
            session = null;
            dispose();
        }
    }

    protected void logOutAction() {
        timer.cancel();
        location = getLocation();
        if (JOptionPane.showConfirmDialog(getContentPane(), "Do you want to LogOut?") == 0) {
            dispose();
            session = null;
            new LoginForm();
        }
    }

    protected void displayHomeForm() {
        timer.cancel();
        location = getLocation();
        new HomeForm();
        dispose();
    }

    protected abstract void defaultAction();  //subclass will implement defaultAction
//    protected abstract void backAction();
}