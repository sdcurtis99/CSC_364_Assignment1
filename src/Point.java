/** Represents a location in 2D space.*/
public class Point {
    /**
     * The horizontal component.
     */
    public final int x;

    /**
     * The vertical component. Larger values are lower on the screen.
     */
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}