import java.util.HashSet;
import java.util.Scanner;

/**
 * InputReader reads user input from the terminal.
 * The text inputted by a user is then broken into words.
 * These words are stored in a set
 */
public class InputReader {
  Scanner input;

  /**
   * Create a new InputReader
   */
  public InputReader() {
    input = new Scanner(System.in);
  }

  /**
   * Read in the user input, split the input into words and store them in a set
   * Return the set of words.
   */
  public HashSet<String> getInput() {
    System.out.print("> ");                // print prompt
    String inputLine = input.nextLine().trim().toLowerCase();

    String[] wordArray = inputLine.split(" ");  // split at spaces

    // add words from array into hashset
    HashSet<String> words = new HashSet<String>();
    for (String word : wordArray) {
      words.add(word);
    }
    return words;
  }
}
