package java_code.window.input.events;

import java_code.event.Event;

public class KeyUpEvent extends Event {
    public final int keyId;

    public KeyUpEvent(int keyId) {
        this.keyId = keyId;
    }
}
