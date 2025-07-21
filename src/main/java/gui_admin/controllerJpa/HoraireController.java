package gui_admin.controllerJpa;

import entite.Horaire;
import entite.Salle;
import gui_admin.view.horaires.HoraireEdit;
import serviceJpa.HoraireService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HoraireController extends GenericCrudController<Horaire, Integer> {

    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HoraireController(EntityManagerFactory emf) {
        super(new HoraireService(emf), Horaire.class);
    }

    public HoraireEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Début", "Fin", "Salles");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        HoraireEdit edit = new HoraireEdit(tableData, columnNames);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Horaire> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof HoraireEdit) {
            HoraireEdit horaireEdit = (HoraireEdit) this.editPanel;
            horaireEdit.getSearchButton().addActionListener(e -> performSearchOperation());
            horaireEdit.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    public void performSearchOperation() {
        String searchTerm = ((HoraireEdit)editPanel).getSearchField().getText();
        try {
            List<Horaire> searchResults = ((HoraireService)service).rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun horaire trouvé pour le terme : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    public List<List<Object>> convertEntitiesToTableData(List<Horaire> horaires) {
        List<List<Object>> data = new ArrayList<>();
        if (horaires != null) {
            for (Horaire h : horaires) {
                String debutStr = h.getDebut() != null ? h.getDebut().format(TABLE_DATE_FORMATTER) : "N/A";
                String finStr = h.getFin() != null ? h.getFin().format(TABLE_DATE_FORMATTER) : "N/A";

                String sallesStr = h.getSalles().stream()
                        .map(Salle::getLibelle)
                        .collect(Collectors.joining(", "));
                if (sallesStr.isEmpty()) {
                    sallesStr = "Aucune";
                }

                data.add(Arrays.asList(h.getId(), debutStr, finStr, sallesStr));
            }
        }
        return data;
    }

    @Override
    protected Integer convertRawKeyToKeyType(Object rawKey) throws ClassCastException {
        if (rawKey instanceof Integer) {
            return (Integer) rawKey;
        }
        throw new ClassCastException("La clé attendue est de type Integer, mais a reçu " + rawKey.getClass().getName());
    }

    @Override
    protected Integer getEntityKey(Horaire entity) {
        return entity.getId();
    }

    @Override
    public void prepareForAdd() {
        super.prepareForAdd();
        ((HoraireEdit)editPanel).clearForm();
    }
}