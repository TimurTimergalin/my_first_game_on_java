package java_code.event;

import java_code.event.events.Event;

public interface IEventHandler {
    void handle(Event e);

    default boolean isDeleted() {return false;}
}
