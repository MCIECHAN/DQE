package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class PartialLSFFunction {

    private Double positionZ;
    private Double probabilityOfDetection;
    public ArrayList<Integer> listOfPositionsOfDetection;
    //public ArrayList<PositionOfDetection> listOfPositionsOfDetection;

    PartialLSFFunction(Constants constants, Double newPositionZ) {
        this.positionZ = newPositionZ;
        this.listOfPositionsOfDetection = generateListOfXPositions(constants, newPositionZ);
        this.probabilityOfDetection = setPropablityOfDetection((double) this.listOfPositionsOfDetection.size(), (double) constants.numberOfLightPhotons);

        System.out.println(this.positionZ + " " + this.probabilityOfDetection);
    }


    //private ArrayList<PositionOfDetection> generateListOfPositionsOfDetection(Constants constants, Double newPositionZ) {
    private ArrayList<Integer> generateListOfXPositions(Constants constants, Double newPositionZ) {
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

        Map<Integer, List<Integer>> intByValue = outputList.stream().collect(Collectors.groupingBy(p -> p));
        System.out.println(intByValue);

        return outputList;
    }

    private Double setPropablityOfDetection(Double No, Double Nd) {
        return No / Nd;
    }

    public Double getPositionZ() {
        return this.positionZ;
    }

    public Double getProbabilityOfDetection() {
        return this.probabilityOfDetection;
    }

    public int getRandomPositionOfDetection() {
        int idx = new Random().nextInt(this.listOfPositionsOfDetection.size());
        return this.listOfPositionsOfDetection.get(idx);
    }

}
