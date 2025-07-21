package gui_client.controller;

import entite.Client;
import entite.DemandeInscription;
import gui_client.ClientWindow;
import gui_client.view.DemandeInscriptionPanel;
import org.mindrot.jbcrypt.BCrypt;
import serviceJpa.ClientService;
import serviceJpa.DemandeInscriptionService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DemandeInscriptionClientController {

    private final DemandeInscriptionService demandeInscriptionService;
    private final ClientService clientService;
    private final DemandeInscriptionPanel view;
    private final ClientWindow mainFrame;

    public DemandeInscriptionClientController(DemandeInscriptionPanel view, ClientWindow mainFrame, EntityManagerFactory emf) {
        this.demandeInscriptionService = new DemandeInscriptionService(emf);
        this.clientService = new ClientService(emf);
        this.view = view;
        this.mainFrame = mainFrame;
        attachListeners();
    }

    private void attachListeners() {
        view.getSoumettreButton().addActionListener(e -> soumettreDemande());
        view.getRetourButton().addActionListener(e -> mainFrame.navigateTo("accueil")); // ou "login"
    }

    private void soumettreDemande() {
        // Validation simple
        if (view.getNomField().getText().trim().isEmpty() ||
            view.getPrenomField().getText().trim().isEmpty() ||
            view.getEmailField().getText().trim().isEmpty() ||
            view.getPasswordField().getPassword().length == 0 ||
            view.getDateNaissanceChooser().getDate() == null) {
            JOptionPane.showMessageDialog(view, "Veuillez remplir tous les champs obligatoires.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String email = view.getEmailField().getText().trim();
        if (clientService.emailExiste(email)) {
            JOptionPane.showMessageDialog(view, "Un compte existe déjà avec cette adresse e-mail.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 1. Créer et persister le Client
            Client newClient = new Client();
            newClient.setNom(view.getNomField().getText().trim());
            newClient.setPrenom(view.getPrenomField().getText().trim());
            newClient.setEmail(email);
            String hashedPassword = BCrypt.hashpw(new String(view.getPasswordField().getPassword()), BCrypt.gensalt());
            newClient.setMotDePasse(hashedPassword);
            Date dateNaissanceUtil = view.getDateNaissanceChooser().getDate();
            java.time.LocalDate dateNaissance = dateNaissanceUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            newClient.setDateNaissance(dateNaissance.atStartOfDay()); // Conversion en LocalDateTime
            clientService.ajouter(newClient);

            // 2. Créer la DemandeInscription et la lier au client
            DemandeInscription demande = new DemandeInscription();
            demande.setNom(newClient.getNom());
            demande.setPrenom(newClient.getPrenom());
            demande.setEmail(newClient.getEmail());
            demande.setMotDePasse(newClient.getMotDePasse());
            demande.setDateNaissance(dateNaissance); // Utiliser le LocalDate
            demande.setClient(newClient); // Lier la demande au client

            demandeInscriptionService.ajouter(demande);

            JOptionPane.showMessageDialog(view, 
                "Votre demande d'inscription a été soumise avec succès !\nVous serez notifié une fois qu'elle aura été examinée par un administrateur.", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Nettoyer le formulaire et retourner à l'accueil
            clearForm();
            mainFrame.navigateTo("accueil");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view, "Une erreur est survenue lors de la soumission de votre demande: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        view.getNomField().setText("");
        view.getPrenomField().setText("");
        view.getEmailField().setText("");
        view.getPasswordField().setText("");
        view.getDateNaissanceChooser().setDate(null);
    }
}
