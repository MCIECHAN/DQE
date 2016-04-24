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
    public ArrayList<Integer> longLSFfuncion;
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
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        ArrayList<LightPhoton> listaZapisanych = mainSimulationLoop(constants, lista);
        ArrayList<Integer> listOfAllXPositions = setListOfXPositions(listaZapisanych);
        ArrayList<Integer> nonNormalizedLSF = mainLSFLoop(listOfAllXPositions, constants);
        this.setLongLSFfuncion(listOfAllXPositions, constants);
        this.setProbabilityOfDetection(nonNormalizedLSF, constants);
        return nonNormalizedLSF;
    }

    private void setProbabilityOfDetection(ArrayList<Integer> listOfAllXPositions, Constants constants) {
        Double x = (double) listOfAllXPositions.stream().mapToInt(Integer::intValue).sum() / constants.numberOfLightPhotons;
        System.out.print(x + "\n");
        this.probabilityOfDetection = x;
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

    private void setLongLSFfuncion(ArrayList<Integer> listOfAllXPositions, Constants constants) {
        ArrayList<Integer> lsf = new ArrayList<>();
        if (listOfAllXPositions.isEmpty()) {
            for (int i = -constants.startPoint; i < constants.startPoint; i = i + constants.resolutionOfDetector) {
                lsf.add(0);
            }
            this.longLSFfuncion = lsf;
        } else {
            int start = constants.startPoint*2;
            for (int i = -start; i < start; i = i + constants.resolutionOfDetector) {
                lsf.add(checkNumberOfOccurencesInGivenRange(listOfAllXPositions, i, i + constants.resolutionOfDetector));
            }
            this.longLSFfuncion = lsf;
        }
    }

/*    private ArrayList<Double> normalizeLSF(Optional<ArrayList<Integer>> entryLSF, Constants constants) {
        ArrayList<Double> normalizedLSF = new ArrayList<>();
        if (entryLSF.isPresent()) {
            for (int i = 0; i < entryLSF.get().size(); i++) {
                double norm = (double) entryLSF.get().get(i);
                normalizedLSF.add(norm / constants.numberOfLightPhotons);
            }
            return normalizedLSF;
        } else {
            for (int i = -constants.startPoint; i < constants.startPoint; i = i + constants.resolutionOfDetector) {
                normalizedLSF.add(0.0);
            }
            return normalizedLSF;
        }
    }*/

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


