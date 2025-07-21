package gui_admin.controllerJpa;

import entite.Client;
import entite.Ticket;
import gui_admin.view.tickets.TicketEdit;
import serviceJpa.ClientService;
import serviceJpa.TicketService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TicketController extends GenericCrudController<Ticket, Integer> {

    private TicketService ticketService;
    private ClientService clientService; // Pour la ComboBox

    public TicketController(EntityManagerFactory emf) {
        super(new TicketService(emf), Ticket.class);
        this.ticketService = (TicketService) service;
        this.clientService = new ClientService(emf);
    }

    public TicketEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Client", "Nb Séances", "Montant");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());
        List<Client> allClients = clientService.listerTous();

        TicketEdit edit = new TicketEdit(tableData, columnNames, allClients);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Ticket> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof TicketEdit) {
            TicketEdit ticketEditPanel = (TicketEdit) this.editPanel;
            ticketEditPanel.getSearchButton().addActionListener(e -> performSearchOperation());
            ticketEditPanel.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((TicketEdit)editPanel).getSearchField().getText();
        try {
            List<Ticket> searchResults = ticketService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun ticket trouvé pour le client : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Ticket> tickets) {
        List<List<Object>> data = new ArrayList<>();
        if (tickets != null) {
            for (Ticket t : tickets) {
                String clientStr = (t.getClient() != null) ? t.getClient().toString() : "N/A";
                data.add(Arrays.asList(t.getId(), clientStr, t.getNombreDeSeance(), t.getMontant()));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(Ticket entity) {
        return entity.getId();
    }

    @Override
    protected Integer convertRawKeyToKeyType(Object rawKey) throws ClassCastException {
        if (rawKey instanceof Integer) {
            return (Integer) rawKey;
        }
        throw new ClassCastException("La clé attendue est de type Integer, mais a reçu " + rawKey.getClass().getName());
    }
}