import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Player {
    static int playerCount = 0;
    final int playerNumber;
    final char[][] field;
    final char[][] fogOfWar;
    final Ship[] ships;
    final Set<String> shotsTaken;
    int shipsSunk;

    public Player() {
        field = Grid.newGrid();
        fogOfWar = Grid.newGrid();
        ships = createShips();
        shotsTaken = new HashSet<>();
        shipsSunk = 0;
        playerCount++;
        playerNumber = playerCount;
    }

    private Ship[] createShips() {
        Ship[] ships = new Ship[ShipType.values().length];

        int i = 0;
        for (ShipType type : ShipType.values()) {
            ships[i] = new Ship(type.name, type.size);
            i++;
        }
        return ships;
    }

    public void setupPlayerGrid(Scanner scanner) {
        // Draw board
        System.out.printf("%nPlayer %d, place your ships on the game field.%n", playerNumber);
        Board.draw(field);

        // Place ships according to user input, update grid and draw board
        for (Ship ship : ships) {
            getShipPosition(scanner, ship);
            placeShipOnGrid(ship);
            Board.draw(field);
        }
    }

    private void getShipPosition(Scanner scanner, Ship ship) {
        String[] input;
        Map<String, Point> coordinates;
        Point headPos;
        Point tailPos;

        do {
            input = getCoordinates(scanner, ship);
            coordinates = parseCoordinates(input);
            headPos = new Point(coordinates.get("headPos"));
            tailPos = new Point(coordinates.get("tailPos"));

        } while (Error.isPresent(headPos, tailPos, ship, field));

        ship.setPosition(headPos, tailPos);
    }

    private String[] getCoordinates(Scanner scanner, Ship ship) {
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

    private Map<String, Point> parseCoordinates(String[] input) {
        final char startingLetter = 'A';

        int x1 = (int) input[0].toUpperCase().charAt(0) - startingLetter;
        int y1 = Integer.parseInt(input[0].substring(1)) - 1;
        int x2 = (int) input[1].toUpperCase().charAt(0) - startingLetter;
        int y2 = Integer.parseInt((input[1]).substring(1)) - 1;

        return Map.of("headPos", new Point(x1, y1), "tailPos", new Point(x2, y2));
    }

    private void placeShipOnGrid(Ship ship) {
        Point headPos = ship.getHeadPos();
        Point tailPos = ship.getTailPos();
        Compass facing = ship.getFacing();

        switch (facing) {
            case WEST -> {
                for (int i = headPos.getY(); i <= tailPos.getY(); i++) {
                    field[headPos.getX()][i] = Cell.SHIP.value;
                    ship.addCell(new Point(headPos.getX(), i).toString(), false);
                }
            }
            case EAST -> {
                for (int i = headPos.getY(); i >= tailPos.getY(); i--) {
                    field[headPos.getX()][i] = Cell.SHIP.value;
                    ship.addCell(new Point(headPos.getX(), i).toString(), false);
                }
            }
            case NORTH -> {
                for (int i = headPos.getX(); i <= tailPos.getX(); i++) {
                    field[i][headPos.getY()] = Cell.SHIP.value;
                    ship.addCell(new Point(i, headPos.getY()).toString(), false);
                }
            }
            case SOUTH -> {
                for (int i = headPos.getX(); i >= tailPos.getX(); i--) {
                    field[i][headPos.getY()] = Cell.SHIP.value;
                    ship.addCell(new Point(i, headPos.getY()).toString(), false);
                }
            }
        }
    }
}
