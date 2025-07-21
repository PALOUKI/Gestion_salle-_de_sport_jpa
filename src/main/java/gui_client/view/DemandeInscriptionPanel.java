package gui_client.view;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;

public class DemandeInscriptionPanel extends JPanel {
    private JTextField nomField, prenomField, emailField;
    private JPasswordField passwordField;
    private JDateChooser dateNaissanceChooser;
    private JButton soumettreButton;
    private JButton retourButton;

    public DemandeInscriptionPanel() {
        // --- Configuration globale du panel ---
        setLayout(new BorderLayout(0, 30)); // Espace vertical entre les composants
        setBorder(BorderFactory.createEmptyBorder(40, 100, 40, 100)); // Marges généreuses
        setBackground(new Color(245, 245, 245)); // Fond gris clair cohérent

        // --- Titre ---
        JLabel titleLabel = new JLabel("Créer votre compte", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(60, 63, 65));
        add(titleLabel, BorderLayout.NORTH);

        // --- Panneau du formulaire ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Transparent
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Création et stylisation des champs ---
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(200, 40);

        nomField = createStyledTextField("Entrez votre nom", fieldFont, fieldSize);
        prenomField = createStyledTextField("Entrez votre prénom", fieldFont, fieldSize);
        emailField = createStyledTextField("exemple@email.com", fieldFont, fieldSize);

        passwordField = new JPasswordField();
        passwordField.putClientProperty("JTextField.placeholderText", "••••••••");
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(fieldSize);

        dateNaissanceChooser = new JDateChooser();
        dateNaissanceChooser.setDateFormatString("dd/MM/yyyy");
        dateNaissanceChooser.setFont(fieldFont);
        dateNaissanceChooser.setPreferredSize(fieldSize);

        // --- Ajout des libellés et des champs au formulaire ---
        Font labelFont = new Font("SansSerif", Font.BOLD, 14);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(createStyledLabel("Nom", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 0; formPanel.add(nomField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(createStyledLabel("Prénom", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(prenomField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(createStyledLabel("Date de Naissance", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(dateNaissanceChooser, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(createStyledLabel("Email", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(createStyledLabel("Mot de passe", labelFont), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(passwordField, gbc);

        // --- Wrapper pour centrer le formulaire ---
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formPanel);
        add(new JScrollPane(formWrapper), BorderLayout.CENTER);

        // --- Panneau des boutons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setOpaque(false);

        soumettreButton = new JButton("Soumettre la Demande");
        soumettreButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        soumettreButton.setPreferredSize(new Dimension(250, 50));
        soumettreButton.putClientProperty("JButton.buttonType", "roundRect");
        soumettreButton.putClientProperty("JButton.defaultButton", true); // Style de bouton par défaut (souvent bleu)

        retourButton = new JButton("Retour");
        retourButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        retourButton.setPreferredSize(new Dimension(150, 50));
        retourButton.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(retourButton);
        buttonPanel.add(soumettreButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // --- Méthodes utilitaires pour la stylisation ---
    private JTextField createStyledTextField(String placeholder, Font font, Dimension size) {
        JTextField textField = new JTextField();
        textField.putClientProperty("JTextField.placeholderText", placeholder);
        textField.setFont(font);
        textField.setPreferredSize(size);
        return textField;
    }

    private JLabel createStyledLabel(String text, Font font) {
        JLabel label = new JLabel(text + ":");
        label.setFont(font);
        label.setForeground(new Color(80, 80, 80));
        return label;
    }

    // Getters pour les champs et boutons
    public JTextField getNomField() { return nomField; }
    public JTextField getPrenomField() { return prenomField; }
    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JDateChooser getDateNaissanceChooser() { return dateNaissanceChooser; }
    public JButton getSoumettreButton() { return soumettreButton; }
    public JButton getRetourButton() { return retourButton; }
}
