import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        final int GRID_SIZE = 10;

        char[][] gameGrid = newGameGrid(GRID_SIZE);
        StringBuilder board = drawBoard(GRID_SIZE, gameGrid);

        System.out.println(board);
        System.out.println("Welcome to Battleship!\n");

        String instructions = """
                When prompted enter your coordinates in the following format: A1 A5
                The first coordinate is the head of your ship, the second the tail.
                Your tail must be exactly the length of your ship away from the head.
                Your ships must be placed on the horizontal or the vertical - no diagonals.
                """;
        System.out.println(instructions);

        System.out.printf(
                "Please enter the coordinates of the %s (%d cells): ",
                ShipType.AIRCRAFT_CARRIER.name,
                ShipType.AIRCRAFT_CARRIER.size
        );

        Scanner scanner = new Scanner(System.in);
        String[] input = scanner.nextLine().trim().split(" ");
        String outOfBoundsErrorMsg = "Error! You must enter a row between A and J and " +
                "a column between 1 and 10 for both coordinates.";

        char startingLetter = 'A';
        int x1 = (int) input[0].toUpperCase().charAt(0) - startingLetter;
        int y1 = Integer.parseInt(input[0].substring(1)) - 1;
        if (isOutOfBounds(x1, y1)) System.out.println(outOfBoundsErrorMsg);
        Point headPos = new Point(x1, y1);

        int x2 = (int) input[1].toUpperCase().charAt(0) - startingLetter;
        int y2 = Integer.parseInt((input[1]).substring(1)) - 1;
        if (isOutOfBounds(x2, y2)) System.out.println(outOfBoundsErrorMsg);
        Point tailPos = new Point(x2, y2);

        if (Point.lengthBetweenPoints(headPos, tailPos) != ShipType.AIRCRAFT_CARRIER.size) {
            System.out.printf(
                    "Error! Wrong length of the %s! Tail is too far from head. Please try again.\n",
                    ShipType.AIRCRAFT_CARRIER.name
            );
        }
        System.out.println(Point.lengthBetweenPoints(headPos, tailPos));
        System.out.printf("Head: %s Tail: %s", headPos, tailPos);
    }

    public static boolean isOutOfBounds(int x, int y) {
        return x < 0 || x > 9 || y < 0 || y > 9;
    }

    public static char[][] newGameGrid(int gridSize) {
        char[][] gameGrid = new char[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            Arrays.fill(gameGrid[i], '~');
            // System.out.println(Arrays.toString(gameGrid[i]));
        }
        return gameGrid;
    }

    public static StringBuilder drawBoard(int gridSize, char[][] gameGrid) {
        StringBuilder board = new StringBuilder();
        board.append("\n  ");
        for (int i = 1; i <= gridSize; i++) {
            board.append(i);
            board.append(" ");
        }
        board.append("\n");
        char letter = 'A';
        for (int i = 0; i < gridSize; i++) {
            board.append(letter++);
            board.append(" ");
            for (int j = 0; j < gridSize; j++) {
                board.append(gameGrid[i][j]);
                board.append(" ");
            }
            board.append("\n");
        }
        return board;
    }
}
