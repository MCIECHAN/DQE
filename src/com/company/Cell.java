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
/*        System.out.println("Szukam granicy odbicia lub rozzproszenia");
        System.out.println("Granice komórki:");
        System.out.println(this.xMin);
        System.out.println(this.xMax);
        System.out.println(this.yMin);
        System.out.println(this.yMax);
        System.out.println(this.zMin);
        System.out.println(this.zMax);*/

        if ((wGranicy(currentPosition) && wGranicy(predictedPosition)) || (!wGranicy(currentPosition) && !wGranicy(predictedPosition))) {
            System.out.println("Oba w lub poza");
            System.out.println(currentPosition.x.toString());
            System.out.println(currentPosition.y.toString());
            System.out.println(currentPosition.z.toString());
            System.out.println("Przewidywana pozycja");
            System.out.println(predictedPosition.x.toString());
            System.out.println(predictedPosition.y.toString());
            System.out.println(predictedPosition.z.toString());

            return Optional.empty();
        } else if ((wGranicy(currentPosition) && !wGranicy(predictedPosition))) {
            //System.out.println("Jeden w komorce, drugi poza");
            if (predictedPosition.x < this.xMin) {
                //System.out.println("Przekroczony Xmin");
                Double wsp = (currentPosition.x - this.xMin) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
            if (predictedPosition.x > this.xMax) {
                //System.out.println("Przekroczony Xmax");
                Double wsp = (currentPosition.x - this.xMax) / currentDirectionCoefficient.x;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
            if (predictedPosition.y < this.yMin) {
                //System.out.println("Przekroczony Ymin");
                Double wsp = (currentPosition.y - this.yMin) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
            if (predictedPosition.y > this.yMax) {
                //System.out.println("Przekroczony Ymax");
                Double wsp = (currentPosition.y - this.yMax) / currentDirectionCoefficient.y;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
            if (predictedPosition.z < this.zMin) {
                //System.out.println("Przekroczony Zmin");
                Double wsp = (currentPosition.z - this.zMin) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
            if((predictedPosition.z > this.zMax)){
                //System.out.println("Przekroczony Zmax");
                Double wsp = (currentPosition.z - this.zMax) / currentDirectionCoefficient.z;
                coordinatesOfCrossing.add(getCrossingPosition(currentPosition, wsp, currentDirectionCoefficient));
            }
        }

        return Optional.of(getClosestCrossBorderPoint(coordinatesOfCrossing, currentPosition));
    }

    public Position getClosestCrossBorderPoint(ArrayList<Position> coordinatesOfCrossing, Position currentPosition) {
        Position closestCrossBorderPoint = coordinatesOfCrossing.get(0);
        Double firstDistance = currentPosition.getDistanceBetweenTwoPositions(closestCrossBorderPoint);
        for (Position position : coordinatesOfCrossing) {
            Double pretendingDistance = currentPosition.getDistanceBetweenTwoPositions(position);
            if (pretendingDistance < firstDistance) closestCrossBorderPoint = position;
        }
/*        System.out.println("Punkt przecięcia:");
        System.out.println(closestCrossBorderPoint.x.toString());
        System.out.println(closestCrossBorderPoint.y.toString());
        System.out.println(closestCrossBorderPoint.z.toString());*/
        return closestCrossBorderPoint;
    }

    public Boolean wGranicy(Position position) {
        return position.x >= xMin && position.x <= xMax && position.y >= yMin && position.y <= yMax && position.z >= zMin && position.z <= zMax;
    }

    public Position getCrossingPosition(Position currentPosition, Double wsp, DirectionCoefficient currentDirectionCoefficient) {
        return new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, currentPosition.y - wsp * currentDirectionCoefficient.y, currentPosition.z - wsp * currentDirectionCoefficient.z);
    }

}
