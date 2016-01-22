package com.company;

import java.util.ArrayList;
import java.util.Collections;

public class Position {
    Double x;
    Double y;
    Double z;

    public Position(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public Position getClosestCrossBorderPoint( ArrayList <Position> coordinatesOfCrossing, Position currentPosition){
        //TODO Chcę żeby to zwróciło  position o najmniejszej odległości
        coordinatesOfCrossing.forEach(Position -> getDistanceBetweenTwoPositions(currentPosition));
            return getClosestCrossBorderPoint min(getDistanceBetweenTwoPositions(currentPosition));
    }

    public Double getDistanceBetweenTwoPositions (Position position2){
        return Math.sqrt(Math.pow(this.x - position2.x, 2) + Math.pow(this.y - position2.y, 2) + Math.pow(this.z - position2.z, 2));
    }



}
