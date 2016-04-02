package com.company;

import java.io.IOException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws IOException {

        Constants zmienne = new Constants(100, 10, 100.0, 500, 500, 5, 200000, 1000, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9,5,true,100);
        Constants zmienne2 = new Constants(100, 10, 100.0, 500, 500, 5, 2000000, 1000, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9,5,false,100);

        LSF lsf = new LSF(zmienne);

/*        PartialLSFFunction lsf = new PartialLSFFunction(zmienne, 98.0);
        lsf.saveLSFfunctions();*/

/*        PartialLSFFunction lsfk = new PartialLSFFunction(zmienne2, 98.0);
        lsfk.saveLSFfunctions();*/

 /*       PartialLSFFunction lsf72 = new PartialLSFFunction(zmienne, 72.0);
        lsf72.saveLSFfunctions();

        PartialLSFFunction lsf77 = new PartialLSFFunction(zmienne, 77.0);
        lsf77.saveLSFfunctions();

        //////

        PartialLSFFunction lsf62k = new PartialLSFFunction(zmienne2, 62.0);
        lsf62k.saveLSFfunctions();

        PartialLSFFunction lsf67k = new PartialLSFFunction(zmienne2, 67.0);
        lsf67k.saveLSFfunctions();

        PartialLSFFunction lsf72k = new PartialLSFFunction(zmienne2, 72.0);
        lsf72k.saveLSFfunctions();

        PartialLSFFunction lsf77k = new PartialLSFFunction(zmienne2, 77.0);
        lsf77k.saveLSFfunctions();*/
    }

}