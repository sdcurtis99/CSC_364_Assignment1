import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;



public class ToolBar extends JPanel implements ActionListener {

    private DynamicGridModel grid;
    private JButton startButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JComboBox<Integer> gridDropDown;

    public ToolBar(DynamicGridModel grid) {

        this.grid = grid;
        JLabel gTitle = new JLabel("Grid Count:");
        Integer[] gridCounts = {10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};
        gridDropDown = new JComboBox<>(gridCounts);

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        pauseButton = new JButton("Pause");

        add(gTitle);
        add(gridDropDown);
        add(startButton);
        add(resetButton);
        add(pauseButton);

        startButton.addActionListener(this);
        pauseButton.addActionListener(this);
        resetButton.addActionListener(this);
        gridDropDown.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == gridDropDown) {
            Integer newSize = (Integer) gridDropDown.getSelectedItem();
            // Tell the rest of the program that a resize was requested
            if (newSize != null) {
                if (newSize != grid.getRows()) {
                    firePropertyChange("resize", null, newSize);
                }
            }
            return;
        }

        // getActionCommand returns the text for buttons
        switch (event.getActionCommand()) {
            case "Start":
                if (grid.isPathfindingRunning()) return;
                grid.clearSearchMarks();
                grid.startPathFinding();
                // BFS thread will be started here later
                //System.out.println("Start pressed");
                break;

            case "Reset":
                grid.reset();
                //System.out.println("Reset pressed");
                break;

            case "Pause":
                grid.pause();
                pauseButton.setText("Resume");
                //System.out.println("Paused");
                break;

            case "Resume":
                grid.resume();
                pauseButton.setText("Pause");
                //System.out.println("Resumed");
                break;
        }
    }
    public void setGrid(DynamicGridModel newGrid) {
        this.grid = newGrid;
    }

}

