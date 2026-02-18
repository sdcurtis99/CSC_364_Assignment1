package javiergs.mqtt;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class Main extends JFrame {

    public Main() {

        setTitle("MQTT Multiplayer Game - sdcurtis99");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        WorldPanel worldPanel = new WorldPanel();
        worldPanel.setPreferredSize(new Dimension(500, 400));

        add(worldPanel, BorderLayout.CENTER);

        addKeyListener(new MyKeyListener());

        Blackboard.getInstance().addPropertyChangeListener(worldPanel);

        pack();
        setVisible(true);
        requestFocusInWindow();
    }

    public static void main(String[] args) {

        String playerId = (args.length > 0)
                ? (args[0] + "_" + UUID.randomUUID())
                : UUID.randomUUID().toString();

        Player me = new Player(playerId, 40, 40, Color.BLUE);
        Blackboard.getInstance().setMe(me);

        new Publisher();
        new Subscriber();

        SwingUtilities.invokeLater(Main::new);
    }
}