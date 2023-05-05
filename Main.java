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
            Game.setup(scanner, player1, player2);
            System.out.println("The game starts!");
            Player currentPlayer = player1;
            Player otherPlayer = player2;

            do {
                Game.process(scanner, currentPlayer, otherPlayer);

                if (!Game.isGameOver(player1, player2)) {
                    if (currentPlayer.equals(player1)) {
                        currentPlayer = player2;
                        otherPlayer = player1;
                    } else {
                        currentPlayer = player1;
                        otherPlayer = player2;
                    }
                }

            } while (!Game.isGameOver(player1, player2));

            // If all ships are sunk, print win message
            System.out.printf("You sank the last ship. You won. Congratulations Player %d!%n",
                              currentPlayer.playerNumber);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
