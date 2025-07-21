package gui_admin.gui_util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

public class ActionsButtonsPanel extends JPanel {
    public final JButton approveButton;
    public final JButton rejectButton;

    public ActionsButtonsPanel() {
        super(new FlowLayout(FlowLayout.CENTER, 15, 0)); 
        setOpaque(true);

        // --- Bouton Approuver ---
        approveButton = new JButton("Approuver");
        setupButton(approveButton, new Color(0, 153, 51), new ApproveIcon());

        // --- Bouton Rejeter ---
        rejectButton = new JButton("Rejeter");
        setupButton(rejectButton, new Color(204, 0, 0), new RejectIcon());

        add(approveButton);
        add(rejectButton);
    }

    private void setupButton(JButton button, Color color, Icon icon) {
        button.setForeground(color);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setIcon(icon);
        
        // Style moderne sans bordure ni fond
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
    }
}
