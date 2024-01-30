package cellularAutomata;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

/**
 * @author arthu
 */
public class WorldPanel extends JPanel implements MouseListener, MouseMotionListener,
        MouseWheelListener, KeyListener {

    private World w;

    private double x0 = 0, y0 = 0, zoom = 10;

    private boolean mouseWheelPressed;
    private int mouseX, mouseY;

    public WorldPanel(World newWorld) {
        w = newWorld;
        mouseWheelPressed = false;
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        // Compute the first and last visible row and column;
        // Paint a square for each cell
        int zoomApp = (int) zoom;
        for (int row = 0; row < w.getNbRows(); row++) {
            for (int col = 0; col < w.getNbCols(); col++) {
                int val = w.get(row, col);
                Color color = this.getColor(val);
                g.setColor(color);
                // Paint a square.
                int xApp = (int) (col * zoomApp + x0);
                int yApp = (int) (row * zoomApp + y0);
                g.fillRect(xApp, yApp, zoomApp, zoomApp);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            mouseWheelPressed = true;
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            mouseWheelPressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mouseWheelPressed) {
            int dx = e.getX() - mouseX;
            int dy = e.getY() - mouseY;
            x0 += dx;
            y0 += dy;
            mouseX = e.getX();
            mouseY = e.getY();
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    private Color getColor(int val) {
        switch (val) {
        case 0:
            return Color.white;
        case 1:
            return Color.black;
        case 2:
            return Color.blue;
        case 3:
            return Color.red;
        default:
            return Color.gray;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            x0 -= zoom;
            break;
        case KeyEvent.VK_RIGHT:
            x0 += zoom;
            break;
        case KeyEvent.VK_UP:
            y0 -= zoom;
            break;
        case KeyEvent.VK_DOWN:
            y0 += zoom;
            break;
        case KeyEvent.VK_ADD:
            if (zoom <= 10) {
                zoom++;
            } else {
                zoom *= 1.1;
            }
            break;
        case KeyEvent.VK_SUBTRACT:
            if (zoom <= 10) {
                zoom--;
            } else {
                zoom /= 1.1;
            }
            break;
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double fact;
        if (e.getWheelRotation() < 0) {
            fact = 1.1;
        } else {
            fact = 1 / 1.1;
        }
        x0 = e.getX() - fact * (e.getX() - x0);
        y0 = e.getY() - fact * (e.getY() - y0);
        zoom *= fact;
        repaint();
    }
}
