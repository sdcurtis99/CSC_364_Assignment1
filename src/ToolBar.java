import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;


public class ToolBar extends JPanel implements ActionListener {

    public ToolBar() {
        JLabel gTitle = new JLabel("Grid Count:");
        Integer[] gridCounts = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};
        JComboBox<Integer> gridDropDown = new JComboBox<>(gridCounts);
        JRadioButton startRadio =  new JRadioButton("Start");
        JRadioButton resetRadio =  new JRadioButton("Reset");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(startRadio);
        buttonGroup.add(resetRadio);
        add(startRadio);
        add(resetRadio);
        startRadio.addActionListener(this);
        resetRadio.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("temp");
    }


}
