package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by michalc on 2016-08-29.
 */
public class niepoprawneZmienne {


    private JPanel panel1;
    private JButton OKButton;


    public niepoprawneZmienne()  {

        //super("MainWindow");
        JFrame fr = new JFrame("niepoprawneZmienne");
        fr.getContentPane().add(new niepoprawneZmienne().panel1);
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(500, 300);
        fr.setVisible(true);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fr.setVisible(false);
            }
        });
    }

}
