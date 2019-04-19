import java.util.Scanner;

/**
 * This class runs the application and handles the Product I/O
 *
 * @version 3.0
 */
public class MenuController {

  private Scanner input = new Scanner(System.in);
  private GymAPI gymAPI;

  public static void main(String[] args) {
    MenuController c = new MenuController();
  }

  public MenuController() {
    gymAPI = new GymAPI();
    loginOrRegisterControl();
  }


  private int loginOrRegisterMenu() {
    System.out.println("Would you like to login(l) or register(r)");
    System.out.println("or enter x to exit:");
    System.out.print("=>");
    try {
      int option = -1;
      String enteredValue = input.nextLine();
      if (enteredValue.equalsIgnoreCase("l")) {
        option = 1;
      } else if (enteredValue.equalsIgnoreCase("r")) {
        option = 2;
      } else if (enteredValue.equalsIgnoreCase("x")) {
        System.out.println("Goodbye....");
        option = 0;
      }
      return option;
    } catch (Exception e) {
      System.err.println("Invalid option l = login, r = register or 0 = exit");
    }
    return -1;
  }

  private void loginOrRegisterControl() {

    int option = loginOrRegisterMenu();
    int memberOrTrainer = -1;
    String emailAddress;
    boolean registered;
    while (option != 0) {

      switch (option) {
//        member has chosen to login
        case 1:
          memberOrTrainer = memberOrTrainerControl();
          System.out.println("What is your email address");
          System.out.println("=>");
          emailAddress = input.nextLine();
          registered = tryToLogin(memberOrTrainer, emailAddress);

          break;


//          member has chosen to register
      case 2:
        memberOrTrainer = memberOrTrainerControl();
        System.out.println("REGISTER" + memberOrTrainer);

        break;
      default:
        System.out.println("Invalid option entered");
        break;
    }
    System.out.println("------------------");
    option = loginOrRegisterMenu();

  }
}


  private int memberOrTrainerMenu() {
    System.out.println("Are you a Member (m) or Trainer (t)");
    System.out.println("or enter x to exit:");
    System.out.print("=>");
    try {
      int option = -1;
      String enteredValue = input.nextLine();

      if (enteredValue.equalsIgnoreCase("m")) {
        option = 1;
      } else if (enteredValue.equalsIgnoreCase("t")) {
        option = 2;
      } else if (enteredValue.equalsIgnoreCase("x")) {
        System.out.println("Goodbye....");
        option = 0;
      }
      return option;
    } catch (Exception e) {
      System.err.println("Invalid option l = login, r = register or 0 = exit");
    }
    return -1;

  }

  private int memberOrTrainerControl() {
    int option = memberOrTrainerMenu();

    while (option != 0) {

      switch (option) {
//        user has chosen that they want to login or register as a member
        case 1:
          System.out.println("MEMBER");
          return 1;
//          user has chosen that they want to login or register as a Trainer
        case 2:
          System.out.println("Trainer");
          return 2;
        default:
          System.out.println("Invalid option entered");
          break;
      }
      System.out.println("------------------");
      option = memberOrTrainerMenu();

    }
    return -1;
  }

  private boolean tryToLogin(int memberOrTrainer, String emailAddress) {
    GymAPI gymApi = new GymAPI();
    Member member;
    Trainer trainer;

    if (memberOrTrainer == 1) {
      member = gymApi.searchMembersByEmail(emailAddress);
      if (member == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        System.out.println("WELCOME BACK");
        return true;
      }
    } else if (memberOrTrainer == 2) {
      trainer = gymApi.searchTrainersByEmail(emailAddress);

      if (trainer == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        System.out.println("WELCOME BACK");
        return true;
      }
    }
    return false;
  }
}