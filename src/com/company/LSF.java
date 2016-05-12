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
    private Double eta;

    LSF(Constants constants) {
        this.DetectorLSFFunction = getDetectorLSFFunction(constants);
    }

    private ArrayList<PartialLSFFunction> getDetectorLSFFunction(Constants constants) {
        ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions = createListOfPartialLSFFunctions(constants);
        ListOfPartialLSFFunctions = returnEndListOfPartialLSFFunctions(ListOfPartialLSFFunctions);
        saveLSFfunctions(ListOfPartialLSFFunctions, constants);

        ArrayList<Integer> LSFOfMTF = countMTF(ListOfPartialLSFFunctions, constants);
        saveLSF(LSFOfMTF, constants, true, 0);

        //generateSetOfNPS(ListOfPartialLSFFunctions, constants);

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
        Double etaCounter = 0.0;
        for (int l = 0; l < constants.numberOfMTFXPhotonsPositions; l++) {
            System.out.println(l+" z "+constants.numberOfMTFXPhotonsPositions+" pętli MTF.");
            ArrayList<PhotonXPosition> list = generatePhotonXPositionsForMTF(constants);
            etaCounter = etaCounter + list.size();
            list.forEach(photonXPosition -> {
                int idx = getIndexOfClosestZPosition(photonXPosition.position.z, listOfPartialLSFFunctions);
                if (listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().count() != 0) {
                    for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                        Double randomVariable = Math.random();
                        if (randomVariable <= listOfPartialLSFFunctions.get(idx).getProbablityOfdetection()) {
                            Random rand = new Random();
                            int randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).listOfPureLSF.size())));
                            double randomVariable2 = 0 + ((listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() - 0)) * Math.random();
                            while (randomVariable2 > listOfPartialLSFFunctions.get(idx).LSFfuncion.get(listOfPartialLSFFunctions.get(idx).listOfPureLSF.get(randomPosition).getPosition())) {
                                randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).listOfPureLSF.size())));
                                randomVariable2 = 0 + ((listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() - 0)) * Math.random();
                            }
                            endVector.set(listOfPartialLSFFunctions.get(idx).listOfPureLSF.get(randomPosition).getPosition(), (endVector.get(listOfPartialLSFFunctions.get(idx).listOfPureLSF.get(randomPosition).getPosition()) + 1));
                        }
                    }
                }
            });
        }
        setEtaValue(etaCounter / (constants.numberOfMTFXPhotons * constants.numberOfMTFXPhotonsPositions));
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
        System.out.println("Wchodzę do count NPS" + "\n");
        for (int h = 0; h < constants.numberOfNPSXPositions; h++) {
            //System.out.println("Wchodzę do pętli NPSXPositions" + "\n");
            ArrayList<PhotonXPosition> list = generatePhotonXPositionsForNPS(constants, endVector.size());
            list.forEach(photonXPosition -> {
                int idx = getIndexOfClosestZPosition(photonXPosition.position.z, listOfPartialLSFFunctions);
                if (listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().count() != 0) {
                    int distance = photonXPosition.position.x.intValue() - listOfPartialLSFFunctions.get(idx).LSFfuncion.size() / 2;
                   // System.out.println("pozycja fotonu X: " + photonXPosition.position.x + "\n");
                    //System.out.println("Dystans: " + distance + "\n");
                    //for (int i = 0; i < 10000; i++) {
                    for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                        //System.out.println("Analizuję " + i + " z 10000 fotonów światła " + listOfPartialLSFFunctions.get(idx).getPositionZ() + "\n");
                        Double randomVariable = Math.random();
                        if (randomVariable <= listOfPartialLSFFunctions.get(idx).getProbablityOfdetection()) {
                            //System.out.println("Foton został zakwalifikowany do zlizenia" + "\n");
                            Random rand = new Random();
                            //int randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).LSFfuncion.size())));
                            int randomPosition = returnRandomPosition(distance, listOfPartialLSFFunctions.get(idx).LSFfuncion.size(), listOfPartialLSFFunctions.get(idx).listOfPureLSF);
                            //System.out.println("Pozycja losowa dla fotonu światła: " + randomPosition + "\n");
                            int diffrence = randomPosition + distance;
                            //System.out.println("Difference: " + diffrence + "\n");
                            double randomVariable2 = 0 + ((listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() - 0)) * Math.random();
/*                            System.out.print("Maksymalna wartość w LSF " + listOfPartialLSFFunctions.get(idx).getPositionZ() + " " + listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() + "\n");
                            System.out.println("Warunek zliczenia w pozycji: " + randomVariable2 + " " + listOfPartialLSFFunctions.get(idx).LSFfuncion.get(diffrence) + "\n");*/
                            while (randomVariable2 > listOfPartialLSFFunctions.get(idx).LSFfuncion.get(diffrence)) {
                                //while (randomVariable2 > listOfPartialLSFFunctions.get(idx).LSFfuncion.get(listOfPartialLSFFunctions.get(idx).LSFfuncion.size() / 2 + diffrence)) {
                                //randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).LSFfuncion.size())));
                                randomPosition = returnRandomPosition(distance, listOfPartialLSFFunctions.get(idx).LSFfuncion.size(), listOfPartialLSFFunctions.get(idx).listOfPureLSF);
                                randomVariable2 = 0 + ((listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() - 0)) * Math.random();
                                diffrence = randomPosition + distance;
/*                                System.out.println("Szukam miejsca zliczenia" + "\n");
                                System.out.println("Pozycja losowa dla fotonu światła: " + randomPosition + "\n");
                                System.out.println("Difference: " + diffrence + "\n");
                                System.out.print("Maksymalna wartość w LSF " + listOfPartialLSFFunctions.get(idx).getPositionZ() + " " + listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax() + "\n");
                                System.out.println("Warunek zliczenia w pozycji: " + randomVariable2 + " " + listOfPartialLSFFunctions.get(idx).LSFfuncion.get(diffrence) + "\n");*/
                            }
                            //System.out.println("Doliczyłem foton" + "\n");
                            endVector.set(randomPosition, (endVector.get(randomPosition) + 1));
                        }
                    }

                }
            });
        }
        return endVector;
    }

    private int returnRandomPosition(int distance, int size, ArrayList<pureLSF> pureLSFArrayList) {
        Random r = new Random();
        int position = r.nextInt(pureLSFArrayList.size());
        if (distance >= 0) {
            while ()
            return r.nextInt(((size - distance) - 0)) + 0;
        } else {
            return r.nextInt(((size - Math.abs(distance)))) + Math.abs(distance);
        }
    }

    private ArrayList<PhotonXPosition> generatePhotonXPositionsForNPS(Constants constants, int lsfSize) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForNPS = new ArrayList<PhotonXPosition>();
        for (int j = 0; j < getPoisson(constants.numberOfNPSXPhotonsInOnePosition); j++) {
            listOFPhotonXPositionsForNPS.add(new PhotonXPosition(new Position(getSingleCoordinate(0.0, (double) lsfSize),
                    0.0, 0.0), new DirectionCoefficient(0.0, 0.0, 1.0)));
        }
        listOFPhotonXPositionsForNPS = makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(listOFPhotonXPositionsForNPS, constants);
        return listOFPhotonXPositionsForNPS;
    }

    private void generateSetOfNPS(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, Constants constants) {
        ArrayList<ArrayList<Integer>> listOfNPS = new ArrayList<>();
        ArrayList<Integer> listOfMediumValues = new ArrayList<>();
        System.out.println("Funkcja główna NPS" + "\n");
        for (int k = 0; k < constants.numberOfNPSLoops; k++) {
            System.out.println("Początek pętli NPS " + k + " z " + constants.numberOfNPSLoops + "\n");
            listOfNPS.add(countNPS(listOfPartialLSFFunctions, constants));
            listOfMediumValues.add(findMean(listOfNPS.get(k)));
            System.out.println("Koniec pętli NPS " + k + " z " + constants.numberOfNPSLoops + "\n");
            System.out.println(listOfMediumValues.get(k) + "\n");
        }
        Integer mediumValue = findMean(listOfMediumValues);
        System.out.println(mediumValue + "\n");

        for (int m = 0; m < listOfNPS.size(); m++) {
            int n = m + 10;
            saveLSF(listOfNPS.get(m), constants, false, n);
        }
        listOfNPS = subtractValueFromListOfarrays(listOfNPS, mediumValue);

        for (int m = 0; m < listOfNPS.size(); m++) {
            saveLSF(listOfNPS.get(m), constants, false, m);
        }
    }

    private Integer findMean(ArrayList<Integer> list) {
        Integer counter = 0;
        for (int l = 0; l < list.size(); l++) {
            counter = counter + list.get(l);
        }
        return counter / list.size();
    }

    private ArrayList<ArrayList<Integer>> subtractValueFromListOfarrays(ArrayList<ArrayList<Integer>> list, int meanValue) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).size(); j++) {
                list.get(i).set(j, list.get(i).get(j) - meanValue);
            }
        }
        return list;
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

    private void setEtaValue(Double Eta) {
        System.out.print("Wartośc Eta detektora: "+Eta+"\n");
        this.eta = Eta;
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

    private void saveLSF(ArrayList<Integer> LSF, Constants constants, boolean MTForNPS, int number) {
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
            String filename = new String(sciezka + detectorType + functionType + number + ".txt");
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
