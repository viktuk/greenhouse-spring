package xyz.viktuk.greenhouse.entity;

public class PointImpl implements Point {
    private final int x;
    private final int y;

    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Integer getX() {
        return x;
    }

    @Override
    public Integer getY() {
        return y;
    }
}
