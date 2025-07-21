package gui_client.controller;

import entite.Client;
import gui_client.ClientWindow;
import gui_client.view.LoginPanel;
import org.mindrot.jbcrypt.BCrypt;
import serviceJpa.ClientService;
import serviceJpa.MembreService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;

public class LoginController {

    private final LoginPanel view;
    private final ClientWindow mainFrame;
    private final ClientService clientService;
    private final MembreService membreService;

    public LoginController(LoginPanel view, ClientWindow mainFrame, EntityManagerFactory emf) {
        this.view = view;
        this.mainFrame = mainFrame;
        this.clientService = new ClientService(emf);
        this.membreService = new MembreService(emf);
        attachListeners();
    }

    private void attachListeners() {
        view.getLoginButton().addActionListener(e -> login());
        view.getRetourButton().addActionListener(e -> mainFrame.navigateTo("accueil"));
    }

    private void login() {
        String email = view.getEmailField().getText().trim();
        String password = new String(view.getPasswordField().getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Client client = clientService.findByEmail(email);

        if (client != null && BCrypt.checkpw(password, client.getMotDePasse())) {
            if (membreService.findByClientId(client.getId()) != null) {
                mainFrame.setClientConnecte(client);
                mainFrame.navigateTo("dashboard");
            } else {
                JOptionPane.showMessageDialog(view, "Votre demande d'inscription n'a pas encore été approuvée.", "Connexion non autorisée", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Email ou mot de passe incorrect.", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
        }
    }
}
