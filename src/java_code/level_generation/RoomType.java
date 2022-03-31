package java_code.level_generation;

public enum RoomType {
    USUAL_ROOM,
    TREASURE_ROOM,
    BOSS_FIGHT_ROOM,
    START_ROOM,
    SHOP_ROOM;

    public String toString() {
        switch (this) {
            case START_ROOM -> {return "S";}
            case USUAL_ROOM -> {return "U";}
            case TREASURE_ROOM -> {return "T";}
            case BOSS_FIGHT_ROOM -> {return "B";}
            case SHOP_ROOM -> {return "M";}
        }
        return "";
    }
}
