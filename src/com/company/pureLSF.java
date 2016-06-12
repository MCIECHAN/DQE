package com.company;

/**
 * Created by ciechan on 2016-05-12.
 */
public class pureLSF {
    private int positionOfDetectorCell;
    private int numberOfDetectedPhotons;

    public pureLSF(int newPositionOfDetectorCell, int newNumberOfDetectedPhotons){
        this.positionOfDetectorCell = newPositionOfDetectorCell;
        this.numberOfDetectedPhotons = newNumberOfDetectedPhotons;
    }

    public int getPosition(){
        return this.positionOfDetectorCell;
    }

    public int getnumberOfDetectedPhotons(){
        return this.numberOfDetectedPhotons;
    }

}
