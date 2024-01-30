package cellularAutomata;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author arthu
 */
public class World {

    private int[][] tab;
    private int[][] nextTab;
    private int width, height;

    private double initialProbability;

    long delay = 0;
    long period = 250;
    boolean isRunning = false;
    Timer timer;
    int currentStep;

    // Tell observers that our state has changed.
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    enum worldType {
        CONWAY,
        ROCKPAPERSCISSORS
    }

    private worldType currentWorldType;

    public World() {
        width = 1000;
        height = 1000;

        currentWorldType = worldType.CONWAY;

        initialProbability = 0.5;

        // Create a 2d array
        tab = new int[height][];
        nextTab = new int[height][];

        for (int row = 0; row < height; row++) {
            tab[row] = new int[width];
            nextTab[row] = new int[width];
            for (int col = 0; col < width; col++) {
                if (new Random().nextDouble() < initialProbability) {
                    tab[row][col] = 1;
                } else {
                    tab[row][col] = 0;
                }
                nextTab[row][col] = 0;
            }
        }

        currentStep = 0;
    }

    public int get(int line, int col) {
        try {
            return tab[line][col];
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    public void set(int row, int col, int newValue) {
        try {
            nextTab[row][col] = newValue;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Trying to set inexistent row " + row + ", col " + col);
        }
    }

    int getNbRows() {
        return height;
    }

    int getNbCols() {
        return width;
    }

    public void startOrStop() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isRunning) {
                        step();
                    }
                }
            }, delay, period);
        }
        if (isRunning) {
            // Stop the timer.
            isRunning = false;
            System.out.println("Timer stopped.");
        } else {
            isRunning = true;
            System.out.println("Timer started.");
        }
    }

    public void step() {
        currentStep++;

        resetNextTab();

        switch (currentWorldType) {
        case CONWAY:
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    int nbActiveNeighbors = countNeighbors(row, col);

                    if (get(row, col) == 0 && nbActiveNeighbors == 3) {
                        // dead  with 3 neighbors -> alive
                        set(row, col, 1);
                    } else if (get(row, col) == 1 && (nbActiveNeighbors == 2 || nbActiveNeighbors == 3)) {
                        // alive with 2 or 3 neigbors -> alive
                        set(row, col, 1);
                    } else {
                        // default -> dead
                        set(row, col, 0);
                    }
                }
            }
            System.out.println("Step " + currentStep
                    + ", density: " + getDensityAsPercentage() + " %");
            break;
        case ROCKPAPERSCISSORS:
            break;
        }
        copyNewTabToCurrent();
        resetNextTab();
        support.firePropertyChange("currentStep", currentStep - 1, currentStep);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(propertyName, listener);
    }

    private int countNeighbors(int row, int col) {
        int count = 0;

        for (int rowN = row - 1; rowN <= row + 1; rowN++) {
            for (int colN = col - 1; colN <= col + 1; colN++) {
                // Do not compare a cell to itself:
                if (rowN != row || colN != col) {
                    if (get(rowN, colN) == 1) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void copyNewTabToCurrent() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                tab[row][col] = nextTab[row][col];
            }
        }
    }

    public void resetNextTab() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                nextTab[row][col] = 0;
            }
        }
    }

    private double getDensity() {
        int nbAvailableCells = width * height;
        int nbActiveCells = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (get(row, col) == 1) {
                    nbActiveCells++;
                }
            }
        }
        return (double) nbActiveCells / (double) nbAvailableCells;
    }

    private double getDensityAsPercentage() {
        double d = getDensity();

        int dX10000 = (int) (d * 10000);
        double res = (double) dX10000 / 100;
        return res;
    }
}
