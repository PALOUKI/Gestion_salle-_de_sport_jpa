package gui_admin.controllerJpa;

import entite.Abonnement;
import entite.Membre;
import entite.TypeAbonnement;
import gui_admin.view.abonnements.AbonnementEdit;
import serviceJpa.AbonnementService;
import serviceJpa.MembreService;
import serviceJpa.TypeAbonnementService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AbonnementController extends GenericCrudController<Abonnement, Integer> {

    private AbonnementService abonnementService;
    private MembreService membreService;
    private TypeAbonnementService typeAbonnementService;

    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public AbonnementController(EntityManagerFactory emf) {
        super(new AbonnementService(emf), Abonnement.class);
        this.abonnementService = (AbonnementService) service;
        this.membreService = new MembreService(emf);
        this.typeAbonnementService = new TypeAbonnementService(emf);
    }

    public AbonnementEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Membre", "Type", "Date Début", "Date Fin", "Statut");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        List<Membre> allMembres = membreService.listerTous();
        List<TypeAbonnement> allTypes = typeAbonnementService.listerTous();

        AbonnementEdit edit = new AbonnementEdit(tableData, columnNames);
        edit.setMembresInComboBox(allMembres);
        edit.setTypeAbonnementsInComboBox(allTypes);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Abonnement> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof AbonnementEdit) {
            AbonnementEdit abonnementEdit = (AbonnementEdit) this.editPanel;
            abonnementEdit.getSearchButton().addActionListener(e -> performSearchOperation());
            abonnementEdit.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((AbonnementEdit)editPanel).getSearchField().getText();
        try {
            List<Abonnement> searchResults = abonnementService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun abonnement trouvé pour le membre : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Abonnement> abonnements) {
        List<List<Object>> data = new ArrayList<>();
        if (abonnements != null) {
            for (Abonnement a : abonnements) {
                String membreStr = (a.getMembre() != null && a.getMembre().getClient() != null)
                        ? a.getMembre().getClient().toString() : "N/A";
                String typeStr = (a.getTypeAbonnement() != null) ? a.getTypeAbonnement().getLibelle() : "N/A";
                String debutStr = (a.getDateDebut() != null) ? a.getDateDebut().format(TABLE_DATE_FORMATTER) : "N/A";
                String finStr = (a.getDateFin() != null) ? a.getDateFin().format(TABLE_DATE_FORMATTER) : "N/A";

                String statut = "Actif";
                if (a.getDateFin() != null && LocalDateTime.now().isAfter(a.getDateFin())) {
                    statut = "Expiré";
                }

                data.add(Arrays.asList(a.getId(), membreStr, typeStr, debutStr, finStr, statut));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(Abonnement entity) {
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