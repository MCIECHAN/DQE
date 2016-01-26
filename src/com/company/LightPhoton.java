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
        Double r = Math.random();
        if (r <= constants.probabilityOfReflection) {
            //todo TO MA SIE ZMIENIC
            return Optional.of(odbicie(photonInNewPosition));
        } else {
            return przejscie(photonInNewPosition, constants);
        }
    }

    private Optional<LightPhoton> absorbcjaLubRozproszenie(Constants constants, LightPhoton photonInNewPosition) {
        if (photonInNewPosition.czyAbsorbowany(constants)) {
            return Optional.empty();
        } else {
            return Optional.of(rozproszony(photonInNewPosition));
        }
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
        return r <= constants.probabilityOfDispersion;
    }

    private LightPhoton rozproszony(LightPhoton przewidywanaNowaPozycja) {
        DirectionCoefficient noweWspolczynnikiKierunkowe = DirectionCoefficient.getRandomDirectionCoefficient(przewidywanaNowaPozycja.directCoefficient);
        return new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
    }

    private Boolean wgranicyKomorki() {
        return position.x > cell.xMin && position.x < cell.xMax && position.y > cell.yMin && position.y < cell.yMax && position.z > cell.zMin && position.z < cell.zMax;
    }

    private Optional<Cell> currentCell(LightPhoton newLightPhoton, Constants constants) {
        if (newLightPhoton.position.x > 0 && newLightPhoton.position.x < constants.cellWallLength * constants.numberOfColumns &&
                newLightPhoton.position.y > 0 && newLightPhoton.position.y < constants.cellWallLength * constants.numberOfRows && newLightPhoton.position.z > 0) {
            Cell newCell = newLightPhoton.cell;
            if (newLightPhoton.position.x <= newLightPhoton.cell.xMin) {
                newCell.xMax = newCell.xMin;
                newCell.xMin = newCell.xMin - constants.cellWallLength;
            } else if (newLightPhoton.position.x >= newLightPhoton.cell.xMax) {
                newCell.xMin = newCell.xMax;
                newCell.xMax = newCell.xMax + constants.cellWallLength;
            } else if (newLightPhoton.position.y <= newLightPhoton.cell.yMin) {
                newCell.yMax = newCell.yMin;
                newCell.yMin = newCell.yMin - constants.cellWallLength;
            } else {
                newCell.yMin = newCell.yMax;
                newCell.yMax = newCell.yMax + constants.cellWallLength;
            }
            return Optional.of(newCell);
        } else {
            return Optional.empty();
        }
    }

    private LightPhoton odbicie(LightPhoton przewidywanaNowaPozycja) {
        Optional<Position> newPosition = przewidywanaNowaPozycja.cell.getCrossedBorderPoint(this.position, this.directCoefficient, przewidywanaNowaPozycja.position);
        DirectionCoefficient noweWspolczynnikiKierunkowe = this.directCoefficient;

        newPosition = newPosition.map

        //TODO: NEVER!STRZEŻ SIĘ! Nigdy nie robimy coścojestoptional.get()! od tego masz .map(), żeby zaglądać do środka Optionala, robić magię w środku i nigdy nie musieć wyciągać z środka :)
        if (newPosition.get().x == this.cell.xMin || newPosition.get().x == this.cell.xMax) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(-przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else if (newPosition.get().y == this.cell.yMin || newPosition.get().y == this.cell.yMax) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, -przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else if (newPosition.get().z == this.cell.zMin || newPosition.get().z == this.cell.zMax) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, -przewidywanaNowaPozycja.directCoefficient.z);
        }

        return new LightPhoton(newPosition.get(), noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
    }


    private Optional<LightPhoton> przejscie(LightPhoton przewidywanaNowaPozycja, Constants constants) {

        Optional<Position> newPosition = przewidywanaNowaPozycja.cell.getCrossedBorderPoint(this.position, this.directCoefficient, przewidywanaNowaPozycja.position);
        DirectionCoefficient noweWspolczynnikiKierunkowe = przewidywanaNowaPozycja.directCoefficient;

        // TODO Czy nie jest tak, że zanim przeprowadze operację "newPosition.get().x" to powinienem sprawdzić, czy dostanę Position, a nie empty?
        LightPhoton newLightPhoton = new LightPhoton(newPosition.get(), noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, false);
        Optional<Cell> newCell = currentCell(newLightPhoton, constants);

        return newCell.map(cell -> {
            Boolean newSaved = false;
            if (przewidywanaNowaPozycja.position.z >= constants.cellHeight) {
                newSaved = true;
            }
            newLightPhoton.saved = newSaved;
            return newLightPhoton;
        });
    }
}