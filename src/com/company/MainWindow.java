package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ciechan on 2016-08-07.
 */
public class MainWindow {
    private JPanel panel1;
    private JLabel detectorTypeLabel;
    private JLabel detectorSubstanceLabel;
    private JLabel resolutionLabel;
    private JLabel detectorThicknesLabel;
    private JLabel XenergyLabel;
    private JLabel cellWidthLabel;
    private JLabel numberOfCellsLabel;
    private JLabel numberOfLightPhotonsLabel;
    private JTextField detectorSubstanceTextField1;
    private JTextField resolutionTextField;
    private JTextField thicknessTextField;
    private JTextField energyTextField;
    private JTextField cellWidthTextField;
    private JTextField cellNumberTextField;
    private JTextField probablityOfAbsorptiontextField;
    private JTextField probablityOfDispersiontextField;
    private JTextField probablityOfReflectionTextField;
    private JTextField xRayConversioncooficientTextField;
    private JTextField lightWaveLengthTextField;
    private JTextField xRayMassatCoTextField;
    private JTextField detectorDensityTextField;
    private JTextField numberOfLSFTextField;
    private JTextField numberOfMTFXPhotonsTextField;
    private JTextField numberOfMTFLoopsextField;
    private JTextField numberOFNPSXPhotonsTextField;
    private JTextField numberOfNPSLoopsTextField;
    private JTextField filenameTextField;
    private JTextField LightPhotonMultiply;

    private JComboBox detectorTypeComboBox1;

    private JCheckBox ustalLiczbęFotonówŚwiatłaCheckBox;

    private JButton symulacjaButton;

    private String filePath;

    public static void main(String[] args)   {

        JFrame frame = new JFrame("MainWindow");
        frame.getContentPane().add(new MainWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1150, 300);
        frame.setVisible(true);

    }

    public MainWindow() {
        setDefaultPath();

        symulacjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean czyMoznaSymulowac = varifyInputData();
                if (czyMoznaSymulowac) {

                } else {

                }
            }
        });

        ustalLiczbęFotonówŚwiatłaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ustalLiczbęFotonówŚwiatłaCheckBox.isSelected()) {
                    energyTextField.setEditable(true);
                    xRayConversioncooficientTextField.setEditable(true);
                } else {
                    energyTextField.setEditable(false);
                    xRayConversioncooficientTextField.setEditable(false);
                }

            }
        });
    }


    private boolean varifyInputData() {
        ArrayList<Optional<String>> list = new ArrayList<>();
        list.add(verifyIfInt(thicknessTextField));
        list.add(verifyIfInt(cellWidthTextField));
        list.add(verifyIfInt(cellNumberTextField));
        list.add(verifyIfInt(resolutionTextField));
        list.add(verifyIfInt(LightPhotonMultiply));
        list.add(verifyIfInt(numberOfLSFTextField));
        list.add(verifyIfInt(numberOfMTFXPhotonsTextField));
        list.add(verifyIfInt(numberOfMTFLoopsextField));
        list.add(verifyIfInt(numberOFNPSXPhotonsTextField));
        list.add(verifyIfInt(numberOfNPSLoopsTextField));
        list.add(verifyIfInt(lightWaveLengthTextField));
        list.add(verifyIfInt(energyTextField));
        //double variables
        list.add(verifyIfDouble(detectorDensityTextField));
        list.add(verifyIfDouble(probablityOfAbsorptiontextField));
        list.add(verifyIfDouble(probablityOfDispersiontextField));
        list.add(verifyIfDouble(probablityOfReflectionTextField));
        list.add(verifyIfDouble(xRayMassatCoTextField));
        list.add(verifyIfDouble(xRayConversioncooficientTextField));
        list.add(verifyIfCorrectFilename(filenameTextField));

        list.stream()
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));

        if (list.size() == 0) {
            System.out.print("Wartości są poprawne" + "\n");
            return true;
        } else {
            System.out.print("Zmienne niepoprawne" + "\n");
            return false;
        }
    }

    private Optional<String> verifyIfInt(JTextField textField) {
        try {
            int number = Integer.parseInt(textField.getText());
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.of(textField.getText());
        }
    }

    private Optional<String> verifyIfDouble(JTextField textField) {
        try {
            double number = Double.parseDouble(textField.getText());
            return Optional.empty();
        } catch (NumberFormatException e) {
            return Optional.of(textField.getText());
        }
    }

    private Optional<String> verifyIfCorrectFilename(JTextField textField) {
        if(filenameTextField.getText()!="wynikSymulacji"){
            File f = new File(filenameTextField.getText());
            try {
                f.getCanonicalPath();
                System.out.print("Poprawna nazwa pliku"+"\n");
                filePath=(f.getCanonicalPath()+".dat");
                //f.createNewFile();
                System.out.print(filePath+"\n");
                return Optional.empty();
            } catch (IOException e) {
                System.out.print("Zła nazwa pliku!!!!!!!!!!!!!!!!!!!!!!!!!!"+"\n");
                return Optional.of(textField.getText());
            }
        }
        else
            return Optional.empty();
    }

    private void setDefaultPath() {
        filenameTextField.setText("wynikSymulacji");
        File f = new File(filenameTextField.getText()+".dat");
        try {
            System.out.print("Domyślnie utworzona ściażka:"+"\n");
            filePath=f.getCanonicalPath();
            System.out.print(f.getCanonicalPath()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
