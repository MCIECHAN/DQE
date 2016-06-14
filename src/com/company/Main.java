package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        Constants zmienne = new Constants(100, 5, 100.0, 1024, 1024, 10, 10, 200000, 100, 10, 0.2, 545.0, 3.2, 7.34, 0.9, 499.55, 0.9,5,true,100,10);
        Constants zmiennek = new Constants(100, 5, 100.0, 1024, 1024, 20, 10, 200000, 100, 10, 0.2, 545.0, 3.2, 7.34, 0.9, 499.55, 0.9,5,false,100,10);

        LSF lsf = new LSF(zmienne);
        LSF lsfk = new LSF(zmiennek);

    }

}