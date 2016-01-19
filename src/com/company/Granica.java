package com.company;

/**
 * Created by ciechan on 2016-01-19.
 */
public class Granica {
    public int[] wskaznik;
    public Double[] odleglosc;
    public Position [] pozycjaPrzeciecia;

    private Granica (){
        wskaznik = new int[6];
        odleglosc = new Double[6];
        pozycjaPrzeciecia = new Position[6];
        for(int i =1; i<=6 ; i++){
            wskaznik[i] = 0;
        }

    }
}
