package java_code.main_loop.events;

import java_code.event.Event;

public class FrameEvent extends Event {
    public final double delta;

    public FrameEvent(double delta) {
        this.delta = delta;
    }

    public double getFPS() {
        return 1000 / delta;
    }
}
