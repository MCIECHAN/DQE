package com.company;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ciechan on 2016-02-20.
 */
public class PartialLSFFunction {

    private Double positonZ;
    private Double propablityOfDetection;
    public ArrayList<PositionOfDetection> listOfPositionsOfDetection;

    PartialLSFFunction (Constants constants,Double newPositionZ){
        this.positonZ = newPositionZ;
        this.listOfPositionsOfDetection = generateListOfPositionsOfDetection(constants, newPositionZ);
        this.propablityOfDetection = setPropablityOfDetection( (double) this.listOfPositionsOfDetection.size(), (double) constants.numberOfLightPhotons);

        System.out.println(this.positonZ +" "+ this.propablityOfDetection);
    }




    private ArrayList<PositionOfDetection> generateListOfPositionsOfDetection(Constants constants, Double newPositionZ){
        Position pozycja = new Position(0.0, 0.0, newPositionZ);
        DirectionCoefficient wspkier = new DirectionCoefficient(Math.random(), Math.random(), Math.random());
        Cell komorka = new Cell(-(constants.cellWallLength/2), constants.cellWallLength/2, -(constants.cellWallLength/2),constants.cellWallLength/2, 0,  constants.cellHeight.intValue());
        PhotonX fotonX = new PhotonX(pozycja, wspkier, komorka, constants.massAttenuationCoefficientOfXray, constants.numberOfLightPhotons);
        ArrayList<LightPhoton> lista = fotonX.generateLightPhotons();
        ArrayList<LightPhoton> listaZapisanych = mainLSFLoop(constants, lista);

        return setListOfPositionsOfDetection(listaZapisanych);
    }

    private static ArrayList<LightPhoton> mainLSFLoop(Constants zmienne, ArrayList<LightPhoton> lista) {
        while (!lista.stream().allMatch((lightPhoton -> lightPhoton.saved))) {
            lista = lista.stream()
                    .map(lightPhoton -> lightPhoton.simulate(zmienne))
                    .filter(Optional::isPresent)
                    .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));
        }
        return lista;
    }

    private ArrayList<PositionOfDetection> setListOfPositionsOfDetection (ArrayList<LightPhoton> listaZapisanych){

        ArrayList<PositionOfDetection> outputList = new ArrayList<PositionOfDetection>();
        listaZapisanych.forEach((lightPhoton) ->{outputList.add(lightPhoton.getPositionOfDetection());} );
        return outputList;
    }


    private Double setPropablityOfDetection (Double No, Double Nd){
        return No/Nd;
    }

    public Double getPositonZ(){return this.positonZ;}


    public void getDetectorLSFFunction(Constants constants){

        CollectorOfLSFFunctions kolektorLSF = new CollectorOfLSFFunctions(constants);
        kolektorLSF.saveLSFfunctions();

        ArrayList<Position>XPhotonsPositions= new ArrayList<Position>();

        for (int i = 1; i<=constants.numberOfXPhotons;i++){
            XPhotonsPositions.add(new Position(constants));
            System.out.println("Utworzono nowy foton X");
        }
    }



}
