package com.company;

import com.sun.xml.internal.bind.v2.model.core.MaybeElement;
import jdk.nashorn.internal.runtime.options.Option;

import java.util.Optional;

/**
 * Created by ciechan on 2016-01-18.
 */
public class LightPhoton {
    private Position position;
    private DirectionCooficient directCoefficient;
    private Cell cell;
    private Boolean saved;

    public LightPhoton (Position newPosition,DirectionCooficient newDirectionCooficient, Cell newCell, Boolean stan){
        position = newPosition;
        directCoefficient = newDirectionCooficient;
        cell = newCell;
        saved = stan;
    }

    private LightPhoton losujDrogeSwobodna(Constants constants ){
        Double r = Math.random();
        Double s = -1 / constants.masowyWspolczynnikOslabieniaSwiatla * Math.log(r);
        Double newX = position.x + directCoefficient.x * s;
        Double newY = position.y + directCoefficient.y * s;
        Double newZ = position.z + directCoefficient.z * s;
        Position  newPosition = new Position(newX, newY, newZ);
        return new LightPhoton(newPosition, directCoefficient, cell, saved);
    }

    Optional <LightPhoton> simulate(Constants constants){
        if(!saved){
            LightPhoton przewidywanaNowaPozycja = losujDrogeSwobodna(constants);
            if (przewidywanaNowaPozycja.czyWgranicyKomorki()) {
                if (przewidywanaNowaPozycja.czyAbsorbowany(constants))
                    return Optional.empty();
                else
                    rozproszony(przewidywanaNowaPozycja);
            }
            else {
                // TODO nale¿y zbudowac funkcje znajdujaca pierwsza przekraczana graice
                Double r = Math.random();
                if (r <= constants.prawdopodobienstwoOdbicia)
                    odbicie(przewidywanaNowaPozycja);
                else
                    przejscie(przewidywanaNowaPozycja);
            }
        }
        else{
            return Optional.of(this);
        }
    }

    private Boolean czyAbsorbowany(Constants constants){
        Double r = Math.random();
        if (r > constants.prawdopodobienstwoRozproszenia)
            return false;
        else
            return true;
    }

    private Boolean czyWgranicyKomorki() {
        if(position.x> cell.xMin || position.x < cell.xMax ||position.y> cell.yMin || position.y < cell.yMax || position.z > cell.zMin || position.z < cell.zMax ){
            return true;
        }
        else{
            return false;
        }
    }

    private Optional <Cell> getNewCell( Position position){
        //Option[Cell] = ??? //TODO: podanie nowej komórki, jeœliby do takiej przeszed³
        return Optional.of(cell);
    }

    private Optional <LightPhoton> rozproszony(LightPhoton przewidywanaNowaPozycja){
        DirectionCooficient noweWspolczynnikiKierunkowe = new DirectionCooficient(przewidywanaNowaPozycja.directCoefficient);
        LightPhoton nowyLightPhoton = new LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved);
        return Optional.of(nowyLightPhoton);
    }

    private Optional <LightPhoton> odbicie(LightPhoton przewidywanaNowaPozycja ){

        if(przewidywanaNowaPozycja.position.x<przewidywanaNowaPozycja.cell.xMin){
            DirectionCooficient newdirectCoefficient = new DirectionCooficient(-przewidywanaNowaPozycja.directCoefficient.x,przewidywanaNowaPozycja.directCoefficient.y,przewidywanaNowaPozycja.directCoefficient.z);
            Double wsp = (przewidywanaNowaPozycja.position.x-przewidywanaNowaPozycja.cell.xMin)/przewidywanaNowaPozycja.directCoefficient.x;
            Position newPosition = new Position(przewidywanaNowaPozycja.position.x-wsp*przewidywanaNowaPozycja.position.x,przewidywanaNowaPozycja.position.y-wsp*przewidywanaNowaPozycja.position.y,przewidywanaNowaPozycja.position.z-wsp*przewidywanaNowaPozycja.position.z);
            return Optional.of(new LightPhoton(newPosition, newdirectCoefficient,przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved));
        }
        else if(przewidywanaNowaPozycja.position.x>przewidywanaNowaPozycja.cell.xMax){
            przewidywanaNowaPozycja.directCoefficient.x= -przewidywanaNowaPozycja.directCoefficient.x;
            Double wsp = (przewidywanaNowaPozycja.position.x-przewidywanaNowaPozycja.cell.xMax)/przewidywanaNowaPozycja.directCoefficient.x;
        }
        else if (przewidywanaNowaPozycja.position.y<przewidywanaNowaPozycja.cell.yMin||przewidywanaNowaPozycja.position.y>przewidywanaNowaPozycja.cell.yMax){
            przewidywanaNowaPozycja.directCoefficient.y= -przewidywanaNowaPozycja.directCoefficient.y;
        }
        else if (przewidywanaNowaPozycja.position.z<przewidywanaNowaPozycja.cell.zMin||przewidywanaNowaPozycja.position.z>przewidywanaNowaPozycja.cell.zMax){
            przewidywanaNowaPozycja.directCoefficient.y= -przewidywanaNowaPozycja.directCoefficient.y;
        }

        Position punktZetknieciaZeSciana = position; //TODO: nowa pozycja wyliczona za pomoc¹ dziwnej matmy

        return Optional.of(LightPhoton(punktZetknieciaZeSciana, noweWspolczynnikiKierunkowe, przewidywanaNowaPozycja.cell, przewidywanaNowaPozycja.saved));
    }

    private Optional <LightPhoton> przejscie(LightPhoton przewidywanaNowaPozycja ){
        //TODO: magia, jeœli przejdzie przez 2 œciany
        DirectionCooficient noweWspolczynnikiKierunkowe = przewidywanaNowaPozycja.directCoefficient;
        Cell newCell = getNewCell(przewidywanaNowaPozycja.position);

        newCell.map{cell =>
            Boolean newSaved = false;
            if(przewidywanaNowaPozycja.position.z >= 150){
                newSaved = true;
            }
            LightPhoton(przewidywanaNowaPozycja.position, noweWspolczynnikiKierunkowe, cell, newSaved);
        }
    }

}
