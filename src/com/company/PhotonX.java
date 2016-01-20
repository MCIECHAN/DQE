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

    ArrayList<LightPhoton> getLightPhotons(int numberOfLPhotons) {
        ArrayList<LightPhoton> listOfLightPhoton = new ArrayList<>();
        for (int i = 1; i <= numberOfLPhotons; i++) {
            listOfLightPhoton.add(i, new LightPhoton(position, directCoefficient, cell, false));
        }
        return listOfLightPhoton;
    }

}
