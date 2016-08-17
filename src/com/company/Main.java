package com.company;
import java.io.IOException;

import java.io.File;
import javax.swing.JFileChooser;

public class Main {

    public static void main(String[] args) throws IOException {

        Constants zmienne = new Constants(5, 150.0, 1024, 1024, 20, 10, 200000, 100, 10, 0.2, 545.0, 3.2, 7.34, 0.9, 499.55, 0.9,5,false,100,20,"Gd2O2S:Tb");
        //Constants zmiennek = new Constants(5, 100.0, 1024, 1024, 20, 10, 200000, 100, 10, 0.2, 545.0, 3.2, 7.34, 0.9, 499.55, 0.9,5,false,100,20,"Gd2O2S:Tb");

         LSF lsf = new LSF(zmienne);
        //LSF lsfk = new LSF(zmiennek);

        lsf.saveLSF2(zmienne);
        //lsfk.saveLSF2(zmiennek);

/*        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
            System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
        }*/



    }
}