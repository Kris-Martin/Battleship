import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {
    final static int GRID_SIZE = 10;

    public static void main(String[] args) {

        char[][] gameGrid = newGameGrid(GRID_SIZE);
        StringBuilder board = drawBoard(GRID_SIZE, gameGrid);
        Ship[] ships = createShips();

        String greeting = "Welcome to Battleship!\n";
        String instructions = """
            Instructions:
            -------------------------------------------------------------------
            To Place your ships on the grid when prompted enter your coordinates
            in the following format: A1 A5
            The first coordinate is the head of your ship, the second the tail.
            The distance of from head to tail must equal the size of the ship.
            Horizontal and vertical placement only - no diagonals.
            """;

        System.out.printf("%s\n%s", greeting, instructions);
        System.out.println(board);

        try (Scanner scanner = new Scanner(System.in)) {
            for (Ship ship : ships) {
                placeShip(scanner, ship);
            }
        }

        System.out.println(Arrays.toString(ships));
    }

    public static void placeShip(Scanner scanner, Ship ship) {
        boolean isInputCorrect;

        do {
            String[] input = getCoordinates(scanner, ship);
            Map<String, Point> coordinates = parseCoordinates(input);
            Point headPos = new Point(coordinates.get("headPos"));
            Point tailPos = new Point(coordinates.get("tailPos"));

            if (isOutOfBounds(headPos.getX(), headPos.getY())) {
                System.out.println(ErrorType.OUT_OF_BOUNDS.message);
                isInputCorrect = false;

            } else if (isOutOfBounds(tailPos.getX(), tailPos.getY())) {
                System.out.println(ErrorType.OUT_OF_BOUNDS.message);
                isInputCorrect = false;

            } else if (headPos.getX() != tailPos.getX() && headPos.getY() != tailPos.getY()) {
                System.out.println(ErrorType.WRONG_LOCATION.message);
                isInputCorrect = false;

            } else if (Point.getDistance(headPos, tailPos) != ship.getSize()) {
                System.out.printf("%sRemember the %s is %d cells long.\n",
                        ErrorType.WRONG_LENGTH.message,
                        ship.getName(),
                        ship.getSize()
                );
                isInputCorrect = false;

            } else {
                isInputCorrect = true;
                ship.setHeadPos(headPos);
                ship.setTailPos(tailPos);
            }

        } while (!isInputCorrect);
    }

    public static String[] getCoordinates(Scanner scanner, Ship ship) {
        String input;
        boolean isCorrect;

        do {
            System.out.printf(
                    "Please enter the coordinates of the %s (%d cells): ",
                    ship.getName(),
                    ship.getSize()
            );

            input = scanner.nextLine().trim().replaceAll("[\\s,]+", " ");

            // Check if input is valid and contains only letters a-j or A-J and numbers 0-10
            if (input.length() < 5 || input.length() > 7 || input.matches("^.*[^a-jA-J0-9 ].*$")) {
               isCorrect = false;
               System.out.println(
                   """ 
                   Please enter coordinates using the letters A-J and 1-10 only,
                   with one space between the first coordinate and the second coordinate eg. A1 A5
                   """
               );
            } else {
                isCorrect = true;
            }

        } while(!isCorrect);

        return input.split(" ");
    }

    public static Map<String, Point> parseCoordinates(String[] input) {
        char startingLetter = 'A';

        int x1 = (int) input[0].toUpperCase().charAt(0) - startingLetter;
        int y1 = Integer.parseInt(input[0].substring(1)) - 1;
        int x2 = (int) input[1].toUpperCase().charAt(0) - startingLetter;
        int y2 = Integer.parseInt((input[1]).substring(1)) - 1;

        return Map.of("headPos", new Point(x1, y1), "tailPos", new Point(x2, y2));
    }

    public static boolean isOutOfBounds(int x, int y) {
        return x < 0 || x > 9 || y < 0 || y > 9;
    }

    public static char[][] newGameGrid(int gridSize) {
        char[][] gameGrid = new char[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            Arrays.fill(gameGrid[i], '~');
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

    public static Ship[] createShips() {
        Ship[] ships = new Ship[ShipType.values().length];

        int i = 0;
        for (ShipType type : ShipType.values()) {
            ships[i] = new Ship(type.name, type.size);
            i++;
        }
        return ships;
    }
}
