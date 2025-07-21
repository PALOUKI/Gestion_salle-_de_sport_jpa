package org.example;

import com.formdev.flatlaf.FlatLightLaf;
import gui_admin.gui_util.MyWindow1;
import util.DataSeeder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.UnsupportedLookAndFeelException;

public class MainAdmin {
    public static void main(String[] args) {
        // Appliquer le Look and Feel FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("salle_de_sport");

        DataSeeder.seedInitialData(emf);

        SwingUtilities.invokeLater(() -> {
            MyWindow1 myAppWindow = new MyWindow1(emf);
            myAppWindow.setVisible(true);
        });
    }
}