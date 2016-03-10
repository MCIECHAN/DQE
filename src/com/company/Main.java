package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        long time1 = System.nanoTime();

        Constants zmienne = new Constants(100, 10, 100.0, 50, 50, 5, 200000, 1, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);
        //Constants zmienne = new Constants(100, 1000000000, 100.0, 1, 1, 20, 2000000, 1, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);

        //PartialLSFFunction funkcjaLSF = new PartialLSFFunction(zmienne,90.0);

        //CollectorOfLSFFunctions kolektorLSF = new CollectorOfLSFFunctions(zmienne);
        //kolektorLSF.saveLSFfunctions();
        LSF lsf = new LSF(zmienne);

        long time2 = System.nanoTime();
        long timeTaken = time2 - time1;
        System.out.println("Time taken " + timeTaken + " ns");


    }

}