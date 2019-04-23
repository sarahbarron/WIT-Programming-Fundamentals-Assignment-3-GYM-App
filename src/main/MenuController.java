import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.awt.*;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class runs the application and handles the Product I/O
 *
 * @version 3.0
 */
public class MenuController {

  private Scanner input = new Scanner(System.in);
  private GymAPI gymApi = new GymAPI();
  private HashMap<String, String> gymPackages = new HashMap<String, String>();
  private String trainerEmail;
  private String package1 = "Allowed access anytime to gym.\nFree access to all classes.\nAccess " +
                            "to all changing areas including deluxe changing rooms.";
  private String package2 = "Allowed access anytime to gym.\n€3 fee for all classes.\nAccess to all changing areas " +
                            "including deluxe changing rooms.";
  private String package3 = "Allowed access to gym at off-peak times.\n€5 fee for all classes. \nNo access " +
                            "to deluxe changing rooms.";
  private String wit = "Allowed access to gym during term time.\n€4 fee for all classes.  \nNo access to deluxe " +
                        "changing rooms.";
  private boolean trainerLoggedIn = false;

  public static void main(String[] args) {
    MenuController c = new MenuController();
  }

  public MenuController() {
    gymPackages.put("Package 1", package1);
    gymPackages.put("Package 2", package2);
    gymPackages.put("Package 3", package3);
    gymPackages.put("WIT", wit);
    runMenu();
  }

  private String wouldYouLikeToLoginOrRegisterMenu() {
    System.out.println("Would you like to Login (l), Register (r) or exit (x): ");
    try {
      String option = input.nextLine();
      return option;
    } catch (Exception e) {
      System.err.println("ERROR: login = l, register = r, exit = x");
    }
    return "";
  }

  private void runMenu() {
    String option = wouldYouLikeToLoginOrRegisterMenu();
    option = option.toLowerCase();
    String memberOrTrainer = "";
    String inputEmail;
    boolean loginValid;
    switch (option) {
      case "l":
        memberOrTrainer = areYouAMemberOrTrainerMenu();
        boolean isMember = memberOrTrainer.equalsIgnoreCase("m");
        boolean isTrainer = memberOrTrainer.equalsIgnoreCase("t");
        if (memberOrTrainer.equalsIgnoreCase("x")) {
          logout();
        } else if (isMember || isTrainer) {
          inputEmail = whatIsYourEmailMenu();
          loginValid = login(memberOrTrainer, inputEmail);
          if (loginValid && isTrainer) {
            trainerEmail = inputEmail;
            trainerLoggedIn = true;
            trainerMenu();
          }
          if(loginValid && isMember)
          {
            memberMenu();
          }
        }
        break;

      case "r":
        memberOrTrainer = areYouAMemberOrTrainerMenu();
        boolean isAMember = memberOrTrainer.equalsIgnoreCase("m");
        boolean isATrainer = memberOrTrainer.equalsIgnoreCase("t");
        boolean isRegistered = false;
        if (memberOrTrainer.equalsIgnoreCase("x")) {
          logout();
        } else if (isAMember || isATrainer) {
          isRegistered = register(memberOrTrainer);
        }
        if(isRegistered && isAMember)
        {
          memberMenu();
        }
        else if(isRegistered && isATrainer)
        {
          trainerMenu();
        }
        break;

      case "x":
        logout();

      default:
        System.out.println("Invalid input: ");
        wouldYouLikeToLoginOrRegisterMenu();
    }
  }


  private String areYouAMemberOrTrainerMenu() {
    String option = "";
    System.out.println("Are your a member (m) or trainer (t) (press x to exit)");
    try {
      option = input.nextLine();
    } catch (Exception e) {
      System.out.println("ERROR: Invalid input for member(m) or trainer(t)");
    }
    return option;
  }

  private String whatIsYourEmailMenu() {
    String option = "";
    System.out.println("What is your email Address: ");
    try {
      option = input.nextLine();
    } catch (Exception e) {
      System.err.println("ERROR: can not read email address");
    }
    return option;
  }

  private boolean login(String memberOrTrainer, String emailAddress) {
    Member member;
    Trainer trainer;
    if (memberOrTrainer.equalsIgnoreCase("m")) {
      member = gymApi.searchMembersByEmail(emailAddress);
      if (member == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        System.out.println("WELCOME BACK");
        return true;
      }
    } else if (memberOrTrainer.equalsIgnoreCase("t")) {
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


  private boolean register(String memberOrTrainer) {
    String email = "";
    String name = "";
    String address = "";
    String gender = "";
    float height = 0;
    float startWeight = 0;
    String chosenPackage = "";
    String speciality = "";
    boolean invalid = false;
    boolean registered = false;

    if (trainerLoggedIn) {
      System.out.println("PLEASE ENTER THE NEW MEMBERS DETAILS BELOW");
    } else {
      System.out.println("PLEASE ENTER YOUR DETAILS BELOW:");
    }

    do {
      try {
        System.out.println("Enter Email Address: ");
        email = input.nextLine();
        invalid = false;

        if (gymApi.searchMembersByEmail(email) != null
            || gymApi.searchTrainersByEmail(email) != null) {
          System.out.println("Sorry somebody with this email address is already registered. Try again");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: Invalid email address");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter Name:");
        name = input.nextLine();
      } catch (Exception e) {
        System.err.println("ERROR: Invalid name");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter address:");
        address = input.nextLine();

      } catch (Exception e) {
        System.err.println("ERROR: Invalid address: ");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter gender Male (m) or Female (f): ");
        gender = input.nextLine();
        if (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
          System.out.println("Invalid input: enter f for female or m for male:");
          invalid = true;
        }

      } catch (Exception e) {
        System.out.println("ERROR: invalid gender");
        invalid = true;
      }
    } while (invalid);

    if (memberOrTrainer.equalsIgnoreCase("m")) {
      do {
        try {
          invalid = false;
          System.out.println("Enter height in metres: ");
          height = input.nextFloat();

        } catch (Exception e) {
          System.err.println("ERROR: invalid height");
          input.nextLine();
          invalid = true;
        }
      } while (invalid);


      do {
        try {
          invalid = false;
          System.out.println("Enter Current Weight in kgs:");
          startWeight = input.nextFloat();
          input.nextLine();
        } catch (Exception e) {
          System.err.println("ERROR: invalid weight");
          invalid = true;
        }
      } while (invalid);

      do {
        try {
          invalid = false;
          System.out.println("Select a Package: ");
          System.out.println("----------------------------------------------------------------------");
          System.out.println("Package 1: "+package1);
          System.out.println("----------------------------------------------------------------------");
          System.out.println("Package 2: "+package2);
          System.out.println("----------------------------------------------------------------------");
          System.out.println("Package 3: "+package3);
          System.out.println("----------------------------------------------------------------------");
          System.out.println("WIT: "+wit);
          System.out.println("----------------------------------------------------------------------");

          chosenPackage = input.nextLine().trim();
          if (chosenPackage.equalsIgnoreCase("1")) {
            chosenPackage = "Package 1";
          } else if (chosenPackage.equalsIgnoreCase("2")) {
            chosenPackage = "Package 2";
          } else if (chosenPackage.equalsIgnoreCase("3")) {
            chosenPackage = "Package 3";
          }

          boolean equalsPackage1 = chosenPackage.equalsIgnoreCase("Package 1");
          boolean equalsPackage2 = chosenPackage.equalsIgnoreCase("Package 2");
          boolean equalsPackage3 = chosenPackage.equalsIgnoreCase("Package 3");
          boolean equalsWit = chosenPackage.equalsIgnoreCase("WIT");

          if (!equalsPackage1 && !equalsPackage2 && !equalsPackage3 && !equalsWit) {
            invalid = true;
          }

        } catch (Exception e) {
          System.out.println("ERROR: Invalid Package");
          invalid = true;
        }
      } while (invalid);

      Member newMember = new Member(email, name, address, gender, height, startWeight, chosenPackage);
      gymApi.addMember(newMember);

      try {
        gymApi.store();
        registered = true;

      } catch (Exception e) {
        System.err.println("ERROR: Unable to register at this time");
      }
    }// end if member statement

    else if (memberOrTrainer.equalsIgnoreCase("t")) {
      do {
        try {
          invalid = false;
          System.out.println("What is speciality: ");
          speciality = input.nextLine();
        } catch (Exception e) {
          System.out.println("ERROR: Invalid speciality");
          invalid = true;
        }
      } while (invalid);

      Trainer newTrainer = new Trainer(email, name, address, gender, speciality);
      gymApi.addTrainer(newTrainer);
      try {
        gymApi.store();
        registered = true;
      } catch (Exception e) {
        System.out.println("ERROR: Unable to register at this time");
        registered = false;
      }
    }
    return registered;
  }

  private void logout() {
    trainerLoggedIn = false;
    System.out.println("Goodbye........");
    System.exit(0);
  }
}

