package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;


public class LSF {

    private ArrayList<PartialLSFFunction> DetectorLSFFunctions;
    private ArrayList<Integer> MTF;
    private ArrayList<ArrayList<Integer>> NPS;
    private Double eta;

    LSF(Constants constants) {
        getDetectorLSFFunction(constants);
    }

    private void getDetectorLSFFunction(Constants constants) {
        ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions = createListOfPartialLSFFunctions(constants);
        this.DetectorLSFFunctions = returnEndListOfPartialLSFFunctions(ListOfPartialLSFFunctions);
        this.MTF = countMTF(ListOfPartialLSFFunctions, constants);
        this.NPS = generateSetOfNPS(ListOfPartialLSFFunctions, constants);

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
        int etaCounter = 0;
        Random rand = new Random();
        for (int l = 0; l < constants.numberOfMTFXPhotonsPositions; l++) {
            ArrayList<PhotonXPosition> list = generatePhotonXPositionsForMTF(constants);
            etaCounter = etaCounter + list.size();
            list.forEach(photonXPosition -> {
                int idx = getIndexOfClosestZPosition(photonXPosition.getZ_position(), listOfPartialLSFFunctions);
                if (listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().anyMatch(x -> x > 0)) {
                    for (int i = 0; i < constants.numberOfLightPhotons; i++) {
                        boolean detected = rand.nextDouble() <= listOfPartialLSFFunctions.get(idx).getProbablityOfdetection();
                        if (detected) addMTFoEndVector(listOfPartialLSFFunctions, endVector, rand, idx);
                    }
                }
            });
        }
        setEtaValue( ((double)etaCounter / (double)(constants.numberOfMTFXPhotons * constants.numberOfMTFXPhotonsPositions)));
        return endVector;
    }

    private void addMTFoEndVector(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, ArrayList<Integer> endVector, Random rand, int idx) {
        int randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).listOfPureLSF.size())));
        double randomVariable2 = ((listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax())) * Math.random();
        while (notDetected(listOfPartialLSFFunctions, idx, randomPosition, randomVariable2)) {
            randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).listOfPureLSF.size())));
            randomVariable2 = ((listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().collect(Collectors.summarizingInt(Integer::intValue)).getMax())) * Math.random();
        }
        endVector.set(listOfPartialLSFFunctions.get(idx).listOfPureLSF.get(randomPosition).getPosition(), (endVector.get(listOfPartialLSFFunctions.get(idx).listOfPureLSF.get(randomPosition).getPosition()) + 1));
    }

    private boolean notDetected(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, int idx, int randomPosition, double randomVariable2) {
        return randomVariable2 > listOfPartialLSFFunctions.get(idx).listOfPureLSF.get(randomPosition).getnumberOfDetectedPhotons();
    }

    private ArrayList<PhotonXPosition> generatePhotonXPositionsForMTF(Constants constants) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForMTF = new ArrayList<PhotonXPosition>();
        for (int i = 0; i < constants.numberOfMTFXPhotons; i++) {
            listOFPhotonXPositionsForMTF.add(new PhotonXPosition(0.0, 1));
        }
        listOFPhotonXPositionsForMTF = makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(listOFPhotonXPositionsForMTF, constants);
        return listOFPhotonXPositionsForMTF;
    }

    private ArrayList<PhotonXPosition> makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(ArrayList<PhotonXPosition> photonXPositions, Constants constants) {
        photonXPositions.stream().forEach(photonXPosition -> photonXPosition.makeOneStepForMTForNPS(constants));
        photonXPositions = photonXPositions.stream()
                .filter(photonXPosition -> photonXPosition.photonX_in_Detector(constants))
                .collect(Collectors.toCollection(ArrayList::new));

        return photonXPositions;
    }

    private ArrayList<Integer> funkcja(Constants constants, ArrayList<PartialLSFFunction> listOfPartialLSFFunctions) {
        ArrayList<Integer> endVector = new ArrayList();
        for (int k = 0; k < listOfPartialLSFFunctions.get(0).LSFfuncion.size(); k++) {
            endVector.add(0);
        }
        ArrayList<PhotonXPosition> list = generatePhotonXPositionsForNPS(constants);
        Random rand = new Random();

        list.forEach(photonXPosition -> {
            int idx = getIndexOfClosestZPosition(photonXPosition.getZ_position(), listOfPartialLSFFunctions);
            if (listOfPartialLSFFunctions.get(idx).LSFfuncion.stream().anyMatch(x -> x > 0)) {
                for (int i = 0; i < getPoisson(constants.numberOfLightPhotons); i++) {
                    boolean detected = rand.nextDouble() <= listOfPartialLSFFunctions.get(idx).getProbablityOfdetection();
                    if (detected) addLSFoNPSEndVector2(listOfPartialLSFFunctions, endVector, rand, idx);
                }
            }
        });
        return endVector;
    }

    private void addLSFoNPSEndVector2(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, ArrayList<Integer> endVector, Random rand, int idx) {
        int randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).listOfNormalizedPureLSF.size())));
        double randomVariable2 = rand.nextDouble();
        while (notDetected2(listOfPartialLSFFunctions, idx, randomPosition, randomVariable2)) {
            randomPosition = rand.nextInt(((listOfPartialLSFFunctions.get(idx).listOfNormalizedPureLSF.size())));
            randomVariable2 = rand.nextDouble();
        }
        endVector.set(listOfPartialLSFFunctions.get(idx).listOfNormalizedPureLSF.get(randomPosition).getPosition(), (endVector.get(listOfPartialLSFFunctions.get(idx).listOfNormalizedPureLSF.get(randomPosition).getPosition()) + 1));
    }

    private boolean notDetected2(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, int idx, int randomPosition, double randomVariable2) {
        return randomVariable2 > listOfPartialLSFFunctions.get(idx).listOfNormalizedPureLSF.get(randomPosition).getPprobablityOfdetection();
    }

    private ArrayList<Integer> addTwoArrayListsOfIntegets(ArrayList<Integer> mainArrayList, ArrayList<Integer> addedArrayList, int startPositionOfAdding) {
        for (int i = 0; i < addedArrayList.size(); i++) {
            mainArrayList.set(startPositionOfAdding + i, mainArrayList.get(startPositionOfAdding + i) + addedArrayList.get(i));
        }
        return mainArrayList;
    }

    private ArrayList<Integer> countNPS(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, Constants constants) {
        ArrayList<Integer> endVector = new ArrayList();
        for (int k = 0; k < listOfPartialLSFFunctions.get(0).LSFfuncion.size() * 3; k++) {
            endVector.add(0);
        }
        for (int h = 0; h < listOfPartialLSFFunctions.get(0).LSFfuncion.size(); h++) {
            endVector = addTwoArrayListsOfIntegets(endVector, funkcja(constants, listOfPartialLSFFunctions), h);
        }
        return new ArrayList<Integer>(endVector.subList(listOfPartialLSFFunctions.get(0).LSFfuncion.size() / 2, listOfPartialLSFFunctions.get(0).LSFfuncion.size() * 3 / 2));
    }

    private ArrayList<PhotonXPosition> generatePhotonXPositionsForNPS(Constants constants) {
        ArrayList<PhotonXPosition> listOFPhotonXPositionsForNPS = new ArrayList<PhotonXPosition>();
        for (int j = 0; j < getPoisson(constants.numberOfNPSXPhotonsInOnePosition); j++) {
            listOFPhotonXPositionsForNPS.add(new PhotonXPosition(0.0, 1));
        }
        listOFPhotonXPositionsForNPS = makeOneStepForAllElementsOfListOfPhotonXPositionsForMTForNPS(listOFPhotonXPositionsForNPS, constants);
        return listOFPhotonXPositionsForNPS;
    }

    private ArrayList<ArrayList<Integer>> generateSetOfNPS(ArrayList<PartialLSFFunction> listOfPartialLSFFunctions, Constants constants) {
        ArrayList<ArrayList<Integer>> listOfNPS = new ArrayList<>();
        ArrayList<Integer> listOfMediumValues = new ArrayList<>();
        for (int k = 0; k < constants.numberOfNPSLoops; k++) {
            System.out.println("Początek pętli NPS " + (k+1) + " z " + constants.numberOfNPSLoops + "\n");
            listOfNPS.add(countNPS(listOfPartialLSFFunctions, constants));
            listOfMediumValues.add(findMean(listOfNPS.get(k)));
        }
        Integer mediumValue = findMean(listOfMediumValues);
        listOfNPS = subtractValueFromListOfarrays(listOfNPS, mediumValue);
        return listOfNPS;
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
        return (int) lambda;
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
        this.eta = Eta;
    }


    public void saveLSF2(Constants constants) {

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        String detectorType = new String("structurized");
        if (!constants.detectorType) {
            detectorType = new String("classic");
        }
        try {
            String filename = new String(sciezka+detectorType+ ".dat");
            File plik = new File(filename);
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            try {
                bw.write("Detector substance: " + constants.detectorSubstance + "\n");
                bw.write("Detector type: " + detectorType + "\n");
                bw.write("Photon X energy: " + constants.photonXEnergy + "\n");
                bw.write("Rows: " + this.MTF.size() + "\n");
                bw.write("Columns: " + (this.NPS.size() + 1) + "\n");
                bw.write("Eta: " + (this.eta) + "\n");
                bw.write("Detector resolution: " + constants.resolutionOfDetector + "\n");
                bw.write("NumberOfParticleLSFFunctions: " + constants.numberOfParticleLSFFunctions + "\n");
                bw.write("Detector height: " + constants.cellHeight + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < this.MTF.size(); i++) {
                try {
                    bw.write(this.MTF.get(i)+returnNPS(i)+ "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < this.MTF.size(); i++) {
                try {
                    bw.write(returnLSFs(i)+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bw.write(returnLSFPositions()+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String returnNPS(int j) {
        String NPSrow = new String();
        for (int k = 0; k < this.NPS.size(); k++) {
            NPSrow = " " + NPSrow + this.NPS.get(k).get(j) + " ";
        }
        return NPSrow;
    }

    private String returnLSFs(int j) {
        String LSFrow = new String();
        for (int k = 0; k < this.DetectorLSFFunctions.size(); k++) {
            LSFrow =LSFrow + this.DetectorLSFFunctions.get(k).LSFfuncion.get(j)+" ";
        }
        return LSFrow;
    }

    private String returnLSFPositions() {
        String PositionRow = new String();
        for (int k = 0; k < this.DetectorLSFFunctions.size(); k++) {
            PositionRow =PositionRow + this.DetectorLSFFunctions.get(k).getPositionZ()+" ";
        }
        return PositionRow;
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

}

