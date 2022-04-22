package java_code.entity;

import java_code.IsDeletable;

import java.awt.*;

abstract public class Entity implements IsDeletable {
    protected Component comp = new Canvas();
    private int x;
    private int y;
    private Direction direction;
    private boolean deleted = false;

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void delete() {
        deleted = true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    abstract public void render();
    abstract public boolean collideWith(Rectangle rectangle);
    public Component getComponent() {return comp;}

    public Direction getOrientation() {
        return direction;
    }

    public void setOrientation(Direction direction) {
        this.direction = direction;
    }

    protected void onKill() {}

    private void kill() {
        delete();
        onKill();
    };




}
