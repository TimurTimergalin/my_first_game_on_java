package java_code.event.events;

public class FrameEvent extends Event{
    public final int delta;

    public FrameEvent(int delta) {
        this.delta = delta;
    }

    public int getFPS() {
        return 1000 / delta;
    }
}
