package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.company.Position.getSingleCoordinate;

public class LSF {

    private ArrayList<PartialLSFFunction> DetectorLSFFunction;

    LSF(Constants constants) {
        this.DetectorLSFFunction = getDetectorLSFFunction(constants);
    }

    private ArrayList<PartialLSFFunction> getDetectorLSFFunction(Constants constants) {

        ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions = createListOfPartialLSFFunctions(constants);
        //saveLSFfunctions(ListOfPartialLSFFunctions);

        ArrayList<PhotonXPosition> lista = generatePhotonXPositionsForNPS(constants);

        //ArrayList<PhotonXPosition> XPhotonsPositions = generateXPhotonsPositions(constants);
/*
        XPhotonsPositions.forEach(position -> {
            int index = getIndexOfClosestZPosition(position.getPositonZ(), ListOfPartialLSFFunctions);
            for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                Double rnd = Math.random();
                {

                }
            }
        });

        // savePositionsOfDetection(listOfPositionsOfDetection);*/

        return ListOfPartialLSFFunctions;
    }

/*    private ArrayList<PhotonXPosition> generateXPhotonsPositions(Constants constants) {
        ArrayList<PhotonXPosition> XPhotonsPositions = new ArrayList<>();
        for (int i = 0; i < constants.numberOfXPhotons; i++) {
            PhotonXPosition nowaPozycja = new PhotonXPosition(constants);
            XPhotonsPositions.add(nowaPozycja);
        }
        return XPhotonsPositions;
    }*/

    private ArrayList<PartialLSFFunction> createListOfPartialLSFFunctions(Constants constants) {
        ArrayList<PartialLSFFunction> listOfPartialLSFFunctions = new ArrayList<>();
        Double tmp = this.getInterval(constants);
        for (Double i = 3.0; i <= constants.cellHeight; i = i + tmp) {
            listOfPartialLSFFunctions.add(new PartialLSFFunction(constants, i));
        }
        return listOfPartialLSFFunctions;
    }

    private Double getInterval(Constants constants) {
        return ((constants.cellHeight - 3.0) - 3.0) / (constants.numberOfParticleLSFFunctions - 1);
    }

    private int getIndexOfClosestZPosition(int positionZ, ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions) {
        int index = 0;
        Double currentDistance = Math.abs(positionZ - ListOfPartialLSFFunctions.get(0).getPositionZ());
        for (int i = 0; i < ListOfPartialLSFFunctions.size(); i++) {
            Double pretendingDistance = Math.abs(positionZ - ListOfPartialLSFFunctions.get(i).getPositionZ());
            if (pretendingDistance < currentDistance) {
                currentDistance = pretendingDistance;
                index = i;
            }
        }
        return index;
    }

    private ArrayList<PhotonXPosition> generatePhotonXPositionsForNPS(Constants constants) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForNPS = new ArrayList<PhotonXPosition>();
        for (int i = 0; i < constants.numberOfNPSXPhotons; i++) {
            Position position = new Position((double) constants.numberOfColumns * constants.cellWallLength / 2, (double) constants.numberOfRows * constants.cellWallLength / 2, getSingleCoordinate(0.0, constants.cellHeight));
            DirectionCoefficient directionCoefficient = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
            listOFPhotonXPositionsForNPS.add(new PhotonXPosition(position, directionCoefficient));
        }
        listOFPhotonXPositionsForNPS.forEach(photonXPosition -> photonXPosition.position.makeOneStep(constants, photonXPosition.directionCoefficient));
        listOFPhotonXPositionsForNPS = listOFPhotonXPositionsForNPS.stream()
                .filter(photonXPosition -> photonXPosition.position.inDetector(constants))
                .collect(Collectors.toCollection(ArrayList::new));
        return listOFPhotonXPositionsForNPS;
    }


    private ArrayList<PhotonXPosition> generatePhotonXPositionsForMTF(Constants constants) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForMTF = new ArrayList<PhotonXPosition>();

        return listOFPhotonXPositionsForMTF;
    }
























    private void saveLSFfunctions(ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions) {

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        ListOfPartialLSFFunctions.forEach(LSFfunction -> {
                    if (LSFfunction.LSFfuncion.isPresent()) {

                        try {
                            String filename = new String(sciezka + LSFfunction.getPositionZ() + ".txt");
                            File plik = new File(filename);
                            if (!plik.exists()) {
                                plik.createNewFile();
                            }
                            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
                            BufferedWriter bw = new BufferedWriter(fw);

                            LSFfunction.LSFfuncion.get().forEach(lightPhoton -> {
                                try {
                                    bw.write(lightPhoton + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            bw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else {
                        return;
                    }
                }

        );

    }

/*    private void savePositionsOfDetection(ArrayList<PositionOfDetection> listOfPositionOfDetection) {

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");


        try {
            String filename = new String(sciezka + "LSF" + ".txt");
            File plik = new File(filename);
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            listOfPositionOfDetection.forEach(positionOfDetection -> {
                try {
                    bw.write(positionOfDetection.naString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }*/
}
