public class Board {
    private final static int GRID_SIZE = Grid.SIZE;

    public static void draw(char[][] gameGrid) {
        final StringBuilder board = new StringBuilder();
        board.append("\n  ");
        for (int i = 1; i <= GRID_SIZE; i++) {
            board.append(i).append(" ");
        }
        board.append("\n");
        char letter = 'A';
        for (int i = 0; i < GRID_SIZE; i++) {
            board.append(letter++).append(" ");
            for (int j = 0; j < GRID_SIZE; j++) {
                board.append(gameGrid[i][j]).append(" ");
            }
            board.append("\n");
        }
        System.out.println(board);
    }
}
