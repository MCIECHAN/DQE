package com.company;

import java.io.FileOutputStream;
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

/*        //FileOutputStream plik = new FileOutputStream(plikDoZapisow);
        Cell komorka2 = new Cell(0, 50, 0, 50, 0, zmienne.cellHeight);


        PhotonX fotonX2 = new PhotonX(pozycja, wspkier, komorka2, zmienne.massAttenuationCoefficientOfXray, zmienne);
        System.out.println(fotonX2.numberOfLPhotons);
        ArrayList<LightPhoton> lista2 = fotonX2.generateLightPhotons();

        while (!lista2.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista2 = lista2.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        ArrayList<LightPhoton> listaZapisanych2 = lista2;

        System.out.println(listaZapisanych2.size());*/



    }
}