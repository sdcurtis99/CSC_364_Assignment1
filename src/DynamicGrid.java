import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DynamicGrid extends JPanel implements PropertyChangeListener{

    private JLabel cells[][] =  new JLabel[10][10];
    private boolean running = true;
    private boolean paused = false;
    private boolean finished = false;


    // Bind this callback function to input from JComboBox changes number of grids in GridHouse
    public void setGridSize(int rows, int columns) {
        removeAll();
        setLayout( new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                JLabel cell = new JLabel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cells[i][j] = cell;
                cells[i][j].setVisible(true);
                cells[i][j].setOpaque(true);
                add(cell);
            }
        }
        repaint();
    }

    public DynamicGrid(int rows, int cols) {
        setGridSize(rows, cols);
    }


    public void setRunning (boolean state) {
        this.running = state;
    }

    public void setPaused (boolean state) {
        this.paused = state;
    }

    public void setFinished(boolean state) {
        this.finished = state;
    }

    public boolean getRunning () {
        return this.running;
    }

    public boolean getPaused () {
        return this.paused;
    }

    public boolean getFinished() {
        return this.finished;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
