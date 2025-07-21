package gui_admin.controllerJpa;

import entite.Equipement;
import entite.Salle;
import gui_admin.view.equipements.EquipementEdit;
import serviceJpa.EquipementService;
import serviceJpa.SalleService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EquipementController extends GenericCrudController<Equipement, Integer> {

    private EquipementService equipementService;
    private SalleService salleService; // Pour la ComboBox

    public EquipementController(EntityManagerFactory emf) {
        super(new EquipementService(emf), Equipement.class);
        this.equipementService = (EquipementService) service;
        this.salleService = new SalleService(emf);
    }

    public EquipementEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Libellé", "Description", "Salle");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());
        List<Salle> allSalles = salleService.listerTous();

        EquipementEdit edit = new EquipementEdit(tableData, columnNames, allSalles);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Equipement> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof EquipementEdit) {
            EquipementEdit equipementEdit = (EquipementEdit) this.editPanel;
            equipementEdit.getSearchButton().addActionListener(e -> performSearchOperation());
            equipementEdit.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((EquipementEdit)editPanel).getSearchField().getText();
        try {
            List<Equipement> searchResults = equipementService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun équipement trouvé pour : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Equipement> equipements) {
        List<List<Object>> data = new ArrayList<>();
        if (equipements != null) {
            for (Equipement e : equipements) {
                String salleStr = (e.getSalle() != null) ? e.getSalle().toString() : "N/A";
                data.add(Arrays.asList(e.getId(), e.getLibelle(), e.getDescription(), salleStr));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(Equipement entity) {
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