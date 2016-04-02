package com.company;

import java.util.Random;

public class PhotonXPosition {
    public Position position;
    public DirectionCoefficient directionCoefficient;

    public PhotonXPosition(Position newPosition, DirectionCoefficient newDirectionCoefficient) {
        this.position = newPosition;
        this.directionCoefficient = newDirectionCoefficient;
    }

    private int getSingleCoordinate(int rangeMin, int rangeMax) {
        Random r = new Random();
        return r.ints(rangeMin, (rangeMax + 1)).findFirst().getAsInt();
    }

    public void displayPhotonXPosition(){
        System.out.println("Pozycja X: " + this.position.x+" Pozycja Y: "+this.position.y+" Pozycja Z: "+this.position.z);
    }

    public void displayPhotonXDirectionCooficient(){
        System.out.println("Kier X: " + this.directionCoefficient.x+" Kier Y: "+this.directionCoefficient.y+" Kier Z: "+this.directionCoefficient.z);
    }

}
