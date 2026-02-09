import javax.swing.*;
import java.awt.*;

public class MainPanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Assignment 1");

        frame.setSize(600, 400);
        frame.add(new GridHouse(), BorderLayout.SOUTH);
        frame.add(new ToolBar(), BorderLayout.NORTH);
        //frame.add(new DynamicGrid(), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
