package gui_client.controller;

import entite.Abonnement;
import entite.Client;
import gui_client.ClientWindow;
import gui_client.view.AbonnementsClientPanel;
import serviceJpa.AbonnementService;

import javax.persistence.EntityManagerFactory;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AbonnementsClientController {

    private final AbonnementsClientPanel view;
    private final ClientWindow mainFrame;
    private final AbonnementService abonnementService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public AbonnementsClientController(AbonnementsClientPanel view, ClientWindow mainFrame, EntityManagerFactory emf) {
        this.view = view;
        this.mainFrame = mainFrame;
        this.abonnementService = new AbonnementService(emf);
        attachListeners();
    }

    private void attachListeners() {
        view.getRetourButton().addActionListener(e -> mainFrame.navigateTo("dashboard"));
    }

    public void loadAbonnements() {
        Client client = mainFrame.getClientConnecte();
        if (client == null) {
            // Gérer le cas où aucun client n'est connecté (sécurité)
            mainFrame.navigateTo("login");
            return;
        }

        List<Abonnement> abonnements = abonnementService.findByClientId(client.getId());
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0); // Vider le tableau avant de le remplir

        for (Abonnement ab : abonnements) {
            String statut = ab.getDateFin().toLocalDate().isBefore(LocalDate.now()) ? "Expiré" : "Actif";
            Object[] row = {
                ab.getTypeAbonnement().getLibelle(),
                ab.getDateDebut().format(formatter),
                ab.getDateFin().format(formatter),
                statut
            };
            model.addRow(row);
        }
    }
}
