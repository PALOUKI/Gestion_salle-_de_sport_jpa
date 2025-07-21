package gui_admin.gui_util;

import javax.swing.*;
import java.awt.*;

public class RejectIcon implements Icon {
    private final int width = 16;
    private final int height = 16;

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(204, 0, 0)); // Rouge
        g2d.setStroke(new BasicStroke(2));

        // Dessiner une croix (X)
        g2d.drawLine(x + 4, y + 4, x + 12, y + 12);
        g2d.drawLine(x + 4, y + 12, x + 12, y + 4);

        g2d.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
