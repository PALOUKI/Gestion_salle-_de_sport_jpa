package gui_admin.controllerJpa;

import entite.TypeAbonnement;
import gui_admin.view.type_abonnements.TypeAbonnementEdit;
import serviceJpa.TypeAbonnementService;

import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contrôleur pour la gestion des types d'abonnements.
 * Il gère les opérations CRUD pour les types d'abonnements.
 */
public class TypeAbonnementController extends GenericCrudController<TypeAbonnement, String> {

    // On utilise TypeAbonnementService directement ici pour accéder à sa méthode de recherche spécifique
    private TypeAbonnementService typeAbonnementService;

    public TypeAbonnementController(EntityManagerFactory emf) {
        // Assurez-vous que le service générique est aussi le TypeAbonnementService
        super(new TypeAbonnementService(emf), TypeAbonnement.class);
        this.typeAbonnementService = (TypeAbonnementService) service; // Cast pour l'accès aux méthodes spécifiques
    }

    /**
     * Crée et configure un panneau d'édition pour l'ajout d'un nouveau TypeAbonnement.
     *
     * @return Le panneau d'édition configuré.
     */
    public TypeAbonnementEdit createAndConfigureEditPanelForAdd() {
        List<String> columnNames = Arrays.asList("Code", "Libellé", "Montant");
        // Lors de la création initiale du panneau, il doit afficher toutes les données existantes.
        // Initialement, la table est remplie par un "tous lister" (ou une recherche vide)
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        TypeAbonnementEdit edit = new TypeAbonnementEdit(tableData, columnNames);

        // C'est ici que le contrôleur générique attachera les listeners, y compris pour le nouveau bouton de recherche
        setEditPanel(edit);

        // On initialise l'entité du panneau et on vide le formulaire
        // pour qu'il soit prêt pour une nouvelle saisie.
        edit.setEntity(new TypeAbonnement()); // Set a new empty entity
        edit.clearForm(); // Clear form fields and re-enable code field (handled by Edit class)

        return edit;
    }

    /**
     * Surcharge de setEditPanel pour attacher les listeners spécifiques à TypeAbonnement,
     * y compris pour le bouton de recherche.
     * @param editPanel Le panneau d'édition.
     */
    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<TypeAbonnement> editPanel) {
        super.setEditPanel(editPanel); // Appelle la méthode parente pour attacher les listeners de base

        // Attacher le listener spécifique au bouton de recherche si le panel est une instance de Edit
        if (this.editPanel instanceof TypeAbonnementEdit) {
            TypeAbonnementEdit typeAbonnementEdit = (TypeAbonnementEdit) this.editPanel;
            typeAbonnementEdit.getSearchButton().addActionListener(e -> {
                System.out.println("--- DIAGNOSTIC: Bouton 'Rechercher' cliqué ---");
                performSearchOperation();
            });
            // On peut aussi ajouter un listener pour la touche Entrée dans le champ de recherche
            typeAbonnementEdit.getSearchField().addActionListener(e -> {
                System.out.println("--- DIAGNOSTIC: Touche Entrée dans le champ de recherche ---");
                performSearchOperation();
            });
        }
    }


    /**
     * Gère la logique de recherche (filtrage) des types d'abonnements.
     * Récupère le terme de recherche et met à jour le tableau.
     */
    protected void performSearchOperation() {
        // S'assure que le editPanel est bien du type Edit pour accéder au searchField
        String searchTerm = ((TypeAbonnementEdit)editPanel).getSearchField().getText();
        System.out.println("Recherche pour le terme : '" + searchTerm + "'");

        try {
            List<TypeAbonnement> searchResults = typeAbonnementService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun type d'abonnement trouvé pour le terme : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<TypeAbonnement> abonnements) {
        List<List<Object>> data = new ArrayList<>();
        if (abonnements != null) {
            for (TypeAbonnement ta : abonnements) {
                data.add(Arrays.asList(ta.getCode(), ta.getLibelle(), ta.getMontant()));
            }
        }
        return data;
    }

    @Override
    protected String getEntityKey(TypeAbonnement entity) {
        return entity.getCode();
    }

    @Override
    protected String convertRawKeyToKeyType(Object rawKey) throws ClassCastException {
        if (rawKey instanceof String) {
            return (String) rawKey;
        }
        throw new ClassCastException("Expected key of type String, but received " + rawKey.getClass().getName());
    }
}