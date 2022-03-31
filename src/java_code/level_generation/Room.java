package java_code.level_generation;

import java_code.level_generation.room_content.RoomContent;

public class Room {
    public final int id;
    public final RoomContent content;
    public final RoomType type;

    public Room(int id, RoomType type, RoomContent content) {
        this.id = id;
        this.content = content;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
