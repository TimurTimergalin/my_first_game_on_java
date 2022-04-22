package java_code.main_loop;

import java_code.event.EventMediator;
import java_code.main_loop.events.ClearDeletedEvent;
import java_code.main_loop.events.FrameEvent;
import java_code.window.MainWindow;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


class Timer {
    private LocalDateTime last_frame = LocalDateTime.now();
    private int clearEventTimer = 0;
    private static final int clearEventDelay = 1;

    private void nextFrame() {
        LocalDateTime cur_time = LocalDateTime.now();
        int delta = (int) ChronoUnit.MILLIS.between(last_frame, cur_time);
        clearEventTimer += delta;
        if (clearEventTimer > clearEventDelay * 60 * 1000) {
            clearEventTimer = 0;
            EventMediator.get().callEvent(new ClearDeletedEvent());
        }
        EventMediator.get().callEvent(new FrameEvent(delta));
        last_frame = cur_time;
    }

    public void run() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::nextFrame, 0L, 8L, TimeUnit.MILLISECONDS);
    }
}


public class MainLoop {
    public static void main(String[] args) {
        Timer t = new Timer();

        t.run();
        MainWindow w = new MainWindow();
        w.setVisible(true);
    }
}
