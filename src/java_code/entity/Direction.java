package java_code.entity;

import java_code.misc.Pair;

public enum Direction {
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT,
    TOP_LEFT;

    public Direction clockwise(Direction or) {
        boolean next = false;

        for (Direction o : Direction.values()) {
            if (next) {
                return o;
            }
            if (o.equals(or)) {
                next = true;
            }
        }
        return TOP;
    }

    public Direction counterclockwise(Direction or) {
        Direction prev = null;

        for (Direction o : Direction.values()) {
            if (o.equals(or)) {
                return prev == null ? TOP_LEFT : prev;
            }
            prev = o;
        }
        return TOP_LEFT;  // never get here
    }

    public Pair<Double> vec(Direction or) {
        double s2 = Math.sqrt(2);

        double x = 0;
        double y = 0;

        switch (or) {
            case TOP -> y = -1.0;
            case TOP_RIGHT -> {
                x = s2;
                y = -s2;
            }
            case RIGHT -> x = 1;
            case BOTTOM_RIGHT -> {
                x = s2;
                y = s2;
            }
            case BOTTOM -> y = 1;
            case BOTTOM_LEFT -> {
                x = -s2;
                y = s2;
            }
            case LEFT -> x = -1;
            case TOP_LEFT -> {x = -s2;y=-s2;}

        }

        return new Pair<>(x, y);
    }
}
