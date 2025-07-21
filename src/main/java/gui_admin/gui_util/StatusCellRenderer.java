package gui_admin.gui_util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof String) {
            String status = (String) value;
            JLabel label = new JLabel(status);
            label.setOpaque(true);
            label.setHorizontalAlignment(CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 12));
            label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            switch (status) {
                case "VALIDEE":
                case "APPROVED":
                    label.setBackground(new Color(223, 240, 216)); // Vert pastel
                    label.setForeground(new Color(60, 118, 61));
                    break;
                case "REJETEE":
                case "REJECT":
                    label.setBackground(new Color(252, 228, 228)); // Rouge pastel
                    label.setForeground(new Color(169, 68, 66));
                    break;
                case "EN_ATTENTE":
                case "PENDING":
                default:
                    label.setBackground(new Color(252, 248, 227)); // Jaune pastel
                    label.setForeground(new Color(138, 109, 59));
                    break;
            }

            // Si la ligne est sélectionnée, on utilise les couleurs de sélection de la table
            if (isSelected) {
                label.setBackground(table.getSelectionBackground());
                label.setForeground(table.getSelectionForeground());
            }

            return label;
        }

        return c;
    }
}
