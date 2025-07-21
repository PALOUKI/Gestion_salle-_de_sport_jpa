package gui_admin.gui_util;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.List;
import entite.DemandeInscription;

public class ActionsButtonsEditor extends AbstractCellEditor implements TableCellEditor {
    private final ActionsButtonsPanel panel = new ActionsButtonsPanel();
    private final JTable table;
    private transient ActionListener approveListener;
    private transient ActionListener rejectListener;
    private int editingRow;
    private final List<DemandeInscription> demandes;

    public ActionsButtonsEditor(JTable table, ActionListener approveListener, ActionListener rejectListener, List<DemandeInscription> demandes) {
        this.table = table;
        this.approveListener = approveListener;
        this.rejectListener = rejectListener;
        this.demandes = demandes;

        panel.approveButton.addActionListener(e -> {
            if (editingRow != -1) {
                approveListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(editingRow)));
                fireEditingStopped();
            }
        });

        panel.rejectButton.addActionListener(e -> {
            if (editingRow != -1) {
                rejectListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, String.valueOf(editingRow)));
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.editingRow = row;
        panel.setBackground(table.getSelectionBackground());
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof java.awt.event.MouseEvent) {
            int row = table.rowAtPoint(((java.awt.event.MouseEvent) anEvent).getPoint());
            if (row >= 0 && row < demandes.size()) {
                DemandeInscription demande = demandes.get(row);
                return "EN_ATTENTE".equals(demande.getStatut());
            }
        }
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }
}
