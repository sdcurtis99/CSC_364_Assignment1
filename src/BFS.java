import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFS implements Runnable {

    private int numRows;
    private int numCols;

    private Point start;
    private Point goal;
    private List<Point> blockedCells;

    private List<Point> resultPath;

    private volatile boolean running = true;

    public BFS(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
    }

    public void setData(Point start, Point goal, List<Point> blockedCells) {
        this.start = start;
        this.goal = goal;
        this.blockedCells = blockedCells;
    }

    public void stop() {
        running = false;
    }

    public List<Point> getResultPath() {
        return resultPath;
    }

    @Override
    public void run() {
        resultPath = findPath(start, goal, blockedCells);
    }

    public List<Point> findPath(Point start, Point goal, List<Point> blockedCells) {

        boolean[][] visited = new boolean[numRows][numCols];
        Point[][] cameFrom = new Point[numRows][numCols];

        Queue<Point> openQueue = new LinkedList<>();
        openQueue.add(start);
        visited[start.x][start.y] = true;

        while (!openQueue.isEmpty() && running) {

            Point currentCell = openQueue.poll();

            if (currentCell.equals(goal)) {
                return buildPath(cameFrom, start, goal);
            }

            int row = currentCell.x;
            int col = currentCell.y;

            if (isValidCell(row - 1, col, visited, blockedCells)) {
                visited[row - 1][col] = true;
                cameFrom[row - 1][col] = currentCell;
                openQueue.add(new Point(row - 1, col));
            }

            if (isValidCell(row + 1, col, visited, blockedCells)) {
                visited[row + 1][col] = true;
                cameFrom[row + 1][col] = currentCell;
                openQueue.add(new Point(row + 1, col));
            }

            if (isValidCell(row, col - 1, visited, blockedCells)) {
                visited[row][col - 1] = true;
                cameFrom[row][col - 1] = currentCell;
                openQueue.add(new Point(row, col - 1));
            }

            if (isValidCell(row, col + 1, visited, blockedCells)) {
                visited[row][col + 1] = true;
                cameFrom[row][col + 1] = currentCell;
                openQueue.add(new Point(row, col + 1));
            }
        }

        return new ArrayList<>();
    }

    private boolean isValidCell(int row, int col, boolean[][] visited, List<Point> blockedCells) {

        if (row < 0 || row >= numRows || col < 0 || col >= numCols) return false;
        if (visited[row][col]) return false;

        for (Point p : blockedCells) {
            if (p.x == row && p.y == col) return false;
        }

        return true;
    }

    private List<Point> buildPath(Point[][] cameFrom, Point start, Point goal) {

        List<Point> path = new ArrayList<>();
        Point current = goal;

        while (true) {
            path.add(0, current);
            if (current.equals(start)) break;
            current = cameFrom[current.x][current.y];
        }

        return path;
    }
}
