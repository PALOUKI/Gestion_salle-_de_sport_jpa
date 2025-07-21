package org.example;

import com.formdev.flatlaf.FlatLightLaf;
import gui_client.ClientWindow;
import util.DataSeeder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import javax.swing.UnsupportedLookAndFeelException;

public class MainClient {
    public static void main(String[] args) {
        // Appliquer Le Look and Feel FlatLaf:
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("salle_de_sport");

        DataSeeder.seedInitialData(emf);

        SwingUtilities.invokeLater(() -> {
            ClientWindow clientWindow = new ClientWindow(emf);
            clientWindow.setVisible(true);
        });
    }
}
