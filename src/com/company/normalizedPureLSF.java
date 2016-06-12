package com.company;

/**
 * Created by ciechan on 2016-06-09.
 */
public class normalizedPureLSF {
    private int positionOfDetectorCell;
    private double probablityOfdetection;

    public normalizedPureLSF(int newPositionOfDetectorCell, double newProbablityOfdetection){
        this.positionOfDetectorCell = newPositionOfDetectorCell;
        this.probablityOfdetection = newProbablityOfdetection;
    }


    public int getPosition(){return this.positionOfDetectorCell;}

    public double getPprobablityOfdetection(){return this.probablityOfdetection;}
}
