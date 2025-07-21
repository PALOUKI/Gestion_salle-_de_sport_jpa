package gui_admin.gui_util;

import gui_admin.controller.HistoriqueSeancesController;
import gui_admin.controllerJpa.*;
import gui_admin.view.TableauDeBordPanel;
import gui_admin.view.seances.HistoriqueSeancesPanel;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MyWindow1 extends MyWindow {

    private final List<MyButton> menuButtons = new ArrayList<>();

    public MyWindow1(EntityManagerFactory emf) {
        super(); // Appel au constructeur de MyWindow
        setTitle("NL Application");

        // --- Contrôleurs ---
        DemandeInscriptionController demandeInscriptionController = new DemandeInscriptionController(emf);
        MembreController membreController = new MembreController(emf);
        ClientController clientController = new ClientController(emf);
        AbonnementController abonnementController = new AbonnementController(emf);
        PaiementController paiementController = new PaiementController(emf);
        SeanceController seanceController = new SeanceController(emf);
        SalleController salleController = new SalleController(emf);
        EquipementController equipementController = new EquipementController(emf);
        HoraireController horaireController = new HoraireController(emf);
        TicketController ticketController = new TicketController(emf);
        TypeAbonnementController typeAbonnementController = new TypeAbonnementController(emf);
        MoyenDePaiementController moyenDePaiementController = new MoyenDePaiementController(emf);
        HistoriqueSeancesController historiqueSeancesController = new HistoriqueSeancesController(new HistoriqueSeancesPanel(), emf);

        // --- Vues (ajoutées au panneau 'center' hérité) ---
        center.add(new TableauDeBordPanel(this, emf), "TableauDeBord");
        center.add(demandeInscriptionController.createView(), "DemandeInscription");
        center.add(membreController.getView(), "Membre");
        center.add(clientController.createAndConfigureEditPanelForAdd(), "Client");
        center.add(abonnementController.createAndConfigureEditPanelForAdd(), "Abonnement");
        center.add(paiementController.createAndConfigureEditPanelForAdd(), "Paiement");
        center.add(seanceController.createAndConfigureEditPanelForAdd(), "Seance");
        center.add(historiqueSeancesController.getView(), "HistoriqueSeances");
        center.add(salleController.createAndConfigureEditPanelForAdd(), "Salle");
        center.add(equipementController.createAndConfigureEditPanelForAdd(), "Equipement");
        center.add(horaireController.createAndConfigureEditPanelForAdd(), "Horaire");
        center.add(ticketController.createAndConfigureEditPanelForAdd(), "Ticket");
        center.add(typeAbonnementController.createAndConfigureEditPanelForAdd(), "TypeAbonnement");
        center.add(moyenDePaiementController.createAndConfigureEditPanelForAdd(), "MoyenPaiement");

        // --- Création des panneaux de l'UI ---
        JPanel menuPanel = createMenuPanel(); // Utilise les couleurs héritées
        JPanel headerPanel = createHeaderPanel();

        // --- Assemblage de la fenêtre principale ---
        // Les panneaux sont déjà dans le layout de MyWindow, il suffit d'ajouter le contenu
        north.add(headerPanel, BorderLayout.CENTER);
        west.add(menuPanel, BorderLayout.CENTER);

        // Afficher le premier panel et marquer le bouton comme actif
        cardLayout.show(center, "TableauDeBord");
        setActiveButton(menuButtons.get(0));
    }

    public void navigateTo(String cardName) {
        cardLayout.show(center, cardName);
        for (MyButton btn : menuButtons) {
            if (cardName.equals(btn.getActionCommand())) {
                setActiveButton(btn);
                break;
            }
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        // Champ de recherche à gauche
        JTextField searchField = new JTextField("Search...");
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        // Placeholder text logic
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        headerPanel.add(searchField, BorderLayout.CENTER);

        // Infos utilisateur à droite
        JLabel userInfo = new JLabel("Admin");
        userInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        headerPanel.add(userInfo, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor); // Couleur héritée
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Titre de l'application dans le menu
        JLabel appTitle = new JLabel("NL Application");
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        appTitle.setForeground(Color.WHITE);
        appTitle.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(appTitle);

        // Création des boutons de menu
        panel.add(createMenuButton("Dashboard", "TableauDeBord", null));
        panel.add(createMenuButton("Demandes d'inscription", "DemandeInscription", "demandeInscription.png"));
        panel.add(createMenuButton("Membres", "Membre", "membres.png"));
        panel.add(createMenuButton("Clients", "Client", "clients.png"));
        panel.add(createMenuButton("Abonnements", "Abonnement", "abonnements.png"));
        panel.add(createMenuButton("Paiements", "Paiement", "paiements.png"));
        panel.add(createMenuButton("Séances", "Seance", "seances.png"));
        panel.add(createMenuButton("Historique Séances", "HistoriqueSeances", "historique.png"));

        panel.add(Box.createVerticalStrut(20));
        JLabel labelDonneesReference = new JLabel("My Data");
        labelDonneesReference.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelDonneesReference.setForeground(new Color(150, 150, 150));
        labelDonneesReference.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 0));
        labelDonneesReference.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(labelDonneesReference);

        panel.add(createMenuButton("Salles", "Salle", "salles.png"));
        panel.add(createMenuButton("Équipements", "Equipement", "equipements.png"));
        panel.add(createMenuButton("Horaires", "Horaire", "horaires.png"));
        panel.add(createMenuButton("Tickets", "Ticket", "tickets.png"));
        panel.add(createMenuButton("Types d'abonnement", "TypeAbonnement", "typeAbonnement.png"));
        panel.add(createMenuButton("Moyens de paiement", "MoyenPaiement", "moyenDePaiement.png"));

        panel.add(Box.createVerticalGlue()); // Pousse tout vers le haut

        return panel;
    }

    private MyButton createMenuButton(String text, String cardName, String iconName) {
        MyButton button = new MyButton(text);
        menuButtons.add(button);
        button.setActionCommand(cardName);

        if (iconName != null) {
            try {
                ImageIcon originalIcon = new ImageIcon(getClass().getResource("/icons/" + iconName));
                Image originalImage = originalIcon.getImage();

                // Créer une BufferedImage pour la manipuler
                BufferedImage bufferedImage = new BufferedImage(
                        originalImage.getWidth(null),
                        originalImage.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);

                // Dessiner l'image originale sur la nouvelle
                Graphics2D g2d = bufferedImage.createGraphics();
                g2d.drawImage(originalImage, 0, 0, null);
                g2d.dispose();

                // Recolorer les pixels en blanc, en préservant la transparence
                for (int x = 0; x < bufferedImage.getWidth(); x++) {
                    for (int y = 0; y < bufferedImage.getHeight(); y++) {
                        int rgba = bufferedImage.getRGB(x, y);
                        if ((rgba & 0xff000000) != 0) { // Si le pixel n'est pas transparent
                            bufferedImage.setRGB(x, y, 0xffffffff); // Le mettre en blanc
                        }
                    }
                }

                // Redimensionner l'icône et l'appliquer
                Image whiteImage = bufferedImage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(whiteImage));

            } catch (Exception e) {
                System.err.println("Impossible de charger ou colorer l'icône : " + iconName);
            }
        }

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15); // Espace entre l'icône et le texte

        button.addActionListener(e -> {
            cardLayout.show(center, cardName);
            setActiveButton(button);
        });

        return button;
    }

    private void setActiveButton(MyButton activeButton) {
        for (MyButton btn : menuButtons) {
            btn.setSelected(btn == activeButton);
        }
    }
}