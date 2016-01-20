package com.company;

public class Constants {
    int areaWallLength;
    int cellWallLength;
    int cellHeight;
    int numberOfRows;
    int numberOfColumns;
    Double photonXEnergy;
    Double  RTGConversionCoefficient;
    Double lengthOfLightWave;
    Double massAttenuationCoefficientOfLight;
    Double massAttenuationCoefficientOfXray;
    Double probabilityOfAbsorption;
    Double  probabilityOfDispersion;
    Double  probabilityOfReflection;

    private Constants(int newAreaWallLength, int newCellWallLength,int newCellHeight,int newNumberOfRows,
                      int newNumberOfColumns, Double newPhotonXEnergy, Double RTGConversionCoefficient,
                      Double newLengthOfLightWave, Double newMassAttenuationCoefficientOfLight,
                      Double newMassAttenuationCoefficientOfXray, Double newProbabilityOfAbsorption,
                      Double  newProbabilityOfDispersion,Double  newProbabilityOfReflection){
        this.areaWallLength = newAreaWallLength;
        this.cellWallLength= newCellWallLength;
        this.cellHeight= newCellHeight;
        this.numberOfRows= newNumberOfRows;
        this.numberOfColumns= newNumberOfColumns;
        this.photonXEnergy= newPhotonXEnergy;
        this.RTGConversionCoefficient = RTGConversionCoefficient;
        this.lengthOfLightWave=newLengthOfLightWave;
        this.massAttenuationCoefficientOfLight = newMassAttenuationCoefficientOfLight;
        this.massAttenuationCoefficientOfXray = newMassAttenuationCoefficientOfXray;
        this.probabilityOfAbsorption = newProbabilityOfAbsorption;
        this.probabilityOfDispersion = newProbabilityOfDispersion;
        this.probabilityOfReflection = newProbabilityOfReflection;
    }
}
