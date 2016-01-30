package com.company;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here

        Constants zmienne = new Constants(100, 10, 150, 5, 5, 10000, 0.2, 545, 13.1, 13.1, 0.1, 0.99, 0.9);
        Position pozycja = new Position(25.0, 25.0, 125.0);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(20, 30, 20, 30, 0, 150);


        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, zmienne.massAttenuationCoefficientOfXray, 100);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();

        while (!lista.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        ArrayList<LightPhoton> listaZapisanych = lista;

        System.out.println(listaZapisanych.size());


    }
}