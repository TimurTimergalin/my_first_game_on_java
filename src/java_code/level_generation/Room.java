package java_code.level_generation;

public class Room {
    public final int id;
    public final RoomContent content;

    public Room(int id, RoomContent content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
