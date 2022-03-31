package java_code.level_generation;

import java_code.level_generation.room_content.RoomContent;
import java_code.level_generation.room_content.RoomContentType;
import java_code.misc.Pair;

import java.util.*;

public class LevelGenerator {
    private static Room[][] map;
    private static int mapSize;
    private static int roomsToGenerate;
    private static int newId = 0;
    private static Random randomGen;
    private static Set<Pair<Integer>> usedCords = new HashSet<>();

    private static class Maths {
        private static int gauss(double center, double width) {
            return (int) (randomGen.nextGaussian() * width + center - (width - 1) / 2);
        }

        private static int rangeBetweenTiles(int x0, int y0, int x1, int y1) {
            return Math.abs(x0 - x1) + Math.abs(y0 - y1);
        }

        private static boolean areNeighbors(int x0, int y0, int x1, int y1) {
            return rangeBetweenTiles(x0, y0, x1, y1) == 1;
        }

        private static boolean areNeighbors(Pair<Integer> p1, Pair<Integer> p2) {
            return areNeighbors(p1.get(0), p1.get(1), p2.get(0), p2.get(1));
        }

        private static Pair<Integer>[] neighbors(int x, int y) {
            return new Pair[]{
                    new Pair<>(x + 1, y),
                    new Pair<>(x - 1, y),
                    new Pair<>(x, y + 1),
                    new Pair<>(x, y - 1)
            };
        }

        private static <T> T chooseRandom(List<T> lst) throws IllegalArgumentException {
            return lst.get(randomGen.nextInt(lst.size()));
        }

        private static Pair<Integer> chooseRandomOnRange(int x, int y, int range) {
            int dx = randomGen.nextInt(range - 1) + 1;
            boolean dxn = randomGen.nextBoolean();
            if (dxn) {
                dx *= -1;
            }

            int dy = range - Math.abs(dx);

            boolean dyn = randomGen.nextBoolean();
            if (dyn) {
                dy *= -1;
            }

            if (!inBorder(x + dx, y + dy)) {
                return chooseRandomOnRange(x, y, range);
            }

            return new Pair<>(x + dx, y + dy);
        }

        private static boolean inBorder(int x, int y) {
            return (x < mapSize && x >= 0 && y < mapSize && y >= 0);
        }

        private static boolean inBorder(Pair<Integer> p) {
            return inBorder(p.get(0), p.get(1));
        }

        private static boolean randomBooleanWithProb(int tProb, int fProb) {
            List<Boolean> lst = new LinkedList<>();

            for (int i = 0; i < tProb; i++) {
                lst.add(true);
            }

            for (int i = 0; i < fProb; i++) {
                lst.add(false);
            }
            return chooseRandom(lst);
        }
    }

    private static boolean areValidCords(Pair<Integer> el) {
        for (Pair<Integer> i : usedCords) {
            Room r = map[el.get(0)][el.get(1)];
            if (r != null && r.type == RoomType.START_ROOM) {
                continue;
            }
            if (i.equals(el) || Maths.areNeighbors(el, i)) {
                return false;
            }
        }
        return true;
    }

    private static void refreshMap(int n) {
        map = new Room[n][n];
        mapSize = n;
    }

    private static void setRandomGen(Random r) {
        randomGen = r;
    }

    private static void setRoom(int x, int y, RoomType type) {
        map[x][y] = new Room(newId, type, RoomContentType.randomContent(randomGen, type));
        newId++;
        roomsToGenerate--;
    }

    private static int[][] rangeMap(int x0, int y0) {
        int[][] res = new int[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                res[i][j] = mapSize + 1;
            }
        }

        res[x0][y0] = 0;

        Stack<Pair<Integer>> cordsStack = new Stack<>();
        cordsStack.add(new Pair<>(x0, y0));

        int curVal;
        Pair<Integer> cur;
        int curX;
        int curY;

        while (!cordsStack.isEmpty()) {
            cur = cordsStack.pop();
            curX = cur.get(0);
            curY = cur.get(1);
            curVal = res[curX][curY];

            for (Pair<Integer> i : Maths.neighbors(curX, curY)) {
                if (!Maths.inBorder(i)) {
                    continue;
                }
                if (!areValidCords(i)) {
                    continue;
                }
                if (res[i.get(0)][i.get(1)] > curVal + 1) {
                    res[i.get(0)][i.get(1)] = curVal + 1;
                    cordsStack.add(i);
                }
            }
        }
        return res;
    }

    private static void populateTo(int x0, int y0, int x1, int y1, RoomType type) {
        int[][] rMap = rangeMap(x0, y0);

        Pair<Integer> cur = new Pair<>(x1, y1);
        Pair<Integer> goal = new Pair<>(x0, y0);

        setRoom(x0, y0, type);

        int curX;
        int curY;
        int curVal;
        while (!cur.equals(goal)) {
            curX = cur.get(0);
            curY = cur.get(1);
            curVal = rMap[curX][curY];

            if (map[curX][curY] == null) {
                setRoom(curX, curY, RoomType.USUAL_ROOM);
            }

            for (Pair<Integer> i : Maths.neighbors(curX, curY)) {
                if (rMap[i.get(0)][i.get(1)] < curVal) {
                    cur = i;
                    break;
                }
            }
        }
    }

    private static void addRooms(int x0, int y0) {
        Queue<Pair<Integer>> cordsQueue = new LinkedList<>();
        while (roomsToGenerate > 0) {
            cordsQueue.add(new Pair<>(x0, y0));

            Pair<Integer> cur;
            int curX;
            int curY;
            while (!cordsQueue.isEmpty() && roomsToGenerate > 0) {
                cur = cordsQueue.poll();
                curX = cur.get(0);
                curY = cur.get(1);
                if (!areValidCords(cur)) {
                    continue;
                }
                if (!Maths.inBorder(curX, curY)) {
                    continue;
                }

                if (map[curX][curY] == null) {
                    setRoom(curX, curY, RoomType.USUAL_ROOM);
                }

                boolean res;
                for (Pair<Integer> i : Maths.neighbors(curX, curY)) {
                    if (!Maths.inBorder(i)) {
                        continue;
                    }
                    if (!areValidCords(i)) {
                        continue;
                    }
                    if (map[i.get(0)][i.get(1)] == null) {
                        res = Maths.randomBooleanWithProb(1, 2);
                    } else {
                        res = randomGen.nextBoolean();
                    }
                    if (res) {
                        cordsQueue.add(i);
                    }
                }
            }
        }
    }

    public static Level generateLevel(int mapSize, int minRooms, Random r, Map<RoomType, Pair<Double>> reqRooms) {
        return generateLevel(mapSize, minRooms, r, reqRooms, true);
    }

    public static Level generateLevel(int mapSize, int minRooms, Random r, Map<RoomType, Pair<Double>> reqRooms, boolean safe) {
        try {
            refreshMap(mapSize);
            roomsToGenerate = minRooms;
            setRandomGen(r);
            usedCords = new HashSet<>();

            int originX = mapSize / 2;
            int originY = mapSize / 2;

            newId = 0;
            setRoom(originX, originY, RoomType.START_ROOM);

            int range;
            Pair<Double> cw;
            Pair<Integer> roomCords;
            for (RoomType type : reqRooms.keySet()) {
                cw = reqRooms.get(type);
                range = Maths.gauss(cw.get(0), cw.get(1));
                roomCords = Maths.chooseRandomOnRange(originX, originY, range);

                while (!areValidCords(roomCords)) {
                    roomCords = Maths.chooseRandomOnRange(originX, originY, range);
                }
                populateTo(roomCords.get(0), roomCords.get(1), originX, originY, type);
                usedCords.add(roomCords);
            }

            addRooms(originX, originY);

            return new Level(map);
        } catch (Exception e) {
            if (safe) {
                return generateLevel(mapSize, minRooms, r, reqRooms, safe);
            }
            throw e;
        }
    }

    public static Level generateLevel(int mapSize, int minRooms, long seed, Map<RoomType, Pair<Double>> reqRooms) {
        Random r = new Random(seed);
        return generateLevel(mapSize, minRooms, r, reqRooms);
    }

    public static Level generateLevel(int mapSize, int minRooms, long seed, Map<RoomType, Pair<Double>> reqRooms, boolean safe) {
        Random r = new Random(seed);
        return generateLevel(mapSize, minRooms, r, reqRooms, safe);
    }

}
