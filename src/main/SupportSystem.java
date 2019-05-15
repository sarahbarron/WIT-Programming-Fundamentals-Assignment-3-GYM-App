import java.util.HashSet;

/*
Gym Support System Class
The support system takes in user input via the terminal and responds to that input
This class uses an instance of the Responder class to generate responses.
It contains a loop that reads input and generates output until the users wants to leave by typing 'bye'.
*/
public class SupportSystem {
  private InputReader reader;
  private Responder responder;


  // Constructor method for the gym support system.
  public SupportSystem() {
    reader = new InputReader();
    responder = new Responder();
  }

  /*
  The startSupport method, starts the gym support system.
  This prints a welcome message and prompts the user for input.
  There is a loop that prompts the user for input, the responder will will return a response and this response will
  be printed. This continues until the user inputs 'bye'
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

  //Prints a welcome message to the terminal.
  private void printWelcome() {

    System.out.println("Welcome to your online Gym Support System.");
    System.out.println();
    System.out.println("If at any stage you want to exit the Gym Support System, type 'bye'");
    System.out.println("How can I help you today?");
  }

  // Prints a leaving/goodbye message to the terminal
  private void printSupportGoodbye() {

    System.out.println("Nice talking to you today, I look forward to seeing you at the Gym soon. Goodbye...");
  }
}
