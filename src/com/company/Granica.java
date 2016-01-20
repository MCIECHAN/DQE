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
        for (int i = 0; i <= 5; i++) {
            wskaznik[i] = 0;
        }

    }
    public static Granica znajdzGranice(LightPhoton przewidywanaNowaPozycja) {

        Granica tmpGranica = new Granica();

        if (przewidywanaNowaPozycja.position.x < przewidywanaNowaPozycja.cell.xMin) {
            tmpGranica.wskaznik[0] = 1;//TODO: te 4 linijki w każdym ifie wyciągnij do osobnej funkcji.. a potem zauważ że wszyskie funkcje są prawie takie same i zrób z nich jedną generyczną
                                        // To chyba nie do końca tak, każda z opcji odwołuje się do innych koordynant. Wolę to zmienić, jak całość odpoali
            Double wsp = (przewidywanaNowaPozycja.oldPosition.x - przewidywanaNowaPozycja.cell.xMin) / przewidywanaNowaPozycja.directCoefficient.x;
            tmpGranica.pozycjaPrzeciecia[0] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[0] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[0].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[0].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[0].z, 2));
        } else if (przewidywanaNowaPozycja.position.x > przewidywanaNowaPozycja.cell.xMax) {
            tmpGranica.wskaznik[1] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.x - przewidywanaNowaPozycja.cell.xMax) / przewidywanaNowaPozycja.directCoefficient.x;
            tmpGranica.pozycjaPrzeciecia[1] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[1] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[1].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[1].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[1].z, 2));
        } else if (przewidywanaNowaPozycja.position.y < przewidywanaNowaPozycja.cell.yMin) {
            tmpGranica.wskaznik[2] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.y - przewidywanaNowaPozycja.cell.yMin) / przewidywanaNowaPozycja.directCoefficient.y;
            tmpGranica.pozycjaPrzeciecia[2] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[2] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[2].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[2].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[2].z, 2));
        } else if (przewidywanaNowaPozycja.position.y > przewidywanaNowaPozycja.cell.yMax) {
            tmpGranica.wskaznik[3] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.y - przewidywanaNowaPozycja.cell.yMax) / przewidywanaNowaPozycja.directCoefficient.y;
            tmpGranica.pozycjaPrzeciecia[3] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[3] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[3].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[3].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[3].z, 2));
        } else if (przewidywanaNowaPozycja.position.z < przewidywanaNowaPozycja.cell.zMin) {
            tmpGranica.wskaznik[4] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.z - przewidywanaNowaPozycja.cell.zMin) / przewidywanaNowaPozycja.directCoefficient.z;
            tmpGranica.pozycjaPrzeciecia[4] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[4] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[4].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[4].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[4].z, 2));
        } else if (przewidywanaNowaPozycja.position.z > przewidywanaNowaPozycja.cell.zMax) {
            tmpGranica.wskaznik[5] = 1;
            Double wsp = (przewidywanaNowaPozycja.oldPosition.z - przewidywanaNowaPozycja.cell.zMax) / przewidywanaNowaPozycja.directCoefficient.z;
            tmpGranica.pozycjaPrzeciecia[5] = new Position(przewidywanaNowaPozycja.oldPosition.x - wsp * przewidywanaNowaPozycja.directCoefficient.x, przewidywanaNowaPozycja.oldPosition.y - wsp * przewidywanaNowaPozycja.directCoefficient.y, przewidywanaNowaPozycja.oldPosition.z - wsp * przewidywanaNowaPozycja.directCoefficient.z);
            tmpGranica.odleglosc[5] = Math.sqrt(Math.pow(przewidywanaNowaPozycja.oldPosition.x - tmpGranica.pozycjaPrzeciecia[5].x, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.y - tmpGranica.pozycjaPrzeciecia[5].y, 2) + Math.pow(przewidywanaNowaPozycja.oldPosition.z - tmpGranica.pozycjaPrzeciecia[5].z, 2));
        }
        return tmpGranica;
    }
}
