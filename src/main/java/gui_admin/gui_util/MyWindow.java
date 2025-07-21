package gui_admin.gui_util;

import javax.swing.*;
import java.awt.*;

public class MyWindow extends JFrame {

    protected JPanel west = new JPanel();
    protected JPanel north = new JPanel();
    protected JPanel south = new JPanel();
    protected JPanel center; 
    protected CardLayout cardLayout = new CardLayout();

    // Définition des couleurs pour être accessibles par les classes filles
    protected Color primaryColor = new Color(26, 26, 69); // Bleu très foncé pour le menu
    protected Color secondaryColor = new Color(32, 64, 128); // Bleu foncé pour l'en-tête
    protected Color lightBlueColor = new Color(173, 216, 230); // Bleu clair pour le pied de page
    protected Color backgroundColor = new Color(245, 245, 245); // Gris clair pour le fond

    public MyWindow(){
        this.setSize(1400, 900);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // DISPOSE_ON_CLOSE est généralement plus sûr pour les sous-fenêtres. Pour la fenêtre principale, EXIT_ON_CLOSE est souvent utilisé.
        this.setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

        // Initialiser le panneau central avec CardLayout
        center = new JPanel(cardLayout);

        // Définition des dimensions (optionnel, peut être ajusté par les layouts)
        west.setPreferredSize(new Dimension(250, 800)); // Largeur fixe pour le panneau de menu
        north.setLayout(new BorderLayout()); // Pour que le contenu s'étende
        west.setLayout(new BorderLayout()); // Pour que le contenu s'étende

        // Définition des couleurs de fond
        north.setBackground(secondaryColor);
        south.setBackground(lightBlueColor);
        west.setBackground(primaryColor);
        center.setBackground(backgroundColor);

        // Ajout des panneaux à la fenêtre de base
        this.setLayout(new BorderLayout());
        this.add(west, BorderLayout.WEST);
        this.add(north, BorderLayout.NORTH);
        this.add(south, BorderLayout.SOUTH);
        this.add(center, BorderLayout.CENTER); // Le panneau central est ajouté ici
    }
}