package com.company;

import java.util.ArrayList;

public class PhotonX {
    Position position;
    DirectionCoefficient directCoefficient;
    Cell cell;
    Double massAtCof;
    int numberOfLPhotons;

    public PhotonX(Position position, DirectionCoefficient directCoefficient, Cell cell, Double massAtCof, int numberOfLPhotons) {
        this.position = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.massAtCof = massAtCof; //TODO: pełna nazwa, bo nie wiadomo co to jest ;)
        this.numberOfLPhotons = numberOfLPhotons;
    }

    private PhotonX losujDrogeSwobodnaPhotonuX (Constants constants) {
        Double r = Math.random();
        Double s = -1 / constants.massAtCof * Math.log(r);
        Double newX = position.x + directCoefficient.x * s;
        Double newY = position.y + directCoefficient.y * s;
        Double newZ = position.z + directCoefficient.z * s;
        Position newPosition = new Position(newX, newY, newZ);
        return new PhotonX(newPosition, directCoefficient, cell, massAtCof, numberOfLPhotons);
    }

    private ArrayList<LightPhoton> getLightPhotons(int numberOfLPhotons) {
        ArrayList<LightPhoton> listOfLightPhoton = new ArrayList<>(); // Tu dodałem null, bo krzyczał że może być cos niezainiclajizowane
        for (int i = 0; i < numberOfLPhotons; i++) {
            listOfLightPhoton.add(i, new LightPhoton(position, directCoefficient, cell, false));
        }
        return listOfLightPhoton;
    }

}
