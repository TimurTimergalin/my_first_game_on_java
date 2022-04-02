package java_code.level_generation.room_content.room_object;

import java_code.level_generation.room_content.Pool;

import java.util.Random;

abstract public class RoomObject {
    public Pool<RoomObject> getPool() {
        return (Random r) -> this;
    }
}
