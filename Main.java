import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {
    final static int GRID_SIZE = 10;

    public static void main(String[] args) {

        char[][] gameGrid = newGameGrid(GRID_SIZE);
        Ship[] ships = createShips();

        String greeting = "Welcome to Battleship!\n";
        String instructions = """
            Instructions:
            -------------------------------------------------------------------
            To place your ships on the grid, when prompted enter coordinates in
            the following format: A1 A5
            Rules:
            -------------------------------------------------------------------
            The first coordinate is the head of your ship, the second the tail.
            The distance of from head to tail must equal the size of the ship.
            Horizontal and vertical placement only - no diagonals.
            """;

        System.out.printf("%s\n%s", greeting, instructions);
        System.out.println(drawBoard(GRID_SIZE, gameGrid));

        try (Scanner scanner = new Scanner(System.in)) {
            for (Ship ship : ships) {
                placeShip(scanner, ship, gameGrid);
                updateGameGrid(gameGrid, ship);
                System.out.println(drawBoard(GRID_SIZE, gameGrid));
            }
        }

//        System.out.println(Arrays.toString(ships));
    }

    public static void updateGameGrid(char[][] gameGrid, Ship ship) {
        Point headPos = ship.getHeadPos();
        Point tailPos = ship.getTailPos();
        Compass facing = ship.getFacing();

        switch (facing) {
            case WEST -> {
                for (int i = headPos.getY(); i <= tailPos.getY(); i++) {
                    gameGrid[headPos.getX()][i] = CellType.SHIP.value;
                }
            }
            case EAST -> {
                for (int i = headPos.getY(); i >= tailPos.getY(); i--) {
                    gameGrid[headPos.getX()][i] = CellType.SHIP.value;
                }
            }
            case NORTH -> {
                for (int i = headPos.getX(); i <= tailPos.getX(); i++) {
                    gameGrid[i][headPos.getY()] = CellType.SHIP.value;
                }
            }
            case SOUTH -> {
                for (int i = headPos.getX(); i >= tailPos.getX(); i--) {
                    gameGrid[i][headPos.getY()] = CellType.SHIP.value;
                }
            }
        }
    }

    public static Compass getFacing(Point headPos, Point tailPos) {

        if (headPos.getX() == tailPos.getX() && headPos.getY() < tailPos.getY()) {
            return Compass.WEST;
        } else if (headPos.getX() == tailPos.getX() && headPos.getY() > tailPos.getY()) {
            return Compass.EAST;
        } else if (headPos.getY() == tailPos.getY() && headPos.getX() < tailPos.getX()) {
            return Compass.NORTH;
        } else {
            return Compass.SOUTH;
        }
    }

    public static void placeShip(Scanner scanner, Ship ship, char[][] gameGrid) {
        boolean isInputCorrect;

        do {
            String[] input = getCoordinates(scanner, ship);
            Map<String, Point> coordinates = parseCoordinates(input);
            Point headPos = new Point(coordinates.get("headPos"));
            Point tailPos = new Point(coordinates.get("tailPos"));

            if (isOutOfBounds(headPos.getX(), headPos.getY()) ||
                isOutOfBounds(tailPos.getX(), tailPos.getY())
            ) {
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

            } else if (isOverlapping(headPos, tailPos, gameGrid)) {
                System.out.println(ErrorType.OVERLAPPING.message);
                isInputCorrect = false;

            } else if (isTooClose(headPos, tailPos, gameGrid)) {
                System.out.println(ErrorType.TOO_CLOSE.message);
                isInputCorrect = false;

            } else {
                isInputCorrect = true;
                ship.setHeadPos(headPos);
                ship.setTailPos(tailPos);
                ship.setFacing(getFacing(headPos, tailPos));
            }

        } while (!isInputCorrect);
    }

    public static boolean isOverlapping(Point headPos, Point tailPos, char[][] gameGrid) {
        Compass facing = getFacing(headPos, tailPos);

        switch (facing) {
            case WEST -> {
                for (int i = headPos.getY(); i <= tailPos.getY(); i++) {
                    if (gameGrid[headPos.getX()][i] == CellType.SHIP.value) {
                        return true;
                    }
                }
            }
            case EAST -> {
                for (int i = headPos.getY(); i >= tailPos.getY(); i--) {
                    if (gameGrid[headPos.getX()][i] == CellType.SHIP.value) {
                        return true;
                    }
                }
            }
            case NORTH -> {
                for (int i = headPos.getX(); i <= tailPos.getX(); i++) {
                    if (gameGrid[i][headPos.getY()] == CellType.SHIP.value) {
                        return true;
                    }
                }
            }
            case SOUTH -> {
                for (int i = headPos.getX(); i >= tailPos.getX(); i--) {
                    if (gameGrid[i][headPos.getY()] == CellType.SHIP.value) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isTooClose(Point headPos, Point tailPos, char[][] gameGrid) {
        Compass facing = getFacing(headPos, tailPos);

        switch (facing) {
            case WEST -> {
                if (isCellLeftOrRightFull(headPos.getX(), headPos.getY(), gameGrid)) return true;
                if (isCellLeftOrRightFull(tailPos.getX(), tailPos.getY(), gameGrid)) return true;
                for (int y = headPos.getY(); y <= tailPos.getY(); y++) {
                    if (isCellAboveOrBelowFull(headPos.getX(), y, gameGrid)) return true;
                }
            }
            case EAST -> {
                if (isCellLeftOrRightFull(headPos.getX(), headPos.getY(), gameGrid)) return true;
                if (isCellLeftOrRightFull(tailPos.getX(), tailPos.getY(), gameGrid)) return true;
                for (int y = headPos.getY(); y >= tailPos.getY(); y--) {
                    if (isCellAboveOrBelowFull(headPos.getX(), y, gameGrid)) return true;
                }
            }
            case NORTH -> {
                if (isCellAboveOrBelowFull(headPos.getX(), headPos.getY(), gameGrid)) return true;
                if (isCellAboveOrBelowFull(tailPos.getX(), tailPos.getY(), gameGrid)) return true;
                for (int x = headPos.getX(); x <= tailPos.getX(); x++) {
                    if (isCellLeftOrRightFull(x, headPos.getY(), gameGrid)) return true;
                }
            }
            case SOUTH -> {
                if (isCellAboveOrBelowFull(headPos.getX(), headPos.getX(), gameGrid)) return true;
                if (isCellAboveOrBelowFull(tailPos.getX(), tailPos.getX(), gameGrid)) return true;
                for (int x = headPos.getX(); x >= tailPos.getX(); x--) {
                    if (isCellLeftOrRightFull(x, headPos.getY(), gameGrid)) return true;
                }
            }
        }
        return false;
    }

    public static boolean isCellAboveOrBelowFull(int x, int y, char[][] gameGrid) {
        return x != 0 && gameGrid[x - 1][y] == CellType.SHIP.value ||
               x != 9 && gameGrid[x + 1][y] == CellType.SHIP.value;
    }

    public static boolean isCellLeftOrRightFull(int x, int y, char[][] gameGrid) {
        return y != 0 && gameGrid[x][y - 1] == CellType.SHIP.value ||
               y != 9 && gameGrid[x][y + 1] == CellType.SHIP.value;
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
        final char startingLetter = 'A';

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
            Arrays.fill(gameGrid[i], CellType.OCEAN.value);
        }
        return gameGrid;
    }

    public static StringBuilder drawBoard(int gridSize, char[][] gameGrid) {
        StringBuilder board = new StringBuilder();
        board.append("\n  ");
        for (int i = 1; i <= gridSize; i++) {
            board.append(i).append(" ");
        }
        board.append("\n");
        char letter = 'A';
        for (int i = 0; i < gridSize; i++) {
            board.append(letter++).append(" ");
            for (int j = 0; j < gridSize; j++) {
                board.append(gameGrid[i][j]).append(" ");
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
