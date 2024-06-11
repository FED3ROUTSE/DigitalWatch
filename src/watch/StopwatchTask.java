package watch;
import java.util.TimerTask;

public class StopwatchTask extends TimerTask {
    private long startTime;
    private Watch watch;

    // Constructor to accept a Watch object
    public StopwatchTask(Watch watch, long startTime) {
        this.watch = watch;
        this.startTime = startTime;
    }

    @Override
    public void run() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        
        // Check if watch is not null before calling updateTimeDisplay
        if (watch != null) {
            watch.updateTimeDisplay(elapsedTime);
        }
    }
}