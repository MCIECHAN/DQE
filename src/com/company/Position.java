package com.company;

public class Position {
    Double x;
    Double y;
    Double z;

    public Position(Double x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double distanceBetween(Position that) {
        return Math.sqrt(Math.pow(this.x - that.x, 2) + Math.pow(this.y - that.y, 2) + Math.pow(this.z - that.z, 2));
    }

/*    public Position (Constants constants){
        this.x = getSingleCoordinate(0.0, (double) constants.numberOfColumns*constants.cellWallLength);
        this.y = getSingleCoordinate(0.0,(double) constants.numberOfRows*constants.cellWallLength);
        this.z = getSingleCoordinate(90.0, constants.cellHeight);
    }

    public Double getSingleCoordinate(Double rangeMin, Double rangeMax){
        Random r = new Random();
        double singleCoordinate = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return singleCoordinate;
    }*/

/*    public ArrayList<Position> getXPhotonsPositions(Constants constants){
        ArrayList<Position>XPhotonsPositions= new ArrayList<Position>();
        for (int i = 1; i<=constants.numberOfXPhotons;i++){
            XPhotonsPositions.add(generateRandomPosition(constants));
        }
        return XPhotonsPositions;
    }*/

}
