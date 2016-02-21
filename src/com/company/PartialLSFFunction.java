package com.company;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ciechan on 2016-02-20.
 */
public class PartialLSFFunction {

    private Double positonZ;
    private Double propablityOfDetection;
    private ArrayList<PositionOfDetection> listOfPositionsOfDetection;

    PartialLSFFunction (Constants constants,Double newPositionZ){
        this.positonZ = newPositionZ;
        this.listOfPositionsOfDetection = generateListOfPositionsOfDetection(constants, newPositionZ);

        //this.setPropablityOfDetection(,listaZapisanych.size());
    }




    private ArrayList<PositionOfDetection> generateListOfPositionsOfDetection(Constants constants, Double newPositionZ){
        Position pozycja = new Position(0.0, 0.0, newPositionZ);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(-(constants.cellWallLength/2), 5, -(constants.cellWallLength/2), 5, 0, constants.cellHeight);
        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, constants.massAttenuationCoefficientOfXray, constants.numberOfLightPhotons);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        ArrayList<LightPhoton> listaZapisanych = mainLSFLoop(constants, lista);

    }

    private static ArrayList<LightPhoton> mainLSFLoop(Constants zmienne, ArrayList<LightPhoton> lista) {
        while (!lista.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        return lista;
    }

/*    private Double setPropablityOfDetection (Double No, Double Nd){
        return No/Nd;
    }*/
}
