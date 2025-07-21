package gui_admin.controllerJpa;

import entite.MoyenDePaiement;
import gui_admin.view.moyen_de_paiements.MoyenDePaiementEdit;
import serviceJpa.MoyenDePaiementService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MoyenDePaiementController extends GenericCrudController<MoyenDePaiement, String> {

    // Service spécifique pour accéder à la méthode de recherche
    private MoyenDePaiementService moyenDePaiementService;

    public MoyenDePaiementController() {
        // Initialise le contrôleur avec le service et la classe d'entité
        super(new MoyenDePaiementService(), MoyenDePaiement.class);
        this.moyenDePaiementService = (MoyenDePaiementService) service; // Cast pour l'accès aux méthodes spécifiques
    }

    public MoyenDePaiementController(EntityManagerFactory emf) {
        // Initialise le contrôleur avec le service et la classe d'entité
        super(new MoyenDePaiementService(emf), MoyenDePaiement.class);
        this.moyenDePaiementService = (MoyenDePaiementService) service; // Cast pour l'accès aux méthodes spécifiques
    }

    /**
     * Crée et configure le panneau d'édition pour la gestion des Moyens de Paiement.
     *
     * @return Le panneau MoyenDePaiementEdit configuré.
     */
    public MoyenDePaiementEdit createAndConfigureEditPanelForAdd() {
        // Définir les noms des colonnes pour le tableau
        List<String> columnNames = Arrays.asList("Code", "Libellé");

        // Remplir le tableau initialement avec tous les moyens de paiement
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        MoyenDePaiementEdit edit = new MoyenDePaiementEdit(tableData, columnNames);

        // Attacher tous les listeners (CRUD + Recherche)
        setEditPanel(edit);

        // Préparer le formulaire pour un nouvel ajout
        edit.setEntity(new MoyenDePaiement());
        edit.clearForm();

        return edit;
    }

    /**
     * Surcharge pour attacher les listeners spécifiques au panneau MoyenDePaiementEdit.
     * @param editPanel Le panneau d'édition.
     */
    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<MoyenDePaiement> editPanel) {
        super.setEditPanel(editPanel); // Attache les listeners CRUD génériques

        // Attacher les listeners pour la recherche
        if (this.editPanel instanceof MoyenDePaiementEdit) {
            MoyenDePaiementEdit mdpEditPanel = (MoyenDePaiementEdit) this.editPanel;

            // Listener pour le bouton "Rechercher"
            mdpEditPanel.getSearchButton().addActionListener(e -> performSearchOperation());

            // Listener pour la touche "Entrée" dans le champ de recherche
            mdpEditPanel.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    /**
     * Exécute l'opération de recherche et met à jour le tableau.
     */
    @Override
    protected void performSearchOperation() {
        String searchTerm = ((MoyenDePaiementEdit)editPanel).getSearchField().getText();
        System.out.println("Recherche de moyens de paiement pour le terme : '" + searchTerm + "'");

        try {
            List<MoyenDePaiement> searchResults = moyenDePaiementService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun moyen de paiement trouvé pour le terme : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Convertit une liste d'entités MoyenDePaiement en un format compatible avec JTable.
     * @param moyensDePaiement La liste des entités à convertir.
     * @return Une liste de listes d'objets pour le tableau.
     */
    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<MoyenDePaiement> moyensDePaiement) {
        List<List<Object>> data = new ArrayList<>();
        if (moyensDePaiement != null) {
            for (MoyenDePaiement mdp : moyensDePaiement) {
                data.add(Arrays.asList(mdp.getCode(), mdp.getLibelle()));
            }
        }
        return data;
    }

    /**
     * Retourne la clé primaire de l'entité.
     * @param entity L'entité MoyenDePaiement.
     * @return Le code du moyen de paiement (String).
     */
    @Override
    protected String getEntityKey(MoyenDePaiement entity) {
        return entity.getCode();
    }

    /**
     * Convertit la clé brute (de type Object) en type de clé attendu (String).
     * @param rawKey La clé brute sélectionnée dans le tableau.
     * @return La clé convertie en String.
     */
    @Override
    protected String convertRawKeyToKeyType(Object rawKey) throws ClassCastException {
        if (rawKey instanceof String) {
            return (String) rawKey;
        }
        throw new ClassCastException("La clé attendue est de type String, mais a reçu " + rawKey.getClass().getName());
    }
}