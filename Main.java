import java.util.*;

public class Main {
    public static void main(String[] args) {

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

        // Create players
        Player player1 = new Player();
        Player player2 = new Player();

        // Print greeting, game instructions and draw board
        System.out.printf("%s\n%s", greeting, instructions);

        try (Scanner scanner = new Scanner(System.in)) {
            // Player setup
            player1.setupPlayerGrid(scanner);

            // Start game
            System.out.println("The game starts!");
            Board.draw(player1.fogOfWar);

            // Get user input for shot coordinate
            System.out.println("Take a shot!");
            Point shot;

            while (player1.shipsSunk != player1.ships.length) {
                shot = getShot(scanner);

                // Check if shot has already been taken
                if (player1.shotsTaken.contains(shot.toString())) {
                    Board.draw(player1.fogOfWar);
                    System.out.print("You have already taken that shot. Try again: ");

                // Check if shot hit a ship
                } else if (player1.field[shot.getX()][shot.getY()] == Cell.SHIP.value) {
                    player1.field[shot.getX()][shot.getY()] = Cell.HIT.value;
                    player1.fogOfWar[shot.getX()][shot.getY()] = Cell.HIT.value;
                    Board.draw(player1.fogOfWar);

                    // Iterate through ships to find the one that was hit
                    for (Ship ship : player1.ships) {
                        if (ship.getCells().containsKey(shot.toString())) {
                            // Set cell hit to true
                            ship.getCells().replace(shot.toString(), true);
                            // Check if ship is sunk
                            if (ship.isSunk() && player1.shipsSunk < player1.ships.length) {
                                // Update ships sunk
                                player1.shipsSunk++;
                                // Only print if not last ship
                                if (player1.shipsSunk < player1.ships.length) {
                                    System.out.print("You sank a ship! Specify a new target: ");
                                }
                            // If not sunk, print hit message
                            } else if (player1.shipsSunk < player1.ships.length) {
                                System.out.print("You hit a ship! Try again: ");
                            }
                            break;
                        }
                    }
                // If shot missed
                } else {
                    // Update field
                    player1.field[shot.getX()][shot.getY()] = Cell.MISS.value;
                    // Update fog of war view
                    player1.fogOfWar[shot.getX()][shot.getY()] = Cell.MISS.value;
                    Board.draw(player1.fogOfWar);
                    System.out.print("You missed! Try again: ");
                }
                // Add shot to shots taken
                player1.shotsTaken.add(shot.toString());
            }
            // If all ships are sunk, print win message
            System.out.println("You sank the last ship. You won. Congratulations!");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Point getShot(Scanner scanner) {
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
