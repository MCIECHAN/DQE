package com.company;

public class Granica {
    //TODO: jakbyś dał radę te 3 listy zastąpić czymś innym, jakimiś obiektami, czy czymś takim
    // bo trochę nie jasne co to robi (jak gdzieś masz na sztywno wpisaną liczbę, np. 6 to już
    // coś jest podejrzane ;)

    //TODO2: nie używamy tej listy[] tylko jak człowiek ArrayListy ;)

    public int[] wskaznik;
    public Double[] odleglosc;
    public Position[] pozycjaPrzeciecia;

    Granica() {
        wskaznik = new int[6];
        odleglosc = new Double[6];
        pozycjaPrzeciecia = new Position[6];
        for (int i = 1; i <= 6; i++) {
            wskaznik[i] = 0;
        }

    }
}
