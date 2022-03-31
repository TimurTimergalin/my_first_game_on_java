import java_code.level_generation.Level;
import java_code.level_generation.LevelGenerator;
import java_code.level_generation.RoomType;
import java_code.misc.Pair;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int mapSize = 10;
        int rooms = 15;

        Map<RoomType, Pair<Double>> reqs = new HashMap<>();
        reqs.put(RoomType.TREASURE_ROOM, new Pair<>(3.0, 1.0));
        reqs.put(RoomType.BOSS_FIGHT_ROOM, new Pair<>(4.0, 0.8));

        long seed = 45627176782874L;

        Level lvl = LevelGenerator.generateLevel(mapSize, rooms, seed, reqs);
        System.out.println(lvl);
    }
}
