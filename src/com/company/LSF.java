package com.company;

import java.util.ArrayList;

/**
 * Created by ciechan on 2016-03-06.
 */
public class LSF {

    private ArrayList<PositionOfDetection>DetectorLSFFunction;

    LSF (Constants constants){
        //this.DetectorLSFFunction=getDetectorLSFFunction(constants);
    }

    //private ArrayList<PositionOfDetection> getDetectorLSFFunction(Constants constants){
        public void getDetectorLSFFunction(Constants constants){

        CollectorOfLSFFunctions LSFcollector = new CollectorOfLSFFunctions(constants);
        LSFcollector.saveLSFfunctions();

        ArrayList<Position> XPhotonsPositions= new ArrayList<Position>();

        for (int i = 1; i<=constants.numberOfXPhotons;i++){
            Position nowaPozycja = new Position(constants);
            XPhotonsPositions.add(nowaPozycja);
        }

        //return ;
    }
}
