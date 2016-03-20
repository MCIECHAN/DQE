package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
        return range;
    }

    private ArrayList<Integer> mainLSFLoop(ArrayList<Integer> listOfAllXPositions, Constants constants) {
        int minvalue = Collections.min(listOfAllXPositions);
        int maxValue = Collections.max(listOfAllXPositions);
        int range = returnRange(minvalue, maxValue, constants.resolutionOfDetector) + constants.resolutionOfDetector / 2;
        ArrayList<Integer> lsf = new ArrayList<>();
        for (int i = (-range / constants.resolutionOfDetector - constants.resolutionOfDetector / 2); i <= range / constants.resolutionOfDetector + constants.resolutionOfDetector / 2; i = i + constants.resolutionOfDetector) {
            lsf.add(checkNumberOfOccurencesInGivenRange(listOfAllXPositions, i, i + constants.resolutionOfDetector));
        }
        return lsf;
    }

    private ArrayList<Double> normalizeLSF(ArrayList<Integer> entryLSF) {
        int sum = entryLSF.stream().mapToInt(i -> i.intValue()).sum();
        ArrayList<Double> normalizedLSF = new ArrayList<>();
        for (int i = 0; i < entryLSF.size(); i++) {
            double norm = (double) entryLSF.get(i);
            normalizedLSF.add(norm / sum);
        }
        return normalizedLSF;
    }

    public Double getPositonZ() {
        return this.positionZ;
    }

    public void saveLSFfunctions() {

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        try {
            String filename = new String(sciezka + this.getPositionZ() + ".txt");
            File plik = new File(filename);
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            this.LSFfuncion.forEach(p -> {
                try {
                    bw.write(p.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


