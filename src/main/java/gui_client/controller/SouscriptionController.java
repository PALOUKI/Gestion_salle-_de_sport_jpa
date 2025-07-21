package gui_client.controller;

import entite.*;
import gui_client.ClientWindow;
import gui_client.view.SouscriptionPanel;
import serviceJpa.*;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.time.LocalDateTime;

public class SouscriptionController {

    private final SouscriptionPanel view;
    private final ClientWindow mainFrame;
    private final TypeAbonnementService typeAbonnementService;
    private final MoyenDePaiementService moyenDePaiementService;
    private final AbonnementService abonnementService;
    private final MembreService membreService;

    public SouscriptionController(SouscriptionPanel view, ClientWindow mainFrame, EntityManagerFactory emf) {
        this.view = view;
        this.mainFrame = mainFrame;
        this.typeAbonnementService = new TypeAbonnementService(emf);
        this.moyenDePaiementService = new MoyenDePaiementService(emf);
        this.abonnementService = new AbonnementService(emf);
        this.membreService = new MembreService(emf);
        attachListeners();
    }

    public void loadInitialData() {
        // Charger les types d'abonnement
        view.getTypeAbonnementComboBox().removeAllItems();
        typeAbonnementService.listerTous().forEach(view.getTypeAbonnementComboBox()::addItem);

        // Charger les moyens de paiement
        view.getMoyenPaiementComboBox().removeAllItems();
        moyenDePaiementService.listerTous().forEach(view.getMoyenPaiementComboBox()::addItem);
    }

    private void attachListeners() {
        view.getRetourButton().addActionListener(e -> mainFrame.navigateTo("dashboard"));
        view.getSouscrireButton().addActionListener(e -> souscrire());

        // Mettre à jour le prix quand le type d'abonnement change
        view.getTypeAbonnementComboBox().addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                TypeAbonnement selected = (TypeAbonnement) e.getItem();
                if (selected != null) {
                    view.getPrixLabel().setText(String.format("Prix : %d €", selected.getMontant()));
                }
            }
        });
    }

    private void souscrire() {
        Client client = mainFrame.getClientConnecte();
        if (client == null) {
            JOptionPane.showMessageDialog(view, "Erreur: Aucun client n'est connecté.", "Erreur", JOptionPane.ERROR_MESSAGE);
            mainFrame.navigateTo("login");
            return;
        }

        TypeAbonnement typeAbonnement = (TypeAbonnement) view.getTypeAbonnementComboBox().getSelectedItem();
        MoyenDePaiement moyenDePaiement = (MoyenDePaiement) view.getMoyenPaiementComboBox().getSelectedItem();

        if (typeAbonnement == null || moyenDePaiement == null) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un type d'abonnement et un moyen de paiement.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Vérifier si un membre existe pour ce client, sinon le créer
            Membre membre = membreService.findByClientId(client.getId());
            if (membre == null) {
                membre = new Membre();
                membre.setClient(client);
                membre.setDateInscription(LocalDateTime.now());
                membreService.ajouter(membre); // Persiste le nouveau membre
                membre = membreService.findByClientId(client.getId()); // Récupère le membre managé
            }

            final Membre membreFinal = membre;
            boolean hasActiveSubscription = abonnementService.listerTous().stream()
                .filter(a -> a.getMembre().getId().equals(membreFinal.getId()))
                .anyMatch(a -> a.getDateFin().isAfter(LocalDateTime.now()));

            if (hasActiveSubscription) {
                JOptionPane.showMessageDialog(view, "Vous avez déjà un abonnement actif. Vous ne pouvez pas en souscrire un nouveau.", "Souscription impossible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Créer l'abonnement
            Abonnement nouvelAbonnement = new Abonnement();
            nouvelAbonnement.setMembre(membre);
            nouvelAbonnement.setTypeAbonnement(typeAbonnement);
            nouvelAbonnement.setDateDebut(LocalDateTime.now());
            nouvelAbonnement.setDateFin(LocalDateTime.now().plusMonths(typeAbonnement.getDureeMois()));

            // 3. Créer le paiement
            Paiement paiement = new Paiement();
            paiement.setMontant(typeAbonnement.getMontant());
            paiement.setDateDePaiement(LocalDateTime.now());
            paiement.setMoyenDePaiement(moyenDePaiement);
            // La liaison au membre se fait via l'abonnement

            // 4. Exécuter la transaction
            abonnementService.creerAbonnementEtPaiement(nouvelAbonnement, paiement);

            JOptionPane.showMessageDialog(view, "Souscription réussie ! Votre abonnement est maintenant actif.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.navigateTo("dashboard");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Une erreur est survenue lors de la souscription: \n" + e.getMessage(), "Erreur de souscription", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
