package gui_admin.controllerJpa;

import entite.Client;
import gui_admin.view.clients.ClientEdit;
import serviceJpa.ClientService;

import javax.persistence.EntityManagerFactory;
import javax.swing.JOptionPane;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientController extends GenericCrudController<Client, Integer> {

    // Service spécifique pour accéder à la méthode de recherche
    private ClientService clientService;

    // Formatter pour afficher la date dans le tableau de manière lisible
    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ClientController(EntityManagerFactory emf) {
        // Initialise le contrôleur avec le service et la classe d'entité appropriés
        super(new ClientService(emf), Client.class);
        this.clientService = (ClientService) service; // Cast pour l'accès aux méthodes spécifiques
    }

    /**
     * Crée et configure le panneau d'édition pour la gestion des Clients.
     *
     * @return Le panneau ClientEdit configuré.
     */
    public ClientEdit createAndConfigureEditPanelForAdd() {
        // Définir les noms des colonnes pour le tableau
        List<String> columnNames = Arrays.asList("ID", "Nom", "Prénom", "Date Naissance", "Email");

        // Remplir le tableau initialement avec tous les clients
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        ClientEdit edit = new ClientEdit(tableData, columnNames);

        // Attacher tous les listeners (CRUD + Recherche) via la méthode setEditPanel
        setEditPanel(edit);

        // Préparer le formulaire pour un nouvel ajout
        edit.setEntity(new Client());
        edit.clearForm();

        return edit;
    }

    /**
     * Surcharge pour attacher les listeners spécifiques au panneau ClientEdit.
     * @param editPanel Le panneau d'édition.
     */
    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<Client> editPanel) {
        super.setEditPanel(editPanel); // Attache les listeners CRUD génériques

        // Attacher les listeners pour la recherche
        if (this.editPanel instanceof ClientEdit) {
            ClientEdit clientEditPanel = (ClientEdit) this.editPanel;

            // Listener pour le bouton "Rechercher"
            clientEditPanel.getSearchButton().addActionListener(e -> performSearchOperation());

            // Listener pour la touche "Entrée" dans le champ de recherche
            clientEditPanel.getSearchField().addActionListener(e -> performSearchOperation());
        }
    }

    /**
     * Exécute l'opération de recherche en utilisant le terme du champ de recherche
     * et met à jour le tableau avec les résultats.
     */
    @Override
    protected void performSearchOperation() {
        String searchTerm = ((ClientEdit)editPanel).getSearchField().getText();
        System.out.println("Recherche de clients pour le terme : '" + searchTerm + "'");

        try {
            List<Client> searchResults = clientService.rechercher(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucun client trouvé pour le terme : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Convertit une liste d'entités Client en un format compatible avec JTable.
     * @param clients La liste des clients à convertir.
     * @return Une liste de listes d'objets pour le tableau.
     */
    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<Client> clients) {
        List<List<Object>> data = new ArrayList<>();
        if (clients != null) {
            for (Client c : clients) {
                // Formate la date pour un affichage propre, en gérant le cas où elle serait nulle
                String formattedDate = (c.getDateNaissance() != null)
                        ? c.getDateNaissance().format(TABLE_DATE_FORMATTER)
                        : "N/A";

                data.add(Arrays.asList(c.getId(), c.getNom(), c.getPrenom(), formattedDate, c.getEmail()));
            }
        }
        return data;
    }

    /**
     * Retourne la clé primaire de l'entité Client.
     * @param entity L'entité Client.
     * @return L'ID du client (Integer).
     */
    @Override
    protected Integer getEntityKey(Client entity) {
        return entity.getId();
    }

    /**
     * Convertit la clé brute (venant du tableau, de type Object) en type de clé attendu (Integer).
     * @param rawKey La clé brute sélectionnée dans le tableau.
     * @return La clé convertie en Integer.
     * @throws ClassCastException si la conversion échoue.
     */
    @Override
    protected Integer convertRawKeyToKeyType(Object rawKey) throws ClassCastException {
        if (rawKey instanceof Integer) {
            return (Integer) rawKey;
        }
        throw new ClassCastException("La clé attendue est de type Integer, mais a reçu " + rawKey.getClass().getName());
    }
}