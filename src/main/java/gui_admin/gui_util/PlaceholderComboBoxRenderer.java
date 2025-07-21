// Fichier: gui_admin/gui_util/PlaceholderComboBoxRenderer.java
package gui_admin.gui_util;

import javax.swing.*;
import java.awt.*;

public class PlaceholderComboBoxRenderer<T> extends DefaultListCellRenderer {

    private final String placeholder;

    public PlaceholderComboBoxRenderer(String placeholder) {
        if (placeholder == null) {
            throw new IllegalArgumentException("Placeholder text cannot be null.");
        }
        this.placeholder = placeholder;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        // Appelle la méthode parente pour obtenir le comportement par défaut (couleurs, etc.)
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null) {
            // Si la valeur est null, on affiche le placeholder
            setText(placeholder);
        } else {
            // Sinon, on affiche le toString() de l'objet, ce que la méthode super fait déjà.
            // On s'assure juste que le texte est bien celui de l'objet.
            setText(value.toString());
        }

        return this;
    }
}