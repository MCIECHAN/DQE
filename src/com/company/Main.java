package com.company;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.io.FileWriter;



public class Main {

    public static void main(String[] args) throws IOException {

        Constants zmienne = new Constants(100, 10, 100, 50, 50, 200000, 0.2, 545.0, 2.1, 2.1, 0.9, 499.55, 0.9);
        Position pozycja = new Position(255.0, 255.0, 80.0);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(250, 260, 250, 2600, 0, zmienne.cellHeight);
        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, zmienne.massAttenuationCoefficientOfXray, zmienne.numberOfLightPhotons);
        System.out.println(fotonX.numberOfLightPhotons);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        ArrayList<LightPhoton> listaZapisanych = mainLoop(zmienne, lista);
        System.out.println(listaZapisanych.size());
        try
        {
            File plik = new File("C:\\Users\\ciechan\\Desktop\\DQE - user story\\plik.txt");
            if (!plik.exists()) {
                plik.createNewFile();
            }
            FileWriter fw = new FileWriter(plik.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

                listaZapisanych.forEach(lightPhoton -> {
                    try{
                        bw.write(lightPhoton.naString() );
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                });

            bw.close();
        }catch (IOException e) {
            e.printStackTrace();
        }


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