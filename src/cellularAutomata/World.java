package cellularAutomata;

import java.util.Random;

/**
 *
 * @author arthu
 */
public class World {

    private int[][] tab;
    private int[][] nextTab;
    private int width, height;

    enum worldType {
        CONWAY,
        ROCKPAPERSCISSORS
    }

    private worldType currentWorldType;

    public World() {
        width = 200;
        height = 100;

        currentWorldType = worldType.CONWAY;

        // Create a 2d array
        tab = new int[height][];
        nextTab = new int[height][];

        for (int row = 0; row < height; row++) {
            tab[row] = new int[width];
            nextTab[row] = new int[width];
            for (int col = 0; col < width; col++) {
                tab[row][col] = new Random().nextInt(2);
                nextTab[row][col] = 0;
            }
        }
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

    public void step() {

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
            break;
        case ROCKPAPERSCISSORS:
            break;
        }
        copyNewTabToCurrent();
        resetNextTab();
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
}
