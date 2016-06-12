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

    public Double distanceBetween(Position that) {
        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2) + Math.pow(this.z - that.z, 2));
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
}
