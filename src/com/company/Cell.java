package com.company;

import java.util.ArrayList;
import java.util.Optional;

public class Cell {

    public int xMin;
    public int xMax;
    public int yMin;
    public int yMax;
    public int zMin;
    public int zMax;

    public Cell( int xMin, int xMax, int yMin, int yMax, int zMin, int zMax){
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public Optional<Position> getCrossedBorder (Cell currentCell,Position currentPosition, DirectionCoefficient currentDirectionCoefficient, Position predictedPosition){

        ArrayList<Position> coordinatesOfCrossing;

        if ((wGranicy(currentPosition) && wGranicy(predictedPosition))||
                (!wGranicy(currentPosition) && !wGranicy(predictedPosition))){
            return Optional.empty();
        }
        else if ((wGranicy(currentPosition) && !wGranicy(predictedPosition))){

            if (predictedPosition.x < currentCell.xMin) {
                Double wsp = (currentPosition.x - currentCell.xMin) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(getCrossingPosition (currentPosition, wsp, currentDirectionCoefficient));
            }
            else if (predictedPosition.x > currentCell.xMax){
                Double wsp = (currentPosition.x - currentCell.xMax) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(getCrossingPosition (currentPosition, wsp, currentDirectionCoefficient));
            }
            else if (predictedPosition.y < currentCell.yMin) {
                Double wsp = (currentPosition.y - currentCell.yMin) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(getCrossingPosition (currentPosition, wsp, currentDirectionCoefficient));
            }
            else if (predictedPosition.y > currentCell.yMax) {
                Double wsp = (currentPosition.y - currentCell.yMax) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(getCrossingPosition (currentPosition, wsp, currentDirectionCoefficient));
            }
            else if (predictedPosition.z < currentCell.zMin) {
                Double wsp = (currentPosition.z - currentCell.zMin) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
            else if (predictedPosition.z > currentCell.zMax) {
                Double wsp = (currentPosition.z - currentCell.zMax) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(getCrossingPosition (currentPosition, wsp, currentDirectionCoefficient));
            }
        }
        return coordinatesOfCrossing.stream().min();
    }

    public  Boolean wGranicy(Position position) {
        return position.x > xMin && position.x < xMax && position.y > yMin && position.y < yMax && position.z > zMin && position.z < zMax;
    }


    public Position getCrossingPosition (Position currentPosition, Double wsp, DirectionCoefficient currentDirectionCoefficient){
        return  new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, currentPosition.x - wsp * currentDirectionCoefficient.y, currentPosition.z - wsp * currentDirectionCoefficient.z);
    }


}
