/*
 * Copyright (c) 2020 Lorenzo Romio. All Right Reserved.
 */

package javabankunimi.gui;

import javabankunimi.bank.Session;
import javabankunimi.database.DBConnect;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
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
    protected final char echochar = '*';

    public MainApp() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setFrameIcon(Icons.BANK);
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

        System.out.println("Session timer running");
        timer.schedule(new CheckSessionTask(), Session.getDuration(), 250);

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
//                    session.updateCreation();
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

    }

    protected static class Icons {

        protected static final String BANK = "icons/bank.png";
        protected static final String NEXT = "icons/next.png";
        protected static final String PREV = "icons/prev.png";
        protected static final String MONEY = "icons/money.png";
        protected static final String SIGNUP = "icons/signUp.png";
        protected static final String SHOWPSW = "icons/showpsw.png";
        protected static final String HIDEPSW = "icons/hidepsw.png";
        protected static final String REFRESH = "icons/refresh.png";
        protected static final String DEPOSITO = "icons/deposito2.png";
        protected static final String PRELIEVO = "icons/prelievo2.png";
        protected static final String CHANGEPSW = "icons/changePsw.png";
        protected static final String BONIFICO_IN = "icons/bonificoIn2.png";
        protected static final String BONIFICO_OUT = "icons/bonificoOut2.png";
        protected static final String DELETE_ACCOUNT = "icons/deleteAccount.png";

    }

    protected void setSessionTimer(JLabel timerLBL) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat time = new SimpleDateFormat("mm:ss:SSS");
                Date now = new Date();
                long millis = session.getExpiredTime().toEpochMilli() - Instant.now().toEpochMilli();
                timerLBL.setText("Tempo rimasto: " + time.format(millis > 0 ? millis : 0));

                if (millis < 10000) {
                    timerLBL.setForeground(Color.red);
                    if (millis < 5000) {
//                        if( millis - millis/1000*1000 < 2) playSound(Sounds.TIMER);
//                        timerLBL.setFont(timerLBL.getFont().deriveFont(20f - millis / 1000));
                    }
                } else {
                    timerLBL.setForeground(new JLabel().getForeground());
                    timerLBL.setFont(timerLBL.getFont().deriveFont(14f));
                }
            }
        }, 0, 30);
    }

    protected void playSound(String soundName) {

        try {
            URL soundURL = this.getClass().getResource("/" + soundName);
            AudioInputStream audioInputStream;
            try {
                audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            } catch (Exception e) {
                audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
            }

            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error with playing sound.");
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

    public static void main(String[] args) {
        new LoginForm();
    }

    protected void displayClock(JLabel clockLBL, JLabel dateLBL) {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                Date now = new Date();
                clockLBL.setText(time.format(now));
                dateLBL.setText(date.format(now));
            }
        }, 0, 1000);
    }

    private void sessionExpired() {
        session = null;
        location = this.getLocation();
        System.out.println("Sessione Expired: waiting for user focus");
        playSound(Sounds.EXPIRED_SESSION);
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

    public void SQLExceptionOccurred(SQLException ex) {
        playSound(Sounds.ERROR);
        String error = "SQL State : " + ex.getSQLState() + "\n" +
                "ErrorCode : " + ex.getErrorCode() + "\n" +
                ex.getMessage();

        JOptionPane.showMessageDialog(getContentPane(), error, "SQL Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void setHandCursor(JButton... buttons) {
        for (JButton button : buttons) {
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
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

    protected static class Sounds {

        protected static final String CASH = "sounds/cash.wav";
        protected static final String PRELIEVO = "sounds/prelievo.wav";
        protected static final String ERROR = "sounds/error.wav";
        protected static final String ACCESS_GRANTED = "sounds/login1.wav";
        protected static final String TIMER = "sounds/timer.wav";
        protected static final String EXPIRED_SESSION = "sounds/expired_session.wav";
        protected static final String REFRESH = "sounds/refresh.wav";
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

    private class CheckSessionTask extends TimerTask {
        @Override
        public synchronized void run() {
            if (session != null) {
                try {
                    session.isValid();
                } catch (TimeoutException e) {
                    sessionExpired();
                }
            }
        }
    }
}