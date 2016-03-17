package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by ciechan on 2016-02-20.
 */
public class PartialLSFFunction {

    private Double positonZ;
    private Double propablityOfDetection;
    public ArrayList<Integer> listOfPositionsOfDetection;
    //public ArrayList<PositionOfDetection> listOfPositionsOfDetection;

    PartialLSFFunction(Constants constants, Double newPositionZ) {
        this.positonZ = newPositionZ;
        this.listOfPositionsOfDetection = generateListOfXPositions(constants, newPositionZ);
        this.propablityOfDetection = setPropablityOfDetection((double) this.listOfPositionsOfDetection.size(), (double) constants.numberOfLightPhotons);

        System.out.println(this.positonZ + " " + this.propablityOfDetection);
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

        ArrayList<PositionOfDetection> outputList = new ArrayList<PositionOfDetection>();
        listaZapisanych.forEach((lightPhoton) -> {
            outputList.add(lightPhoton.getPositionOfDetection());
        });
        return outputList;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private ArrayList<Integer> setListOfXPositions(ArrayList<LightPhoton> listaZapisanych) {

        ArrayList<Integer> outputList = new ArrayList<>();
        listaZapisanych.forEach((lightPhoton) -> {
            outputList.add(lightPhoton.getPositionX());
        });

        Map<Integer,List<Integer>> intByValue = outputList.stream().collect(Collectors.groupingBy(p-> p.intValue()));
        System.out.println(intByValue);

        return outputList;
    }

    private Double setPropablityOfDetection(Double No, Double Nd) {
        return No / Nd;
    }

    public Double getPositonZ() {
        return this.positonZ;
    }

    public Double getPropablityOfDetection() {
        return this.propablityOfDetection;
    }

    public int getRandomPositionOfDetection() {
        int idx = new Random().nextInt(this.listOfPositionsOfDetection.size());
        return this.listOfPositionsOfDetection.get(idx);
    }

}
