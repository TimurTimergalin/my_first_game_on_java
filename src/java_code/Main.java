package java_code;

import java_code.level_generation.Level;
import java_code.level_generation.LevelGenerator;
import java_code.level_generation.RoomType;
import java_code.misc.Pair;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        int mapSize = 10;
        int rooms = 18;

        Map<RoomType, Pair<Double>> reqs = new HashMap<>();
        reqs.put(RoomType.TREASURE_ROOM, new Pair<>(3.0, 2.0));
        reqs.put(RoomType.BOSS_FIGHT_ROOM, new Pair<>(5.0, 3.0));
        reqs.put(RoomType.SHOP_ROOM, new Pair<>(3.0, 2.0));

        long seed = 45625403547572874L;

        LevelGenerator.setMaxRange(5);
        Level lvl = LevelGenerator.generateLevel(mapSize, rooms, seed, reqs, true);
        System.out.println(lvl);
    }
}
