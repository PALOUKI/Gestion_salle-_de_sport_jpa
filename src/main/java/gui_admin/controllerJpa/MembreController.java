package gui_admin.controllerJpa;

import entite.Client;
import entite.Membre;
import gui_admin.view.membres.MembreEdit;
import serviceJpa.ClientService;
import serviceJpa.MembreService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MembreController extends GenericCrudController<Membre, Integer> {

    private MembreService membreService;
    private ClientService clientService;

    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MembreController(EntityManagerFactory emf) {
        super(new MembreService(emf), Membre.class);
        this.membreService = (MembreService) service;
        this.clientService = new ClientService(emf);
    }

    public JPanel getView() {
        if (editPanel == null) {
            List<String> columnNames = Arrays.asList("ID Membre", "Nom Client", "Prénom Client", "Date d'Inscription");
            editPanel = new MembreEdit(new ArrayList<>(), columnNames);
            ((MembreEdit) editPanel).setClientsInComboBox(clientService.listerTous());
            setEditPanel(editPanel);
            refreshTableData();
        }
        return (JPanel) editPanel;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Membre> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof MembreEdit) {
            MembreEdit membreEditPanel = (MembreEdit) this.editPanel;
            membreEditPanel.getSearchButton().addActionListener(e -> performSearchOperation());
            membreEditPanel.getSearchField().addActionListener(e -> performSearchOperation());

            // Connexion de la logique standard
            membreEditPanel.getSaveButton().addActionListener(e -> performSaveOperation());
            membreEditPanel.getModifyButton().addActionListener(e -> performSelectForModification());
            membreEditPanel.getDeleteButton().addActionListener(e -> performDeleteOperation());

            // Sélection dans le tableau
            membreEditPanel.getCustomTablePanel().getTable().getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    performSelectForModification();
                }
            });
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((MembreEdit) editPanel).getSearchField().getText();
        List<Membre> searchResult = membreService.rechercherParNomOuPrenomClient(searchTerm);
        editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResult));
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Membre> membres) {
        List<List<Object>> data = new ArrayList<>();
        if (membres != null) {
            for (Membre membre : membres) {
                data.add(Arrays.asList(
                        membre.getId(),
                        membre.getClient() != null ? membre.getClient().getNom() : "N/A",
                        membre.getClient() != null ? membre.getClient().getPrenom() : "N/A",
                        membre.getDateInscription() != null ? membre.getDateInscription().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : ""
                ));
            }
        }
        return data;
    }

    @Override
    public void prepareForAdd() {
        super.prepareForAdd(); // Appelle clearForm() et met l'entité à null

        // On s'assure que la ComboBox contient tous les clients disponibles.
        // La logique pour empêcher la sélection d'un client déjà membre sera gérée
        // au moment de la sauvegarde si nécessaire, ou via la validation.
        if (editPanel instanceof MembreEdit) {
            List<Client> allClients = clientService.listerTous();
            ((MembreEdit) editPanel).setClientsInComboBox(allClients);
        }
    }

    @Override
    protected Integer getEntityKey(Membre entity) {
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