package gui_admin.controllerJpa;

import entite.Client;
import entite.DemandeInscription;
import entite.Membre;
import gui_admin.view.demande_inscriptions.DemandeInscriptionEdit;
import serviceJpa.ClientService;
import serviceJpa.DemandeInscriptionService;
import serviceJpa.MembreService;

import javax.persistence.EntityManagerFactory;
import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DemandeInscriptionController extends GenericCrudController<DemandeInscription, Integer> {

    private final DemandeInscriptionService demandeInscriptionService;
    private final ClientService clientService; // Pour la création de client lors de la validation
    private final MembreService membreService;

    private static final DateTimeFormatter TABLE_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public DemandeInscriptionController(EntityManagerFactory emf) {
        super(new DemandeInscriptionService(emf), DemandeInscription.class);
        this.demandeInscriptionService = (DemandeInscriptionService) service;
        this.clientService = new ClientService(emf);
        this.membreService = new MembreService(emf);
    }

    public DemandeInscriptionEdit createView() {
        List<String> columnNames = Arrays.asList("ID", "Nom", "Prénom", "Email", "Date Demande", "Statut");
        List<List<Object>> tableData = convertEntitiesToTableData(service.listerTous());

        DemandeInscriptionEdit edit = new DemandeInscriptionEdit(tableData, columnNames);

        setEditPanel(edit);
        edit.clearForm();

        return edit;
    }

    @Override
    public void setEditPanel(gui_admin.gui_util.GenericEdit<DemandeInscription> editPanel) {
        super.setEditPanel(editPanel); // Attache les listeners de base (sélection, annuler)
        if (this.editPanel instanceof DemandeInscriptionEdit) {
            DemandeInscriptionEdit diEditPanel = (DemandeInscriptionEdit) this.editPanel;

            // Connexion des boutons spécifiques à la vue
            diEditPanel.getSearchButton().addActionListener(e -> performSearchOperation());
            diEditPanel.getSearchField().addActionListener(e -> performSearchOperation());

            // Les boutons principaux sont gérés par GenericCrudController, qui appelle les méthodes surchargées ci-dessous
        }
    }

    @Override
    protected void performSearchOperation() {
        String searchTerm = ((DemandeInscriptionEdit)editPanel).getSearchField().getText();
        try {
            List<DemandeInscription> searchResults = demandeInscriptionService.rechercherParEmail(searchTerm);
            editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(searchResults));
            if (searchResults.isEmpty() && !searchTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucune demande trouvée pour l'email : '" + searchTerm + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de la recherche : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    protected List<List<Object>> convertEntitiesToTableData(List<DemandeInscription> demandes) {
        List<List<Object>> data = new ArrayList<>();
        if (demandes != null) {
            for (DemandeInscription di : demandes) {
                String dateDemandeStr = (di.getDateDeDemande() != null) ? di.getDateDeDemande().format(TABLE_DATE_FORMATTER) : "";
                data.add(Arrays.asList(
                        di.getId(),
                        di.getNom(),
                        di.getPrenom(),
                        di.getEmail(),
                        dateDemandeStr,
                        di.getStatut()
                ));
            }
        }
        return data;
    }

    @Override
    protected Integer getEntityKey(DemandeInscription entity) {
        return entity.getId();
    }

    @Override
    protected Integer convertRawKeyToKeyType(Object rawKey) throws ClassCastException {
        if (rawKey instanceof Integer) {
            return (Integer) rawKey;
        }
        throw new ClassCastException("La clé attendue est de type Integer, mais a reçu " + rawKey.getClass().getName());
    }

    protected void performSaveOperation() {
        Integer selectedKey = getSelectedEntityKey();
        if (selectedKey == null) {
            JOptionPane.showMessageDialog(editPanel, "Veuillez sélectionner une demande à traiter.", "Aucune sélection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DemandeInscription demande = demandeInscriptionService.trouver(selectedKey);
        if (demande == null) {
            JOptionPane.showMessageDialog(editPanel, "La demande sélectionnée n'a pas pu être trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        performSaveOperation(demande);
    }

    protected void performSaveOperation(DemandeInscription demande) {
        DemandeInscriptionEdit diEditPanel = (DemandeInscriptionEdit) this.editPanel;
        String nouveauStatut = (String) diEditPanel.getStatutComboBox().getSelectedItem();
        String ancienStatut = demande.getStatut();

        // Si le statut n'a pas changé, on effectue une simple mise à jour des champs modifiables
        if (nouveauStatut.equals(ancienStatut)) {
            try {
                demande.setDateDeTraitement(diEditPanel.getDateTraitement());
                service.modifier(demande);
                JOptionPane.showMessageDialog(editPanel, "Mise à jour effectuée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(editPanel, "Erreur lors de la mise à jour : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        // Si la demande a déjà été traitée (VALIDEE ou REJETEE), on ne peut pas la re-traiter
        if (!"EN_ATTENTE".equals(ancienStatut)) {
            JOptionPane.showMessageDialog(diEditPanel, "Cette demande a déjà été traitée et son statut ne peut plus être modifié.", "Opération non autorisée", JOptionPane.WARNING_MESSAGE);
            diEditPanel.getStatutComboBox().setSelectedItem(ancienStatut); // Rétablir l'ancien statut dans la vue
            return;
        }

        // Logique pour la validation
        if ("VALIDEE".equals(nouveauStatut)) {
            try {
                Client clientAssocie = demande.getClient();
                if (clientAssocie == null) {
                    JOptionPane.showMessageDialog(diEditPanel, "Erreur : Aucun client n'est associé à cette demande.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (membreService.membreExistePourClient(clientAssocie.getId())) {
                    JOptionPane.showMessageDialog(diEditPanel, "Un membre existe déjà pour ce client.", "Doublon", JOptionPane.WARNING_MESSAGE);
                } else {
                    Membre nouveauMembre = new Membre();
                    nouveauMembre.setClient(clientAssocie);
                    nouveauMembre.setDateInscription(LocalDateTime.now());
                    membreService.ajouter(nouveauMembre);
                    JOptionPane.showMessageDialog(diEditPanel, "Membre créé avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }

                demande.setStatut("VALIDEE");
                demande.setDateDeTraitement(LocalDateTime.now());
                service.modifier(demande);
                diEditPanel.initFormWithEntity(demande); // Mettre à jour le formulaire avec la date de traitement

            } catch (Exception e) {
                JOptionPane.showMessageDialog(diEditPanel, "Erreur lors de la création du membre : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else if ("REJETEE".equals(nouveauStatut)) {
            demande.setStatut("REJETEE");
            demande.setDateDeTraitement(LocalDateTime.now());
            service.modifier(demande);
            JOptionPane.showMessageDialog(diEditPanel, "La demande a été marquée comme rejetée.", "Info", JOptionPane.INFORMATION_MESSAGE);
            diEditPanel.initFormWithEntity(demande);
        } else { // Retour à EN_ATTENTE, ne devrait pas arriver depuis l'état traité, mais par sécurité
            demande.setStatut(nouveauStatut);
            service.modifier(demande);
        }
    }

    @Override
    protected void performDeleteOperation() {
        super.performDeleteOperation();
    }

    public void searchByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            refreshTableData();
        } else {
            List<DemandeInscription> results = demandeInscriptionService.rechercherParEmail(email.trim());
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(editPanel, "Aucune demande trouvée pour l'email : '" + email + "'", "Résultats de recherche", JOptionPane.INFORMATION_MESSAGE);
            } else {
                editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(results));
            }
        }
    }

    private Integer getSelectedEntityKey() {
        int selectedRow = editPanel.getCustomTablePanel().getTable().getSelectedRow();
        if (selectedRow >= 0) {
            Object rawKey = editPanel.getCustomTablePanel().getTable().getValueAt(selectedRow, 0); // Colonne 0 pour l'ID
            return convertRawKeyToKeyType(rawKey);
        }
        return null;
    }
}