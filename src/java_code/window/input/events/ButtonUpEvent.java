package java_code.window.input.events;

import java_code.event.Event;

public class ButtonUpEvent extends Event {
    public final int buttonId;

    public ButtonUpEvent(int buttonId) {
        this.buttonId = buttonId;
    }
}
