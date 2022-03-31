package java_code.misc;

public class Pair <T>{
    private final T x;
    private final T y;

    public Pair(T first, T second) {
        x = first;
        y = second;
    }

    public T get(int ind) {
        if (ind == 0) {
            return x;
        }
        if (ind == 1) {
            return y;
        }
        return null;
    }

    public boolean equals(Pair<T> other) {
        return (x.equals(other.get(0))) && (y.equals(other.get(1)));
    }

    @Override
    public String toString() {
        return "Pair{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
