package com.company;

import java.util.Random;

public class Position {
    Double x;
    Double y;
    Double z;

    public Position(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double distanceBetween(Position that) {
        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2) + Math.pow(this.z - that.z, 2));
    }

    public Double distanceInXdimension(Position that) {
        return this.x-that.x;
    }

    public static Double getSingleCoordinate(Double rangeMin, Double rangeMax) {
        Random r = new Random();
        double singleCoordinate = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return singleCoordinate;
    }

    public void makeOneStepForMTForNPS(Constants constants, DirectionCoefficient directCoefficient) {
        Double r = Math.random();
        Double s = (-1 * Math.log(r)) / constants.massAttenuationCoefficientOfXray;
        this.z = this.z + directCoefficient.z * s;
    }

    public boolean inDetector(Constants constants) {
        boolean xInBoarder = this.x >= 0 && this.x <= constants.cellWallLength * constants.numberOfColumns;
        boolean yInBoarder = this.y >= 0 && this.y <= constants.cellWallLength * constants.numberOfRows;
        boolean zInBoarder = this.z >= 0 && this.z <= constants.cellHeight;
        if (xInBoarder && yInBoarder && zInBoarder) {
            return true;
        } else {
            return false;
        }
    }

    public void displayPosition() {
        System.out.println("Pozycja X: " + this.x + " Pozycja Y: " + this.y + " Pozycja Z: " + this.z);
    }

}
