package java_code.event;

public enum EventPriority {
    HIGHEST,
    HIGH,
    NORMAL,
    LOW,
    LOWEST;

    public static boolean smaller(EventPriority f, EventPriority s) {
        return getIntRepresentation(f) < getIntRepresentation(s);
    }

    private static int getIntRepresentation(EventPriority ep) {
        switch (ep) {
            case LOWEST -> {return 1;}
            case LOW -> {return 2;}
            case NORMAL -> {return 3;}
            case HIGH -> {return 4;}
            case HIGHEST -> {return 5;}
        }
        return 0;
    }
}
