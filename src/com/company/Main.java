package com.company;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;



public class Main {

    public static void main(String[] args) throws IOException {

        long time1 = System.nanoTime();

        Constants zmienne = new Constants(100, 10, 100.0, 50, 50, 20, 2000, 2, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);
        //Constants zmienne = new Constants(100, 1000000000, 100.0, 1, 1, 20, 200000, 2, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);

        //PartialLSFFunction funkcjaLSF = new PartialLSFFunction(zmienne,90.0);

        //CollectorOfLSFFunctions kolektorLSF = new CollectorOfLSFFunctions(zmienne);
        //kolektorLSF.saveLSFfunctions();
        LSF lsf = new LSF(zmienne);
        lsf.getDetectorLSFFunction(zmienne);




        long time2 = System.nanoTime();
        long timeTaken = time2 - time1;
        System.out.println("Time taken " + timeTaken + " ns");

/*       try
        {
            File plik = new File("C:\\Users\\ciechan\\Desktop\\DQE - user story\\plik.txt");
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
        }*/
        }

        }