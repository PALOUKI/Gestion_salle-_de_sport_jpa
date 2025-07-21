package gui_admin.controller;

import entite.Client;
import entite.Seance;
import gui_admin.view.seances.HistoriqueSeancesPanel;
import serviceJpa.ClientService;
import serviceJpa.SeanceService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoriqueSeancesController {

    private final HistoriqueSeancesPanel view;
    private final ClientService clientService;
    private final SeanceService seanceService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public HistoriqueSeancesController(HistoriqueSeancesPanel view, EntityManagerFactory emf) {
        this.view = view;
        this.clientService = new ClientService(emf);
        this.seanceService = new SeanceService(emf);
        this.view.getSearchButton().addActionListener(e -> searchClient());
    }

    public HistoriqueSeancesPanel getView() {
        return view;
    }

    private void searchClient() {
        String searchTerm = view.getSearchField().getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Veuillez entrer un nom ou un email pour la recherche.", "Champ vide", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Client> clients = clientService.searchClients(searchTerm);

        if (clients.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Aucun client trouvé pour '" + searchTerm + "'.", "Non trouvé", JOptionPane.INFORMATION_MESSAGE);
            view.getClientInfoLabel().setText("Aucun client sélectionné.");
            view.getTableModel().setRowCount(0);
        } else if (clients.size() == 1) {
            displayClientHistory(clients.get(0));
        } else {
            // Si plusieurs clients sont trouvés, proposer une sélection
            Client selectedClient = (Client) JOptionPane.showInputDialog(
                view,
                "Plusieurs clients correspondent à votre recherche. Veuillez en sélectionner un :",
                "Sélectionner un client",
                JOptionPane.QUESTION_MESSAGE,
                null,
                clients.toArray(),
                clients.get(0)
            );
            if (selectedClient != null) {
                displayClientHistory(selectedClient);
            }
        }
    }

    private void displayClientHistory(Client client) {
        view.getClientInfoLabel().setText("Historique pour : " + client.getPrenom() + " " + client.getNom() + " (ID: " + client.getId() + ")");

        List<Seance> seances = seanceService.findSeancesByClientId(client.getId());

        view.getTableModel().setRowCount(0); // Vider le tableau

        if (seances.isEmpty()) {
            view.getTableModel().addRow(new Object[]{"Aucune séance trouvée", "", "", ""});
        } else {
            for (Seance seance : seances) {
                view.getTableModel().addRow(new Object[]{
                    seance.getDateDebut().format(dateFormatter),
                    seance.getDateDebut().format(timeFormatter),
                    seance.getDateFin().format(timeFormatter),
                    seance.getSalle() != null ? seance.getSalle().getLibelle() : "N/A"
                });
            }
        }
    }
}
