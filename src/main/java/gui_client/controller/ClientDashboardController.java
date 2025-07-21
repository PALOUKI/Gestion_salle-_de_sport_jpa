package gui_client.controller;

import gui_client.ClientWindow;
import gui_client.view.ClientDashboardPanel;

public class ClientDashboardController {

    private final ClientDashboardPanel view;
    private final ClientWindow mainFrame;

    public ClientDashboardController(ClientDashboardPanel view, ClientWindow mainFrame) {
        this.view = view;
        this.mainFrame = mainFrame;
        attachListeners();
    }

    private void attachListeners() {
        view.getDeconnexionButton().addActionListener(e -> deconnecter());
        view.getVoirAbonnementsButton().addActionListener(e -> voirAbonnements());
        view.getSouscrireButton().addActionListener(e -> souscrireAbonnement());
        
        // TODO: Ajouter les listeners pour les autres boutons
    }

    private void deconnecter() {
        mainFrame.setClientConnecte(null); // Effacer les informations du client
        mainFrame.navigateTo("accueil"); // Retourner à l'écran d'accueil
    }

    private void voirAbonnements() {
        mainFrame.getAbonnementsClientController().loadAbonnements();
        mainFrame.navigateTo("abonnements_client");
    }

    private void souscrireAbonnement() {
        mainFrame.getSouscriptionController().loadInitialData();
        mainFrame.navigateTo("souscription");
    }
}
