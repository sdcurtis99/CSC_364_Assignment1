import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BFS implements Runnable {

    private DynamicGridModel model;
    private List<Point> resultPath;
    private volatile boolean running = true;

    public BFS(DynamicGridModel model) {
        this.model = model;
    }

    public void stop() {
        running = false;
    }

    public List<Point> getResultPath() {
        return resultPath;
    }

    @Override
    public void run() {

        Point start = model.getStart();
        Point goal = model.getEnd();
        int rows = model.getRows();
        int cols = model.getCols();

        if (start == null || goal == null) {
            resultPath = new ArrayList<>();
            return;
        }

        model.clearSearchMarks();
        resultPath = findPath(start, goal, rows, cols);
    }

    public List<Point> findPath(Point start, Point goal, int rows, int cols) {

        boolean[][] visited = new boolean[rows][cols];
        Point[][] cameFrom = new Point[rows][cols];

        Queue<Point> openQueue = new LinkedList<>();
        openQueue.add(start);
        visited[start.y][start.x] = true;
        model.markFrontier(start);

        while (!openQueue.isEmpty() && running) {

            Point currentCell = openQueue.poll();
            model.markVisited(currentCell);

            if (currentCell.equals(goal)) {
                List<Point> path = buildPath(cameFrom, start, goal);
                model.markPath(path);
                return path;
            }

            int row = currentCell.y;
            int col = currentCell.x;

            if (isValidCell(row - 1, col, visited, rows, cols)) {
                visited[row - 1][col] = true;
                cameFrom[row - 1][col] = currentCell;
                Point next = new Point(col, row - 1);
                openQueue.add(next);
                model.markFrontier(next);
            }

            if (isValidCell(row + 1, col, visited, rows, cols)) {
                visited[row + 1][col] = true;
                cameFrom[row + 1][col] = currentCell;
                Point next = new Point(col, row + 1);
                openQueue.add(next);
                model.markFrontier(next);
            }

            if (isValidCell(row, col - 1, visited, rows, cols)) {
                visited[row][col - 1] = true;
                cameFrom[row][col - 1] = currentCell;
                Point next = new Point(col - 1, row);
                openQueue.add(next);
                model.markFrontier(next);
            }

            if (isValidCell(row, col + 1, visited, rows, cols)) {
                visited[row][col + 1] = true;
                cameFrom[row][col + 1] = currentCell;
                Point next = new Point(col + 1, row);
                openQueue.add(next);
                model.markFrontier(next);
            }
        }

        return new ArrayList<>();
    }

    private boolean isValidCell(int row, int col, boolean[][] visited, int rows, int cols) {

        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        if (visited[row][col]) return false;

        return model.getCell(row, col) != DynamicGridModel.CellType.OBSTACLE;
    }

    private List<Point> buildPath(Point[][] cameFrom, Point start, Point goal) {

        List<Point> path = new ArrayList<>();
        Point current = goal;

        while (true) {
            path.add(0, current);
            sleep(300);
            if (current.equals(start)) break;
            current = cameFrom[current.y][current.x];
            if (current == null) return new ArrayList<>();
        }

        return path;
    }
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
