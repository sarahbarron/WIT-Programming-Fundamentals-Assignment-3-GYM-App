import java.util.HashSet;

/**
 * Gym Support System Class
 * The support system takes in user input via the terminal and responds to that input
 * This class uses an instance of the Responder class to generate responses.
 * It contains a loop that repeatedly reads input and generates
 * output until the users wants to leave by typing bye.
 */
public class SupportSystem {
  private InputReader reader;
  private Responder responder;

  /**
   * Creates a technical support system.
   */
  public SupportSystem() {
    reader = new InputReader();
    responder = new Responder();
  }

  /**
   * Start the technical support system. This will print a welcome message and enter
   * into a dialog with the user, until the user ends the dialog.
   */
  public void startSupport() {

    printWelcome();
    HashSet<String> input = reader.getInput();
    while (!input.contains("bye")) {

      String response = responder.generateResponse(input);
      System.out.println(response);

      input = reader.getInput();
    }
    printSupportGoodbye();
  }


  /**
   * Print a welcome message to the screen.
   */
  private void printWelcome() {
    System.out.println("Welcome to your online Gym Support System.");
    System.out.println();
    System.out.println("If at any stage you want to exit the Gym Support System, type 'bye'");
    System.out.println("How can I help you today?");
  }

  /**
   * Print a good-bye message to the screen.
   */
  private void printSupportGoodbye() {
    System.out.println("Nice talking to you today, I look forward to seeing you at the Gym soon. Goodbye...");
  }
}
