package com.company;

import java.util.*;

public class LightPhoton {
    private Position position;
    private DirectionCoefficient directCoefficient;
    private Cell cell;
    public Boolean saved;

    public LightPhoton(Position position, DirectionCoefficient directCoefficient, Cell cell, Boolean saved) {
        this.position = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.saved = saved;
    }

    public Optional<LightPhoton> simulate(Constants constants) {
        if (!saved) {
            LightPhoton photonInNewPosition = losujDrogeSwobodna(constants);
            if (photonInNewPosition.wgranicyKomorki()) return absorbcjaLubRozproszenie(constants, photonInNewPosition);
            else return przejscieLubOdbicie(constants, photonInNewPosition);
        } else {
            return Optional.of(this);
        }
    }

    private Optional<LightPhoton> przejscieLubOdbicie(Constants constants, LightPhoton photonInNewPosition) {
        if (photonInNewPosition.position.z >= constants.cellHeight) {
            return przejscie(photonInNewPosition, constants);
        } else {
            Double r = Math.random();
            if (r <= constants.probabilityOfReflection) return odbicie(photonInNewPosition);
            else return przejscie(photonInNewPosition, constants);
        }
    }

    private Optional<LightPhoton> absorbcjaLubRozproszenie(Constants constants, LightPhoton photonInNewPosition) {
        if (photonInNewPosition.czyAbsorbowany(constants)) {
            return Optional.empty();
        } else return Optional.of(rozproszony(photonInNewPosition));
    }

    private LightPhoton losujDrogeSwobodna(Constants constants) {
        Double r = Math.random();
        Double s = -1 / constants.massAttenuationCoefficientOfLight * Math.log(r);
        Double newX = position.x + directCoefficient.x * s;
        Double newY = position.y + directCoefficient.y * s;
        Double newZ = position.z + directCoefficient.z * s;
        Position newPosition = new Position(newX, newY, newZ);
        return new LightPhoton(newPosition, directCoefficient, cell, saved);
    }

    private Boolean czyAbsorbowany(Constants constants) {
        Double r = Math.random();
        return r >= constants.probabilityOfDispersion;
    }

    private LightPhoton rozproszony(LightPhoton przewidywanaNowaPozycja) {
        DirectionCoefficient noweWspolczynnikiKierunkowe = DirectionCoefficient.getRandomDirectionCoefficient(przewidywanaNowaPozycja.directCoefficient);
        return new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
    }

    private Boolean wgranicyKomorki() {
        return position.x >= cell.xMin && position.x <= cell.xMax && position.y >= cell.yMin && position.y <= cell.yMax && position.z >= cell.zMin && position.z <= cell.zMax;
    }

    private Optional<Cell> currentCell(LightPhoton newLightPhoton, Constants constants) {
        if (newLightPhoton.position.z > 0) {
            Cell newCell = new Cell(newLightPhoton.cell.xMin, newLightPhoton.cell.xMax, newLightPhoton.cell.yMin, newLightPhoton.cell.yMax, newLightPhoton.cell.zMin, newLightPhoton.cell.zMax);
            if (newLightPhoton.position.x <= newLightPhoton.cell.xMin) {
                newCell.xMax = newCell.xMin;
                newCell.xMin = newCell.xMin - constants.cellWallLength;
            }
            if (newLightPhoton.position.x >= newLightPhoton.cell.xMax) {
                newCell.xMin = newCell.xMax;
                newCell.xMax = newCell.xMax + constants.cellWallLength;
            }
            if (newLightPhoton.position.y <= newLightPhoton.cell.yMin) {
                newCell.yMax = newCell.yMin;
                newCell.yMin = newCell.yMin - constants.cellWallLength;
            }
            if ((newLightPhoton.position.y >= newLightPhoton.cell.yMax)) {
                newCell.yMin = newCell.yMax;
                newCell.yMax = newCell.yMax + constants.cellWallLength;
            }
            return Optional.of(newCell);
        } else {
            return Optional.empty();
        }
    }

    private Optional<LightPhoton> odbicie(LightPhoton przewidywanaNowaPozycja) {
        Optional<Position> newPosition = przewidywanaNowaPozycja.cell.getCrossedBorderPoint(this.position, this.directCoefficient, przewidywanaNowaPozycja.position);
        return newPosition.map(position -> {
            DirectionCoefficient noweWspolczynnikiKierunkowe = directCoefficient;
            if (position.x == cell.xMin || position.x == cell.xMax) {
                noweWspolczynnikiKierunkowe = new DirectionCoefficient(-przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
            } else if (position.y == cell.yMin || position.y == cell.yMax) {
                noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, -przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
            } else {
                noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, -przewidywanaNowaPozycja.directCoefficient.z);
            }
            return new LightPhoton(position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
        });
    }


    private Optional<LightPhoton> przejscie(LightPhoton przewidywanaNowaPozycja, Constants constants) {
        Optional<Position> newPosition = przewidywanaNowaPozycja.cell.getCrossedBorderPoint(position, directCoefficient, przewidywanaNowaPozycja.position);
        DirectionCoefficient noweWspolczynnikiKierunkowe = przewidywanaNowaPozycja.directCoefficient;
        return newPosition.flatMap(pozycja -> {
            LightPhoton newLightPhoton = new LightPhoton(pozycja, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, false);
            return currentCell(newLightPhoton, constants).map(cell -> {
                newLightPhoton.cell = cell;
                if (przewidywanaNowaPozycja.position.z >= constants.cellHeight) {
                    newLightPhoton.saved = true;
                }
                return newLightPhoton;
            });
        });
    }

    public void wyswietl() {
        System.out.println("x: " + position.x + " y: " + position.y + " z: " + position.z);
    }

    public String naString() {
        return position.x + " " + position.y + "\n";
    }

    public PositionOfDetection getPositionOfDetection() {
        return new PositionOfDetection(this.position.x.intValue(), this.position.y.intValue());
    }

    public int getPositionX() {
        return this.position.x.intValue();
    }

}