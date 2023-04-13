import java.util.Arrays;

public class Grid {
    public final static int SIZE = 10;

    public static char[][] newGrid() {
        char[][] gameGrid = new char[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(gameGrid[i], Cell.OCEAN.value);
        }
        return gameGrid;
    }

    public static boolean isCellAboveOrBelowFull(int x, int y, char[][] gameGrid) {
        return x != 0 && gameGrid[x - 1][y] == Cell.SHIP.value ||
                x != 9 && gameGrid[x + 1][y] == Cell.SHIP.value;
    }

    public static boolean isCellLeftOrRightFull(int x, int y, char[][] gameGrid) {
        return y != 0 && gameGrid[x][y - 1] == Cell.SHIP.value ||
                y != 9 && gameGrid[x][y + 1] == Cell.SHIP.value;
    }

}
