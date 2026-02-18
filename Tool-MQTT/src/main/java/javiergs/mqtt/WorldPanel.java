package javiergs.mqtt;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class WorldPanel extends JPanel implements PropertyChangeListener {

    private static final int GRID_SIZE = 20;

    public WorldPanel() {
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawGrid(g);

        // Draw all players
        for (Player p : Blackboard.getInstance().getPlayers()) {
            p.draw(g);
        }
    }

    private void drawGrid(Graphics g) {

        g.setColor(Color.LIGHT_GRAY);

        int width = getWidth();
        int height = getHeight();

        for (int x = 0; x < width; x += GRID_SIZE) {
            g.drawLine(x, 0, x, height);
        }

        for (int y = 0; y < height; y += GRID_SIZE) {
            g.drawLine(0, y, width, y);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        // Ensure repaint happens on Swing thread
        if (SwingUtilities.isEventDispatchThread()) {
            repaint();
        } else {
            SwingUtilities.invokeLater(this::repaint);
        }
    }
}