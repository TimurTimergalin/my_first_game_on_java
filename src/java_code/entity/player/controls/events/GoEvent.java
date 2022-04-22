package java_code.entity.player.controls.events;

import java_code.entity.Direction;
import java_code.event.Event;

public class GoEvent extends Event {
    public final Direction dr;

    public GoEvent(Direction dr) {
        this.dr = dr;
    }
}
