package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by ciechan on 2016-03-06.
 */
public class LSF {

    private ArrayList<PositionOfDetection> DetectorLSFFunction;

    LSF(Constants constants) {
        this.DetectorLSFFunction = getDetectorLSFFunction(constants);
    }

    private ArrayList<PositionOfDetection> getDetectorLSFFunction(Constants constants) {

        ArrayList<PositionOfDetection> listOfPositionsOfDetection = new ArrayList<PositionOfDetection>();
        ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions = createListOfPartialLSFFunctions(constants);
        saveLSFfunctions(ListOfPartialLSFFunctions);
        ArrayList<PhotonXPosition> XPhotonsPositions = generateXPhotonsPositions(constants);

        XPhotonsPositions.forEach(position -> {
            int index = getIndexOfClosestZPosition(position.getPositonZ(), ListOfPartialLSFFunctions);

            System.out.println(ListOfPartialLSFFunctions.get(index).getPropablityOfDetection().toString());
            for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                Double rnd = Math.random();
                if (rnd <= ListOfPartialLSFFunctions.get(index).getPropablityOfDetection()) {
                    listOfPositionsOfDetection.add(ListOfPartialLSFFunctions.get(index).getRandomPositionOfDetection().translationOfPositionOfDetection(position));
                }
            }
        });
        System.out.println(listOfPositionsOfDetection.size());
        savePositionsOfDetection(listOfPositionsOfDetection);
        return listOfPositionsOfDetection;
    }

    private ArrayList<PhotonXPosition> generateXPhotonsPositions(Constants constants) {
        ArrayList<PhotonXPosition> XPhotonsPositions = new ArrayList<PhotonXPosition>();

        for (int i = 1; i <= constants.numberOfXPhotons; i++) {
            PhotonXPosition nowaPozycja = new PhotonXPosition(constants);
            XPhotonsPositions.add(nowaPozycja);
        }
        return XPhotonsPositions;
    }

    private ArrayList<PartialLSFFunction> createListOfPartialLSFFunctions(Constants constants) {
        ArrayList<PartialLSFFunction> listOfPartialLSFFunctions = new ArrayList<PartialLSFFunction>();
        Double tmp = this.getInterval(constants);
        for (Double i = 3.0; i <= constants.cellHeight; i = i + tmp) {
            listOfPartialLSFFunctions.add(new PartialLSFFunction(constants, i));
        }
        return listOfPartialLSFFunctions;
    }

    private Double getInterval(Constants constants) {
        Double tmp = ((constants.cellHeight - 3.0) - 3.0) / (constants.numberOfParticleLSFFunctions - 1);
        return tmp;
    }

    private int getIndexOfClosestZPosition(int positionZ, ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions) {
        int indeks = 0;
        Double currentDistance = Math.abs(positionZ - ListOfPartialLSFFunctions.get(0).getPositonZ());
        for (int i = 0; i < ListOfPartialLSFFunctions.size(); i++) {
            Double pretendingDistance = Math.abs(positionZ - ListOfPartialLSFFunctions.get(i).getPositonZ());
            if (pretendingDistance < currentDistance) {
                currentDistance = pretendingDistance;
                indeks = i;
            }
        }
        return indeks;
    }

    private void saveLSFfunctions(ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions) {

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        ListOfPartialLSFFunctions.forEach(LSFfunction -> {

                    try {
                        String filename = new String(sciezka + LSFfunction.getPositonZ() + ".txt");
                        File plik = new File(filename);
                        if (!plik.exists()) {
                            plik.createNewFile();
                        }
                        FileWriter fw = new FileWriter(plik.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);

                        LSFfunction.listOfPositionsOfDetection.forEach(lightPhoton -> {
                            try {
                                bw.write(lightPhoton.naString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        );

    }

    private void savePositionsOfDetection(ArrayList<PositionOfDetection> listOfPositionOfDetection) {

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


    }
}
