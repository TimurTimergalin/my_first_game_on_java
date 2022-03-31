package java_code.level_generation;

import java.util.Random;

public class RoomContent {
    public final RoomType type;

    public static RoomContent randomContent(Random r, RoomType type) {
        return new RoomContent(type);
    }

    public RoomContent(RoomType t) {
        this.type = t;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
