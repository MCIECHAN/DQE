package com.company;

import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;


/**
 * Created by ciechan on 2016-02-20.
 */
public class CollectorOfLSFFunctions {

    private ArrayList <PartialLSFFunction> listOfPartialLSFFunctions;

    CollectorOfLSFFunctions (Constants constants){
        this.listOfPartialLSFFunctions = getListOfPartialLSFFunctions(constants);
    }

    private ArrayList <PartialLSFFunction> getListOfPartialLSFFunctions(Constants constants ){
        ArrayList <PartialLSFFunction>  listOfPartialLSFFunctions = new  ArrayList <PartialLSFFunction>();
        Double tmp = this.getInterval(constants);
        for (Double i = 3.0; i<=constants.cellHeight; i=i+ tmp){
            listOfPartialLSFFunctions.add(new PartialLSFFunction(constants,i));
        }
        return listOfPartialLSFFunctions;
    }

    private Double getInterval (Constants constants) {
        Double tmp =  ((constants.cellHeight-3.0)-3.0)/(constants.numberOfParticleLSFFunctions-1);
        return tmp;
    }

    public int getIndexOfClosestZPosition(Double positionZ){
        int indeks=0;
        Double currentDistance = Math.abs(positionZ-this.listOfPartialLSFFunctions.get(0).getPositonZ());
        for (int i =0;i< this.listOfPartialLSFFunctions.size();i++ ){
            Double pretendingDistance = Math.abs(positionZ - this.listOfPartialLSFFunctions.get(i).getPositonZ());
            if (pretendingDistance<currentDistance){
                currentDistance = pretendingDistance;
                indeks=i;
            }
        }
        return indeks;
    }


    public void saveLSFfunctions (){

        String sciezka = new String("C:\\Users\\ciechan\\Desktop\\DQE - user story\\");

        this.listOfPartialLSFFunctions.forEach(LSFfunction -> {

                    try {
                        String filename = new String(sciezka + LSFfunction.getPositonZ()+".txt");
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

}
