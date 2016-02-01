package com.company;

import java.util.ArrayList;
import static java.lang.Math.toIntExact;

public class PhotonX {
    Position position;
    DirectionCoefficient directCoefficient;
    Cell cell;
    Double massAttenuationCoefficientOfXray;
    long numberOfLightPhotons;

    public PhotonX(Position position, DirectionCoefficient directCoefficient, Cell cell, Double massAttenuationCoefficientOfXray, Constants constants) {
        this.position = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.massAttenuationCoefficientOfXray = massAttenuationCoefficientOfXray;
        this.numberOfLightPhotons = wyznaczLiczbeGenerowanychFotonowSwiatla(constants);
    }

    public ArrayList<LightPhoton> generateLightPhotons() {
        ArrayList<LightPhoton> listOfLightPhotons = new ArrayList<>(toIntExact(numberOfLightPhotons));
        for (int i = 0; i < numberOfLightPhotons; i++) {
            listOfLightPhotons.add(i, new LightPhoton(position, DirectionCoefficient.getRandomDirectionCoefficient(directCoefficient), cell, false));
        }
        return listOfLightPhotons;
    }

    private long wyznaczLiczbeGenerowanychFotonowSwiatla(Constants constants) {
        Double sredniaEnergiaPromieniowaniaSwietlnego = 12410 / constants.lengthOfLightWave;
        return Math.round(constants.photonXEnergy * constants.RTGConversionCoefficient / sredniaEnergiaPromieniowaniaSwietlnego);
    }

//    private PhotonX losujDrogeSwobodna(Constants constants) {
//        Double r = Math.random();
//        Double s = -1 / constants.massAttenuationCoefficientOfXray * Math.log(r);
//        Double newX = position.x + directCoefficient.x * s;
//        Double newY = position.y + directCoefficient.y * s;
//        Double newZ = position.z + directCoefficient.z * s;
//        Position newPosition = new Position(newX, newY, newZ);
//        return new PhotonX(newPosition, directCoefficient, cell, massAttenuationCoefficientOfXray, constants);
//    }
}
