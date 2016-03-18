package com.company;

import java.util.Random;

public class PhotonXPosition {
    private int X;
    private int Y;
    private int Z;

    public PhotonXPosition(Constants constants) {
        this.X = getSingleCoordinate(0, constants.numberOfColumns * constants.cellWallLength);
        this.Y = getSingleCoordinate(0, constants.numberOfRows * constants.cellWallLength);
/*        this.X = getSingleCoordinate(0, 5000);
        this.Y = getSingleCoordinate(0, 5000);*/
        this.Z = getSingleCoordinate(0, constants.cellHeight.intValue());
        System.out.println("Pozycja nowego fotonu X:");
        System.out.print(this.Z);
    }

    private int getSingleCoordinate(int rangeMin, int rangeMax) {
        Random r = new Random();
        return r.ints(rangeMin, (rangeMax + 1)).findFirst().getAsInt();
    }

    public int getPositonZ() {
        return this.Z;
    }

    public int getPositonX() {
        return this.X;
    }

    public int getPositonY() {
        return this.Y;
    }
}
