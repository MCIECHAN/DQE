package com.company;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        Constants zmienne = new Constants(100, 10, 100, 5, 5, 1, 0.2, 545, 13.1, 13.1, 0.9, 499.55, 0.9);
        Position pozycja = new Position(25.0, 25.0, 90.0);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(20, 30, 20, 30, 0, zmienne.cellHeight);

        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, zmienne.massAttenuationCoefficientOfXray, zmienne);
        System.out.println(fotonX.numberOfLightPhotons);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();

        ArrayList<LightPhoton> listaZapisanych = mainLoop(zmienne, lista);;
        System.out.println(listaZapisanych.size());
    }

    private static ArrayList<LightPhoton> mainLoop(Constants zmienne, ArrayList<LightPhoton> lista) {
        while (!lista.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        return lista;
    }
}