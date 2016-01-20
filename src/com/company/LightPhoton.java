package com.company;


import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class LightPhoton {
    private Position position;
    private DirectionCoefficient directCoefficient;
    private Cell cell;
    private Boolean saved;

    public LightPhoton(Position position, DirectionCoefficient directCoefficient, Cell cell, Boolean saved) {
        this.position = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.saved = saved;
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

    Optional<LightPhoton> simulate(Constants constants) {
        if (!saved) {
            LightPhoton przewidywanaNowaPozycja = losujDrogeSwobodna(constants);
            if (przewidywanaNowaPozycja.czyWgranicyKomorki()) {
                if (przewidywanaNowaPozycja.czyAbsorbowany(constants)) {
                    return Optional.empty();
                } else {
                    return rozproszony(przewidywanaNowaPozycja);
                }
            } else {
                Double r = Math.random();
                if (r <= constants.probabilityOfReflection) {
                    return odbicie(przewidywanaNowaPozycja);
                } else {
                    return przejscie(przewidywanaNowaPozycja, constants);
                }
            }
        } else {
            return Optional.of(this);
        }
    }

    private Boolean czyAbsorbowany(Constants constants) {
        Double r = Math.random();
        return r <= constants.probabilityOfDispersion;
    }

    private Boolean czyWgranicyKomorki() {
        return position.x > cell.xMin || position.x < cell.xMax || position.y > cell.yMin || position.y < cell.yMax || position.z > cell.zMin || position.z < cell.zMax;
    }

    private Optional<Cell> currentCell(LightPhoton newLightPhoton, Constants constants) {
        //TODO: podanie nowej kom�rki, je�liby do takiej przeszed�
        if (newLightPhoton.position.x > 0 && newLightPhoton.position.x < constants.cellWallLength * constants.numberOfColumns &&
                newLightPhoton.position.y > 0 && newLightPhoton.position.y < constants.cellWallLength * constants.numberOfRows && newLightPhoton.position.z > 0) {
            Cell newCell = newLightPhoton.cell;
            if (newLightPhoton.position.x <= newLightPhoton.cell.xMin) {
                newCell.xMax = newCell.xMin;
                newCell.xMin = newCell.xMin - constants.cellWallLength;
            } else if (newLightPhoton.position.x >= newLightPhoton.cell.xMax) {
                newCell.xMin = newCell.xMax;
                newCell.xMax = newCell.xMax + constants.cellWallLength;
            }
            else if (newLightPhoton.position.y <= newLightPhoton.cell.yMin) {
                newCell.yMax = newCell.yMin;
                newCell.yMin = newCell.yMin - constants.cellWallLength;
            } else {
                newCell.yMin = newCell.yMax;
                newCell.yMax = newCell.yMax + constants.cellWallLength;
            }
            return Optional.of(newCell);
        } else {
            return Optional.of(cell);
        }
    }

    private Optional<LightPhoton> rozproszony(LightPhoton przewidywanaNowaPozycja) {
        DirectionCoefficient noweWspolczynnikiKierunkowe = DirectionCoefficient.getRandomDirectionCoefficient(przewidywanaNowaPozycja.directCoefficient);
        LightPhoton nowyLightPhoton = new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
        return Optional.of(nowyLightPhoton);
    }

    private Optional<LightPhoton> odbicie(LightPhoton przewidywanaNowaPozycja) {

        Granica punktOdbicia = Granica.znajdzGranice(przewidywanaNowaPozycja);
        int indeksNajblizszejGranicy = Arrays.asList(punktOdbicia.odleglosc).indexOf(Collections.min(Arrays.asList(punktOdbicia.odleglosc)));
        Position punktZetknieciaZeSciana = punktOdbicia.pozycjaPrzeciecia[indeksNajblizszejGranicy];
        DirectionCoefficient noweWspolczynnikiKierunkowe = przewidywanaNowaPozycja.directCoefficient;

        if (indeksNajblizszejGranicy == 0 || indeksNajblizszejGranicy == 1) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(-przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else if (indeksNajblizszejGranicy == 2 || indeksNajblizszejGranicy == 3) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, -przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, -przewidywanaNowaPozycja.directCoefficient.z);
        }
        LightPhoton nowyLightPhoton = new LightPhoton(punktZetknieciaZeSciana, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
        return Optional.of(nowyLightPhoton);
    }


    private Optional<LightPhoton> przejscie(LightPhoton przewidywanaNowaPozycja, Constants constants) {

        DirectionCoefficient noweWspolczynnikiKierunkowe = przewidywanaNowaPozycja.directCoefficient;

        Granica punktOdbicia = Granica.znajdzGranice(przewidywanaNowaPozycja);
        int indeksNajblizszejGranicy = Arrays.asList(punktOdbicia.odleglosc).indexOf(Collections.min(Arrays.asList(punktOdbicia.odleglosc)));
        Position punktZetknieciaZeSciana = punktOdbicia.pozycjaPrzeciecia[indeksNajblizszejGranicy];

        LightPhoton newLightPhoton = new LightPhoton(punktZetknieciaZeSciana, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, false);

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
