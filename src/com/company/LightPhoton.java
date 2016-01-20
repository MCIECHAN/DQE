package com.company;


import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class LightPhoton {
    private Position position;
    //TODO: usuń old position, nie jest potrzebne, to jest position (serio, think about it ;) )
    private Position oldPosition;
    private DirectionCoefficient directCoefficient;
    private Cell cell;
    private Boolean saved;

    public LightPhoton(Position position, DirectionCoefficient directCoefficient, Cell cell, Boolean saved) {
        this.position = position;
        oldPosition = position;
        this.directCoefficient = directCoefficient;
        this.cell = cell;
        this.saved = saved;
    }

    private LightPhoton losujDrogeSwobodna(Constants constants) {
        Double r = Math.random();
        Double s = -1 / constants.masowyWspolczynnikOslabieniaSwiatla * Math.log(r);
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
                // TODO nale�y zbudowac funkcje znajdujaca pierwsza przekraczana graice
                Double r = Math.random();
                if (r <= constants.prawdopodobienstwoOdbicia) {
                    return odbicie(przewidywanaNowaPozycja);
                } else {
                    return przejscie(przewidywanaNowaPozycja);
                }
            }
        } else {
            return Optional.of(this);
        }
    }

    private Boolean czyAbsorbowany(Constants constants) {
        Double r = Math.random();
        return r <= constants.prawdopodobienstwoRozproszenia;
    }

    private Boolean czyWgranicyKomorki() {
        return position.x > cell.xMin || position.x < cell.xMax || position.y > cell.yMin || position.y < cell.yMax || position.z > cell.zMin || position.z < cell.zMax;
    }

    private Optional<Cell> getNewCell(Position position) {
        //Option[Cell] = ??? //TODO: podanie nowej kom�rki, je�liby do takiej przeszed�
        return Optional.of(cell);
    }

    private Optional<LightPhoton> rozproszony(LightPhoton przewidywanaNowaPozycja) {
        DirectionCoefficient noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient);
        LightPhoton nowyLightPhoton = new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
        return Optional.of(nowyLightPhoton);
    }

    private Optional<LightPhoton> odbicie(LightPhoton przewidywanaNowaPozycja) {

        Granica punktOdbicia = new Granica(przewidywanaNowaPozycja);
        int indeksNajblizszejGranicy = Arrays.asList(punktOdbicia.odleglosc).indexOf(Collections.min(Arrays.asList(punktOdbicia.odleglosc)));
        Position punktZetknieciaZeSciana = punktOdbicia.pozycjaPrzeciecia[indeksNajblizszejGranicy]; //Nowa pozycja fotonu swiat�a
        if (indeksNajblizszejGranicy == 0 || indeksNajblizszejGranicy == 1) {
            DirectionCoefficient noweWspolczynnikiKierunkowe = new DirectionCoefficient(-przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else if (indeksNajblizszejGranicy == 2 || indeksNajblizszejGranicy == 3) {
            DirectionCoefficient noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, -przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.directCoefficient.z);
        } else if (indeksNajblizszejGranicy == 4 || indeksNajblizszejGranicy == 5) {
            DirectionCoefficient noweWspolczynnikiKierunkowe = new DirectionCoefficient(przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.directCoefficient.y, -przewidywanaNowaPozycja.directCoefficient.z);
        } else {
            // TODO: tu brakuje elsa
        }
        LightPhoton nowyLightPhoton = new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);// DLaczego to nie dzia�a?
        return Optional.of(nowyLightPhoton);
    }


    private Optional<LightPhoton> przejscie(LightPhoton przewidywanaNowaPozycja) {
        //TODO: magia, je�li przejdzie przez 2 �ciany
        DirectionCoefficient noweWspolczynnikiKierunkowe = przewidywanaNowaPozycja.directCoefficient;
        Optional<Cell> newCell = getNewCell(przewidywanaNowaPozycja.position);

        return newCell.map((Cell cell1) -> {
            Boolean newSaved = false;
            if (przewidywanaNowaPozycja.position.z >= 150) { newSaved = true;}
            return new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, cell1, newSaved);
        });
        }

    private Granica znajdzGranice(LightPhoton przewidywanaNowaPozycja) {

        Granica tmpGranica = new Granica();

        if (przewidywanaNowaPozycja.position.x < przewidywanaNowaPozycja.cell.xMin) {
            tmpGranica.wskaznik[0] = 1;//TODO: te 4 linijki w każdym ifie wyciągnij do osobnej funkcji.. a potem zauważ że wszyskie funkcje są prawie takie same i zrób z nich jedną generyczną
            Double wsp = (przewidywanaNowaPozycja.oldPosition.x - przewidywanaNowaPozycja.cell.xMin) / przewidywanaNowaPozycja.directCoefficient.x;
            tmpGranica.pozycjaPrzeciecia[0] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[0] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[0].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[0].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[0].z, 2));
        } else if (przewidywanaNowaPozycja.position.x > przewidywanaNowaPozycja.cell.xMax) {
            tmpGranica.wskaznik[1] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.x - przewidywanaNowaPozycja.cell.xMax) / przewidywanaNowaPozycja.directCoefficient.x;
            tmpGranica.pozycjaPrzeciecia[1] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[1] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[1].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[1].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[1].z, 2));
        } else if (przewidywanaNowaPozycja.position.y < przewidywanaNowaPozycja.cell.yMin) {
            tmpGranica.wskaznik[2] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.y - przewidywanaNowaPozycja.cell.yMin) / przewidywanaNowaPozycja.directCoefficient.y;
            tmpGranica.pozycjaPrzeciecia[2] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[2] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[2].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[2].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[2].z, 2));
        } else if (przewidywanaNowaPozycja.position.y > przewidywanaNowaPozycja.cell.yMax) {
            tmpGranica.wskaznik[3] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.y - przewidywanaNowaPozycja.cell.yMax) / przewidywanaNowaPozycja.directCoefficient.y;
            tmpGranica.pozycjaPrzeciecia[3] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[3] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[3].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[3].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[3].z, 2));
        } else if (przewidywanaNowaPozycja.position.z < przewidywanaNowaPozycja.cell.zMin) {
            tmpGranica.wskaznik[4] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.z - przewidywanaNowaPozycja.cell.zMin) / przewidywanaNowaPozycja.directCoefficient.z;
            tmpGranica.pozycjaPrzeciecia[4] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[4] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[4].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[4].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[4].z, 2));
        } else if (przewidywanaNowaPozycja.position.z > przewidywanaNowaPozycja.cell.zMax) {
            tmpGranica.wskaznik[5] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.z - przewidywanaNowaPozycja.cell.zMax) / przewidywanaNowaPozycja.directCoefficient.z;
            tmpGranica.pozycjaPrzeciecia[5] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[5] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[5].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[5].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[5].z, 2));
        }
        return tmpGranica;
    }

}
