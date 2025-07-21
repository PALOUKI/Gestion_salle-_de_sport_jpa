package gui_admin.controllerJpa;

import entite.Salle;
import gui_admin.view.salles.SalleEdit;
import serviceJpa.SalleService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SalleController extends GenericCrudController<Salle, Integer> {

    private SalleService salleService;

    public SalleController() {
        super(new SalleService(), Salle.class);
        this.salleService = (SalleService) service;
    }

    public SalleController(EntityManagerFactory emf) {
        super(new SalleService(emf), Salle.class);
        this.salleService = (SalleService) service;
    }

    public SalleEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Libellé", "Description");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        SalleEdit edit = new SalleEdit(tableData, columnNames);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Salle> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof SalleEdit) {
            SalleEdit salleEditPanel = (SalleEdit) this.editPanel;
            salleEditPanel.getSearchButton().addActionListener(e -> performSearchOperation());
            salleEditPanel.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((SalleEdit)editPanel).getSearchField().getText();
        try {
            List<Salle> searchResults = salleService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucune salle trouvée pour : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Salle> salles) {
        List<List<Object>> data = new ArrayList<>();
        if (salles != null) {
            for (Salle s : salles) {
                data.add(Arrays.asList(s.getId(), s.getLibelle(), s.getDescription()));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(Salle entity) {
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