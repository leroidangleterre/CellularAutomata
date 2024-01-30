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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;

/**
 * @author arthu
 */
public class WorldPanel extends JPanel implements MouseListener, MouseMotionListener,
        MouseWheelListener, KeyListener, PropertyChangeListener {

    private World w;

    private double x0 = 0, y0 = 0, zoom = 10;

    private boolean mouseWheelPressed;
    private int mouseX, mouseY;

    public WorldPanel(World newWorld) {
        w = newWorld;
        mouseWheelPressed = false;
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {

        // Background
        g.setColor(Color.white);
        g.fillRect(0, 0, g.getClipBounds().width, g.getClipBounds().height);

        // Compute the first and last visible row and column;
        // Paint a square for each cell
        for (int row = 0; row < w.getNbRows(); row++) {
            for (int col = 0; col < w.getNbCols(); col++) {
                int val = w.get(row, col);
                Color color = this.getColor(val);
                g.setColor(color);
                // Paint a square.
                int xApp = (int) (col * zoom + x0);
                int yApp = (int) (row * zoom + y0);

                // Avoid 1-pixel blank between rows or columns
                int xAppNext = (int) ((col + 1) * zoom + x0);
                int yAppNext = (int) ((row + 1) * zoom + y0);
                int cellAppSize = Math.max(xAppNext - xApp, yAppNext - yApp);

                g.fillRect(xApp, yApp, cellAppSize, cellAppSize);
            }
        }
        // Borders
        g.setColor(Color.black);
        g.drawRect((int) x0, (int) y0, (int) (w.getNbCols() * zoom), (int) (w.getNbRows() * zoom));
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
                setZoom(zoom + 1);
            } else {
                setZoom(zoom * 1.1);
            }
            break;
        case KeyEvent.VK_SUBTRACT:
            if (zoom <= 10) {
                setZoom(zoom - 1);
            } else {
                setZoom(zoom / 1.1);
            }
            break;
        case KeyEvent.VK_NUMPAD0:
            setZoom(1);
            x0 = 0;
            y0 = 0;
            repaint();
            break;
        }
        repaint();
    }

    public void setZoom(double newZoom) {
        if (newZoom > 0) {
            zoom = newZoom;
        }
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
        setZoom(zoom * fact);
        repaint();
    }

    public void startOrStop() {
        w.startOrStop();
        repaint();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /* Récupère l'objet source */
        World w = (World) evt.getSource();

        /* Récupère l'ancienne et la nouvelle valeur */
        //        System.out.println("WorldPanel: la valeur " + evt.getPropertyName() + " est passée de " + evt.getOldValue() + " à " + evt.getNewValue());
        repaint();
    }

}
