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
        width = 800;
        height = 800;

        currentWorldType = worldType.ROCKPAPERSCISSORS;

        initialProbability = 0.5;

        // Create a 2d array
        tab = new int[height][];
        nextTab = new int[height][];

        for (int row = 0; row < height; row++) {
            tab[row] = new int[width];
            nextTab[row] = new int[width];
            for (int col = 0; col < width; col++) {
                if (currentWorldType.equals(worldType.CONWAY)) {
                    setupValuesConway(row, col);
                } else if (currentWorldType.equals(worldType.ROCKPAPERSCISSORS)) {
                    setupValuesRockPaperScissors(row, col);
                }
            }
        }

        currentStep = 0;
    }

    private void setupValuesConway(int row, int col) {
        if (new Random().nextDouble() < initialProbability) {
            tab[row][col] = 1;
        } else {
            tab[row][col] = 0;
        }
        nextTab[row][col] = 0;
    }

    private void setupValuesRockPaperScissors(int row, int col) {
        double randomValue = new Random().nextDouble();
        if (randomValue < 0.333) {;
            tab[row][col] = 0;
        } else if (randomValue < 0.666) {
            tab[row][col] = 1;
        } else {
            tab[row][col] = 2;
        }
        nextTab[row][col] = 0;
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
                    setValueConway(row, col);
                }
            }
            System.out.println("Step " + currentStep
                    + ", density: " + getDensityAsPercentage() + " %");
            break;
        case ROCKPAPERSCISSORS:
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    setValueRockPaperScissors(row, col);
                }
            }
            System.out.println("Step " + currentStep + ": " + getRPSDensities());
            break;
        }
        copyNewTabToCurrent();
        resetNextTab();
        support.firePropertyChange("currentStep", currentStep - 1, currentStep);
    }

    /**
     * Compute the cell's next value based on the Conway rules.
     *
     * @param row
     * @param col
     */
    private void setValueConway(int row, int col) {
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

    /**
     * Compute the cell's next value based on the RockPaperScissors rules.
     *
     * @param row
     * @param col
     */
    private void setValueRockPaperScissors(int row, int col) {
        int currentVal = get(row, col);
        int strongerVal = (currentVal + 1) % 3; // The value that beats the current cell

        int nbOfStrongerNeighbors = 0;
        for (int rowN = row - 1; rowN <= row + 1; rowN++) {
            for (int colN = col - 1; colN <= col + 1; colN++) {

                // If at least one neighbor is stronger, we change to that stronger value.
                if (get(rowN, colN) == strongerVal) {
                    nbOfStrongerNeighbors++;
                }
            }
        }
        if (nbOfStrongerNeighbors >= 3) {
            // Cell switches to stronger value
            set(row, col, strongerVal);
        } else {
            // Cell remains unchanged
            set(row, col, currentVal);
        }
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

    private String getRPSDensities() {
        int r = 0, g = 0, b = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                switch (get(row, col)) {
                case 0:
                    r++;
                    break;
                case 1:
                    g++;
                    break;
                case 2:
                    b++;
                    break;
                default:
                    break;
                }
            }
        }
        double total = width * height;
        int rPercentage = (int) (r / total * 100);
        int gPercentage = (int) (g / total * 100);
        int bPercentage = (int) (b / total * 100);
        return rPercentage + ", " + gPercentage + ", " + bPercentage;
    }
}
