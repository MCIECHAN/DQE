package com.company;


public class PhotonXPosition {
    private double Z_position;
    public int Z_directionCoefficient;

    public PhotonXPosition(double new_Z_Position, int new_Z_DirectionCoefficient) {
        this.Z_position = new_Z_Position;
        this.Z_directionCoefficient = new_Z_DirectionCoefficient;
    }

    public double getZ_position() {
        return this.Z_position;
    }

    public void makeOneStepForMTForNPS(Constants constants) {
        Double r = Math.random();
        Double s = (-1 * Math.log(r)) / constants.massAttenuationCoefficientOfXray;
        this.Z_position = this.Z_position + this.Z_directionCoefficient * s;
    }

    public boolean photonX_in_Detector(Constants constants) {
        boolean zInBoarder = this.Z_position >= 0 && this.Z_position <= constants.cellHeight;
        return zInBoarder;
    }

}
