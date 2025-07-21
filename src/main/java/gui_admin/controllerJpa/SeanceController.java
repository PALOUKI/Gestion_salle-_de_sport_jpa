package gui_admin.controllerJpa;

import entite.Salle;
import entite.Seance;
import gui_admin.view.seances.SeanceEdit;
import serviceJpa.SalleService;
import serviceJpa.SeanceService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeanceController extends GenericCrudController<Seance, Integer> {

    private SeanceService seanceService;
    private SalleService salleService; // Pour la ComboBox

    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SeanceController(EntityManagerFactory emf) {
        super(new SeanceService(emf), Seance.class);
        this.seanceService = (SeanceService) service;
        this.salleService = new SalleService(emf);
    }

    public SeanceEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Salle", "Date de Début", "Date de Fin");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());
        List<Salle> allSalles = salleService.listerTous();

        SeanceEdit edit = new SeanceEdit(tableData, columnNames);
        edit.setSallesInComboBox(allSalles);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Seance> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof SeanceEdit) {
            SeanceEdit seanceEdit = (SeanceEdit) this.editPanel;
            seanceEdit.getSearchButton().addActionListener(e -> performSearchOperation());
            seanceEdit.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((SeanceEdit)editPanel).getSearchField().getText();
        try {
            List<Seance> searchResults = seanceService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucune séance trouvée pour la salle : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Seance> seances) {
        List<List<Object>> data = new ArrayList<>();
        if (seances != null) {
            for (Seance s : seances) {
                String salleStr = (s.getSalle() != null) ? s.getSalle().toString() : "N/A";
                String debutStr = (s.getDateDebut() != null) ? s.getDateDebut().format(TABLE_DATE_FORMATTER) : "N/A";
                String finStr = (s.getDateFin() != null) ? s.getDateFin().format(TABLE_DATE_FORMATTER) : "N/A";
                data.add(Arrays.asList(s.getId(), salleStr, debutStr, finStr));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(Seance entity) {
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