package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private JButton wybierzFolderButton;
    private JButton symulacjaButton;
    private JLabel ll;


    public static void main(String[] args) {

        JFrame frame = new JFrame("MainWindow");
        frame.getContentPane().add(new MainWindow().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1150, 300);
        frame.setVisible(true);

    }

    public MainWindow() {
        symulacjaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                varifyInputData();
            }
        });

        wybierzFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Wybieranie folderu do zapisania pliku");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
                    System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
                } else {
                    System.out.println("Nie wybrano folderu");
                }

            }
        });
        LightPhotonMultiply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int number = Integer.parseInt(LightPhotonMultiply.getText());
                    int str =  (number*10000);
                    String s = String.valueOf(str);
                    ll.setText(s);
                } catch (NumberFormatException ee) {
                    ll.setText(String.valueOf(100000));
                    LightPhotonMultiply.setText(String.valueOf(10));
                }
            }
        });
    }

    private void varifyInputData() {
        ArrayList<Optional<String>> list= new ArrayList<>();
        System.out.print(detectorTypeComboBox1.getSelectedItem() + "\n");
        System.out.print(detectorSubstanceTextField1.getText() + "\n");
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

        list.stream()
                .filter(Optional::isPresent)
                .map(Optional::get).collect(Collectors.toCollection(ArrayList::new));


        list.forEach(str -> System.out.print(str+"\n"));
    }

    private Optional<String> verifyIfInt(JTextField textField) {
        try {
            int number = Integer.parseInt(textField.getText());
            System.out.print("To int !!!!"+"\n");
            return Optional.empty();
        } catch (NumberFormatException e) {
            System.out.print("To nie int !!!!"+"\n");
            return Optional.of(textField.getText());
        }
    }

    private Optional<String> verifyIfDouble(JTextField textField) {
        try {
            double number = Double.parseDouble(textField.getText());
            System.out.print("To double !!!!"+"\n");
            return Optional.empty();
        } catch (NumberFormatException e) {
            System.out.print("To nie double !!!!"+"\n");
            return Optional.of(textField.getText());
        }
    }

}
