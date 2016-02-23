package com.company;

import java.util.ArrayList;

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
            listOfPartialLSFFunctions.add(new PartialLSFFunction(constants,tmp));
        }
        System.out.print(listOfPartialLSFFunctions.size());
        return listOfPartialLSFFunctions;
    }

    private Double getInterval (Constants constants) {
        Double tmp =  ((constants.cellHeight-3.0)-3.0)/(constants.numberOfParticleLSFFunctions-1);
        return tmp;
    }

}
