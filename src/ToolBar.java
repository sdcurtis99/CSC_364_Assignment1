import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;


public class ToolBar extends JPanel implements ActionListener {

    public ToolBar() {
        JLabel gTitle = new JLabel("Grid Count:");
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
