package com.company;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;



public class Main {

    public static void main(String[] args) throws IOException {

        //Constants zmienne = new Constants(100, 10, 100, 50, 50, 200000, 20, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);
        Constants zmienne = new Constants(100, 10000, 100, 1, 1, 20, 200000, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);

        PartialLSFFunction funkcjaLSF = new PartialLSFFunction(zmienne,95.0);

        CollectorOfLSFFunctions kolektorLSF = new CollectorOfLSFFunctions(zmienne);

       try
        {
            File plik = new File("C:\\Users\\ciechan\\Desktop\\DQE - user story\\plik3.txt");
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

                funkcjaLSF.listOfPositionsOfDetection.forEach(lightPhoton -> {
                    try{
                        bw.write(lightPhoton.naString() );
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                });

            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}