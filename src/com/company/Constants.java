package com.company;

public class Constants {
    int areaWallLength;
    int cellWallLength;
    int cellHeight;
    int numberOfRows;
    int numberOfColumns;
    int numberOfParticleLSFFunctions;
    int photonXEnergy;
    Double RTGConversionCoefficient;
    Double lengthOfLightWave;
    Double massAttenuationCoefficientOfLight;
    Double massAttenuationCoefficientOfXray;
    Double probabilityOfAbsorption;
    Double probabilityOfDispersion;
    Double probabilityOfReflection;
    int numberOfLightPhotons;

    public Constants(int newAreaWallLength, int newCellWallLength, int newCellHeight, int newNumberOfRows,
                     int newNumberOfColumns, int newNumberOfParticleLSFFunctions, int newPhotonXEnergy,
                     Double RTGConversionCoefficient, Double newLengthOfLightWave,
                     Double newMassAttenuationCoefficientOfLight, Double newMassAttenuationCoefficientOfXray,
                     Double newProbabilityOfAbsorption, Double newProbabilityOfDispersion, Double newProbabilityOfReflection)
    {
        this.areaWallLength = newAreaWallLength;
        this.cellWallLength = newCellWallLength;
        this.cellHeight = newCellHeight;
        this.numberOfRows = newNumberOfRows;
        this.numberOfColumns = newNumberOfColumns;
        this.numberOfParticleLSFFunctions = newNumberOfParticleLSFFunctions;
        this.photonXEnergy = newPhotonXEnergy;
        this.RTGConversionCoefficient = RTGConversionCoefficient;
        this.lengthOfLightWave = newLengthOfLightWave;
        this.massAttenuationCoefficientOfLight = newMassAttenuationCoefficientOfLight;
        this.massAttenuationCoefficientOfXray = newMassAttenuationCoefficientOfXray;
        this.probabilityOfAbsorption = newProbabilityOfAbsorption/(newProbabilityOfAbsorption+newProbabilityOfDispersion);
        this.probabilityOfDispersion = newProbabilityOfDispersion/(newProbabilityOfAbsorption+newProbabilityOfDispersion);
        this.probabilityOfReflection = newProbabilityOfReflection;
        this.numberOfLightPhotons = 200000;
    }

/*
    private long wyznaczLiczbeGenerowanychFotonowSwiatla(){
        Double sredniaEnergiaPromieniowaniaSwietlnego = 1240 / this.lengthOfLightWave;
        return Math.round(this.photonXEnergy * this.RTGConversionCoefficient / sredniaEnergiaPromieniowaniaSwietlnego);
    }
*/

}
