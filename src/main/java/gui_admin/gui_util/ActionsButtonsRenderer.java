package gui_admin.gui_util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import entite.DemandeInscription;

public class ActionsButtonsRenderer implements TableCellRenderer {
    private final ActionsButtonsPanel panel = new ActionsButtonsPanel();
    private final JLabel statusLabel = new JLabel();
    private final List<DemandeInscription> demandes;

    public ActionsButtonsRenderer(List<DemandeInscription> demandes) {
        this.demandes = demandes;
        statusLabel.setOpaque(true);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        DemandeInscription demande = (DemandeInscription) value;

        if ("EN_ATTENTE".equals(demande.getStatut())) {
            panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            return panel;
        } else {
            statusLabel.setText(demande.getStatut());
            if ("VALIDEE".equals(demande.getStatut())) {
                statusLabel.setBackground(new Color(223, 240, 216)); // Vert clair
                statusLabel.setForeground(new Color(60, 118, 61)); // Vert foncé
            } else if ("REJETEE".equals(demande.getStatut())) {
                statusLabel.setBackground(new Color(242, 222, 222)); // Rouge clair
                statusLabel.setForeground(new Color(169, 68, 66)); // Rouge foncé
            } else {
                statusLabel.setBackground(table.getBackground());
                statusLabel.setForeground(table.getForeground());
            }
            return statusLabel;
        }
    }
}
