package java_code.entity.player;

import java_code.entity.Entity;

import java.awt.*;

public class Player extends Entity {
    public Player() {}
    @Override
    public void render() {

    }

    @Override
    public boolean collideWith(Rectangle rectangle) {
        return false;
    }

}
