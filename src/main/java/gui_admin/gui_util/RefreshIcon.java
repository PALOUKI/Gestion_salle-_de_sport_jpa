package gui_admin.gui_util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;

public class RefreshIcon implements Icon {
    private final int width = 16;
    private final int height = 16;

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));

        // Dessiner une flèche circulaire
        g2d.draw(new Arc2D.Double(x + 2, y + 2, 12, 12, 30, 300, Arc2D.OPEN));
        // Flèche
        g2d.drawLine(x + 8, y + 2, x + 11, y - 1);
        g2d.drawLine(x + 8, y + 2, x + 11, y + 5);

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
