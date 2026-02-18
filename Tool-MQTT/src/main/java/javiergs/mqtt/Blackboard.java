package javiergs.mqtt;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;

public class Blackboard {

    public static final String BROKER = "tcp://broker.hivemq.com:1883";
    public static final String TOPIC = "csc509/brokerverse/sdcurtis99";

    private static Blackboard instance = new Blackboard();

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private Player me;
    private final Vector<Player> players = new Vector<>();

    private static final int STEP = Player.SIZE;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 400;

    private Blackboard() {}

    public static Blackboard getInstance() {
        return instance;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public void setMe(Player me) {
        this.me = me;
        players.add(me);
        support.firePropertyChange("playerAdded", null, me);
    }

    public Player getMe() {
        return me;
    }

    public Vector<Player> getPlayers() {
        return players;
    }

    private int clampX(int x) {
        return Math.max(0, Math.min(x, WIDTH - Player.SIZE));
    }

    private int clampY(int y) {
        return Math.max(0, Math.min(y, HEIGHT - Player.SIZE));
    }

    public void up() {
        me.setY(clampY(me.getY() - STEP));
        support.firePropertyChange("move", null, me);
    }

    public void down() {
        me.setY(clampY(me.getY() + STEP));
        support.firePropertyChange("move", null, me);
    }

    public void left() {
        me.setX(clampX(me.getX() - STEP));
        support.firePropertyChange("move", null, me);
    }

    public void right() {
        me.setX(clampX(me.getX() + STEP));
        support.firePropertyChange("move", null, me);
    }

    public void updateRemotePlayer(String payload) {

        String[] parts = payload.split(",");
        if (parts.length != 3) return;

        String id = parts[0];

        int x, y;
        try {
            x = clampX(Integer.parseInt(parts[1]));
            y = clampY(Integer.parseInt(parts[2]));
        } catch (NumberFormatException e) {
            return;
        }

        if (me.getId().equals(id)) return;

        Player p = findOrCreatePlayer(id, x, y);
        p.setX(x);
        p.setY(y);

        support.firePropertyChange("remoteMove", null, p);
    }

    private Player findOrCreatePlayer(String id, int x, int y) {

        for (Player p : players) {
            if (p.getId().equals(id)) {
                return p;
            }
        }

        Player newPlayer = new Player(
                id,
                x,
                y,
                new Color((int)(Math.random() * 0x1000000))
        );

        players.add(newPlayer);
        return newPlayer;
    }
}