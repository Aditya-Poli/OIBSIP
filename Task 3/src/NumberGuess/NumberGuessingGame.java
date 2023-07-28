package NumberGuess;

/**
 * 
 * @author Aditya Poli
 *
 */

import java.util.Scanner;

public class NumberGuessingGame {
    private final Scanner scanner = new Scanner(System.in);
    private final int lowerBound = 1;
    private final int upperBound = 100;
    private final int maxAttempts = 10;
    private int totalScore = 0;

    public void start() {
        System.out.println("Welcome to the Number Guessing Game!");

        boolean playAgain = true;
        while (playAgain) {
            Round round = new Round(lowerBound, upperBound, maxAttempts);
            round.play();
            totalScore += round.getRoundScore();

            System.out.print("Play again? (yes/no): ");
            String playAgainInput = scanner.next();
            playAgain = playAgainInput.equalsIgnoreCase("yes");
        }

        System.out.println("🙏 Thank you for playing the Number Guessing Game! Final Score: " + totalScore);
        scanner.close();
    }
}

