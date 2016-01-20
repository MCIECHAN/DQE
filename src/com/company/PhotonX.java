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

    private PhotonX losujDrogeSwobodnaPhotonuX (Constants constants) {
        Double r = Math.random();
        Double s = -1 / constants.massAtCof * Math.log(r);
        Double newX = position.x + directCoefficient.x * s;
        Double newY = position.y + directCoefficient.y * s;
        Double newZ = position.z + directCoefficient.z * s;
        Position newPosition = new Position(newX, newY, newZ);
        return new PhotonX(newPosition, directCoefficient, cell, massAtCof, numberOfLPhotons);
    }

    private LightPhoton[] getLightPhotons(int numberOfLPhotons) {
        LightPhoton[] listOfLightPhoton = null; // Tu dodałem null, bo krzyczał że może być cos niezainiclajizowane
        for (int i = 0; i < numberOfLPhotons; i++) {
            listOfLightPhoton[i] = new LightPhoton(position, directCoefficient, cell, false);
        }
        return listOfLightPhoton;
    }

}
