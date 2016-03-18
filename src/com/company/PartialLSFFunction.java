package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PartialLSFFunction {

    private Double positionZ;
    public ArrayList<Integer> LSFfuncion;

    PartialLSFFunction(Constants constants, Double newPositionZ) {
        this.positionZ = newPositionZ;
        this.LSFfuncion = generateLSFfuncion(constants, newPositionZ);
    }


    //private ArrayList<PositionOfDetection> generateListOfPositionsOfDetection(Constants constants, Double newPositionZ) {
    private ArrayList<Integer> generateLSFfuncion(Constants constants, Double newPositionZ) {
        Position pozycja = new Position(0.0, 0.0, newPositionZ);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(-(constants.cellWallLength / 2), constants.cellWallLength / 2, -(constants.cellWallLength / 2), constants.cellWallLength / 2, 0, constants.cellHeight.intValue());
        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, constants.massAttenuationCoefficientOfXray, constants.numberOfLightPhotons);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        ArrayList<LightPhoton> listaZapisanych = mainLSFLoop(constants, lista);

        return setListOfXPositions(listaZapisanych);
        //return setListOfPositionsOfDetection(listaZapisanych);
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

    private ArrayList<PositionOfDetection> setListOfPositionsOfDetection(ArrayList<LightPhoton> listaZapisanych) {

        ArrayList<PositionOfDetection> outputList = new ArrayList<>();
        listaZapisanych.forEach((lightPhoton) -> outputList.add(lightPhoton.getPositionOfDetection()));
        return outputList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private ArrayList<Integer> setListOfXPositions(ArrayList<LightPhoton> listaZapisanych) {

        ArrayList<Integer> outputList = new ArrayList<>();
        listaZapisanych.forEach((lightPhoton) -> outputList.add(lightPhoton.getPositionX()));
        System.out.print(checkNumberOfOccurencesInGivenRange(outputList,-2,2));

        return outputList;
    }

    public Double getPositionZ() {
        return this.positionZ;
    }


    private int checkNumberOfOccurencesInGivenRange(ArrayList<Integer> values,int minValue, int maxValue) {
         return values.stream().filter(i -> (i < maxValue)&&(i >=minValue))
                  .collect(Collectors.toList()).size();
    }

}
