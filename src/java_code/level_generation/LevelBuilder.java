package java_code.level_generation;

import java_code.level_generation.room_content.RoomContentType;
import java_code.misc.Pair;

import java.util.*;


class IllegalCallException extends RuntimeException {}
class UnfinishedInitException extends RuntimeException {}


public class LevelBuilder {
    private Room[][] map;
    private int mapSize;
    private int roomsToGenerate;
    private int newId = 0;
    private Random randomGen;
    private Set<Pair<Integer>> usedCords = new HashSet<>();
    private int maxRange;
    private boolean safeMode;
    private final Maths maths = new Maths();
    private final List<Runnable> tasks = new LinkedList<>();

    private class Maths {
        private int gauss(double center, double min) {
            int res = Math.max((int) ((randomGen.nextGaussian() - 0.5) * 2 + center), (int) min);
            return Math.min(res, maxRange);
        }

        private int rangeBetweenTiles(int x0, int y0, int x1, int y1) {
            return Math.abs(x0 - x1) + Math.abs(y0 - y1);
        }

        private boolean areNeighbors(int x0, int y0, int x1, int y1) {
            return rangeBetweenTiles(x0, y0, x1, y1) == 1;
        }

        private boolean areNeighbors(Pair<Integer> p1, Pair<Integer> p2) {
            return areNeighbors(p1.get(0), p1.get(1), p2.get(0), p2.get(1));
        }

        private Pair<Integer>[] neighbors(int x, int y) {
            return new Pair[]{
                    new Pair<>(x + 1, y),
                    new Pair<>(x - 1, y),
                    new Pair<>(x, y + 1),
                    new Pair<>(x, y - 1)
            };
        }

        private <T> T chooseRandom(List<T> lst) throws IllegalArgumentException {
            return lst.get(randomGen.nextInt(lst.size()));
        }

        private Pair<Integer> chooseRandomOnRange(int x, int y, int range) {
            int dx;
            try {
                dx = randomGen.nextInt(range - 1) + 1;
            } catch (IllegalArgumentException e) {
                dx = 1;
            }
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

        private boolean inBorder(int x, int y) {
            return (x < mapSize && x >= 0 && y < mapSize && y >= 0);
        }

        private boolean inBorder(Pair<Integer> p) {
            return inBorder(p.get(0), p.get(1));
        }

        private boolean randomBooleanWithProb(int tProb, int fProb) {
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

    private void refreshMap(int n) {
        map = new Room[n][n];
        mapSize = n;
    }

    public LevelBuilder(int mapSize, int roomsToGenerate, int maxRange) {
        refreshMap(mapSize);
        this.roomsToGenerate = roomsToGenerate;
        this.maxRange = maxRange;

        safeMode = true;
    }

    public LevelBuilder(int mapSize, int roomsToGenerate, int maxRange, boolean safeMode) {
        this(mapSize, roomsToGenerate, maxRange);
        this.safeMode = safeMode;
    }

    public LevelBuilder setRandom(long seed) {
        return setRandom(new Random(seed));
    }

    public LevelBuilder setRandom(Random r) {
        if (randomGen != null) {
            throw new IllegalCallException();
        }
        randomGen = r;
        return this;
    }

    private void setRoom(int x, int y, RoomType type) {
        map[x][y] = new Room(newId, type, RoomContentType.randomContent(randomGen, type));
        newId++;
        roomsToGenerate--;
    }

    public LevelBuilder init() {
        if (randomGen == null) {
            throw new UnfinishedInitException();
        }

        int originX = mapSize / 2;
        int originY = mapSize / 2;

        newId = 0;
        setRoom(originX, originY, RoomType.START_ROOM);
        return this;
    }

    private boolean areValidCords(Pair<Integer> el) {
        for (Pair<Integer> i : usedCords) {
            Room r = map[el.get(0)][el.get(1)];
            if (r != null && r.type == RoomType.START_ROOM) {
                continue;
            }
            if (i.equals(el) || this.maths.areNeighbors(el, i)) {
                return false;
            }
        }
        return true;
    }

    private int[][] rangeMap(int x0, int y0) {
        int[][] res = new int[mapSize][mapSize];

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                res[i][j] = mapSize * mapSize + 1;
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

            for (Pair<Integer> i : this.maths.neighbors(curX, curY)) {
                if (!this.maths.inBorder(i)) {
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

    private void populateTo(int x0, int y0, int x1, int y1, RoomType type) {
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

            if (curVal > maxRange + 3) {
                throw new RuntimeException();
            }

            if (map[curX][curY] == null) {
                setRoom(curX, curY, RoomType.USUAL_ROOM);
            }

            for (Pair<Integer> i : this.maths.neighbors(curX, curY)) {
                if (!this.maths.inBorder(i)) {continue;}
                if (rMap[i.get(0)][i.get(1)] < curVal) {
                    cur = i;
                    break;
                }
            }
        }
    }

    private boolean hasNeighbors(int x, int y) {
        Room cur;
        for (Pair<Integer> i: this.maths.neighbors(x, y)) {
            if (!this.maths.inBorder(i)) {
                continue;
            }
            cur = map[i.get(0)][i.get(1)];
            if (cur != null && cur.type != RoomType.START_ROOM) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNeighbors(Pair<Integer> p) {
        return hasNeighbors(p.get(0), p.get(1));
    }


    private void _populateReqRooms(Map<RoomType, Pair<Double>> reqRooms) {
        int originX = mapSize / 2;
        int originY = mapSize / 2;

        int range;
        Pair<Double> cw;
        Pair<Integer> roomCords;
        int counter = 0;
        for (RoomType type : reqRooms.keySet()) {
            cw = reqRooms.get(type);
            range = this.maths.gauss(cw.get(0), cw.get(1));
            roomCords = this.maths.chooseRandomOnRange(originX, originY, range);

            while (!areValidCords(roomCords) || hasNeighbors(roomCords)) {
                roomCords = this.maths.chooseRandomOnRange(originX, originY, range);
                counter++;

                if (counter > 5) {
                    range++;
                    counter = 0;
                }
            }
            populateTo(roomCords.get(0), roomCords.get(1), originX, originY, type);
            usedCords.add(roomCords);
        }
    }

    public LevelBuilder populateReqRooms(Map<RoomType, Pair<Double>> reqRooms) {
        tasks.add(() -> _populateReqRooms(reqRooms));
        return this;
    }

    private void _addRooms(int x0, int y0) {
        Queue<Pair<Integer>> cordsQueue = new LinkedList<>();
        while (roomsToGenerate > 0) {
            cordsQueue.addAll(Arrays.asList(this.maths.neighbors(x0, y0)));

            Pair<Integer> cur;
            int curX;
            int curY;
            Room curRoom;
            while (!cordsQueue.isEmpty() && roomsToGenerate > 0) {
                cur = cordsQueue.poll();
                curX = cur.get(0);
                curY = cur.get(1);
                curRoom = map[curX][curY];
                if (!areValidCords(cur)) {
                    if (curRoom == null) {
                        continue;
                    }
                    if (curRoom.type != RoomType.USUAL_ROOM) {
                        continue;
                    }
                }
                if (!this.maths.inBorder(curX, curY)) {
                    continue;
                }

                if (map[curX][curY] == null) {
                    setRoom(curX, curY, RoomType.USUAL_ROOM);
                }

                boolean res;
                for (Pair<Integer> i : this.maths.neighbors(curX, curY)) {
                    if (!this.maths.inBorder(i)) {
                        continue;
                    }
                    if (!areValidCords(i)) {
                        continue;
                    }
                    res = this.maths.randomBooleanWithProb(3, 1);
                    if (res) {
                        cordsQueue.add(i);
                    }
                }
            }
        }
    }

    public LevelBuilder addRooms() {
        int originX = mapSize / 2;
        int originY = mapSize / 2;

        tasks.add(() -> _addRooms(originX, originY));
        return this;
    }

    private void run() {
        try {
            for (Runnable t: tasks) {
                t.run();
            }
        } catch (RuntimeException e) {
            if (safeMode) {
                refreshMap(mapSize);
                init();
                run();
            } else throw e;
        }
    }

    public Level build() {
        run();
        return new Level(map);
    }

    public static void main(String[] args) {
        int mapSize = 10;
        int rooms = 18;
        int maxRange = 6;

        Map<RoomType, Pair<Double>> reqs = new HashMap<>();
        reqs.put(RoomType.TREASURE_ROOM, new Pair<>(5.0, 3.0));
        reqs.put(RoomType.BOSS_FIGHT_ROOM, new Pair<>(6.0, 4.0));
        reqs.put(RoomType.SHOP_ROOM, new Pair<>(5.0, 4.0));

        long seed = 456254347572874L;

        Level lvl = new LevelBuilder(mapSize, rooms, maxRange)
                .setRandom(seed).init()
                .populateReqRooms(reqs)
                .addRooms()
                .build();
        System.out.println(lvl);
    }

}
