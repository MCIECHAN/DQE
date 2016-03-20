package com.company;

import java.util.ArrayList;

public class LSF {

    private ArrayList<PartialLSFFunction> DetectorLSFFunction;

    LSF(Constants constants) {
        this.DetectorLSFFunction = getDetectorLSFFunction(constants);
    }

    private ArrayList<PartialLSFFunction> getDetectorLSFFunction(Constants constants) {

        ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions = createListOfPartialLSFFunctions(constants);
        ListOfPartialLSFFunctions.forEach(fun -> System.out.print(fun.getPositionZ()+"\n"));

/*        ArrayList<PhotonXPosition> XPhotonsPositions = generateXPhotonsPositions(constants);
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

    private ArrayList<PhotonXPosition> generateXPhotonsPositions(Constants constants) {
        ArrayList<PhotonXPosition> XPhotonsPositions = new ArrayList<>();
        for (int i = 0; i < constants.numberOfXPhotons; i++) {
            PhotonXPosition nowaPozycja = new PhotonXPosition(constants);
            XPhotonsPositions.add(nowaPozycja);
        }
        return XPhotonsPositions;
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

/*    private void saveLSFfunctions(ArrayList<PartialLSFFunction> ListOfPartialLSFFunctions) {

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        ListOfPartialLSFFunctions.forEach(LSFfunction -> {

                    try {
                        String filename = new String(sciezka + LSFfunction.getPositionZ() + ".txt");
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


    }*/
}
