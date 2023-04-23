import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        char[][] field = Grid.newGrid();
        char[][] fogOfWar = Grid.newGrid();
        Ship[] ships = createShips();

        final String greeting = "Welcome to Battleship!\n";
        final String instructions = """
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

        // Print greeting, game instructions and draw board
        System.out.printf("%s\n%s", greeting, instructions);
        drawBoard(field);

        // Place ships according to user input, update grid and draw board
        try (Scanner scanner = new Scanner(System.in)) {
            for (Ship ship : ships) {
                getShipPosition(scanner, ship, field);
                updateGameGrid(field, ship);
                drawBoard(field);
            }

            // Start game
            System.out.println("The game starts!");
            drawBoard(fogOfWar);

            // Get user input for shot coordinate
            Point shot = getShot(scanner);

            // Check if shot hit a ship
            if (field[shot.getX()][shot.getY()] == Cell.SHIP.value) {
                // Update field
                field[shot.getX()][shot.getY()] = Cell.HIT.value;
                // Update fog of war view
                fogOfWar[shot.getX()][shot.getY()] = Cell.HIT.value;
                drawBoard(fogOfWar);
                System.out.println("You hit a ship!");
                drawBoard(field);
            } else {
                // Update field
                field[shot.getX()][shot.getY()] = Cell.MISS.value;
                // Update fog of war view
                fogOfWar[shot.getX()][shot.getY()] = Cell.MISS.value;
                drawBoard(fogOfWar);
                System.out.println("You missed!");
                drawBoard(field);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
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

    public static void drawBoard(char[][] gameGrid) {
        final int gridSize = Grid.SIZE;
        final StringBuilder board = new StringBuilder();
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
        System.out.println(board);
    }

    public static String[] getCoordinates(Scanner scanner, Ship ship) {
        String input;
        boolean isCorrect;

        do {
            System.out.printf("Please enter the coordinates of the %s (%d cells): ",
                ship.getName(),
                ship.getSize()
            );

            input = scanner.nextLine().trim().replaceAll("[\\s,]+", " ");

            // Is valid if contains letters a-j or A-J and numbers 0-10 and is 5-7 chars
            if (!input.matches("^[a-jA-J0-9 ]{5,7}$")) {
                isCorrect = false;
                String message = """ 
                    Please enter coordinates using the letters A-J and 1-10 only,
                    with one space between the first coordinate and the second coordinate eg. A1 A5
                    """;
                System.out.println(message);
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

    public static void getShipPosition(Scanner scanner, Ship ship, char[][] gameGrid) {
        String[] input;
        Map<String, Point> coordinates;
        Point headPos;
        Point tailPos;

        do {
            input = getCoordinates(scanner, ship);
            coordinates = parseCoordinates(input);
            headPos = new Point(coordinates.get("headPos"));
            tailPos = new Point(coordinates.get("tailPos"));

        } while (Error.isPresent(headPos, tailPos, ship, gameGrid));

        ship.setPosition(headPos, tailPos);
    }

    public static void updateGameGrid(char[][] gameGrid, Ship ship) {
        Point headPos = ship.getHeadPos();
        Point tailPos = ship.getTailPos();
        Compass facing = ship.getFacing();

        switch (facing) {
            case WEST -> {
                for (int i = headPos.getY(); i <= tailPos.getY(); i++) {
                    gameGrid[headPos.getX()][i] = Cell.SHIP.value;
                }
            }
            case EAST -> {
                for (int i = headPos.getY(); i >= tailPos.getY(); i--) {
                    gameGrid[headPos.getX()][i] = Cell.SHIP.value;
                }
            }
            case NORTH -> {
                for (int i = headPos.getX(); i <= tailPos.getX(); i++) {
                    gameGrid[i][headPos.getY()] = Cell.SHIP.value;
                }
            }
            case SOUTH -> {
                for (int i = headPos.getX(); i >= tailPos.getX(); i--) {
                    gameGrid[i][headPos.getY()] = Cell.SHIP.value;
                }
            }
        }
    }

    private static Point getShot(Scanner scanner) {
        System.out.println("Take a shot!");
        String input = scanner.nextLine().trim();
        while (!input.matches("^[a-jA-J0-9]{2,3}$") ||
               Integer.parseInt(input.substring(1)) > 10
        ) {
            System.out.println("Error! Please enter a valid coordinate. Try again:");
            input = scanner.nextLine().trim();
        }

        // Convert user input to Point
        final char startingLetter = 'A';
        int x = Character.toUpperCase(input.charAt(0)) - startingLetter;
        int y = Integer.parseInt(input.substring(1)) - 1;
        return new Point(x, y);
    }
}
