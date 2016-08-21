package com.company;

public class Constants {
    int cellWallLength;                        //
    Double cellHeight;                         //
    int numberOfRows;                          //
    int numberOfColumns;                       //
    int numberOfParticleLSFFunctions;          //
    int numberOfLoops;                         //
    int photonXEnergy;                         //
    int numberOfMTFXPhotons;                   //
    int numberOfMTFXPhotonsPositions;          //
    Double RTGConversionCoefficient;           //
    Double lengthOfLightWave;                  //
    Double massAttenuationCoefficientOfLight;  //
    Double attenuationCoefficientOfXray;       //
    Double detectorDensity;                    //
    Double massAttenuationCoefficientOfXray;   //
    Double probabilityOfAbsorption;            //
    Double probabilityOfDispersion;            //
    Double probabilityOfReflection;            //
    int numberOfLightPhotons;                  //
    int numberOfNPSXPhotonsInOnePosition;      //
    int resolutionOfDetector;                  //
    boolean detectorType;                      //
    boolean energyDepended;                    //
    int startPoint;                            //
    int numberOfNPSLoops;                      //
    String detectorSubstance;                  //

    public Constants(int newCellWallLength, Double newCellHeight, int newNumberOfRows,
                     int newNumberOfColumns, int newNumberOfParticleLSFFunctions, int newNumber, int newPhotonXEnergy, int newNumberOfLightPhotons, int newNumberOfMTFXPhotons,
                     int newNumberOfMTFXPhotonsPositions, Double RTGConversionCoefficient, Double newLengthOfLightWave,
                     Double newAttenuationCoefficientOfXray, Double newDetectorDensity,
                     Double newProbabilityOfAbsorption, Double newProbabilityOfDispersion, Double newProbabilityOfReflection,
                     int newResolutionOfDetector, boolean newDetectorType, boolean newenergyDepended, int newNumberOfNPSXPhotonsInOnePosition,
                     int newNumberOfNPSLoops, String newDtetectorSubstance) {
        this.cellWallLength = newCellWallLength;
        this.cellHeight = newCellHeight;
        this.numberOfRows = newNumberOfRows;
        this.numberOfColumns = newNumberOfColumns;
        this.numberOfParticleLSFFunctions = newNumberOfParticleLSFFunctions;
        this.photonXEnergy = newPhotonXEnergy;
        this.numberOfMTFXPhotons = newNumberOfMTFXPhotons;
        this.numberOfMTFXPhotonsPositions = newNumberOfMTFXPhotonsPositions;
        this.RTGConversionCoefficient = RTGConversionCoefficient;
        this.lengthOfLightWave = newLengthOfLightWave;
        this.attenuationCoefficientOfXray = newAttenuationCoefficientOfXray;
        this.detectorDensity = newDetectorDensity;
        this.massAttenuationCoefficientOfXray = setMassAttenuationCoefficientOfXray(detectorDensity, attenuationCoefficientOfXray);
        this.probabilityOfAbsorption = newProbabilityOfAbsorption / (newProbabilityOfAbsorption + newProbabilityOfDispersion);
        this.probabilityOfDispersion = newProbabilityOfDispersion / (newProbabilityOfAbsorption + newProbabilityOfDispersion);
        this.massAttenuationCoefficientOfLight = setMassAttenuationCoefficientOfLight(detectorDensity, newProbabilityOfAbsorption, newProbabilityOfDispersion);
        this.probabilityOfReflection = newProbabilityOfReflection;
        this.numberOfNPSXPhotonsInOnePosition = newNumberOfNPSXPhotonsInOnePosition;
        this.resolutionOfDetector = newResolutionOfDetector;
        this.detectorType = newDetectorType;
        this.startPoint = returnStartPoint(this);
        this.numberOfNPSLoops = newNumberOfNPSLoops;
        this.detectorSubstance = newDtetectorSubstance;
        this.numberOfLoops = setLoopNumber(newenergyDepended, newNumber);
        this.numberOfLightPhotons = wyznaczLiczbeGenerowanychFotonowSwiatla(newenergyDepended,newNumberOfLightPhotons);
    }

    private Double setMassAttenuationCoefficientOfXray(Double detectorDensity, Double attenuationCoefficientOfXray) {
        return attenuationCoefficientOfXray * detectorDensity * 0.0001;
    }

    private Double setMassAttenuationCoefficientOfLight(Double detectorDensity, Double newProbabilityOfAbsorption, Double newProbabilityOfDispersion) {
        return (newProbabilityOfAbsorption + newProbabilityOfDispersion) * detectorDensity * 0.0001;
    }

    private int wyznaczLiczbeGenerowanychFotonowSwiatla(boolean newenergyDepended, int newNumberOfLightPhotons) {
        if (newenergyDepended) {
            Double sredniaEnergiaPromieniowaniaSwietlnego = 1240 / this.lengthOfLightWave;
            return (int) Math.round(this.photonXEnergy * this.RTGConversionCoefficient / sredniaEnergiaPromieniowaniaSwietlnego);
        } else
            return newNumberOfLightPhotons;
    }


    private int setLoopNumber(boolean newenergyDepended, int newNumber) {
        if (newenergyDepended) {
            return 1;
        } else {
            return newNumber;
        }
    }

    private int returnStartPoint(Constants constants) {
        int overAllLength = constants.numberOfColumns * constants.cellWallLength;
        int startPoint = 0;
        if ((overAllLength % 2) == 0) {
            startPoint = ((overAllLength / 2));

        } else {
            int tmp = (((overAllLength - 1) / 2) + 1) - (constants.resolutionOfDetector - 1) / 2;
            for (int i = tmp; i > 0; i = i - constants.resolutionOfDetector) {
                startPoint = i;
            }
        }
        return startPoint;
    }

}
