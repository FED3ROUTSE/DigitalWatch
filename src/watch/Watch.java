package watch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.sound.sampled.*;
import java.io.*;

public class Watch implements Runnable {
    JFrame frame;
    Thread thread = null;
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    String time = "";
    String dateFormat = "dd/MM/yyyy 'at' HH:mm:ss z";  // Default date format
    JPanel panel;
    JLabel label;
    JLabel label1;
    JMenuBar menu;
    JMenu m, m1;
    JMenuItem item, item1;

    private String alarm = null;

    private String[] dateFormats = {
        "dd/MM/yyyy 'at' HH:mm:ss z",
        "MM/dd/yyyy 'at' hh:mm:ss a",
        "yyyy-MM-dd 'at' HH:mm:ss",
        "E, MMM dd yyyy 'at' hh:mm:ss a z",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "dd/MM/yy 'at' HH:mm:ss",
        "MM/dd/yy 'at' hh:mm:ss a",
        "EEEE, MMMM dd, yyyy 'at' HH:mm:ss",
        "EEEE, dd MMMM yyyy 'at' hh:mm:ss a",
        "EEEE, MMMM dd, yyyy 'at' hh:mm:ss a z",
        "EEEE, MMM dd, yyyy 'at' HH:mm:ss z",
        "HH:mm:ss a, z 'on' dd/MM/yyyy 'at' HH:mm:ss",
        "hh 'o''clock' a, zzzz 'at' HH:mm:ss",
        "yyyy-MM-dd 'at' HH:mm:ss",
        "yyyyMMddHHmmss",
        "yyyyMMdd'T'HHmmss 'at' HH:mm:ss",
        "HH:mm:ss 'at' HH:mm:ss",
        "hh:mm a 'at' hh:mm:ss a",
        "dd MMM yyyy 'at' HH:mm:ss",
        "MMMM dd, yyyy 'at' hh:mm:ss a"
    };

    public void setAlarm(String alarmTime) {
        this.alarm = alarmTime;
    }

    public void checkAlarm() {
        if (alarm != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            String currentTime = sdf.format(new Date());
            if (currentTime.equals(alarm)) {
                playAlarmSound();  // Play alarm sound
                JOptionPane.showMessageDialog(frame, "Alarm! It's " + alarm, "Alarm", JOptionPane.INFORMATION_MESSAGE);
                alarm = null;  // Reset the alarm
            }
        }
    }

    public void playAlarmSound() {
        try {
            // Path to the alarm sound file (WAV format)
        	File soundFile = new File("C:\\Users\\dimit\\Downloads\\Nirvana - Dumb -- Lyrics.mp3");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Watch() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());

        Font font = new Font("Monospaced", Font.CENTER_BASELINE, 15);

        thread = new Thread(this);
        thread.start();

        item = new JMenuItem("Change Date Format");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> formatBox = new JComboBox<>(dateFormats);
                int option = JOptionPane.showConfirmDialog(frame, formatBox, "Select Date Format", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (option == JOptionPane.OK_OPTION) {
                    String selectedFormat = (String) formatBox.getSelectedItem();
                    if (selectedFormat != null && !selectedFormat.trim().isEmpty()) {
                        setDateFormat(selectedFormat);
                    }
                }
            }
        });

        item1 = new JMenuItem("Set Alarm");
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alarmInput = JOptionPane.showInputDialog(frame, "Enter alarm time (HH:mm:ss (14:12:59) ):", "Set Alarm", JOptionPane.PLAIN_MESSAGE);
                if (alarmInput != null && !alarmInput.trim().isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                        sdf.setLenient(false);  // Set lenient to false to strictly parse the time
                        Date date = sdf.parse(alarmInput.trim());
                        setAlarm(alarmInput.trim());
                        JOptionPane.showMessageDialog(frame, "Alarm set for " + alarmInput);
                    } catch (ParseException pe) {
                        JOptionPane.showMessageDialog(frame, "Invalid time format. Please enter in HH:mm:ss format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        m = new JMenu("Alarm Clock");
        m1 = new JMenu("Settings");
        m1.add(item);
        m1.add(item1);

        menu = new JMenuBar();
        menu.setBackground(Color.white);
        menu.setPreferredSize(new Dimension(100, 40));
        menu.add(m);
        menu.add(m1);

        label = new JLabel("Time and Date: ");
        label.setFont(font);
        label.setForeground(Color.magenta);

        label1 = new JLabel();
        label1.setFont(font);
        label1.setForeground(Color.magenta);
        label1.setPreferredSize(new Dimension(300, 200));

        panel = new JPanel(new FlowLayout());
        panel.add(label);
        panel.add(label1);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(menu, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 800);
        frame.setVisible(true);
    }

    public void run() {
        try {
            while (true) {
                Calendar cal = Calendar.getInstance();
                hours = cal.get(Calendar.HOUR_OF_DAY);
                minutes = cal.get(Calendar.MINUTE);
                seconds = cal.get(Calendar.SECOND);

                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);  // Use the updated format
                Date date = cal.getTime();
                time = formatter.format(date);

                checkAlarm();
                printTime();

                Thread.sleep(1000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDateFormat(String newFormat) {
        this.dateFormat = newFormat;
    }

    public void printTime() {
        SwingUtilities.invokeLater(() -> label1.setText(time));
    }

    public static void main(String args[]) {
        new Watch();
    }
}
