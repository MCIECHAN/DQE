package com.company;

import java.util.ArrayList;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        // write your code here


        Constants zmienne = new Constants(100, 10, 150, 3, 3, 10000, 0.2,545,0.27,0.27,0.1,0.9,0.9);
        Position pozycja = new Position(15.0,15.0,0.75);
        DirectionCoefficient wspkier = new DirectionCoefficient(0.0,0.0,1.0);
        Cell komorka = new Cell(10,20, 10,20,0,150);

        PhotonX fotonX = new PhotonX(pozycja, wspkier,komorka, zmienne.massAttenuationCoefficientOfXray,10000000 );

        LightPhoton fotonSwiatla = new LightPhoton(pozycja, wspkier, komorka,false);

        fotonX.generateLightPhotons();

        Optional <LightPhoton> lista;
       lista = fotonX.listOfLightPhotons.s






    }
}
