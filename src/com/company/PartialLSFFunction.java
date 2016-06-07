package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PartialLSFFunction {

    private Double positionZ;
    public ArrayList<Integer> LSFfuncion;
    public ArrayList<pureLSF> listOfPureLSF;
    private Double probabilityOfDetection;
    private boolean type;

    PartialLSFFunction(Constants constants, Double newPositionZ) {
        this.positionZ = newPositionZ;
        this.LSFfuncion = generateLSFfuncion(constants, newPositionZ);
        this.type = constants.detectorType;
    }


    private ArrayList<Integer> generateLSFfuncion(Constants constants, Double newPositionZ) {
        Position pozycja = new Position(0.0, 0.0, newPositionZ);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = returnCellAdjusted(constants);
        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, constants.massAttenuationCoefficientOfXray, constants.numberOfLightPhotons);

        //ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        //ArrayList<LightPhoton> listaZapisanych = mainSimulationLoop(constants, lista);

        ArrayList<LightPhoton> listaZapisanych = testLoop(fotonX, constants);


        ArrayList<Integer> listOfAllXPositions = setListOfXPositions(listaZapisanych);
        ArrayList<Integer> nonNormalizedLSF = mainLSFLoop(listOfAllXPositions, constants);
        this.setProbabilityOfDetection(nonNormalizedLSF, constants);
        this.setListOfpureLSF(nonNormalizedLSF);
        return nonNormalizedLSF;
    }

    private void setProbabilityOfDetection(ArrayList<Integer> listOfAllXPositions, Constants constants) {
        Double x = (double) listOfAllXPositions.stream().mapToInt(Integer::intValue).sum() / (constants.numberOfLightPhotons * constants.number);
        System.out.print(x + "\n");
        this.probabilityOfDetection = x;
    }

    private ArrayList<LightPhoton> testLoop(PhotonX fotonX, Constants constants) {
        ArrayList<LightPhoton> mainList = new ArrayList<>();
        for (int i = 0; i < constants.number; i++) {
            ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
            ArrayList<LightPhoton> listaZapisanych = mainSimulationLoop(constants, lista);
            mainList.addAll(listaZapisanych);
        }
        return mainList;
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

    private Integer checkNumberOfOccurencesInGivenRange(ArrayList<Integer> values, int minValue, int maxValue) {
        return (values.stream().filter(i -> (i < maxValue) && (i >= minValue))
                .collect(Collectors.toList()).size());
    }

    private ArrayList<Integer> mainLSFLoop(ArrayList<Integer> listOfAllXPositions, Constants constants) {
        ArrayList<Integer> lsf = new ArrayList<>();
        if (listOfAllXPositions.isEmpty()) {
            for (int i = -constants.startPoint; i < constants.startPoint; i = i + constants.resolutionOfDetector) {
                lsf.add(0);
            }
            return lsf;
        } else {
            int start = constants.startPoint;
            for (int i = -start; i < start; i = i + constants.resolutionOfDetector) {
                lsf.add(checkNumberOfOccurencesInGivenRange(listOfAllXPositions, i, i + constants.resolutionOfDetector));
            }
            return lsf;
        }
    }

    private void setListOfpureLSF(ArrayList<Integer> nonNormalizedLSF) {
        ArrayList<pureLSF> list = new ArrayList<>();
        for (int i = 0; i < nonNormalizedLSF.size(); i++) {
            if (nonNormalizedLSF.get(i) != 0) {
                list.add(new pureLSF(i, nonNormalizedLSF.get(i)));
            }
        }
        this.listOfPureLSF = list;
    }

    private Cell returnCellAdjusted(Constants constants) {
        if (constants.detectorType) {
            return new Cell(-(constants.cellWallLength / 2), constants.cellWallLength / 2, -(constants.cellWallLength / 2), constants.cellWallLength / 2, 0, constants.cellHeight.intValue());
        } else {
            return new Cell(-100000, 100000, -100000, 100000, 0, constants.cellHeight.intValue());
        }
    }

    public Double getProbablityOfdetection() {
        return this.probabilityOfDetection;
    }


    public void save(ArrayList<Integer> listOfAllXPositions, Constants constants) {
        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        try {
            String filename = new String(sciezka + constants.detectorType + ".txt");

            File plik = new File(filename);
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            listOfAllXPositions.forEach(p -> {
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

    public void saveLSFfunctions() {
        if (this.LSFfuncion.size() == 0) {
            return;
        } else {
            String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

            try {
                String filename = new String(sciezka + this.getPositionZ() + this.type + ".txt");

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

}


