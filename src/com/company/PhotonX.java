package com.company;

import java.util.ArrayList;

public class PhotonX {
    Position position;
    DirectionCoefficient directCoefficient;
    Cell cell;
    Double massAttenuationCoefficientOfXray;
    long numberOfLPhotons;
    ArrayList<LightPhoton> listOfLightPhotons;

    public PhotonX(Position position, DirectionCoefficient directCoefficient, Cell cell, Double massAttenuationCoefficientOfXray, long  numberOfLPhotons) {
        this.position = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.massAttenuationCoefficientOfXray = massAttenuationCoefficientOfXray;
        this.numberOfLPhotons = numberOfLPhotons;
        this.listOfLightPhotons = new ArrayList<>();
    }

    private PhotonX losujDrogeSwobodna(Constants constants) {
        Double r = Math.random();
        Double s = -1 / constants.massAttenuationCoefficientOfXray * Math.log(r);
        Double newX = position.x + directCoefficient.x * s;
        Double newY = position.y + directCoefficient.y * s;
        Double newZ = position.z + directCoefficient.z * s;
        Position newPosition = new Position(newX, newY, newZ);
        return new PhotonX(newPosition, directCoefficient, cell, massAttenuationCoefficientOfXray, numberOfLPhotons);
    }

    public ArrayList<LightPhoton> generateLightPhotons() {

        for (int i = 0; i < this.numberOfLPhotons; i++) {
            this.listOfLightPhotons.add(i, new LightPhoton(position, directCoefficient, cell, false));
        }
        return this.listOfLightPhotons;
    }

}
