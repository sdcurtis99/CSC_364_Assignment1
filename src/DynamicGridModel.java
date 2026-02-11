import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class DynamicGridModel extends JPanel implements MouseListener {

    private CellType[][] grid;
    private JLabel[][] labels;

    private Point start = null;
    private Point end = null;
    private int rows;
    private int cols;

    private boolean paused = false;
    private int activeSearchToken = 0;
    private boolean pathfindingrunning = false;

    public DynamicGridModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        setLayout(new GridLayout(rows, cols));
        grid = new CellType[rows][cols];
        labels = new JLabel[rows][cols];
        initGrid();
    }

    // =============================
    // ===== Thread-Safe Model =====
    // =============================

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

    public Point getStart() { return start; }
    public Point getEnd() { return end; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public synchronized void pause() { paused = true; }
    public synchronized void resume() { paused = false; }

    public synchronized boolean isPaused() { return paused; }

    public synchronized int getActiveSearchToken() {
        return activeSearchToken;
    }

    public synchronized void cancelSearchToken() {
        activeSearchToken++;
    }

    public synchronized boolean isPathfindingRunning() {
        return pathfindingrunning;
    }

    public synchronized boolean tryStartPathfinding() {
        if (pathfindingrunning) return false;
        pathfindingrunning = true;
        return true;
    }

    public synchronized void finishPathfinding() {
        pathfindingrunning = false;
    }

    public synchronized CellType getCell(int r, int c) {
        return grid[r][c];
    }

    public synchronized void toggleObstacle(int r, int c) {
        if (grid[r][c] == CellType.START || grid[r][c] == CellType.END)
            return;

        if (grid[r][c] == CellType.EMPTY)
            grid[r][c] = CellType.OBSTACLE;
        else if (grid[r][c] == CellType.OBSTACLE)
            grid[r][c] = CellType.EMPTY;

        notifyChange();
    }

    public synchronized void markFrontier(Point p) {
        if (grid[p.y][p.x] == CellType.EMPTY)
            grid[p.y][p.x] = CellType.FRONTIER;

        notifyChange();
    }

    public synchronized void markVisited(Point p) {
        if (grid[p.y][p.x] != CellType.START &&
                grid[p.y][p.x] != CellType.END)
            grid[p.y][p.x] = CellType.VISITED;

        notifyChange();
    }

    public synchronized void markPath(List<Point> path) {
        for (Point p : path) {
            if (grid[p.y][p.x] != CellType.START &&
                    grid[p.y][p.x] != CellType.END)
                grid[p.y][p.x] = CellType.PATH;
        }
        notifyChange();
    }

    public synchronized void clearSearchMarks() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == CellType.FRONTIER ||
                        grid[r][c] == CellType.PATH ||
                        grid[r][c] == CellType.VISITED) {
                    grid[r][c] = CellType.EMPTY;
                }
            }
        }
        notifyChange();
    }

    public synchronized void reset() {
        cancelSearchToken();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = CellType.EMPTY;
            }
        }
        start = null;
        end = null;
        notifyChange();
    }

    public void startPathFinding() {
        if (start == null || end == null) return;
        if (!tryStartPathfinding()) return;

        BFS bfsPath = new BFS(this);
        Thread bfsThread = new Thread(bfsPath);
        bfsThread.start();
    }

    public int delayforgridsize() {
        int cells = rows * cols;
        double delay = 1000.0 / Math.sqrt(cells);
        return Math.max(5, (int) delay);
    }

    // =============================
    // ===== Swing UI Section ======
    // =============================

    private void notifyChange() {
        if (SwingUtilities.isEventDispatchThread()) {
            refreshView();
        } else {
            SwingUtilities.invokeLater(this::refreshView);
        }
    }

    private void refreshView() {
        if (!SwingUtilities.isEventDispatchThread()) {
            throw new IllegalStateException("refreshView must run on EDT");
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                labels[r][c].setBackground(colorFor(grid[r][c]));
            }
        }
    }

    private void initGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
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

    private Color colorFor(CellType type) {
        switch (type) {
            case START: return Color.GREEN;
            case END: return Color.YELLOW;
            case OBSTACLE: return Color.BLACK;
            case FRONTIER: return Color.PINK;
            case VISITED: return Color.BLUE;
            case PATH: return Color.ORANGE;
            default: return Color.WHITE;
        }
    }

    private void clearCell(CellType type) {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == type)
                    grid[r][c] = CellType.EMPTY;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JLabel label = (JLabel) e.getSource();
        int r = (int) label.getClientProperty("row");
        int c = (int) label.getClientProperty("col");
        toggleObstacle(r, c);
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    enum CellType {
        EMPTY, START, END, OBSTACLE, FRONTIER, VISITED, PATH
    }
}