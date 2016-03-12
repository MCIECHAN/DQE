package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        Constants zmienne = new Constants(100, 10, 100.0, 500, 500, 20, 200000, 20, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);
        //Constants zmienne = new Constants(100, 1000000000, 100.0, 1, 1, 5, 2000000, 1, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);

        LSF lsf = new LSF(zmienne);

    }

}