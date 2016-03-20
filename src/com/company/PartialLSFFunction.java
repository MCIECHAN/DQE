package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class PartialLSFFunction {

    private Double positionZ;
    public ArrayList<Double> LSFfuncion;

    PartialLSFFunction(Constants constants, Double newPositionZ) {
        this.positionZ = newPositionZ;
        this.LSFfuncion = generateLSFfuncion(constants, newPositionZ);
    }


    private ArrayList<Double> generateLSFfuncion(Constants constants, Double newPositionZ) {
        Position pozycja = new Position(0.0, 0.0, newPositionZ);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(-(constants.cellWallLength / 2), constants.cellWallLength / 2, -(constants.cellWallLength / 2), constants.cellWallLength / 2, 0, constants.cellHeight.intValue());
        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, constants.massAttenuationCoefficientOfXray, constants.numberOfLightPhotons);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        ArrayList<LightPhoton> listaZapisanych = mainSimulationLoop(constants, lista);
        ArrayList<Integer> listOfAllXPositions = setListOfXPositions(listaZapisanych);

        return normalizeLSF(mainLSFLoop(listOfAllXPositions, constants));
    }

    private static ArrayList<LightPhoton> mainSimulationLoop(Constants zmienne, ArrayList<LightPhoton> lista) {
        while (!lista.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        return lista;
    }


    private ArrayList<Integer> setListOfXPositions(ArrayList<LightPhoton> listaZapisanych) {
        ArrayList<Integer> outputList = new ArrayList<>();
        listaZapisanych.forEach((lightPhoton) -> outputList.add(lightPhoton.getPositionX()));
        return outputList;
    }

    public Double getPositionZ() {
        return this.positionZ;
    }

    private int checkNumberOfOccurencesInGivenRange(ArrayList<Integer> values, int minValue, int maxValue) {
        return values.stream().filter(i -> (i < maxValue) && (i >= minValue))
                .collect(Collectors.toList()).size();
    }

    private int returnRange(int minValue, int maxValue, int resolutionOfDetector) {
        int range = 0;
        if (Math.abs(minValue) <= Math.abs(maxValue)) {
            range = (Math.abs(maxValue) + resolutionOfDetector);
        } else {
            range = (Math.abs(minValue) + resolutionOfDetector);
        }
/*        System.out.println("Wyliczony zakres:");
        System.out.print(range+"\n");*/
        return range;
    }

    private ArrayList<Integer> mainLSFLoop(ArrayList<Integer> listOfAllXPositions, Constants constants) {
/*        System.out.println("Rozmiar listy pozycji:");
        System.out.print(listOfAllXPositions.size()+"\n");*/
        int minvalue = Collections.min(listOfAllXPositions);
/*        System.out.println("minvalue");
        System.out.print(minvalue+"\n");*/
        int maxValue = Collections.max(listOfAllXPositions);
/*        System.out.println("maxValue");
        System.out.print(maxValue+"\n");*/
        int range = returnRange(minvalue, maxValue, constants.resolutionOfDetector) + constants.resolutionOfDetector / 2;
        ArrayList<Integer> lsf = new ArrayList<>();
        //System.out.println("Pętla LSF:");
        for (int i = (-range / constants.resolutionOfDetector - constants.resolutionOfDetector / 2); i <= range / constants.resolutionOfDetector + constants.resolutionOfDetector / 2; i = i + constants.resolutionOfDetector) {
            lsf.add(checkNumberOfOccurencesInGivenRange(listOfAllXPositions, i, i + constants.resolutionOfDetector));
            System.out.print(checkNumberOfOccurencesInGivenRange(listOfAllXPositions, i, i + constants.resolutionOfDetector));
        }
/*        System.out.println("Rozmiar lsf:");
        System.out.print(lsf.size());
        System.out.println("Zliczenia");*/
        lsf.forEach(p -> System.out.print(p + "\n"));
        return lsf;
    }

    private ArrayList<Double> normalizeLSF(ArrayList<Integer> entryLSF) {
        int sum = entryLSF.stream().mapToInt(i -> i.intValue()).sum();
        System.out.println("Uwaga suma:" + "\n");
        System.out.print(sum + "\n");
        ArrayList<Double> normalizedLSF = new ArrayList<>();
        for (int i = 0; i < entryLSF.size(); i++) {
            double norm = (double) entryLSF.get(i);
            normalizedLSF.add(norm / sum);
        }
        return normalizedLSF;
    }

}
