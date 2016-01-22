package com.company;

import java.util.ArrayList;
import java.util.Optional;

public class Position {
    Double x;
    Double y;
    Double z;

    public Position(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Optional<Position> getCrossedBorder (Cell currentCell,Position currentPosition, DirectionCoefficient currentDirectionCoefficient, Position predictedPosition){

        ArrayList<Position> coordinatesOfCrossing;

        if ((wgranicyKomorki(currentCell, currentPosition)== true && wgranicyKomorki(currentCell, predictedPosition)==true)||
                (wgranicyKomorki(currentCell, currentPosition)== false && wgranicyKomorki(currentCell, predictedPosition)==false) ){
            return Optional.empty();
        }
        else if ((wgranicyKomorki(currentCell, currentPosition)== true && wgranicyKomorki(currentCell, predictedPosition)==false)){

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
        return coordinatesOfCrossing;
    }

    private  Boolean wgranicyKomorki(Cell cell, Position position) {
        return position.x > cell.xMin && position.x < cell.xMax && position.y > cell.yMin && position.y < cell.yMax && position.z > cell.zMin && position.z < cell.zMax;
    }

    private Position getCrossingPosition (Position currentPosition, Double wsp, DirectionCoefficient currentDirectionCoefficient){
        return  new Position(currentPosition.x - wsp * currentDirectionCoefficient.x, currentPosition.x - wsp * currentDirectionCoefficient.y, currentPosition.z - wsp * currentDirectionCoefficient.z);
    }

    private Position getClosestCrossBorderPoint( ArrayList <Position> coordinatesOfCrossing){

        for (coordinatesOfCrossing : Position){

        }

        return ;
    }

    private Double getDistanceBetweenTwoPositions (Position position1, Position position2){
        return Math.sqrt(Math.pow(position1.x - position2.x, 2) + Math.pow(position1.y - position2.y, 2) + Math.pow(position1.z - position2.z, 2));
    }



    // jeśli jeden jest a drugi nie, to robimygeomatryczną magię i wyznaczamy punkt przecięcia prostej między dwoma position a granicą fukcji i go zwracamy

}
