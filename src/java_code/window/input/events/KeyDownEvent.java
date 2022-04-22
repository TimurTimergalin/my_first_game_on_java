package java_code.window.input.events;

import java_code.event.Event;

public class KeyDownEvent extends Event {
    public final int keyId;

    public KeyDownEvent(int keyId) {
        this.keyId = keyId;
    }
}
