package java_code.level_generation.room_content;

import java.util.Random;

abstract public class RoomObject {
    public Pool<RoomObject> getPool() {
        return (Random r) -> this;
    }
}
