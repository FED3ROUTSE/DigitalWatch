package watch;
import javax.swing.*;  
import java.awt.*;  
import java.text.*;  
import java.util.*; 

public class Watch implements Runnable {
	JFrame frame;
	Thread thread = null;
	int hours = 0;
	int minutes = 0;
	int seconds = 0;
	String time = "";
	JButton button;
	
	Watch(){
		frame = new JFrame();
		
		thread = new Thread(this);
		thread.start();
		
		button = new JButton();
		button.setBounds(200, 200, 300, 200);
		
		frame.add(button);
		frame.setSize(700, 800);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	public void run() {
		try {
			while(true) {
				Calendar cal = Calendar.getInstance();
				hours = cal.get(Calendar.HOUR_OF_DAY);
				minutes = cal.get(Calendar.MINUTE);
				seconds = cal.get(Calendar.SECOND);
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss z");
				Date date = cal.getTime();
				time = formatter.format(date);
				
				printTime();
				
				thread.sleep(1000);
			}
		}catch(Exception e) {}
	}
	
	 public void printTime(){  
		    button.setText(time);  
		  }  
	 
	 public static void main (String args[]) {
		 new Watch();
	 }

}
