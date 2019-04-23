import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.awt.*;
import java.util.ArrayList;
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
    runWouldYouLikeToLoginOrRegisterMenu();
  }

  private String wouldYouLikeToLoginOrRegisterMenu() {
    String option="";
    boolean invalid = false;
    do {
      try {
        System.out.println("Would you like to Login (l), Register (r) or exit (x): ");
        option = input.nextLine().trim();
        invalid = false;
        boolean loginRequested = option.equalsIgnoreCase("l");
        boolean registerRequested = option.equalsIgnoreCase("r");
        boolean exitRequested = option.equalsIgnoreCase("x");

        if(!loginRequested && !registerRequested && !exitRequested)
        {
          System.out.println("Invalid input for login input l, for register input r and for exit input x");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: login = l, register = r, exit = x");
      }
    }while(invalid);
    return option;
  }

  private void runWouldYouLikeToLoginOrRegisterMenu() {
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
            Trainer loggedInTrainer = gymApi.searchTrainersByEmail(inputEmail);
            trainerLoggedIn = true;
            System.out.println("TRAINER \nWELCOME BACK "+ loggedInTrainer.getName());
            runTrainerMenu(loggedInTrainer);
          }
          if(loginValid && isMember)
          {
            Member loggedInMember = gymApi.searchMembersByEmail(inputEmail);
            System.out.println("MEMBER \nWELCOME BACK "+loggedInMember.getName());
            runMemberMenu(loggedInMember);
          }
        }
        break;

      case "r":
        memberOrTrainer = areYouAMemberOrTrainerMenu();
        boolean isAMember = memberOrTrainer.equalsIgnoreCase("m");
        boolean isATrainer = memberOrTrainer.equalsIgnoreCase("t");
        String registeredEmail="";
        if (memberOrTrainer.equalsIgnoreCase("x")) {
          logout();
        } else if (isAMember || isATrainer) {
          registeredEmail = register(memberOrTrainer);
        }
        if(!registeredEmail.equals(null) && isAMember)
        {
          Member member = gymApi.searchMembersByEmail(registeredEmail);
          System.out.println("MEMBER \nWELCOME "+member.getName());
          runMemberMenu(member);
        }
        else if(!registeredEmail.equals(null) && isATrainer)
        {
          Trainer trainer = gymApi.searchTrainersByEmail(registeredEmail);
          System.out.println("TRAINER \nWELCOME "+trainer.getName());
          runTrainerMenu(trainer);
        }
        break;

      case "x":
        logout();

      default:
        System.out.println("Invalid input: ");
        runWouldYouLikeToLoginOrRegisterMenu();
    }
    runWouldYouLikeToLoginOrRegisterMenu();
  }


  private String areYouAMemberOrTrainerMenu() {
    String option = "";
    boolean invalid = false;

    do {
      try {
        System.out.println("Are your a member (m) or a trainer (t) (press x to exit)");
        invalid = false;
        option = input.nextLine().trim();
        boolean isAMember = option.equalsIgnoreCase("m");
        boolean isATrainer = option.equalsIgnoreCase("t");
        boolean exit = option.equalsIgnoreCase("x");
        if(exit)
        {
          logout();
        }
        else if(!isAMember && !isATrainer)
        {
          System.out.println("Invalid input for member input m for trainer input t and for exit input x");
          invalid = true;
        }
      } catch (Exception e) {
        System.out.println("ERROR: Invalid input for member(m) or trainer(t)");
      }
    }while(invalid);
    return option;
  }

  private String whatIsYourEmailMenu() {
    String option = "";
    System.out.println("What is your email Address: ");
    try {
      option = input.nextLine().trim();
    } catch (Exception e) {
      System.err.println("ERROR: can not read email address");
    }
    return option;
  }

  private boolean login(String memberOrTrainer, String emailAddress) {
    Member member;
    Trainer trainer;
    boolean personIsAMember = memberOrTrainer.equalsIgnoreCase("m");
    boolean personIsATrainer = memberOrTrainer.equalsIgnoreCase("t");
    if (personIsAMember) {
      member = gymApi.searchMembersByEmail(emailAddress);
      if (member == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        System.out.println("WELCOME BACK");
        return true;
      }
    } else if (personIsATrainer) {
      trainer = gymApi.searchTrainersByEmail(emailAddress);
      if (trainer == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        System.out.println("WELCOME BACK");
        trainerLoggedIn = true;
        return true;
      }
    }
    return false;
  }


  private String register(String memberOrTrainer) {
    String email = "";
    String name = "";
    String address = "";
    String gender = "";
    float height = 0;
    float startWeight = 0;
    String chosenPackage = "";
    String speciality = "";
    boolean invalid = false;


    if (trainerLoggedIn) {
      System.out.println("PLEASE ENTER THE NEW MEMBERS DETAILS BELOW");
    } else {
      System.out.println("PLEASE ENTER YOUR DETAILS BELOW:");
    }

    do {
      try {
        System.out.println("Enter Email Address: ");
        email = input.nextLine().trim();
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
        name = input.nextLine().trim();
      } catch (Exception e) {
        System.err.println("ERROR: Invalid name");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter address:");
        address = input.nextLine().trim();

      } catch (Exception e) {
        System.err.println("ERROR: Invalid address: ");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter gender Male (m) or Female (f): ");
        gender = input.nextLine().trim();
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
          input.nextLine();
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
            System.err.println("Invalid option to select Package 1 input 'Package 1' or the number 1, " +
                "\nfor Package 2 input 'Package 2' or the number 2, \nfor Package 3 input 'Package 3' or the number" +
                "3, \nand for the student WIT package input 'WIT'");
            System.out.println("----------------------------------------------------------------------");

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
      } catch (Exception e) {
        System.out.println("ERROR: Unable to register at this time");
      }
      if(!trainerLoggedIn){
        trainerLoggedIn = true;
      }
    }
    return email;
  }

  private int trainerMenu() {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("Enter the menu number you wish to select: ");
        System.out.println(" 1. Add a new member. ");
        System.out.println(" 2. List all members.");
        System.out.println(" 3. Search for a member by email.");
        System.out.println(" 4. Assessment sub menu.");
        System.out.println(" 0. Exit");
        System.out.print("=>");

        invalid = false;
        option = input.nextInt();
        input.nextLine();
        if(option<0 && option>4)
        {
          invalid = true;
        }
      }catch (Exception e){
        invalid = true;
        System.err.println("ERROR: your must enter a number between 0 and 4");
      }
    }while(invalid);
    return option;
  }


  private void runTrainerMenu(Trainer loggedInTrainer){
    int option = trainerMenu();
  while(option !=0) {
      switch (option) {
        case 1:
          register("m");
          break;
        case 2:
          ArrayList<Member> members = gymApi.getMembers();

          for(int i=0; i<members.size(); i++) {
            Member member = members.get(i);
            System.out.println(member);
            System.out.println("----------------------------------------------------" +
                "-------------------------------------------------------------------");
          }
          break;
        case 3:
          System.out.println("Input the members email address:");
          String email = input.nextLine();
          Member member = gymApi.searchMembersByEmail(email);
          System.out.println(member);
          break;
      }

      System.out.println("\nPress any key to continue");
      input.nextLine();
      option = trainerMenu();
    }
    logout();
  }


  public int memberMenu()
  {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("Enter the menu number you wish to select: ");
        System.out.println(" 1. View your Profile. ");
        System.out.println(" 2. Update your profile");
        System.out.println(" 3. Progress sub menu");
        System.out.println(" 0. Exit");
        System.out.print("=>");

        invalid = false;
        option = input.nextInt();
        if(option<0 && option>3)
        {
          invalid = true;
        }
      }catch (Exception e){
        invalid = true;
        System.err.println("ERROR: your must enter a number between 0 and 3");
      }
    }while(invalid);
    return option;
  }


  private void runMemberMenu(Member loggedInMember){
    int option = memberMenu();

    while(option !=0) {
      switch (option) {

        case 1:
          try{
            System.out.println("YOUR PROFILE "+loggedInMember.getName().toUpperCase());
            System.out.println(loggedInMember);
          }catch (Exception e){
            System.err.println("ERROR: Unable to view members profile.");
          }
          break;

        case 2:
          System.out.println(option);
          try{
            updateProfile(loggedInMember);
          }catch (Exception e)
          {
            System.err.println("ERROR: Unable to update your profile");
          }
          break;
      }

      System.out.println("\nPress any key to continue");
      input.nextLine();
      input.nextLine();
      option = memberMenu();
    }
    logout();
  }

  private void updateProfile(Member loggedInMember)
  {
    String email = "";
    String name = "";
    String address = "";
    String gender = "";
    float height = -1;
    float startWeight = -1;
    String chosenPackage = "";
    boolean invalid = false;


    System.out.println("UPDATE PROFILE : " +loggedInMember);
    do {
      try {
        invalid = false;
        System.out.println("Enter Name:");
        name = input.nextLine().trim();
      } catch (Exception e) {
        System.err.println("ERROR: Invalid name");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter address:");
        address = input.nextLine().trim();

      } catch (Exception e) {
        System.err.println("ERROR: Invalid address: ");
        invalid = true;
      }
    } while (invalid);

    do {
      try {
        invalid = false;
        System.out.println("Enter gender Male (m) or Female (f): ");
        gender = input.nextLine().trim();
        if (!gender.equalsIgnoreCase("m") && !gender.equalsIgnoreCase("f")) {
          System.out.println("Invalid input: enter f for female or m for male:");
          invalid = true;
        }

      } catch (Exception e) {
        System.out.println("ERROR: invalid gender");
        invalid = true;
      }
    } while (invalid);

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
          input.nextLine();
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
            System.err.println("Invalid option to select Package 1 input 'Package 1' or the number 1, " +
                "\nfor Package 2 input 'Package 2' or the number 2, \nfor Package 3 input 'Package 3' or the number" +
                "3, \nand for the student WIT package input 'WIT'");
            System.out.println("----------------------------------------------------------------------");

            invalid = true;
          }

        } catch (Exception e) {
          System.out.println("ERROR: Invalid Package");
          invalid = true;
        }
      } while (invalid);


        loggedInMember.setName(name);
        loggedInMember.setAddress(address);
        loggedInMember.setGender(gender);
        loggedInMember.setHeight(height);
        loggedInMember.setStartWeight(startWeight);
        loggedInMember.setChosenPackage(chosenPackage);


      try {
        gymApi.store();

      } catch (Exception e) {
        System.err.println("ERROR: Unable to register at this time");
      }

  }

  private void logout() {
    try {
      gymApi.store();
    }catch (Exception e){
      System.err.println("ERROR: Unable to save GymAPI before logout");
    }
      trainerLoggedIn = false;
      System.out.println("Goodbye........");
      System.exit(0);

  }
}

