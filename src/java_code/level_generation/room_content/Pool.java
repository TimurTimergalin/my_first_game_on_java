package java_code.level_generation.room_content;

import java.util.Random;

public interface Pool<T>{
    T getRandom(Random r);
}
