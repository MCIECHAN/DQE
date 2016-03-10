package com.company;

/**
 * Created by ciechan on 2016-02-20.
 */
public class PositionOfDetection {
    int X;
    int Y;

    public PositionOfDetection(int newX, int newY) {
        this.X = newX;
        this.Y = newY;
    }

    public String naString() {
        return new String(this.X + " " + this.Y + "\n");
    }

    public PositionOfDetection translationOfPositionOfDetection(PhotonXPosition photonXPosition) {
        System.out.println("Pozycja fotonu X:");
        System.out.print(photonXPosition.getPositonX() + "\n");
        System.out.print(photonXPosition.getPositonY() + "\n");
        System.out.println("Stare LSF:");
        System.out.print(this.X + "\n");
        System.out.print(this.Y + "\n");
        this.X = photonXPosition.getPositonX() + this.X;
        this.Y = photonXPosition.getPositonY() + this.Y;
        System.out.println("Pozycja Detekcji:");
        System.out.print(this.X + "\n");
        System.out.print(this.Y + "\n");
        return this;
    }


}
