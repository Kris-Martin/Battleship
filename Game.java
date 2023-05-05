import java.util.Scanner;

public class Game {

    public static boolean isGameOver(Player player1, Player player2) {
        return Game.isWon(player1) || Game.isWon(player2);
    }

    private static boolean isWon(Player player) {
        return player.shipsSunk == player.ships.length;
    }

    public static void setup(Scanner scanner, Player player1, Player player2) {
        player1.setupPlayerGrid(scanner);
        nextTurn(scanner);

        player2.setupPlayerGrid(scanner);
        nextTurn(scanner);
    }

    private static void nextTurn(Scanner scanner) {
        System.out.println("Press Enter and pass the move to another player.");
        String nextPlayer = scanner.nextLine().strip();
    }

    public static void process(Scanner scanner, Player currentPlayer, Player otherPlayer) {
        drawPlayerBoard(currentPlayer);
        System.out.printf("Player %d, it's your turn: ", currentPlayer.playerNumber);
        Point shot = getShot(scanner);

        // Check if shot has already been taken
        if (currentPlayer.shotsTaken.contains(shot.toString())) {
            System.out.print("You have already taken that shot. Try again: ");

        // Check if shot hit a ship
        } else if (otherPlayer.field[shot.getX()][shot.getY()] == Cell.SHIP.value) {
            otherPlayer.field[shot.getX()][shot.getY()] = Cell.HIT.value;
            currentPlayer.fogOfWar[shot.getX()][shot.getY()] = Cell.HIT.value;

            // Iterate through ships to find the one that was hit
            for (Ship ship : otherPlayer.ships) {
                if (ship.getCells().containsKey(shot.toString())) {
                    ship.getCells().replace(shot.toString(), true);

                    if (ship.isSunk() && otherPlayer.shipsSunk < otherPlayer.ships.length) {
                        otherPlayer.shipsSunk++;

                        // Only print if not last ship
                        if (otherPlayer.shipsSunk < otherPlayer.ships.length) {
                            System.out.println("You sank a ship!");
                            nextTurn(scanner);
                        }

                    // If not sunk, print hit message
                    } else if (otherPlayer.shipsSunk < otherPlayer.ships.length) {
                        System.out.println("You hit a ship!");
                        nextTurn(scanner);
                    }
                    break;
                }
            }

        } else { // If shot missed
            otherPlayer.field[shot.getX()][shot.getY()] = Cell.MISS.value;
            currentPlayer.fogOfWar[shot.getX()][shot.getY()] = Cell.MISS.value;
            System.out.println("You missed!");
            nextTurn(scanner);
        }
        currentPlayer.shotsTaken.add(shot.toString());
    }

    private static void drawPlayerBoard(Player player) {
        Board.draw(player.fogOfWar);
        System.out.println("---------------------");
        Board.draw(player.field);
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
