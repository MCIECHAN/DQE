package com.company;

public class PhotonX {
    Position position;
    DirectionCoefficient directCoefficient;
    Cell cell;
    Double massAtCof;
    int numberOfLPhotons;

    public PhotonX(Position newPosition, DirectionCoefficient newDirectionCoefficient, Cell newCell, Double newmassAtCof, int newnumberOfLPhotons) {
        position = newPosition;
        directCoefficient = newDirectionCoefficient;
        cell = newCell;
        massAtCof = newmassAtCof;
        numberOfLPhotons = newnumberOfLPhotons;
    }

    LightPhoton[] getLightPhotons(int numberOfLPhotons) {
        LightPhoton[] listOfLightPhoton;
        for (int i = 1; i <= numberOfLPhotons; i++) {
            listOfLightPhoton[i] = LightPhoton(position, directCoefficient, cell, false);
        }
        return listOfLightPhoton;
    }

}
