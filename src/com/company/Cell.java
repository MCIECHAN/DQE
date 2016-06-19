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
                double p = ((double) this.xMin);
                Double wsp = (currentPosition.x - this.xMin) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(new Position(p, currentPosition.y - wsp * currentDirectionCoefficient.y, currentPosition.z - wsp * currentDirectionCoefficient.z));
            }
            if (predictedPosition.x > this.xMax) {
                double p = ((double) this.xMax);
                Double wsp = (currentPosition.x - this.xMax) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(new Position(p, currentPosition.y - wsp * currentDirectionCoefficient.y, currentPosition.z - wsp * currentDirectionCoefficient.z));
            }
            if (predictedPosition.y < this.yMin) {
                double p = ((double) this.yMin);
                Double wsp = (currentPosition.y - this.yMin) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, p, currentPosition.z - wsp * currentDirectionCoefficient.z));
            }
            if (predictedPosition.y > this.yMax) {
                double p = ((double) this.yMax);
                Double wsp = (currentPosition.y - this.yMax) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, p, currentPosition.z - wsp * currentDirectionCoefficient.z));
            }
            if (predictedPosition.z < this.zMin) {
                double p = ((double) this.zMin);
                Double wsp = (currentPosition.z - this.zMin) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, currentPosition.y - wsp * currentDirectionCoefficient.y, p));
            }
            if ((predictedPosition.z > this.zMax)) {
                double p = ((double) this.zMax);
                Double wsp = (currentPosition.z - this.zMax) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, currentPosition.y - wsp * currentDirectionCoefficient.y, p));
            }
        }
        return Optional.of(getClosestCrossBorderPoint(coordinatesOfCrossing, currentPosition));
    }

    public Position getClosestCrossBorderPoint(ArrayList<Position> coordinatesOfCrossing, Position currentPosition) {
        Position closestCrossBorderPoint = coordinatesOfCrossing.get(0);
        Double firstDistance = currentPosition.distanceBetween(closestCrossBorderPoint);
        for (Position position : coordinatesOfCrossing) {
            Double pretendingDistance = currentPosition.distanceBetween(position);
            if (pretendingDistance < firstDistance) closestCrossBorderPoint = position;
        }
        return closestCrossBorderPoint;
    }

    public Boolean wGranicy(Position position) {
        boolean xInBoarder = position.x >= xMin && position.x <= xMax;
        boolean yInBoarder = position.y >= yMin && position.y <= yMax;
        boolean zInBoarder = position.z >= zMin && position.z <= zMax;
        return xInBoarder && yInBoarder && zInBoarder;
    }

}
