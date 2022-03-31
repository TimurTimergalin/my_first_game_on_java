package java_code.level_generation;

import java.util.Arrays;

public class Level {
    public final Room[][] map;

    public Level(Room[][] map) {
        this.map = map;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("Level:\n");

        String rep;
        for (Room[] i: map) {
            for (Room j: i) {
                if (j == null) {
                    rep = " ";
                } else {
                    rep = j.toString();
                }
                res.append(rep).append(" ");
            }
            res.append("\n");
        }
        return res.toString();
    }
}
