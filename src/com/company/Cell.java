package com.company;

public class Cell {

    public int xMin;
    public int xMax;
    public int yMin;
    public int yMax;
    public int zMin;
    public int zMax;

    public Cell( int xMin, int xMax, int yMin, int yMax, int zMin, int zMax){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public static Boolean wgranicyKomorki(Cell cell, Position position) {
        return position.x > cell.xMin && position.x < cell.xMax && position.y > cell.yMin && position.y < cell.yMax && position.z > cell.zMin && position.z < cell.zMax;
    }


}
