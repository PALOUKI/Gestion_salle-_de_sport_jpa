package gui_client.view;

import entite.Client;

import javax.swing.*;
import java.awt.*;

public class ClientDashboardPanel extends JPanel {
    private JLabel welcomeLabel;
    private JButton voirAbonnementsButton;
    private JButton souscrireButton;
    private JButton deconnexionButton;

    public ClientDashboardPanel() {
        setLayout(new BorderLayout(40, 40));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        setBackground(new Color(245, 245, 245));

        // --- Panneau supérieur (En-tête) ---
        JPanel topPanel = new JPanel(new BorderLayout(20, 20));
        topPanel.setOpaque(false);

        welcomeLabel = new JLabel("", SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        welcomeLabel.setForeground(new Color(60, 63, 65));

        deconnexionButton = new JButton("Déconnexion");
        deconnexionButton.putClientProperty("JButton.buttonType", "roundRect");
        deconnexionButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        topPanel.add(welcomeLabel, BorderLayout.CENTER);
        topPanel.add(deconnexionButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- Panneau central avec les cartes d'action ---
        JPanel cardsPanel = new JPanel(new GridLayout(1, 0, 40, 40)); // Grille pour les cartes, espacement de 40px
        cardsPanel.setOpaque(false);

        voirAbonnementsButton = new JButton("Gérer mes abonnements");
        JPanel abonnementsCard = createActionCard(
            "Mes Abonnements",
            "Consultez l'état de vos abonnements, renouvelez ou annulez.",
            voirAbonnementsButton,
            "/icons/abonnement.png"
        );

        souscrireButton = new JButton("Découvrir nos offres");
        JPanel souscrireCard = createActionCard(
            "Nouvel Abonnement",
            "Explorez nos différentes formules et trouvez celle qui vous convient.",
            souscrireButton,
            "/icons/offre.png"
        );

        cardsPanel.add(abonnementsCard);
        cardsPanel.add(souscrireCard);

        add(cardsPanel, BorderLayout.CENTER);

        // --- Pied de page ---
        JLabel footerLabel = new JLabel("Votre centre de fitness - Toujours à vos côtés", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        footerLabel.setForeground(Color.GRAY);
        add(footerLabel, BorderLayout.SOUTH);
    }

    /**
     * Crée une carte d'action stylisée.
     * @param title Le titre de la carte.
     * @param description La description de l'action.
     * @param button Le bouton d'action.
     * @param iconPath Le chemin vers l'icône de la carte.
     * @return Un JPanel représentant la carte.
     */
    private JPanel createActionCard(String title, String description, JButton button, String iconPath) {
        JPanel card = new JPanel(new BorderLayout(15, 15));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(220, 220, 220)), // Bordure fine
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setBackground(Color.WHITE);
        // Effet d'ombre (simple via une bordure plus épaisse en bas et à droite)
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(200, 200, 200)),
                card.getBorder()
        ));

        // --- Icône ---
        // TODO: Remplacez les chemins par de vraies icônes (ex: /icons/abonnement.png)
        // Idéalement, des icônes de 48x48 ou 64x64 pixels.
        JLabel iconLabel = new JLabel();
        try {
            // Essayer de charger l'icône depuis les ressources
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            iconLabel.setIcon(icon);
        } catch (Exception e) {
            // Icône de secours si le fichier n'est pas trouvé
            iconLabel.setText("[ICON]");
            iconLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
            iconLabel.setForeground(new Color(0, 123, 255));
        }
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);


        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descriptionArea.setForeground(Color.DARK_GRAY);

        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(0, 45)); // Hauteur de 45px
        button.putClientProperty("JButton.buttonType", "roundRect");
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new BorderLayout(10, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(iconLabel, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        card.add(titlePanel, BorderLayout.NORTH);
        card.add(descriptionArea, BorderLayout.CENTER);
        card.add(button, BorderLayout.SOUTH);

        return card;
    }

    public void setClient(Client client) {
        if (client != null) {
            welcomeLabel.setText("Bienvenue, " + client.getPrenom() + " !");
        }
    }

    // Getters
    public JButton getVoirAbonnementsButton() { return voirAbonnementsButton; }
    public JButton getSouscrireButton() { return souscrireButton; }
    public JButton getDeconnexionButton() { return deconnexionButton; }
}
