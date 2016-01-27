package com.company;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // write your code here

        Constants zmienne = new Constants(100, 10, 150, 3, 3, 10000, 0.2, 545, 0.27, 0.27, 0.1, 0.9, 0.9);
        Position pozycja = new Position(15.0, 15.0, 75.0);
        DirectionCoefficient wspkier = new DirectionCoefficient(0.0, 0.0, 1.0);
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

    }
}
/*        Supplier <Stream<LightPhoton>> lista = () ->  fotonX.generateLightPhotons().stream();

        while (!lista.get().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.get()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .;

        ArrayList<LightPhoton> listaZapisanych = lista.get().collect(Collectors.toCollection(ArrayList::new));
        }
        http://winterbe.com/posts/2014/07/31/java8-stream-tutorial-examples/
        */
