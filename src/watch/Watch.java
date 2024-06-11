package watch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.*;
import java.io.*;

public class Watch extends TimerTask implements Runnable {
    JFrame frame, frameStopwatch;
    Thread thread = null;
    int hours = 0;
    int minutes = 0;
    int seconds = 0;
    String time = "";
    String dateFormat = "dd/MM/yyyy 'at' HH:mm:ss z";  // Default date format
    JPanel panel, stopwatchPanel, stopwatchPanel1;
    JLabel label;
    JLabel label1, stopwatchLabel;
    JButton startButton, stopButton, resetButton, continueButton;
    JMenuBar menu;
    JMenu m, m1;
    JMenuItem item, item1, item2;
    


    private String alarm = null;
    private boolean running = false;
    private long startTime;
    private Timer timer;
    private long startTime1;

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
                JOptionPane.showMessageDialog(frame, "Alarm! It's " + alarm, "Alarm", JOptionPane.INFORMATION_MESSAGE);
                alarm = null;  // Reset the alarm
            }
        }
    }


    Watch() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frameStopwatch = new JFrame();
        frameStopwatch.setLayout(new BorderLayout());

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
        
        stopwatchLabel = new JLabel("00:00:00.000");
        stopwatchLabel.setFont(font);
   
        
        item2 = new JMenuItem("Stopwatch");

        m = new JMenu("Alarm Clock");
        m1 = new JMenu("Settings");
        m1.add(item);
        m1.add(item1);
        m1.add(item2);

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
        
        
        stopwatchPanel = new JPanel(new FlowLayout());
        stopwatchPanel1 = new JPanel(new FlowLayout());
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        resetButton = new JButton("Reset");
        continueButton = new JButton("Continue");

        stopwatchPanel.add(stopwatchLabel);
        stopwatchPanel1.add(startButton);
        stopwatchPanel1.add(continueButton);
        stopwatchPanel1.add(stopButton);
        stopwatchPanel1.add(resetButton);
        
        
        frameStopwatch.add(stopwatchPanel, BorderLayout.CENTER);
        frameStopwatch.add(stopwatchPanel1, BorderLayout.SOUTH);
        frameStopwatch.setSize(500, 500);
        frameStopwatch.setVisible(false);
        
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	frameStopwatch.setVisible(true);
            }
        });
        
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) { // If stopwatch is not 
                    running = true; // Set running to true
                    startTime = System.currentTimeMillis(); // Record the start 
                    timer = new Timer();
                    StopwatchTask stopwatchTask = new StopwatchTask(Watch.this, startTime);
                    timer.scheduleAtFixedRate(stopwatchTask, 0, 10);
                    startTime1 =  startTime;
                }
            }
        });
        
        
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    running = false; // Stop the stopwatch
                    timer.cancel(); // Cancel the Timer
                }
            }
        });
        
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!running) {
                	long currentTime = System.currentTimeMillis();
                    long elapsedTime = currentTime - startTime;
                    startTime1 -= elapsedTime; // Adjust the start time by subtracting the elapsed time
                    running = true; // Set running to true to resume the stopwatch
                    timer = new Timer();
                    StopwatchTask stopwatchTask = new StopwatchTask(Watch.this, startTime1);
                    timer.scheduleAtFixedRate(stopwatchTask, 0, 10);
                }
            }
        });
        
        
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!running) {
                	stopwatchLabel.setText("00:00:00.000"); // Reset the stopwatch label to 00:00:00.000
                    startTime = 0; // Reset the start time
                }
            }
        });


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
    
    private String formatElapsedTime(long elapsedTime) {
        // Calculate hours, minutes, seconds, and milliseconds
        // Example calculation:
    	long hours = elapsedTime / (1000 * 60 * 60);
        long minutes = (elapsedTime % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((elapsedTime % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
        long milliseconds = elapsedTime % 1000;

        // Format the time as HH:mm:ss.SSS
        String formattedTime = String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds);

        // Update the stopwatch label with the formatted time
        stopwatchLabel.setText(formattedTime);
		return formattedTime;
    }
    
    public void updateTimeDisplay(long elapsedTime) {
        // Format elapsed time as hours, minutes, seconds, and milliseconds
        // Example format: HH:mm:ss.SSS
        String formattedTime = formatElapsedTime(elapsedTime);

        // Update the stopwatch label or text field with the formatted time
        stopwatchLabel.setText(formattedTime);
    }

    public static void main(String args[]) {
        new Watch();
    }
    
}
