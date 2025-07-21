package gui_admin.controllerJpa;

import entite.GenericEntity;
import entite.TypeAbonnement; // Import the specific entity for constructor casting
import gui_admin.gui_util.GenericEdit;
import serviceJpa.GenericService;
import serviceJpa.TypeAbonnementService; // Import the specific service for constructor casting

import javax.swing.JOptionPane;
import java.util.List;

public abstract class GenericCrudController<Entity extends GenericEntity, Key> {

    protected GenericService<Entity, Key> service;
    protected GenericEdit<Entity> editPanel;
    private Class<Entity> entityClass;

    // Corrected constructor to accept GenericService and Class
    // The TypeAbonnementController will instantiate with its specific service and class
    public GenericCrudController(GenericService<Entity, Key> service, Class<Entity> entityClass) {
        this.service = service;
        this.entityClass = entityClass;
    }

    public void setEditPanel(GenericEdit<Entity> editPanel) {
        this.editPanel = editPanel;
        attachListeners();
    }

    protected void attachListeners() {
        if (editPanel == null) {
            System.err.println("ERREUR: editPanel est NULL lors de l'attachement des listeners !");
            return;
        }

        editPanel.getSaveButton().addActionListener(e -> {
            System.out.println("--- DIAGNOSTIC: Bouton 'Enregistrer' cliqué ---");
            performSaveOperation();
        });

        editPanel.getCancelButton().addActionListener(e -> {
            System.out.println("Opération annulée. Nettoyage du formulaire.");
            editPanel.clearForm();
            createNewEntityForForm();
        });

        if (editPanel.getModifyButton() != null) {
            editPanel.getModifyButton().addActionListener(e -> {
                System.out.println("--- DIAGNOSTIC: Bouton 'Modifier' cliqué ---");
                performSelectForModification();
            });
        }

        if (editPanel.getDeleteButton() != null) {
            editPanel.getDeleteButton().addActionListener(e -> {
                System.out.println("--- DIAGNOSTIC: Bouton 'Supprimer' cliqué ---");
                performDeleteOperation();
            });
        }

        if (editPanel.getRefreshButton() != null) {
            editPanel.getRefreshButton().addActionListener(e -> {
                System.out.println("--- DIAGNOSTIC: Bouton 'Rafraîchir' cliqué ---");
                refreshTableData();
            });
        }
    }

    public void prepareForAdd() {
        if (editPanel == null) {
            System.err.println("ERREUR: editPanel n'est pas défini avant prepareForAdd.");
            return;
        }
        createNewEntityForForm();
        editPanel.clearForm();
        System.out.println("Formulaire préparé pour un nouvel ajout.");
    }

    public void prepareForModify(Entity entityToModify) {
        if (editPanel == null) {
            System.err.println("ERREUR: editPanel n'est pas défini avant prepareForModify.");
            return;
        }
        editPanel.setEntity(entityToModify);
        editPanel.initFormWithEntity(entityToModify);
        System.out.println("Formulaire préparé pour modification de l'entité: " + entityToModify);
    }

    protected void performSaveOperation() {
        try {
            editPanel.initEntityFromForm();
            Entity entityToSave = editPanel.getEntity();

            if (entityToSave == null) {
                JOptionPane.showMessageDialog(editPanel, "Erreur interne: l'entité est nulle.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Key entityKey = getEntityKey(entityToSave);

            // Determine if it's an add or modify
            // Check if entity exists in DB by its key
            Entity existingEntity = null;
            if (entityKey != null) { // Only try to find if key is not null
                existingEntity = service.trouver(entityKey);
            }

            if (existingEntity == null) { // If not found, it's an ADD
                System.out.println("DEBUG: Tentative d'ajout d'une nouvelle entité.");
                service.ajouter(entityToSave);
                JOptionPane.showMessageDialog(editPanel, "Entité ajoutée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                editPanel.clearForm();
                createNewEntityForForm();
                refreshTableData();
            } else { // If found, it's a MODIFY
                System.out.println("DEBUG: Tentative de modification de l'entité avec clé: " + entityKey);
                service.modifier(entityToSave);
                JOptionPane.showMessageDialog(editPanel, "Entité modifiée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                refreshTableData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur de format numérique: " + ex.getMessage(), "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(editPanel, "Erreur lors de l'opération d'enregistrement : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.err.println("DEBUG: Exception complète lors de l'enregistrement:");
            ex.printStackTrace();
        }
    }

    protected void performSelectForModification() {
        int selectedRow = editPanel.getCustomTablePanel().getSelectedRow();
        if (selectedRow != -1) {
            Object rawKey = editPanel.getCustomTablePanel().getValueAt(selectedRow, 0);

            try {
                Key entityKey = convertRawKeyToKeyType(rawKey);
                Entity entityToModify = service.trouver(entityKey);
                if (entityToModify != null) {
                    prepareForModify(entityToModify);
                    System.out.println("Entité sélectionnée pour modification : " + entityToModify);
                } else {
                    JOptionPane.showMessageDialog(editPanel, "Impossible de trouver l'entité correspondante.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ClassCastException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(editPanel, "Erreur de conversion de la clé: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editPanel, "Erreur lors de la sélection pour modification : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(editPanel, "Veuillez sélectionner une entité dans le tableau pour la modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    protected void performDeleteOperation() {
        int selectedRow = editPanel.getCustomTablePanel().getSelectedRow();
        if (selectedRow != -1) {
            Object rawKey = editPanel.getCustomTablePanel().getValueAt(selectedRow, 0);

            try {
                Key entityKey = convertRawKeyToKeyType(rawKey);
                int confirm = JOptionPane.showConfirmDialog(editPanel,
                        "Êtes-vous sûr de vouloir supprimer cette entité avec la clé " + rawKey + "?\nAttention : cela peut entraîner des erreurs si d'autres données y sont liées !",
                        "Confirmation de suppression", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    Entity entityToDelete = service.trouver(entityKey);
                    if (entityToDelete != null) {
                        service.supprimer(entityToDelete);
                        JOptionPane.showMessageDialog(editPanel, "Entité supprimée avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        editPanel.clearForm();
                        createNewEntityForForm();
                        refreshTableData();
                    } else {
                        JOptionPane.showMessageDialog(editPanel, "L'entité sélectionnée n'existe plus en base de données.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (ClassCastException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(editPanel, "Erreur de conversion de la clé: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(editPanel, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(editPanel, "Veuillez sélectionner une entité dans le tableau pour la supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    protected void refreshTableData() {
        List<Entity> allEntities = service.listerTous();
        editPanel.getCustomTablePanel().updateTableData(convertEntitiesToTableData(allEntities));
    }

    private void createNewEntityForForm() {
        try {
            Entity newInstance = entityClass.getDeclaredConstructor().newInstance();
            editPanel.setEntity(newInstance);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création d'une nouvelle instance de l'entité: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(editPanel, "Erreur interne: Impossible de créer une nouvelle entité.", "Erreur Grave", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected abstract void performSearchOperation();

    protected abstract List<List<Object>> convertEntitiesToTableData(List<Entity> entities);

    protected abstract Key getEntityKey(Entity entity);

    protected abstract Key convertRawKeyToKeyType(Object rawKey) throws ClassCastException, NumberFormatException;
}