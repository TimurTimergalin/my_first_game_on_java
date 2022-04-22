package java_code.event;

public interface IEventHandler {
    void handle(Event e);

    default boolean isDeleted() {return false;}
}
