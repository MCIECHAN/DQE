package com.company;

import java.util.Random;

/**
 * Created by ciechan on 2016-03-10.
 */
public class PhotonXPosition {
    private int X;
    private int Y;
    private int Z;

    public PhotonXPosition(Constants constants) {
        this.X = getSingleCoordinate(0, constants.numberOfColumns * constants.cellWallLength);
        this.Y = getSingleCoordinate(0, constants.numberOfRows * constants.cellWallLength);
        this.Z = getSingleCoordinate(90, constants.cellHeight.intValue());
        System.out.print(this.X + "\n");
        System.out.print(this.Y + "\n");
        System.out.print(this.Z + "\n");
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
