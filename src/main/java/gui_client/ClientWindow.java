package gui_client;

import com.formdev.flatlaf.FlatLightLaf;
import entite.Client;
import gui_client.controller.*;
import gui_client.view.*;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.awt.*;

public class ClientWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private Client clientConnecte;
    private ClientDashboardPanel clientDashboardPanel;
    private AbonnementsClientPanel abonnementsClientPanel;
    private AbonnementsClientController abonnementsClientController;
    private SouscriptionPanel souscriptionPanel;
    private SouscriptionController souscriptionController;

    public ClientWindow(EntityManagerFactory emf) {
        // Appliquer le Look and Feel FlatLaf
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
            // Gérer l'erreur si le look and feel n'est pas supporté
        }

        setTitle("Espace Client - Gestion Salle de Sport");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrer la fenêtre

        // Initialisation du CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // --- Panneau d'accueil amélioré ---
        JPanel welcomePanel = new JPanel(new BorderLayout(20, 20));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));
        welcomePanel.setBackground(new Color(245, 245, 245)); // Fond gris clair

        // Titre et sous-titre
        JPanel titleContainer = new JPanel();
        titleContainer.setLayout(new BoxLayout(titleContainer, BoxLayout.Y_AXIS));
        titleContainer.setOpaque(false);

        JLabel titleLabel = new JLabel("Bienvenue à Votre Salle de Sport", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(new Color(60, 63, 65));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Connectez-vous ou créez un compte pour commencer.", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleContainer.add(titleLabel);
        titleContainer.add(Box.createRigidArea(new Dimension(0, 10)));
        titleContainer.add(subtitleLabel);

        welcomePanel.add(titleContainer, BorderLayout.NORTH);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false); // Transparent pour voir le fond du welcomePanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        JButton loginButton = new JButton("Se Connecter");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginButton.setPreferredSize(new Dimension(220, 60));
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.setBackground(new Color(0, 123, 255));
        loginButton.setForeground(Color.WHITE);
        buttonPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        JButton inscriptionButton = new JButton("Créer un compte");
        inscriptionButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        inscriptionButton.setPreferredSize(new Dimension(220, 60));
        inscriptionButton.putClientProperty("JButton.buttonType", "roundRect");
        buttonPanel.add(inscriptionButton, gbc);

        welcomePanel.add(buttonPanel, BorderLayout.CENTER);

        // Footer
        JLabel footerLabel = new JLabel("Votre santé, notre priorité.", SwingConstants.CENTER);
        footerLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        footerLabel.setForeground(Color.GRAY);
        welcomePanel.add(footerLabel, BorderLayout.SOUTH);

        mainPanel.add(welcomePanel, "accueil");

        // --- Panneau d'inscription ---
        DemandeInscriptionPanel inscriptionPanel = new DemandeInscriptionPanel();
        new DemandeInscriptionClientController(inscriptionPanel, this, emf);
        mainPanel.add(inscriptionPanel, "inscription");

        // --- Panneau de connexion ---
        LoginPanel loginPanel = new LoginPanel();
        new LoginController(loginPanel, this, emf);
        mainPanel.add(loginPanel, "login");

        // --- Panneau du tableau de bord client ---
        clientDashboardPanel = new ClientDashboardPanel();
        new ClientDashboardController(clientDashboardPanel, this);
        mainPanel.add(clientDashboardPanel, "dashboard");

        // --- Panneau de consultation des abonnements ---
        abonnementsClientPanel = new AbonnementsClientPanel();
        abonnementsClientController = new AbonnementsClientController(abonnementsClientPanel, this, emf);
        mainPanel.add(abonnementsClientPanel, "abonnements_client");

        // --- Panneau de souscription ---
        souscriptionPanel = new SouscriptionPanel();
        souscriptionController = new SouscriptionController(souscriptionPanel, this, emf);
        mainPanel.add(souscriptionPanel, "souscription");

        // Listeners de navigation
        inscriptionButton.addActionListener(e -> navigateTo("inscription"));
        loginButton.addActionListener(e -> navigateTo("login"));
        // Le bouton de retour est géré dans les contrôleurs respectifs

        // Afficher le panneau d'accueil par défaut
        cardLayout.show(mainPanel, "accueil");

        add(mainPanel);
    }

    // Méthode pour naviguer entre les panneaux
    public void navigateTo(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }

    public void setClientConnecte(Client client) {
        this.clientConnecte = client;
        if (client != null) {
            clientDashboardPanel.setClient(client);
        }
    }

    public Client getClientConnecte() {
        return clientConnecte;
    }

    public AbonnementsClientController getAbonnementsClientController() {
        return abonnementsClientController;
    }

    public SouscriptionController getSouscriptionController() {
        return souscriptionController;
    }
}
