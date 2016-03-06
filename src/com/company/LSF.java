package com.company;

import com.company.CollectorOfLSFFunctions;
import com.company.Constants;
import com.company.Position;

import java.util.ArrayList;

/**
 * Created by ciechan on 2016-03-06.
 */
public class LSF {

    LSF (Constants constants){

    }

    public void getDetectorLSFFunction(Constants constants){

        CollectorOfLSFFunctions kolektorLSF = new CollectorOfLSFFunctions(constants);
        kolektorLSF.saveLSFfunctions();

        ArrayList<Position> XPhotonsPositions= new ArrayList<Position>();

        for (int i = 1; i<=constants.numberOfXPhotons;i++){
            XPhotonsPositions.add(new Position(constants));
            System.out.println("Utworzono nowy foton X");
        }
    }
}
