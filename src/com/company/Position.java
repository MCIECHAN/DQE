package com.company;

public class Position {
    Double x;
    Double y;
    Double z;

    public Position(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getDistanceBetweenTwoPositions (Position position2){
        System.out.println("Licze odległosc miedzy fotonami");
        return Math.sqrt(Math.pow(this.x - position2.x, 2) + Math.pow(this.y - position2.y, 2) + Math.pow(this.z - position2.z, 2));
    }

}
