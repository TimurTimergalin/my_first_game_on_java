package java_code;

import java_code.event.EventMediator;
import java_code.event.events.ClearDeletedEvent;
import java_code.event.events.FrameEvent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Timer {
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
