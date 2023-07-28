package NumberGuess;

/**
 * 
 * @author Aditya Poli
 *
 */

import java.util.Random;
import java.util.Scanner;

public class Round {
    private final Scanner scanner = new Scanner(System.in);
    private final Random random = new Random();
    private final int secretNumber;
    private final int maxAttempts;
    private int roundScore = 0;

    public Round(int lowerBound, int upperBound, int maxAttempts) {
        this.maxAttempts = maxAttempts;
        this.secretNumber = random.nextInt(upperBound - lowerBound + 1) + lowerBound;
    }

    public void play() {
        System.out.println("\n🎲 Welcome to the Number Guessing Game! 🎲");
        System.out.println("I have chosen a random number between 1 and 100.");
        System.out.println("You have " + maxAttempts + " attempts to guess it.");

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.print("Attempt #" + attempt + ": Enter your guess: ");
            int userGuess = scanner.nextInt();

            if (userGuess == secretNumber) {
                roundScore = calculateScore(attempt);
                System.out.println("🎉 Congratulations! You guessed the number in " + attempt + " attempts. 🎉");
                System.out.println("🏆 Round Score: " + roundScore + " | Total Score: " + roundScore + " 🏆");
                break;
            } else if (userGuess < secretNumber) {
                System.out.println("⬇️ Your guess is too low. Try again. ⬇️");
            } else {
                System.out.println("⬆️ Your guess is too high. Try again. ⬆️");
            }
        }

        if (roundScore == 0) {
            System.out.println("❌ Sorry, you've used all your attempts. The secret number was: " + secretNumber + " ❌");
            System.out.println("🏆 Round Score: 0 | Total Score: " + roundScore + " 🏆");
        }
    }

    public int getRoundScore() {
        return roundScore;
    }

    private int calculateScore(int attempts) {
        double score = (double) maxAttempts / attempts * 100;
        return (int) score;
    }
}
