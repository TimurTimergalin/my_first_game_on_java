package java_code.level_generation.room_content;

import java_code.level_generation.RoomType;
import java_code.level_generation.room_content.room_object.RoomObject;
import java_code.misc.Pair;

import java.util.*;

public class RoomContentType {
    private final Map<Pair<Integer>, Pool<RoomObject>> objects = new HashMap<>();

    private static final Map<RoomType, List<RoomContentType>> allGenerations = new HashMap<>();

    static {
        for (RoomType t: RoomType.values()) {
            allGenerations.put(t, new ArrayList<>());
        }
    }

    static { // Здесь генерируются пустые комнаты (не для прода, пред билдлм удалить)
        for (RoomType i: RoomType.values()) {
            register(new Builder(i));
        }
    }

    static { // Здесь будут все генерации комнат

    }

    private static <T> T chooseFrom(List<T> lst, Random r) {
        int ind = r.nextInt(lst.size());
        return lst.get(ind);
    }

    private Map<Pair<Integer>, RoomObject> createObjMap(Random r) {
        Map<Pair<Integer>, RoomObject> res = new HashMap<>();

        for (Pair<Integer> i: objects.keySet()) {
            res.put(i, objects.get(i).getRandom(r));
        }
        return res;
    }


    public static RoomContent randomContent(Random r, RoomType type) {
        return new RoomContent(chooseFrom(allGenerations.get(type), r).createObjMap(r));
    }

    public static class Builder {
        private RoomContentType rct;
        private RoomType type;

        private static boolean inBorder(Pair<Integer> c) {
            int x = c.get(0);
            int y = c.get(1);

            return x >= 0 && x < RoomContent.ROOM_HEIGHT && y >= 0 && y < RoomContent.ROOM_WIDTH;
        }

        public Builder(RoomType type) {
            restart(type);
        }

        public void restart(RoomType type) {
            this.type = type;
            this.rct = new RoomContentType();
        }


        public Builder addObject(Pair<Integer> key, Pool<RoomObject> val) {
            if (!inBorder(key)) throw new IllegalArgumentException();
            rct.objects.put(key, val);
            return this;
        }

        public RoomContentType build() {
            return rct;
        }
    }

    public static void register(Builder b) {
        RoomContentType res = b.build();
        allGenerations.get(b.type).add(res);
    }
}
