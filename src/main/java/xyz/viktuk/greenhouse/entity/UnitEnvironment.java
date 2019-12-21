package xyz.viktuk.greenhouse.entity;

import java.io.Serializable;

public class UnitEnvironment implements Serializable {
    private int maxX;
    private int maxY;
    private int[][] matrix;

    public UnitEnvironment(int maxX, int maxY, int defaultValue) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.matrix = new int[maxX][maxY];
        for (int x = 0; x < getMaxX(); x++) {
            for (int y = 0; y < getMaxY(); y++) {
                setValue(x, y, defaultValue);
            }
        }
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getValue(int x, int y) {
        return matrix[x][y];
    }


    public void setValue(int x, int y, int value) {
        matrix[x][y] = value;
    }

    public int[][] getMatrix() {
        return matrix;
    }
}