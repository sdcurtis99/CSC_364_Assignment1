import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class DynamicGrid extends JPanel implements PropertyChangeListener{

    // The grid represents the logical state of the array
    // The labels are responsible for depicting what each cell represents
    private CellType[][] grid;
    private JLabel[][] labels;

    private int rows;
    private int cols;
    private boolean running = true;
    private boolean paused = false;
    private boolean finished = false;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listner) {
        pcs.addPropertyChangeListener(listner);
    }

    private void notifyChange() {
        pcs.firePropertyChange("grid", null, null);
    }

    // enum for the different types of cells
    enum CellType {
        EMPTY,
        START,
        END,
        OBSTACLE,
        FRONTIER,
        VISITED,
        PATH
    }

    public DynamicGrid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(rows, cols));
        grid = new CellType[rows][cols];
        labels = new JLabel[rows][cols];
        initGrid();
    }

    private void initGrid() {

        // loop through each "cell" and assign it a label to later be used to show state of path finding algorithm
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = CellType.EMPTY;
                JLabel label = new JLabel();
                label.setOpaque(true);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setBackground(Color.WHITE);
                labels[r][c] = label;
                add(label);
            }
        }
    }

    // Assign each enum with a specfic color
    private Color colorFor(CellType type) {
        switch(type) {
            case START:
                return Color.GREEN;
            case END:
                return Color.YELLOW;
            case OBSTACLE:
                return Color.BLACK;
            case FRONTIER:
                return Color.PINK;
            case VISITED:
                return Color.BLUE;
            case PATH:
                return Color.ORANGE;
            default:
                return Color.WHITE;
        }
    }

    private void refreshView() {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    labels[r][c].setBackground(colorFor(grid[r][c]));
                }
            }
        }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    public void setRunning (boolean state) {
        this.running = state;
    }

    public void setPaused (boolean state) {
        this.paused = state;
    }

    public void setFinished(boolean state) {
        this.finished = state;
    }

    public boolean getRunning () {
        return this.running;
    }

    public boolean getPaused () {
        return this.paused;
    }

    public boolean getFinished() {
        return this.finished;
    }


}
