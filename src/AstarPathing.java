import java.util.*;
import java.util.stream.Stream;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class AStarPathingStrategy implements PathingStrategy {
    /**
     * Return a list containing a single point representing the next step toward a goal
     * If the start is within reach of the goal, the returned list is empty.
     *
     * @param start              the point to begin the search from
     * @param end                the point to search for a point within reach of
     * @param canPassThrough     a function that returns true if the given point is traversable
     * @param withinReach        a function that returns true if both points are within reach of each other
     * @param potentialNeighbors a function that returns the neighbors of a given point, as a stream
     */

    //add inputs for obstacles
    //convert cells into point objects

    public List<Point> computePath(
            Point start,
            Point end,
            Predicate<Point> canPassThrough,
            BiPredicate<Point, Point> withinReach,
            Function<Point, Stream<Point>> potentialNeighbors
    ) {
        //Create Needed Data Structures
        Set<Point> openSet = new HashSet<>();
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Integer> gScore = new HashMap<>();
        Map<Point, Integer> fScore = new HashMap<>();
        Map<Point, Point> prevNode = new HashMap<>();

        // Find and record the G and F value for the starting node
        gScore.put(start, 0);
        fScore.put(start, gScore.get(start) + start.manhattanDistanceTo(end));

        // Add the starting node to the openSet
        openSet.add(start);

        // 3 While openSet
        Point bestPoint = null;
        while (!openSet.isEmpty()) {

            // 3-1. Find the f-scores for all neighboring nodes and pick the one with the best or make a choice
            Integer bestFScore = 99999;
            for (Point currPoint : openSet) {
                int currFScore = fScore.get(currPoint);
                if (currFScore < bestFScore) {
                    bestFScore = currFScore;
                    bestPoint = currPoint;
                }
            }
            // 3-2. Move that node to from the open set to the closed set
            openSet.remove(bestPoint);
            closedSet.add(bestPoint);

            // 3-3. If that node is next to end goal --> 3.5
            if (withinReach.test(bestPoint, end)) {
                break;
            }

            // 3-4. For each traversable neighbor of the current node not in the closed set:
            Stream<Point> neighboringPoints = potentialNeighbors.apply(bestPoint);
            List<Point> neighborList = neighboringPoints.filter(canPassThrough).filter(neighbor -> !closedSet.contains(neighbor)).toList();

            // 3-4-1. If neighbor not in open set add it there
            for (Point neighbor : neighborList) {
                int tentativeGScore = gScore.get(bestPoint) + 1;

                // If neighbor not in openSet OR this path is better
                if (!openSet.contains(neighbor) || tentativeGScore < gScore.get(neighbor)) {

                    // Update everything
                    prevNode.put(neighbor, bestPoint);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + neighbor.manhattanDistanceTo(end));

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        // 4. If no path is found return an empty list, null, the start node
        if (!withinReach.test(bestPoint, end)) {
            return List.of();
        }
        // 5. Reconstruct and return the path using the previous mappings and add currentNode
        else {
            List<Point> bestPath = new LinkedList<>();
            Point curr = bestPoint;
            while (!curr.equals(start)) {
                bestPath.add(curr);
                curr = prevNode.get(curr);
            }
            Collections.reverse(bestPath);
            return bestPath;
        }
    }
