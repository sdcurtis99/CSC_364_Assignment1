package javiergs.mqtt;

import java.awt.*;

public class Player {

    private String id;
    private int x;
    private int y;
    private Color color;

    public static final int SIZE = 20;

    public Player(String id, int x, int y, Color color) {
        this.id = id;
        this.x = alignToGrid(x);
        this.y = alignToGrid(y);
        this.color = color;
    }

    private int alignToGrid(int value) {
        return (value / SIZE) * SIZE;
    }

    public String getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Color getColor() { return color; }

    public void setX(int x) { this.x = alignToGrid(x); }
    public void setY(int y) { this.y = alignToGrid(y); }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, SIZE, SIZE);
    }
}