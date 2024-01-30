package cellularAutomata;

import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author arthu
 */
public class CellularAutomata {

    public static void main(String[] args) {

        World w = new World();

        JFrame frame = new JFrame();
        WorldPanel panel = new WorldPanel(w);

        final Dimension dimension = new Dimension(800, 600);

        frame.setSize(dimension);
        frame.setContentPane(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
