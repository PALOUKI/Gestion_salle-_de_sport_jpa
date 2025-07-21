package gui_admin.gui_util;

import javax.swing.*;
import java.awt.*;

public class ActionButton extends JButton {

    public enum ButtonType {
        SAVE, CANCEL, MODIFY, DELETE, ADD, VIEW, SEARCH, REFRESH
    }

    public ActionButton(String text, ButtonType type) {
        super(text);
        customizeButton(type);
    }

    private void customizeButton(ButtonType type) {
        // Exemples de personnalisation (à adapter à ton thème)
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        switch (type) {
            case SAVE:
                setBackground(new Color(40, 167, 69)); // Vert pour sauvegarder
                break;
            case CANCEL:
                setBackground(new Color(108, 117, 125)); // Gris pour annuler
                break;
            case MODIFY:
                setBackground(new Color(0, 123, 255)); // Bleu pour modifier
                break;
            case DELETE:
                setBackground(new Color(220, 53, 69)); // Rouge pour supprimer
                break;
            case ADD:
                setBackground(new Color(23, 162, 184)); // Cyan pour ajouter
                break;
            case VIEW:
                setBackground(new Color(50, 50, 150)); // Bleu foncé pour voir
                break;
            case SEARCH:
                setBackground(new Color(219, 159, 243)); // Bleu foncé pour voir
                break;
            case REFRESH:
                setBackground(new Color(23, 162, 184)); // Un cyan, similaire à ADD
                break;
            default:
                setBackground(new Color(60, 60, 60)); // Couleur par défaut
                break;
        }
        setRolloverEnabled(true);
        // Ajoute un effet au survol si tu veux
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(getBackground().brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                // Revenir à la couleur d'origine ou une version légèrement plus foncée
                Color originalColor;
                switch (type) {
                    case SAVE: originalColor = new Color(40, 167, 69); break;
                    case CANCEL: originalColor = new Color(108, 117, 125); break;
                    case MODIFY: originalColor = new Color(0, 123, 255); break;
                    case DELETE: originalColor = new Color(220, 53, 69); break;
                    case ADD: originalColor = new Color(23, 162, 184); break;
                    case VIEW: originalColor = new Color(50, 50, 150); break;
                    case REFRESH: originalColor = new Color(23, 162, 184); break;
                    default: originalColor = new Color(60, 60, 60); break;
                }
                setBackground(originalColor);
            }
        });
    }
}