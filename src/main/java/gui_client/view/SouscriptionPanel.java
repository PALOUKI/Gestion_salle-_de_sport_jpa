package gui_client.view;

import entite.MoyenDePaiement;
import entite.TypeAbonnement;

import javax.swing.*;
import java.awt.*;

public class SouscriptionPanel extends JPanel {
    private JComboBox<TypeAbonnement> typeAbonnementComboBox;
    private JComboBox<MoyenDePaiement> moyenPaiementComboBox;
    private JLabel prixLabel;
    private JButton souscrireButton;
    private JButton retourButton;

    public SouscriptionPanel() {
        setLayout(new BorderLayout(40, 30));
        setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        setBackground(new Color(245, 245, 245));

        // --- Titre ---
        JLabel titleLabel = new JLabel("Nouvel Abonnement", SwingConstants.LEFT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(60, 63, 65));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // --- Panneau central pour le formulaire ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(createSubscriptionOptionsPanel());
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        centerPanel.add(createPaymentPanel());

        add(centerPanel, BorderLayout.CENTER);

        // --- Panneau des boutons d'action en bas ---
        JPanel buttonPanel = new JPanel(new BorderLayout(20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        souscrireButton = new JButton("Confirmer et Payer");
        souscrireButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        souscrireButton.setPreferredSize(new Dimension(0, 45));
        souscrireButton.putClientProperty("JButton.buttonType", "roundRect");

        retourButton = new JButton("Retour");
        retourButton.setFont(new Font("SansSerif", Font.PLAIN, 14));

        buttonPanel.add(souscrireButton, BorderLayout.CENTER);
        buttonPanel.add(retourButton, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createSubscriptionOptionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel sectionTitle = new JLabel("1. Choisissez votre formule");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(sectionTitle, BorderLayout.NORTH);

        typeAbonnementComboBox = new JComboBox<>();
        typeAbonnementComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        typeAbonnementComboBox.setPreferredSize(new Dimension(0, 40));

        prixLabel = new JLabel("-- €");
        prixLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        prixLabel.setForeground(new Color(0, 120, 0));

        JPanel selectionPanel = new JPanel(new BorderLayout(20, 0));
        selectionPanel.setOpaque(false);
        selectionPanel.add(typeAbonnementComboBox, BorderLayout.CENTER);
        selectionPanel.add(prixLabel, BorderLayout.EAST);

        panel.add(selectionPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel sectionTitle = new JLabel("2. Sélectionnez votre moyen de paiement");
        sectionTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(sectionTitle, BorderLayout.NORTH);

        moyenPaiementComboBox = new JComboBox<>();
        moyenPaiementComboBox.setFont(new Font("SansSerif", Font.PLAIN, 16));
        moyenPaiementComboBox.setPreferredSize(new Dimension(0, 40));

        panel.add(moyenPaiementComboBox, BorderLayout.CENTER);
        return panel;
    }

    // Getters
    public JComboBox<TypeAbonnement> getTypeAbonnementComboBox() { return typeAbonnementComboBox; }
    public JComboBox<MoyenDePaiement> getMoyenPaiementComboBox() { return moyenPaiementComboBox; }
    public JLabel getPrixLabel() { return prixLabel; }
    public JButton getSouscrireButton() { return souscrireButton; }
    public JButton getRetourButton() { return retourButton; }
}
