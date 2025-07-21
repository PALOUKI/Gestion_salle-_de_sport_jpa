package gui_admin.controllerJpa;

import entite.Abonnement;
import entite.MoyenDePaiement;
import entite.Paiement;
import gui_admin.view.paiements.PaiementEdit;
import serviceJpa.AbonnementService;
import serviceJpa.MoyenDePaiementService;
import serviceJpa.PaiementService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaiementController extends GenericCrudController<Paiement, Integer> {

    private PaiementService paiementService;
    private AbonnementService abonnementService;
    private MoyenDePaiementService moyenDePaiementService;

    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public PaiementController(EntityManagerFactory emf) {
        super(new PaiementService(emf), Paiement.class);
        this.paiementService = (PaiementService) service;
        this.abonnementService = new AbonnementService(emf);
        this.moyenDePaiementService = new MoyenDePaiementService(emf);
    }

    public PaiementEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("ID", "Membre", "Abonnement", "Montant", "Date Paiement", "Moyen");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        List<Abonnement> allAbonnements = abonnementService.listerTous();
        List<MoyenDePaiement> allMoyens = moyenDePaiementService.listerTous();

        PaiementEdit edit = new PaiementEdit(tableData, columnNames, allAbonnements, allMoyens);

        setEditPanel(edit);
        prepareForAdd();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Paiement> editPanel) {
        super.setEditPanel(editPanel);
        if (this.editPanel instanceof PaiementEdit) {
            PaiementEdit paiementEdit = (PaiementEdit) this.editPanel;
            paiementEdit.getSearchButton().addActionListener(e -> performSearchOperation());
            paiementEdit.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((PaiementEdit)editPanel).getSearchField().getText();
        try {
            List<Paiement> searchResults = paiementService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun paiement trouvé pour le membre : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Paiement> paiements) {
        List<List<Object>> data = new ArrayList<>();
        if (paiements != null) {
            for (Paiement p : paiements) {
                String membreStr = (p.getAbonnement() != null && p.getAbonnement().getMembre() != null && p.getAbonnement().getMembre().getClient() != null)
                        ? p.getAbonnement().getMembre().getClient().toString() : "N/A";
                String abonnementStr = (p.getAbonnement() != null && p.getAbonnement().getTypeAbonnement() != null)
                        ? p.getAbonnement().getTypeAbonnement().getLibelle() : "N/A";
                String moyenStr = (p.getMoyenDePaiement() != null) ? p.getMoyenDePaiement().getLibelle() : "N/A";
                String dateStr = (p.getDateDePaiement() != null) ? p.getDateDePaiement().format(TABLE_DATE_FORMATTER) : "N/A";

                data.add(Arrays.asList(p.getId(), membreStr, abonnementStr, p.getMontant(), dateStr, moyenStr));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(Paiement entity) {
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