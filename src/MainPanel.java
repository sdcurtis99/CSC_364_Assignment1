import javax.swing.*;
import java.awt.*;

public class MainPanel {

    private JFrame frame;
    private GridHouse gridHouse;
    private DynamicGridModel grid;
    private ToolBar toolBar;

    public MainPanel() {

        frame = new JFrame("Concurrent Grid Pathfinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        grid = new DynamicGridModel(10, 10);

        toolBar = new ToolBar(grid);

        gridHouse = new GridHouse();

        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(gridHouse, BorderLayout.SOUTH);
        gridHouse.add(grid, BorderLayout.CENTER);

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    // Do we need resize requests

    public static void main(String[] args) {
        MainPanel mainWindow = new MainPanel();
    }
}
