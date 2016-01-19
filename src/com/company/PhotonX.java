package com.company;

import com.sun.xml.internal.bind.v2.model.core.MaybeElement;

/**
 * Created by ciechan on 2016-01-18.
 */
public class PhotonX {
    Position position;
    DirectionCooficient directCoefficient;
    Cell cell;
    Double massAtCof;
    int numberOfLPhotons;

    public PhotonX(Position newPosition, DirectionCooficient newDirectionCooficient, Cell newCell, Double newmassAtCof, int newnumberOfLPhotons){
        position = newPosition;
        directCoefficient = newDirectionCooficient;
        cell = newCell;
        massAtCof = newmassAtCof;
        numberOfLPhotons = newnumberOfLPhotons;
    }

    LightPhoton[] getLightPhotons(int numberOfLPhotons) {
        LightPhoton[] listOfLightPhoton;
        for (int i= 1; i<=numberOfLPhotons; i++) {
            listOfLightPhoton[i] = LightPhoton(position, directCoefficient, cell, false);
            }
        return listOfLightPhoton;
    }

}
