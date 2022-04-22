package java_code.window.input.events;

import java_code.event.Event;

public class ButtonDownEvent extends Event {
    public final int buttonId;

    public ButtonDownEvent(int buttonId) {
        this.buttonId = buttonId;
    }
}
