package com.company;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Constants zmienne = new Constants(100, 5, 100, 10, 10, 10000, 20.0, 545, 13.1, 13.1, 0.1, 0.9, 0.9);
        Position pozycja = new Position(25.0, 25.0, 95.0);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(20, 30, 20, 30, 0, zmienne.cellHeight);


        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, zmienne.massAttenuationCoefficientOfXray, zmienne);
        System.out.println(fotonX.numberOfLPhotons);
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