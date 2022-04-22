package java_code.level_generation.room_content;

import java_code.level_generation.room_content.room_object.RoomObject;
import java_code.misc.Pair;

import java.util.Map;

public class RoomContent {
    public static final int ROOM_WIDTH = 40;
    public static final int ROOM_HEIGHT = 30;

    public final Map<Pair<Integer>, RoomObject> objects;

    public RoomContent(Map<Pair<Integer>, RoomObject> objects) {
        this.objects = objects;
    }
}
