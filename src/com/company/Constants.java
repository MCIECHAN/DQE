package com.company;

public class Constants {
    int areaWallLength;
    int cellWallLength;
    Double cellHeight;
    int numberOfRows;
    int numberOfColumns;
    int numberOfParticleLSFFunctions;
    int number;
    int photonXEnergy;
    int numberOfMTFXPhotons;
    int numberOfMTFXPhotonsPositions;
    Double RTGConversionCoefficient;
    Double lengthOfLightWave;
    Double massAttenuationCoefficientOfLight;
    Double attenuationCoefficientOfXray;
    Double detectorDensity;
    Double massAttenuationCoefficientOfXray;
    Double probabilityOfAbsorption;
    Double probabilityOfDispersion;
    Double probabilityOfReflection;
    int numberOfLightPhotons;
    int numberOfNPSXPhotonsInOnePosition;
    int resolutionOfDetector;
    boolean detectorType;
    int startPoint;
    int numberOfNPSLoops;

    public Constants(int newAreaWallLength, int newCellWallLength, Double newCellHeight, int newNumberOfRows,
                     int newNumberOfColumns, int newNumberOfParticleLSFFunctions, int newNumber, int newPhotonXEnergy, int newNumberOfMTFXPhotons,
                     int newNumberOfMTFXPhotonsPositions, Double RTGConversionCoefficient, Double newLengthOfLightWave,
                     Double newAttenuationCoefficientOfXray, Double newDetectorDensity,
                     Double newProbabilityOfAbsorption, Double newProbabilityOfDispersion, Double newProbabilityOfReflection,
                     int newResolutionOfDetector, boolean newDetectorType, int newNumberOfNPSXPhotonsInOnePosition,
                     int newNumberOfNPSLoops) {
        this.areaWallLength = newAreaWallLength;
        this.cellWallLength = newCellWallLength;
        this.cellHeight = newCellHeight;
        this.numberOfRows = newNumberOfRows;
        this.numberOfColumns = newNumberOfColumns;
        this.numberOfParticleLSFFunctions = newNumberOfParticleLSFFunctions;
        this.number = newNumber;
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
        //this.massAttenuationCoefficientOfLight = newMassAttenuationCoefficientOfLight;
        this.massAttenuationCoefficientOfLight = setMassAttenuationCoefficientOfLight(detectorDensity,newProbabilityOfAbsorption, newProbabilityOfDispersion);
        this.probabilityOfReflection = newProbabilityOfReflection;
        //this.numberOfLightPhotons = wyznaczLiczbeGenerowanychFotonowSwiatla();
        this.numberOfLightPhotons = 10000;
        this.numberOfNPSXPhotonsInOnePosition = newNumberOfNPSXPhotonsInOnePosition;
        this.resolutionOfDetector = newResolutionOfDetector;
        this.detectorType = newDetectorType;
        this.startPoint = returnStartPoint(this);
        this.numberOfNPSLoops = newNumberOfNPSLoops;
    }

    private Double setMassAttenuationCoefficientOfXray(Double detectorDensity, Double attenuationCoefficientOfXray) {
        return attenuationCoefficientOfXray*detectorDensity*0.0001;
    }

    private Double setMassAttenuationCoefficientOfLight(Double detectorDensity, Double newProbabilityOfAbsorption, Double newProbabilityOfDispersion) {
        return (newProbabilityOfAbsorption + newProbabilityOfDispersion)*detectorDensity*0.0001;
    }

    private int wyznaczLiczbeGenerowanychFotonowSwiatla() {
        Double sredniaEnergiaPromieniowaniaSwietlnego = 1240 / this.lengthOfLightWave;
        return (int) Math.round(this.photonXEnergy * this.RTGConversionCoefficient / sredniaEnergiaPromieniowaniaSwietlnego);
    }

    private int returnStartPoint(Constants constants) {
        int overAllLength = constants.numberOfColumns * constants.cellWallLength;
        int startPoint = 0;
        if ((overAllLength % 2) == 0) { /* x is even */
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
