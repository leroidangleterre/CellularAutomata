package cellularAutomata;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author arthu
 */
public class CellularAutomata {

    public static void main(String[] args) {

        World w = new World();

        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();

        WorldPanel worldPanel = new WorldPanel(w);
        JPanel buttonPanel = new JPanel();
        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w.step();
                worldPanel.repaint();
            }
        });
        buttonPanel.add(stepButton);

        JButton timerButton = new JButton("start");
        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worldPanel.startOrStop();

                if (timerButton.getText().equals(("start"))) {
                    timerButton.setText("stop");
                } else {
                    timerButton.setText("start");
                }
            }

        });
        buttonPanel.add(timerButton);

        JButton resetZoomButton = new JButton("Set zoom to 1");
        resetZoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worldPanel.setZoom(1);
                worldPanel.repaint();
            }
        });
        buttonPanel.add(resetZoomButton);

        w.addPropertyChangeListener("currentStep", worldPanel);

        final Dimension dimension = new Dimension(1600, 1000);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(worldPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(dimension);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
