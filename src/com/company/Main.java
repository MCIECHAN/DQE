package com.company;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here

        Constants zmienne = new Constants(100, 10, 150, 3, 3, 10000, 0.2, 545, 0.1, 0.1, 0.1, 0.9, 0.9);
        Position pozycja = new Position(15.0, 15.0, 125.0);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), 0.3);
        Cell komorka = new Cell(10, 20, 10, 20, 0, 150);


        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, zmienne.massAttenuationCoefficientOfXray, 100000);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();

        while (!lista.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        ArrayList<LightPhoton> listaZapisanych = lista;

        System.out.println(listaZapisanych.size());

        for(LightPhoton foton : listaZapisanych){
            foton.wyswietl();
        }

    }
}