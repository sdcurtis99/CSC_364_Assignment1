import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainPanel implements PropertyChangeListener {

    private JFrame frame;
    private GridHouse gridHouse;
    private DynamicGridModel grid;
    private ToolBar toolBar;

    public MainPanel() {

        frame = new JFrame("Concurrent Grid Pathfinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        grid = new DynamicGridModel(10, 10);
        grid.setPreferredSize(new Dimension(400, 400));


        toolBar = new ToolBar(grid);
        toolBar.addPropertyChangeListener(this);

        gridHouse = new GridHouse();

        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(gridHouse, BorderLayout.CENTER);
        gridHouse.add(grid);

        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    // Do we need resize requests

    public static void main(String[] args) {
        MainPanel mainWindow = new MainPanel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if ("resize".equals(event.getPropertyName())) {
            int newSize = (int) event.getNewValue();

            gridHouse.remove(grid);

            grid = new DynamicGridModel(newSize, newSize);
            grid.setPreferredSize(new Dimension(400, 400));

            gridHouse.add(grid);

            toolBar.setGrid(grid);

            gridHouse.revalidate();
            gridHouse.repaint();
        }
    }
}
