package gui_client.view;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton retourButton;

    public LoginPanel() {
        setLayout(new BorderLayout(0, 30));
        setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));
        setBackground(new Color(245, 245, 245));

        // --- Titre ---
        JLabel titleLabel = new JLabel("Connexion à votre espace", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(60, 63, 65));
        add(titleLabel, BorderLayout.NORTH);

        // --- Panneau du formulaire ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- Champs de texte stylisés ---
        Font fieldFont = new Font("SansSerif", Font.PLAIN, 16);
        Dimension fieldSize = new Dimension(200, 40);

        emailField = new JTextField();
        emailField.putClientProperty("JTextField.placeholderText", "Adresse e-mail");
        emailField.setFont(fieldFont);
        emailField.setPreferredSize(fieldSize);

        passwordField = new JPasswordField();
        passwordField.putClientProperty("JTextField.placeholderText", "Mot de passe");
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(fieldSize);

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(passwordField, gbc);

        // --- Wrapper pour centrer le formulaire ---
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(formPanel);
        add(formWrapper, BorderLayout.CENTER);

        // --- Panneau des boutons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Se Connecter");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(200, 50));
        loginButton.putClientProperty("JButton.buttonType", "roundRect");
        loginButton.putClientProperty("JButton.defaultButton", true);

        retourButton = new JButton("Retour");
        retourButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        retourButton.setPreferredSize(new Dimension(150, 50));
        retourButton.putClientProperty("JButton.buttonType", "roundRect");

        buttonPanel.add(retourButton);
        buttonPanel.add(loginButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Getters
    public JTextField getEmailField() { return emailField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getLoginButton() { return loginButton; }
    public JButton getRetourButton() { return retourButton; }
}
