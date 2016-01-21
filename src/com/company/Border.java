package com.company;

/**
 * Created by ciechan on 2016-01-20.
 */
public class Border {

    public Position position;
    public Cell cell;
    public Double distance;

    public Border (Position newPosition, Cell newCell, Double newDistance){
        this.position = newPosition;
        this.cell = newCell;
        this.distance = newDistance;
    }

}
