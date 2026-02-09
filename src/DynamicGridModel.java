import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class DynamicGridModel extends JPanel implements PropertyChangeListener, MouseListener {    // The grid represents the logical state of the array
    // The labels are responsible for depicting what each cell represents
    private CellType[][] grid;
    private JLabel[][] labels;

    private Point start = null;
    private Point end = null;
    private int rows;
    private int cols;
    private boolean paused = false;

    public DynamicGridModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(rows, cols));
        grid = new CellType[rows][cols];
        labels = new JLabel[rows][cols];
        initGrid();
    }

    public synchronized void setStart(int r, int c) {
        clearCell(CellType.START);
        start = new Point(c, r);
        grid[r][c] = CellType.START;
        notifyChange();
    }

    public synchronized void setEnd(int r, int c) {
        clearCell(CellType.END);
        end = new Point(c, r);
        grid[r][c] = CellType.END;
        notifyChange();
    }

    public synchronized Point getStart() {
        return start;
    }

    public synchronized Point getEnd() {
        return end;
    }

    public synchronized int getRows() {
        return rows;
    }

    public synchronized int getCols() {
        return cols;
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        // wakes up all threads???
        notifyAll();
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public synchronized CellType getCell(int r, int c) {
        return grid[r][c];
    }

    // Let's out Grid alert that something has changed, creates an object that keeps a list of
    // listeners and notifies them when something has changed ie there is new data to get from the blackboard
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    // Whenever something changes notify the listeners
    public void addPropertyChangeListener(PropertyChangeListener listner) {
        pcs.addPropertyChangeListener(listner);
    }

    // Method that is called to begin the chain of events of alerting the listerns
    // Will get called in the grids logic updates
    private void notifyChange() {
        pcs.firePropertyChange("grid", null, null);
        refreshView();
    }

    // When there is a change we must refresh our grid.
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        SwingUtilities.invokeLater(this::refreshView);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        int r = (int) label.getClientProperty("row");
        int c = (int) label.getClientProperty("col");
        switch(e.getClickCount()) {
            case 1:
                setStart(r, c);
                System.out.println("1");
                break;
            case 2:
                setEnd(r, c);
                System.out.println("2");
                break;
            case 3:
                toggleObstacle(r, c);
                System.out.println("3");
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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

    private void initGrid() {

        // loop through each "cell" and assign it a label to later be used to show state of path finding algorithm
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                grid[r][c] = CellType.EMPTY;
                JLabel label = new JLabel();
                label.putClientProperty("row", r);
                label.putClientProperty("col", c);
                label.addMouseListener(this);
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

    private void clearCell(CellType type) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == type) {
                    grid[r][c] = CellType.EMPTY;
                }
            }
        }
    }

    public synchronized void toggleObstacle(int r, int c) {
        if (grid[r][c] == CellType.EMPTY) {grid[r][c] = CellType.OBSTACLE;}
        else if (grid[r][c] == CellType.OBSTACLE) {grid[r][c] = CellType.EMPTY;}
        notifyChange();
    }

    public synchronized void markFrontier(Point p) {
        if (grid[p.y][p.x] == CellType.EMPTY) {grid[p.y][p.x] = CellType.FRONTIER;}
        notifyChange();
    }

    public synchronized void markVisited (Point p) {
        if (grid[p.y][p.x] != CellType.START && grid[p.y][p.x] !=CellType.END) {grid[p.y][p.x] = CellType.VISITED;}
        notifyChange();
    }

    public synchronized void markPath(List<Point> path) {
        for(Point p : path) {
            if (grid[p.y][p.x] != CellType.START && grid[p.y][p.x] !=CellType.END) {grid[p.y][p.x] = CellType.PATH;}
        }
        notifyChange();
    }

    public void startPathFinding () {
        Thread bfs = new Thread();
        bfs.start();
    }

    // Clear the algorithms setup, leave users choices in palace
    public synchronized void clearSearchMarks() {
        for (int r = 0; r < rows; r++) {
            for (int c  = 0; c < cols; c++) {
                if(grid[r][c] == CellType.FRONTIER || grid[r][c] == CellType.PATH || grid[r][c] == CellType.VISITED) {
                    grid[r][c] = CellType.EMPTY;
                }
            }
        }
        notifyChange();
    }

    // Reset the grid
    public synchronized void reset() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }
        start = null;
        end =  null;
        notifyChange();
    }

}
