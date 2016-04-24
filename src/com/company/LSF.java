package com.company;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

import static com.company.Position.getSingleCoordinate;

public class LSF {

    private ArrayList<PartialLSFFunction> DetectorLSFFunction;

    LSF(Constants constants) {
        this.DetectorLSFFunction = getDetectorLSFFunction(constants);
    }

    private ArrayList<PartialLSFFunction> getDetectorLSFFunction(Constants constants) {
        ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions = createListOfPartialLSFFunctions(constants);
        ListOfPartialLSFFunctions = returnEndListOfPartialLSFFunctions(ListOfPartialLSFFunctions);
        saveLSFfunctions(ListOfPartialLSFFunctions, constants);

        ArrayList<Integer> LSFOfMTF = countMTF(ListOfPartialLSFFunctions, constants);
        saveLSF(LSFOfMTF, constants, true);


        ArrayList<Integer> LSFofNPS = countNPS(ListOfPartialLSFFunctions, constants);
        saveLSF(LSFofNPS, constants, false);

        return ListOfPartialLSFFunctions;
    }

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

    private ArrayList<PartialLSFFunction> returnEndListOfPartialLSFFunctions(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions) {
        ArrayList<PartialLSFFunction> endListOfPartialLSFFunctions = new ArrayList<>();
        for (int i = 0; i < listOfPartialLSFFunctions.size(); i++) {
            if (!listOfPartialLSFFunctions.get(i).LSFfuncion.isEmpty()) {
                endListOfPartialLSFFunctions.add(listOfPartialLSFFunctions.get(i));
            }
        }
        return endListOfPartialLSFFunctions;
    }

    private ArrayList<Integer> countMTF(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, Constants constants) {
        ArrayList<Integer> endVector = new ArrayList();
        for (int k = 0; k < listOfPartialLSFFunctions.get(0).LSFfuncion.size(); k++) {
            endVector.add(0);
        }
        for (int l = 0; l < constants.numberOfMTFXPhotonsPositions; l++) {
            ArrayList<PhotonXPosition> list = generatePhotonXPositionsForMTF(constants);
            list.forEach(photonXPosition -> {
                int idx = getIndexOfClosestZPosition(photonXPosition.position.z, listOfPartialLSFFunctions);
                if (listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().count() != 0) {
                    for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                        Double randomVariable = Math.random();
                        if (randomVariable <= listOfPartialLSFFunctions.get(idx).getProbablityOfdetection()) {
                            Random rand = new Random();
                            int randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).LSFfuncion.size())));
                            while (listOfPartialLSFFunctions.get(idx).LSFfuncion.get(randomPosition) == 0) {
                                randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).LSFfuncion.size())));
                            }
                            endVector.set(randomPosition, (endVector.get(randomPosition) + 1));
                        }
                    }
                }
            });
        }
        return endVector;
    }

    private ArrayList<PhotonXPosition> generatePhotonXPositionsForMTF(Constants constants) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForMTF = new ArrayList<PhotonXPosition>();
        for (int i = 0; i < constants.numberOfMTFXPhotons; i++) {
            Position position = new Position((double) constants.numberOfColumns * constants.cellWallLength / 2, (double) constants.numberOfRows * constants.cellWallLength / 2, 0.0);
            DirectionCoefficient directionCoefficient = new DirectionCoefficient(Math.random(), Math.random(), 1.0);
            listOFPhotonXPositionsForMTF.add(new PhotonXPosition(position, directionCoefficient));
        }
        listOFPhotonXPositionsForMTF = makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(listOFPhotonXPositionsForMTF, constants);
        return listOFPhotonXPositionsForMTF;
    }

    private ArrayList<PhotonXPosition> makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(ArrayList<PhotonXPosition> photonXPositions, Constants constants) {
        photonXPositions.forEach(photonXPosition -> photonXPosition.position.makeOneStepForMTForNPS(constants, photonXPosition.directionCoefficient));
        photonXPositions = photonXPositions.stream()
                .filter(photonXPosition -> photonXPosition.position.inDetector(constants))
                .collect(Collectors.toCollection(ArrayList::new));

        return photonXPositions;
    }

    private ArrayList<Integer> countNPS(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, Constants constants) {
        ArrayList<Integer> endVector = new ArrayList();
        for (int k = 0; k < listOfPartialLSFFunctions.get(0).LSFfuncion.size(); k++) {
            endVector.add(0);
        }
        for (int h = 0; h < constants.numberOfNPSXPositions; h++) {
            ArrayList<PhotonXPosition> list = generatePhotonXPositionsForNPS(constants,endVector.size());
            list.forEach(photonXPosition -> {
                int idx = getIndexOfClosestZPosition(photonXPosition.position.z, listOfPartialLSFFunctions);
                if (listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().count() != 0) {
                    for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                        Double randomVariable = Math.random();
                        if (randomVariable <= listOfPartialLSFFunctions.get(idx).getProbablityOfdetection()) {
                            Random rand = new Random();
                            int randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).LSFfuncion.size())));
                            int distance = randomPosition - listOfPartialLSFFunctions.get(idx).LSFfuncion.size()/2;
                            while ((listOfPartialLSFFunctions.get(idx).LSFfuncion.size()/2+distance) == 0) {
                                randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).LSFfuncion.size())));
                                distance = randomPosition - listOfPartialLSFFunctions.get(idx).LSFfuncion.size()/2;
                            }
                            endVector.set(randomPosition, (endVector.get(randomPosition) + 1));
                        }
                    }
                }
            });
        }
        return endVector;
    }

    private ArrayList<PhotonXPosition> generatePhotonXPositionsForNPS(Constants constants, int lsfSize) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForNPS = new ArrayList<PhotonXPosition>();
        for (int j = 0; j < getPoisson(constants.numberOfNPSXPhotonsInOnePosition); j++) {
            listOFPhotonXPositionsForNPS.add(new PhotonXPosition(new Position(getSingleCoordinate(0.0, (double) constants.cellWallLength * constants.numberOfColumns),
                    getSingleCoordinate(0.0, (double) lsfSize), 0.0), new DirectionCoefficient(0.0, 0.0, 1.0)));
        }
        listOFPhotonXPositionsForNPS = makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(listOFPhotonXPositionsForNPS, constants);
        return listOFPhotonXPositionsForNPS;
    }


    private int getPoisson(double lambda) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        do {
            k++;
            p *= Math.random();
        } while (p > L);
        return k - 1;
    }

    private int getIndexOfClosestZPosition(Double positionZ, ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions) {
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

    private void saveLSFfunctions(ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions, Constants constants) {
        String detectorType = new String("s");
        if (!constants.detectorType) {
            detectorType = new String("k");
        }
        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\" + detectorType);
        ListOfPartialLSFFunctions.forEach(LSFfunction -> {
                    try {
                        String filename = new String(sciezka + LSFfunction.getPositionZ() + ".txt");
                        File plik = new File(filename);
                        if (!plik.exists()) {
                            plik.createNewFile();
                        }
                        FileWriter fw = new FileWriter(plik.getAbsoluteFile());
                        BufferedWriter bw = new BufferedWriter(fw);

                        LSFfunction.LSFfuncion.forEach(lightPhoton -> {
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

                }

        );

    }

    private void saveLSF(ArrayList<Integer> LSF, Constants constants, boolean MTForNPS) {
        String functionType = new String("MTF");
        if (MTForNPS != true) {
            functionType = new String("NPS");
        }
        String detectorType = new String("s");
        if (!constants.detectorType) {
            detectorType = new String("k");
        }

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");
        try {
            String filename = new String(sciezka + detectorType + functionType + ".txt");
            File plik = new File(filename);
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            LSF.forEach(integer -> {
                try {
                    bw.write(integer + "\n");
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
