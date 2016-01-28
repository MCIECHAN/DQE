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

    public Cell(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public Optional<Position> getCrossedBorderPoint(Position currentPosition, DirectionCoefficient currentDirectionCoefficient, Position predictedPosition) {

        ArrayList<Position> coordinatesOfCrossing = new ArrayList<>();

        if ((wGranicy(currentPosition) && wGranicy(predictedPosition)) || (!wGranicy(currentPosition) && !wGranicy(predictedPosition))) {
            return Optional.empty();
        } else if ((wGranicy(currentPosition) && !wGranicy(predictedPosition))) {
            if (predictedPosition.x < this.xMin) {
                Double wsp = (currentPosition.x - this.xMin) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            } else if (predictedPosition.x > this.xMax) {
                Double wsp = (currentPosition.x - this.xMax) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            } else if (predictedPosition.y < this.yMin) {
                Double wsp = (currentPosition.y - this.yMin) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            } else if (predictedPosition.y > this.yMax) {
                Double wsp = (currentPosition.y - this.yMax) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            } else if (predictedPosition.z < this.zMin) {
                Double wsp = (currentPosition.z - this.zMin) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            } else{
                Double wsp = (currentPosition.z - this.zMax) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
                //TODO tablica nigdy nie może byc pusta
            }
        }

        return Optional.of(getClosestCrossBorderPoint(coordinatesOfCrossing, currentPosition));
    }

    public Position getClosestCrossBorderPoint(ArrayList<Position> coordinatesOfCrossing, Position currentPosition) {
        Position closestCrossBorderPoint = coordinatesOfCrossing.get(0);
        Double firstDistance = currentPosition.getDistanceBetweenTwoPositions(closestCrossBorderPoint);
        for (Position position : coordinatesOfCrossing) {
            Double pretendingDistance = currentPosition.getDistanceBetweenTwoPositions(position);
            if (pretendingDistance > firstDistance) closestCrossBorderPoint = position;
        }
        return closestCrossBorderPoint;
    }

    public Boolean wGranicy(Position position) {
        return position.x > xMin && position.x < xMax && position.y > yMin && position.y < yMax && position.z > zMin && position.z < zMax;
    }

    public Position getCrossingPosition(Position currentPosition, Double wsp, DirectionCoefficient currentDirectionCoefficient) {
        return new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, currentPosition.x - wsp * currentDirectionCoefficient.y, currentPosition.z - wsp * currentDirectionCoefficient.z);
    }

}
