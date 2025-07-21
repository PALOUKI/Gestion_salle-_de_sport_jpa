package gui_admin.gui_util;

import entite.GenericEntity;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class GenericEdit<Entity extends GenericEntity> extends JPanel {

    protected JPanel form = new JPanel();
    protected ButtonPanel buttonPanel = new ButtonPanel(); // ButtonPanel manages Save and Cancel buttons
    protected CustomTablePanel customTablePanel;
    protected Entity currentEntity;
    protected JButton refreshButton; // Bouton de rafraîchissement

    public GenericEdit(List<List<Object>> tableData, List<String> columnNames) {
        this.setLayout(new BorderLayout()); // A default layout, concrete class will set the final layout

        this.customTablePanel = new CustomTablePanel(tableData, columnNames);
        this.refreshButton = new JButton(new RefreshIcon());
        refreshButton.setToolTipText("Rafraîchir la liste");
        refreshButton.setOpaque(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setBorderPainted(false);

        this.currentEntity = null;
    }

    public abstract void initEntityFromForm();

    public abstract void initFormWithEntity(Entity entity);

    public abstract void clearForm();

    @SuppressWarnings("unchecked")
    public Entity getEntity() {
        return currentEntity;
    }

    public void setEntity(Object entity) {
        if (entity == null) {
            this.currentEntity = null;
        } else {
            try {
                this.currentEntity = (Entity) entity;
            } catch (ClassCastException e) {
                System.err.println("Erreur de transtypage (cast) de l'entité dans GenericEdit: " + e.getMessage());
                throw e;
            }
        }
    }

    public JPanel getForm() {
        return form;
    }

    public CustomTablePanel getCustomTablePanel() {
        return customTablePanel;
    }

    // Getters for Save and Cancel buttons, provided by ButtonPanel
    public JButton getSaveButton() {
        return buttonPanel.getSaveButton();
    }

    public JButton getCancelButton() {
        return buttonPanel.getCancelButton();
    }

    // Abstract methods for Add, Modify, Delete buttons, to be implemented by concrete subclasses
    public abstract JButton getModifyButton();
    public abstract JButton getDeleteButton();

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public void afficher(){
        this.setVisible(true);
    }
}