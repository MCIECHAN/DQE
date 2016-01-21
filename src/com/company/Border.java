package com.company;

public class Border {

    public Position position;
    public Cell cell;
    public Double distance;

    //TODO: jak robisz this. to nie musisz dawać new w nazwie
    public Border (Position position, Cell cell, Double distance){
        this.position = position;
        this.cell = cell;
        this.distance = distance;
    }



}
