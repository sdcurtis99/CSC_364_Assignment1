import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;

public class MainPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Assignment 1");
        GridHouse gridHouse = new GridHouse();
        DynamicGrid dynamicGrid = new DynamicGrid();
        ToolBar toolBar = new ToolBar();

        frame.setSize(600, 400);
        frame.add(gridHouse, BorderLayout.SOUTH);
        frame.add(toolBar, BorderLayout.NORTH);
        frame.add(dynamicGrid, BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
