package com.company;


import java.util.*;

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
            LightPhoton photonInNewPosition = losujDrogeSwobodna(constants);
            if (photonInNewPosition.wgranicyKomorki()) {
                if (photonInNewPosition.czyAbsorbowany(constants)) {
                    return Optional.empty();
                } else {
                    return Optional.of(rozproszony(photonInNewPosition));
                }
            } else {
                Double r = Math.random();
                if (r <= constants.probabilityOfReflection) {
                    return Optional.of(odbicie(photonInNewPosition));
                } else {
                    return przejscie(photonInNewPosition, constants);
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

    private LightPhoton rozproszony(LightPhoton przewidywanaNowaPozycja) {
        DirectionCoefficient noweWspolczynnikiKierunkowe = DirectionCoefficient.getRandomDirectionCoefficient(przewidywanaNowaPozycja.directCoefficient);
        return new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
    }

    private Boolean wgranicyKomorki() {
        return position.x > cell.xMin && position.x < cell.xMax && position.y > cell.yMin && position.y < cell.yMax && position.z > cell.zMin && position.z < cell.zMax;
    }

    //TODO: czy to nie powinno czasem zwracać Optional.empty <- jeśli wypadnie poza pole?
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
            return Optional.of(cell);
        }
    }

    private LightPhoton odbicie(LightPhoton przewidywanaNowaPozycja) {

        Granica punktOdbicia = Granica.znajdzGranice(przewidywanaNowaPozycja);
        int indeksNajblizszejGranicy = Arrays.asList(punktOdbicia.odleglosc).indexOf(Collections.min(Arrays.asList(punktOdbicia.odleglosc)));
        Position punktZetknieciaZeSciana = punktOdbicia.pozycjaPrzeciecia[indeksNajblizszejGranicy];
        DirectionCoefficient noweWspolczynnikiKierunkowe;

        if (indeksNajblizszejGranicy == 0 || indeksNajblizszejGranicy == 1) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(-przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else if (indeksNajblizszejGranicy == 2 || indeksNajblizszejGranicy == 3) {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, -przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else {
            noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, -przewidywanaNowaPozycja.directCoefficient.z);
        }
        return new LightPhoton(punktZetknieciaZeSciana, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
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
