package com.company;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        Constants zmienne = new Constants(100, 10, 100.0, 514, 514, 20, 200000, 1000000, 0.2, 545.0, 1.0, 3.2, 7.34, 0.9, 499.55, 0.9,5,true,1000,10000);
        Constants zmiennek = new Constants(100, 10, 100.0, 514, 514, 20, 200000, 1000000, 0.2, 545.0, 1.0, 3.2, 7.34, 0.9, 499.55, 0.9,5,false,1000,10000);

        LSF lsf = new LSF(zmienne);
        LSF lsfk = new LSF(zmiennek);

        //PartialLSFFunction lsf = new PartialLSFFunction(zmienne, 80.0);
        //lsf.saveLSFfunctions();

/*        PartialLSFFunction lsfk = new PartialLSFFunction(zmienne2, 98.0);
        lsfk.saveLSFfunctions();*/

 /*       PartialLSFFunction lsf72 = new PartialLSFFunction(zmienne, 72.0);
        lsf72.saveLSFfunctions();

        PartialLSFFunction lsf77 = new PartialLSFFunction(zmienne, 77.0);
        lsf77.saveLSFfunctions();*/

        //////

      //PartialLSFFunction lsf62k = new PartialLSFFunction(zmiennek, 80.0);
/*        lsf62k.saveLSFfunctions();*/

        /*PartialLSFFunction lsf67k = new PartialLSFFunction(zmienne2, 67.0);
        lsf67k.saveLSFfunctions();

        PartialLSFFunction lsf72k = new PartialLSFFunction(zmienne2, 72.0);
        lsf72k.saveLSFfunctions();

        PartialLSFFunction lsf77k = new PartialLSFFunction(zmienne2, 77.0);
        lsf77k.saveLSFfunctions();*/

    }
}