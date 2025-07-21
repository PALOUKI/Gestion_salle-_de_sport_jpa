package gui_admin.view;

import entite.DemandeInscription;
import entite.Paiement;
import entite.Client;
import entite.Membre;

import gui_admin.gui_util.*;
import serviceJpa.*;

import javax.imageio.ImageIO;
import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TableauDeBordPanel extends JPanel {

    private final MyWindow1 parentWindow;
    private final MembreService membreService;
    private final PaiementService paiementService;
    private final ClientService clientService;
    private final AbonnementService abonnementService;
    private final DemandeInscriptionService demandeInscriptionService;

    private CustomTablePanel demandesTablePanel;
    private CustomTablePanel paiementsTablePanel;
    private List<DemandeInscription> latestDemandes;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TableauDeBordPanel(MyWindow1 parentWindow, EntityManagerFactory emf) {
        this.parentWindow = parentWindow;
        this.membreService = new MembreService(emf);
        this.paiementService = new PaiementService(emf);
        this.clientService = new ClientService(emf);
        this.abonnementService = new AbonnementService(emf);
        this.demandeInscriptionService = new DemandeInscriptionService(emf);

        this.setLayout(new BorderLayout());
        this.setBackground(new Color(237, 237, 237)); // Fond gris clair général

        // Titre principal
        JLabel titre = new JLabel("BIENVENUE SUR LE TABLEAU DE BORD !");
        titre.setFont(new Font("SansSerif", Font.BOLD, 24));
        titre.setForeground(new Color(60, 63, 65));
        titre.setBorder(new EmptyBorder(20, 30, 20, 0));
        this.add(titre, BorderLayout.NORTH);

        // Panneau central avec défilement
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(new EmptyBorder(0, 30, 30, 30));
        centre.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(centre);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane, BorderLayout.CENTER);

        // Panneau des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        statsPanel.setOpaque(false);

        // --- Cartes de statistiques ---
        JPanel membresPanel = createStatsCard(null, String.valueOf(membreService.countAll()), "Membres inscrits", new Color(217, 229, 242), new Color(153, 180, 209));
        addNavigationListener(membresPanel, "Membre", new Color(200, 215, 230));
        statsPanel.add(membresPanel);

        JPanel abonnementsPanel = createStatsCard("/icons/calendar.png", String.valueOf(abonnementService.countAll()), "Abonnements Actifs", new Color(250, 240, 215), new Color(224, 214, 196));
        addNavigationListener(abonnementsPanel, "Abonnement", new Color(235, 225, 200));
        statsPanel.add(abonnementsPanel);

        JPanel inscriptionsPanel = createStatsCard("/icons/envelope.png", String.valueOf(demandeInscriptionService.getUnprocessedRequestCount()), "Demandes d'Inscription", new Color(217, 242, 229), new Color(153, 209, 180));
        addNavigationListener(inscriptionsPanel, "DemandeInscription", new Color(200, 230, 215));
        statsPanel.add(inscriptionsPanel);

        centre.add(statsPanel);
        centre.add(Box.createVerticalStrut(30));

        // --- Section des tables ---
        JPanel demandesSection = createDemandesSection();
        centre.add(demandesSection);

        centre.add(Box.createVerticalStrut(30));

        JPanel paiementsSection = createPaiementsSection();
        centre.add(paiementsSection);
        centre.add(Box.createVerticalStrut(30));

    }

    private JPanel createStatsCard(String iconPath, String valueText, String labelText, Color bgColor, Color borderColor) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(bgColor);
        cardPanel.setBorder(new LineBorder(borderColor, 1));
        GridBagConstraints gbc = new GridBagConstraints();

        if (iconPath != null) {
            try (InputStream is = getClass().getResourceAsStream(iconPath)) {
                if (is != null) {
                    BufferedImage originalImage = ImageIO.read(is);
                    Image scaledImage = originalImage.getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                    JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
                    gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2;
                    gbc.insets = new Insets(10, 15, 10, 15);
                    cardPanel.add(iconLabel, gbc);
                } else {
                     System.err.println("Icon not found: " + iconPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JLabel valueLabel = new JLabel(valueText);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridheight = 1; gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 10, 0, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        cardPanel.add(valueLabel, gbc);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 1; gbc.gridy = 1; gbc.insets = new Insets(0, 10, 10, 10);
        cardPanel.add(textLabel, gbc);

        return cardPanel;
    }

    private void addNavigationListener(JPanel panel, String cardName, Color hoverColor) {
        Color originalColor = panel.getBackground();
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { parentWindow.navigateTo(cardName); }
            @Override
            public void mouseEntered(MouseEvent e) { panel.setBackground(hoverColor); }
            @Override
            public void mouseExited(MouseEvent e) { panel.setBackground(originalColor); }
        });
    }

    private JPanel createDemandesSection() {
        JPanel sectionPanel = new JPanel(new BorderLayout(0, 0));
        sectionPanel.setOpaque(false);

        // --- Panneau Titre avec bouton de rafraîchissement ---
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(60, 75, 110));
        titlePanel.setBorder(new EmptyBorder(8, 15, 4, 15));

        JLabel titleLabel = new JLabel("Nouvelles demandes d'inscription");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshButton = new JButton(new RefreshIcon());
        refreshButton.setToolTipText("Rafraîchir la liste");
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshDemandesTable());
        titlePanel.add(refreshButton, BorderLayout.EAST);

        sectionPanel.add(titlePanel, BorderLayout.NORTH);

        // --- Tableau ---
        latestDemandes = demandeInscriptionService.listerRecents(5);
        List<String> columns = Arrays.asList("ID", "Nom", "Prénom", "Email", "Date Demande", "Actions");
        demandesTablePanel = new CustomTablePanel(convertDemandesToTableData(latestDemandes), columns);

        // Ajouter le renderer et l'editor pour les boutons
        int actionsColumnIndex = 5;
        demandesTablePanel.getTable().getColumnModel().getColumn(actionsColumnIndex).setCellRenderer(new ActionsButtonsRenderer(latestDemandes));
        demandesTablePanel.getTable().getColumnModel().getColumn(actionsColumnIndex).setCellEditor(new ActionsButtonsEditor(demandesTablePanel.getTable(), this::approuverAction, this::rejeterAction, latestDemandes));
        demandesTablePanel.getTable().getColumnModel().getColumn(actionsColumnIndex).setMinWidth(160);
        demandesTablePanel.getTable().setRowHeight(30);

        demandesTablePanel.getTable().getTableHeader().setBackground(new Color(220, 225, 230));
        demandesTablePanel.getTable().getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        demandesTablePanel.setBorder(new LineBorder(new Color(200, 200, 220), 1));
        sectionPanel.add(demandesTablePanel, BorderLayout.CENTER);

        return sectionPanel;
    }

    private JPanel createPaiementsSection() {
        JPanel sectionPanel = new JPanel(new BorderLayout(0, 0));
        sectionPanel.setOpaque(false);

        // Titre et bouton de rafraîchissement
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(60, 75, 110));
        titlePanel.setBorder(new EmptyBorder(8, 15, 4, 15));

        JLabel titleLabel = new JLabel("Historique des paiements récents");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JButton refreshButton = new JButton(new RefreshIcon());
        refreshButton.setToolTipText("Rafraîchir l'historique des paiements");
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> refreshPaiementsTable());
        titlePanel.add(refreshButton, BorderLayout.EAST);

        sectionPanel.add(titlePanel, BorderLayout.NORTH);

        // Initialisation de la table
        List<String> columnNames = Arrays.asList("ID", "Membre", "Montant", "Date Paiement", "Moyen");
        paiementsTablePanel = new CustomTablePanel(
                convertPaiementsToTableData(paiementService.listerRecents(5)),
                columnNames
        );
        paiementsTablePanel.getTable().getTableHeader().setBackground(new Color(220, 225, 230));
        paiementsTablePanel.getTable().getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        paiementsTablePanel.setBorder(new LineBorder(new Color(200, 200, 220), 1));

        // Renderer pour la colonne Montant
        int montantColumnIndex = 2;
        paiementsTablePanel.getTable().getColumnModel().getColumn(montantColumnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof Number) {
                    setText(String.format("%,.0f FCFA", ((Number) value).doubleValue()));
                    setHorizontalAlignment(SwingConstants.RIGHT);
                }
                return c;
            }
        });

        sectionPanel.add(paiementsTablePanel, BorderLayout.CENTER);

        return sectionPanel;
    }

    private void refreshDemandesTable() {
        latestDemandes = demandeInscriptionService.listerRecents(5);
        List<List<Object>> data = convertDemandesToTableData(latestDemandes);
        demandesTablePanel.updateTableData(data);
        // Réappliquer le renderer et l'editor
        int actionsColumnIndex = 5;
        demandesTablePanel.getTable().getColumnModel().getColumn(actionsColumnIndex).setCellRenderer(new ActionsButtonsRenderer(latestDemandes));
        demandesTablePanel.getTable().getColumnModel().getColumn(actionsColumnIndex).setCellEditor(new ActionsButtonsEditor(demandesTablePanel.getTable(), this::approuverAction, this::rejeterAction, latestDemandes));
    }

    private void refreshPaiementsTable() {
        List<Paiement> recentPaiements = paiementService.listerRecents(5);
        paiementsTablePanel.updateTableData(convertPaiementsToTableData(recentPaiements));
        System.out.println("Tableau des paiements rafraîchi.");
    }

    private void approuverAction(ActionEvent e) {
        int row = Integer.parseInt(e.getActionCommand());
        DemandeInscription demande = latestDemandes.get(row);

        if (!demande.getStatut().equals("EN_ATTENTE")) {
            JOptionPane.showMessageDialog(this, "Cette demande a déjà été traitée.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            Client clientAssocie = demande.getClient();
            if (clientAssocie == null) {
                JOptionPane.showMessageDialog(this, "Erreur critique : Aucun client n'est associé à cette demande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (membreService.membreExistePourClient(clientAssocie.getId())) {
                JOptionPane.showMessageDialog(this, "Un membre existe déjà pour ce client.", "Doublon", JOptionPane.WARNING_MESSAGE);
                // Marquer la demande comme traitée pour éviter de la re-traiter
                demande.setStatut("VALIDEE"); // ou un statut spécial comme "DOUBLON_TRAITE"
                demande.setDateDeTraitement(java.time.LocalDateTime.now());
                demandeInscriptionService.modifier(demande);
                refreshDemandesTable();
                return;
            }

            Membre nouveauMembre = new Membre();
            nouveauMembre.setClient(clientAssocie);
            nouveauMembre.setDateInscription(java.time.LocalDateTime.now());
            membreService.ajouter(nouveauMembre);

            demande.setStatut("VALIDEE");
            demande.setDateDeTraitement(java.time.LocalDateTime.now());
            demandeInscriptionService.modifier(demande);

            JOptionPane.showMessageDialog(this, "La demande a été approuvée avec succès. Le membre a été créé.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            refreshDemandesTable();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'approbation : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void rejeterAction(ActionEvent e) {
        int row = Integer.parseInt(e.getActionCommand());
        DemandeInscription demande = latestDemandes.get(row);

        if (!demande.getStatut().equals("EN_ATTENTE")) {
            JOptionPane.showMessageDialog(this, "Cette demande a déjà été traitée.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir rejeter cette demande ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                demande.setStatut("REJETEE");
                demande.setDateDeTraitement(java.time.LocalDateTime.now());
                demandeInscriptionService.modifier(demande);
                JOptionPane.showMessageDialog(this, "La demande a été rejetée.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                refreshDemandesTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors du rejet : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private JPanel createTableSection(String title, String[] columns, List<List<Object>> data) {
        JPanel sectionPanel = new JPanel(new BorderLayout());
        sectionPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(60, 75, 110)); // Fond bleu pour le titre
        titleLabel.setBorder(new EmptyBorder(8, 15, 4, 15));
        sectionPanel.add(titleLabel, BorderLayout.NORTH);

        CustomTablePanel customTable = new CustomTablePanel(data, Arrays.asList(columns));
        customTable.getTable().getTableHeader().setBackground(new Color(220, 225, 230));
        customTable.getTable().getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        customTable.setBorder(new LineBorder(new Color(200, 200, 220), 1));
        sectionPanel.add(customTable, BorderLayout.CENTER);

        return sectionPanel;
    }

    private List<List<Object>> convertDemandesToTableData(List<DemandeInscription> demandes) {
        return demandes.stream()
                .map(demande -> new ArrayList<>(Arrays.asList(
                        (Object) demande.getId(),
                        demande.getNom(),
                        demande.getPrenom(),
                        demande.getEmail(),
                        demande.getDateDeDemande() != null ? demande.getDateDeDemande().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A",
                        demande)) // Passer l'objet entier
                )
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private List<List<Object>> convertPaiementsToTableData(List<Paiement> paiements) {
        return paiements.stream()
                .map(p -> {
                    String membreStr = (p.getAbonnement() != null && p.getAbonnement().getMembre() != null && p.getAbonnement().getMembre().getClient() != null)
                            ? p.getAbonnement().getMembre().getClient().getNom() + " " + p.getAbonnement().getMembre().getClient().getPrenom()
                            : "N/A";
                    return Arrays.asList(
                            (Object) p.getId(),
                            membreStr,
                            String.format("%.2f €", (double) p.getMontant()),
                            p.getDateDePaiement() != null ? p.getDateDePaiement().format(DATE_FORMATTER) : "N/A",
                            p.getMoyenDePaiement() != null ? p.getMoyenDePaiement().getLibelle() : "N/A");
                })
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }
}