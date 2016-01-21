package com.company;


import java.util.ArrayList;
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
    private Optional<LightPhoton> rozproszony(LightPhoton przewidywanaNowaPozycja) {
        DirectionCoefficient noweWspolczynnikiKierunkowe = DirectionCoefficient.getRandomDirectionCoefficient(przewidywanaNowaPozycja.directCoefficient);
        LightPhoton nowyLightPhoton = new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
        return Optional.of(nowyLightPhoton);
    }

    private Boolean czyWgranicyKomorki() {
        return position.x > cell.xMin || position.x < cell.xMax || position.y > cell.yMin || position.y < cell.yMax || position.z > cell.zMin || position.z < cell.zMax;
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

    private Border findBorder(LightPhoton przewidywanaNowaPozycja, LightPhoton oldLightPhoton, Constants constants) {

        ArrayList<Border> listOfPotentialBorders = new ArrayList<>();

        if (przewidywanaNowaPozycja.position.x < przewidywanaNowaPozycja.cell.xMin) {
            Double wsp = (oldLightPhoton.position.x - przewidywanaNowaPozycja.cell.xMin) / przewidywanaNowaPozycja.directCoefficient.x;
            Position coordinatesOfCrossing = new Position(oldLightPhoton.position.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, oldLightPhoton.position.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, oldLightPhoton.position.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            Double distanceToBorder = Math.sqrt(Math.pow(oldLightPhoton.position.x - coordinatesOfCrossing.x, 2) + Math.pow(oldLightPhoton.position.y - coordinatesOfCrossing.y, 2) + Math.pow(oldLightPhoton.position.z - coordinatesOfCrossing.z, 2));
            Cell newCell = new Cell(przewidywanaNowaPozycja.cell.xMin - constants.cellWallLength,przewidywanaNowaPozycja.cell.xMin,przewidywanaNowaPozycja.cell.yMin,przewidywanaNowaPozycja.cell.yMax,przewidywanaNowaPozycja.cell.zMin,przewidywanaNowaPozycja.cell.zMax);
            listOfPotentialBorders.add(new Border(coordinatesOfCrossing,newCell,distanceToBorder));
        } else if (przewidywanaNowaPozycja.position.x > przewidywanaNowaPozycja.cell.xMax) {

            Double wsp = (przewidywanaNowaPozycja.oldPosition.x - przewidywanaNowaPozycja.cell.xMax) / przewidywanaNowaPozycja.directCoefficient.x;
            tmpGranica.pozycjaPrzeciecia[1] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[1] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[1].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[1].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[1].z, 2));

        } else if (przewidywanaNowaPozycja.position.y < przewidywanaNowaPozycja.cell.yMin) {

            Double wsp = (przewidywanaNowaPozycja.oldPosition.y - przewidywanaNowaPozycja.cell.yMin) / przewidywanaNowaPozycja.directCoefficient.y;
            tmpGranica.pozycjaPrzeciecia[2] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[2] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[2].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[2].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[2].z, 2));
        } else if (przewidywanaNowaPozycja.position.y > przewidywanaNowaPozycja.cell.yMax) {

            Double wsp = (przewidywanaNowaPozycja.oldPosition.y - przewidywanaNowaPozycja.cell.yMax) / przewidywanaNowaPozycja.directCoefficient.y;
            tmpGranica.pozycjaPrzeciecia[3] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[3] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[3].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[3].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[3].z, 2));
        } else if (przewidywanaNowaPozycja.position.z < przewidywanaNowaPozycja.cell.zMin) {

            Double wsp = (przewidywanaNowaPozycja.oldPosition.z - przewidywanaNowaPozycja.cell.zMin) / przewidywanaNowaPozycja.directCoefficient.z;
            tmpGranica.pozycjaPrzeciecia[4] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[4] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[4].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[4].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[4].z, 2));
        } else if (przewidywanaNowaPozycja.position.z > przewidywanaNowaPozycja.cell.zMax) {

            Double wsp = (przewidywanaNowaPozycja.oldPosition.z - przewidywanaNowaPozycja.cell.zMax) / przewidywanaNowaPozycja.directCoefficient.z;
            tmpGranica.pozycjaPrzeciecia[5] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[5] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[5].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[5].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[5].z, 2));
        }
        return;
    }

}
