/* Hangman practice project. */
import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/words.txt";
    private static final int GUESS_COUNT = 6;
    private static HashMap<Character, Tuple<Boolean, ArrayList<Integer>>> guessMap = new HashMap<>();
    private static String newWord = "";

    public static void main (String[] args) {
        runGame();
    }

    /**
     * Main execution loop.
     */
    private static void runGame() {
        // Clear the map
        guessMap.clear();

        // Choose a new word from file
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            ArrayList<String> list = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty())
                    break;
                else
                    list.add(line);
            }
            Random rand = new Random();
            newWord = list.get(rand.nextInt(list.size()));
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Prepare the guess
        for (int i = 0; i < newWord.length(); i++) {
            if (guessMap.containsKey(newWord.charAt(i))) {
                guessMap.get(newWord.charAt(i)).y.add(i);
            } else {
                ArrayList<Integer> cList = new ArrayList<>();
                cList.add(i);
                guessMap.put(newWord.charAt(i), new Tuple<>(false, cList));
            }
        }

        // Greet the player
        System.out.println("Hello, and welcome to Hangman!");

        // Main execution loop
        int guessesLeft = GUESS_COUNT;
        int correctGuesses = 0;
        Scanner scanner = new Scanner(System.in);
        boolean shouldRun = true;
        while (shouldRun) {
            // Collect and handle input
            printGuessMap();
            System.out.println("You have " + guessesLeft + " guesses left.");
            System.out.println("Take a guess!");
            String guess = scanner.next().toLowerCase();
            System.out.println();

            if (guess.matches("^[a-zA-Z]$")) {
                // Check guess against the word
                if (newWord.contains(guess)) {
                    boolean alreadyGuessed = guessMap.get(guess.charAt(0)).x;

                    if (alreadyGuessed) {
                        System.out.println("You can't guess that again!");
                        guessesLeft--;
                    } else {
                        guessMap.get(guess.charAt(0)).x = true;
                        System.out.println("Nice work!");
                        correctGuesses++;
                    }
                } else {
                    System.out.println("Your guess isn't in the word!");
                    guessesLeft--;
                }

                // End the game?
                if (correctGuesses == guessMap.size()) {
                    System.out.println("\nYou won!");
                    System.out.println("The word was: " + newWord);
                    shouldRun = false;
                } else if (guessesLeft == 0) {
                    System.out.println("\nYou lost!");
                    System.out.println("The word was: " + newWord);
                    shouldRun = false;
                }
            } else {
                System.out.println("Sorry! Please input a valid letter.");
            }
        }

        // Play again?
        System.out.println("Play again?");
        String ans = scanner.next().toLowerCase();
        while (!ans.matches("[a-z]")) {
            System.out.println("Please type 'y' or 'n'.");
            ans = scanner.next().toLowerCase();
        }
        if (ans.equals("y")) {
            runGame();
        }
    }

    /**
     * Prints out the game's Guess Map.
     */
    private static void printGuessMap() {
        Character[] mapArray = new Character[newWord.length()];
        for (Character key : guessMap.keySet()) {
            Tuple<Boolean, ArrayList<Integer>> characterInfo = guessMap.get(key);
            if (characterInfo.x) {
                // This character has been guessed
                for (Integer i : characterInfo.y) {
                    mapArray[i] = key;
                }
            } else {
                // This character has not been guessed
                for (Integer i : characterInfo.y) {
                    mapArray[i] = '_';
                }
            }
        }
        for (Character c : mapArray) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}