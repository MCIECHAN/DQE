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
        return new PositionOfDetection(this.X + photonXPosition.getPositonX(), this.Y +photonXPosition.getPositonY());
    }


}
