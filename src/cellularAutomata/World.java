package cellularAutomata;

import java.util.Random;

/**
 *
 * @author arthu
 */
public class World {

    private int[][] tab;
    private int width, height;

    public World() {
        width = 50;
        height = 30;

        // Create a 2d array
        tab = new int[height][];
        for (int row = 0; row < height; row++) {
            tab[row] = new int[width];
            for (int col = 0; col < width; col++) {
                tab[row][col] = new Random().nextInt(2);
            }
        }
    }

    public int get(int line, int col) {
        try {
            return tab[line][col];
        } catch (NullPointerException e) {
            return -1;
        }
    }

    int getNbRows() {
        return height;
    }

    int getNbCols() {
        return width;
    }
}
