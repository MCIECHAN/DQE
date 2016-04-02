package com.company;

import java.util.ArrayList;

import static java.lang.Math.toIntExact;

public class PhotonX {
    Position position;
    DirectionCoefficient directCoefficient;
    Cell cell;
    Double massAttenuationCoefficientOfXray;
    long numberOfLightPhotons;

    public PhotonX(Position position, DirectionCoefficient directCoefficient, Cell cell, Double massAttenuationCoefficientOfXray, long newNumberOfLightPhotons) {
        this.position = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.massAttenuationCoefficientOfXray = massAttenuationCoefficientOfXray;
        this.numberOfLightPhotons = newNumberOfLightPhotons;
    }

    public ArrayList<LightPhoton> generateLightPhotons() {
        ArrayList<LightPhoton> listOfLightPhotons = new ArrayList<>(toIntExact(numberOfLightPhotons));
        for (int i = 0; i < numberOfLightPhotons; i++) {
            listOfLightPhotons.add(i, new LightPhoton(position, DirectionCoefficient.getRandomDirectionCoefficient(directCoefficient), cell, false));
        }
        return listOfLightPhotons;
    }
}
