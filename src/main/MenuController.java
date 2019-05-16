import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/*
This class is the main menu controller for the gym application
*/
public class MenuController{

  private Scanner input = new Scanner(System.in);
  private GymAPI gymApi = new GymAPI();
  private HashMap<String, String> gymPackages = new HashMap<String, String>();
  private Member loggedInMember;
  private Trainer loggedInTrainer;
  private String package1 = "Allowed access anytime to gym.\nFree access to all classes.\nAccess " +
      "to all changing areas including deluxe changing rooms.";
  private String package2 = "Allowed access anytime to gym.\n€3 fee for all classes.\nAccess to all changing areas " +
      "including deluxe changing rooms.";
  private String package3 = "Allowed access to gym at off-peak times.\n€5 fee for all classes. \nNo access " +
      "to deluxe changing rooms.";
  private String wit = "Allowed access to gym during term time.\n€4 fee for all classes.  \nNo access to deluxe " +
      "changing rooms.";
  private boolean trainerLoggedIn = false;

  // Starts the menuController
  public static void main(String[] args) {
    MenuController c = new MenuController();
  }

  /*
  Loads the gymApi data from the members-and-trainers.xml file.
  Puts the packages key and value into the gymPackages HashMap and
  calls the runWouldYouLikeToLoginOrRegisterMenu method
   */
  public MenuController() {

    try {
      gymApi.load();
    } catch (Exception e) {
      System.err.println("Unable to load from xml file" + e);
    }
    gymPackages.put("Package 1", package1);
    gymPackages.put("Package 2", package2);
    gymPackages.put("Package 3", package3);
    gymPackages.put("WIT", wit);
    runWouldYouLikeToLoginOrRegisterMenu();
  }

  /*
  Method to show the menu asking the user if they want to login or register. The user must enter l to login or r to
  register or x to exit. The method waits for user input. If the user input is either l, r or x the response is returned
  otherwise the user is asked again if they would like to login, register or exit.
  */
  private String wouldYouLikeToLoginOrRegisterMenu() {
    String option = "";
    boolean invalid = false;
    do {
      try {
        System.out.println("Would you like to Login (l), Register (r) or exit (x): ");
        option = input.nextLine().trim();
        invalid = false;
        boolean loginRequested = option.equalsIgnoreCase("l");
        boolean registerRequested = option.equalsIgnoreCase("r");
        boolean exitRequested = option.equalsIgnoreCase("x");

        if (!loginRequested && !registerRequested && !exitRequested) {
          System.out.println("Invalid input for login input l, for register input r and for exit input x");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: login = l, register = r, exit = x");
      }
    } while (invalid);
    return option;
  }

  /*
  Method to run the menu asking a member if they want to login or register. The method waits for a response.
  and completes an action based on the response
  */
  private void runWouldYouLikeToLoginOrRegisterMenu() {
    String option = wouldYouLikeToLoginOrRegisterMenu();
    option = option.toLowerCase();
    String memberOrTrainer = "";
    String inputEmail;
    boolean loginValid;
    switch (option) {

      /*
      If the user has inputted l, they have chosen to login
      the user is asked if they are a member or trainer.
      Ask the user to input their email.
      Check if the user is registered. if the user is registered run the appropriate menu for either member or trainer
      */
      case "l":
        memberOrTrainer = areYouAMemberOrTrainerMenu();

        // if the memberOrTrainer method returns m - set isMember to true otherwise set to false
        boolean isMember = memberOrTrainer.equalsIgnoreCase("m");
        // if the memberOrTrainer method return t - set isTrainer to true otherwise set to false
        boolean isTrainer = memberOrTrainer.equalsIgnoreCase("t");

        // if the memberOrTrainer return x. exit the program
        if (memberOrTrainer.equalsIgnoreCase("x")) {
          exit();
        }

        // If the user has stated they are a member or trainer ask them their email address and try to login the member
        else if (isMember || isTrainer) {
          inputEmail = whatIsYourEmailMenu();
          loginValid = login(memberOrTrainer, inputEmail);

          // if the member is a trainer, print a welcome message and call the runTrainerMenu method
          if (loginValid && isTrainer) {
            loggedInTrainer = gymApi.searchTrainersByEmail(inputEmail);
            trainerLoggedIn = true;
            System.out.println("\n************************************** WELCOME BACK "
                + loggedInTrainer.getName().toUpperCase()
                + "**************************************\n");
            runTrainerMenu();
          }

          // if the member is a member, print a welcome message and call the runMemberMenu method
          if (loginValid && isMember) {
            loggedInMember = gymApi.searchMembersByEmail(inputEmail);
            System.out.println("\n************************************** WELCOME BACK "
                + loggedInMember.getName().toUpperCase()
                + "**************************************\n");
            runMemberMenu();
          }
        }
        break;

      /*
      If the user has inputted r, they have chosen to register
      Ask the user if they are a member or trainer.
      Try to register the user
      */
      case "r":
        memberOrTrainer = areYouAMemberOrTrainerMenu();
        boolean isAMember = memberOrTrainer.equalsIgnoreCase("m");
        boolean isATrainer = memberOrTrainer.equalsIgnoreCase("t");
        String registeredEmail = "";

        // If the user has inputted x exit the program
        if (memberOrTrainer.equalsIgnoreCase("x")) {
          exit();
        }

        /*
        Otherwise if they stated they are a member or trainer call the register method which will return the
        users email address which is needed to get the members details later
        */
        else if (isAMember || isATrainer) {
          registeredEmail = register(memberOrTrainer);
        }

        // If the user is a member  print a welcome message and call the runMemberMenu method
        if (!registeredEmail.equals(null) && isAMember) {
          loggedInMember = gymApi.searchMembersByEmail(registeredEmail);
          System.out.println("\n************************************** WELCOME "
              + loggedInMember.getName().toUpperCase()
              + "**************************************\n");
          runMemberMenu();
        }
        // Otherwise if the user is a trainer print a welcome message and call the runTrainerMenu method
        else if (!registeredEmail.equals(null) && isATrainer) {
          loggedInTrainer = gymApi.searchTrainersByEmail(registeredEmail);
          System.out.println("\n************************************** WELCOME "
              + loggedInTrainer.getName().toUpperCase()
              + "**************************************\n");
          runTrainerMenu();
        }
        break;

      // if the user inputted x, exit the system
      case "x":
        exit();

      /*
      The default response if the member hasn't inputted l,r or x. Print invalid input and
      call the runWouldYouLikeToLoginOrRegisterMenu method asking the user if they want to login or register again.
      */
      default:
        System.out.println("Invalid input: ");
        runWouldYouLikeToLoginOrRegisterMenu();
    }
    runWouldYouLikeToLoginOrRegisterMenu();
  }

  /*
  Method to print the menu asking the user if they are a member or trainer. The method expects an input of m for
  member, t for trainer or x for exit. If user inputs anything other than those 3 responses they will be asked the
  same question again. Otherwise the response is returned.
  */
  private String areYouAMemberOrTrainerMenu() {
    String option = "";
    boolean invalid = false;

    // Ask the user are they a member or trainer and accept user input
    do {
      try {
        System.out.println("Are your a member (m) or a trainer (t) (enter x to exit)");
        invalid = false;
        option = input.nextLine().trim();
        boolean isAMember = option.equalsIgnoreCase("m");
        boolean isATrainer = option.equalsIgnoreCase("t");
        boolean exit = option.equalsIgnoreCase("x");
        if (exit) {
          exit();
        } else if (!isAMember && !isATrainer) {
          System.out.println("Invalid input for member input m for trainer input t and for exit input x");
          invalid = true;
        }
      } catch (Exception e) {
        System.out.println("ERROR: Invalid input for member(m) or trainer(t)");
      }
    } while (invalid);
    return option;
  }

  // Method asking the user to input their email address. The user input for the email address is returned
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

  // Method to login a user, it accepts a string either 'm' for member or 't' for trainer and the users email address
  private boolean login(String memberOrTrainer, String emailAddress) {
    Member member;
    Trainer trainer;
    // if memberOrTrainer is 'm' set to true otherwise set to false
    boolean personIsAMember = memberOrTrainer.equalsIgnoreCase("m");
    // if memberOrTrainer is 't' set to true otherwise set to false
    boolean personIsATrainer = memberOrTrainer.equalsIgnoreCase("t");

    /*
    If the user trying to login is a member search the list of members email  addresses, if the email address
    is not on the list print 'access denied' and return false. Otherwise return true.
    */
    if (personIsAMember) {
      member = gymApi.searchMembersByEmail(emailAddress);
      if (member == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        return true;
      }
    }
    /*
    Otherwise if the user trying to login is a trainer search the list of trainers email addresses, if the email address
    is not on the list print 'access denied' and return false. otherwise return true.
     */
    else if (personIsATrainer) {
      trainer = gymApi.searchTrainersByEmail(emailAddress);
      if (trainer == null) {
        System.out.println("ACCESS DENIED");
        return false;
      } else {
        trainerLoggedIn = true;
        return true;
      }
    }
    return false;
  }

  /*
  Register method accepts a string either 'm' for member or 't' for trainer
  The user is asked for their details. If the user is a member (m) a Member object is created using the user input
  or if the user is a trainer (t) a Trainer object is created using the user input.
   */
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
    int studentId = 0;

    if (trainerLoggedIn) {
      System.out.println("\n--PLEASE ENTER THE NEW MEMBERS DETAILS BELOW  -----------------------------------------\n");
    } else {
      System.out.println("\n--PLEASE ENTER YOUR DETAILS BELOW: ----------------------------------------------------\n");
    }

    // Ask the user for their email address and accept input from the user
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

    // Ask the user for their name and accept input from the user
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

    // Ask the user for their address and accept input from the user
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

    // Ask the user for their gender and accept input from the user
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

    // If the user wants to register as a member ask the user these extra questions
    if (memberOrTrainer.equalsIgnoreCase("m")) {

      // Ask the user for their height and accept user input
      do {
        try {
          invalid = false;
          System.out.println("Enter height in metres:");
          height = input.nextFloat();
          if (height < 1 || height > 3) {
            System.out.println("This is an invalid height. Your height must be between 1 and 3 metres");
            invalid = true;
          }

        } catch (Exception e) {
          System.err.println("ERROR: invalid height");
          input.nextLine();
          invalid = true;
        }
      } while (invalid);

      // Ask the user for their weight and accept user input
      do {
        try {
          invalid = false;
          System.out.println("Enter Current Weight in kgs:");
          startWeight = input.nextFloat();
          input.nextLine();
          if (startWeight < 35 || startWeight > 250) {
            System.out.println("This is an invalid weight. Your weight must be between 35 and 250");
            invalid = true;
          }
        } catch (Exception e) {
          System.err.println("ERROR: invalid weight");
          input.nextLine();
          invalid = true;
        }
      } while (invalid);

      // Ask the user what gym package they want and accept user input
      do {
        try {
          invalid = false;
          System.out.println("Select a Package: ");
          System.out.println("----------------------------------------------------------------------");
          System.out.println("Package 1: " + package1);
          System.out.println("----------------------------------------------------------------------");
          System.out.println("Package 2: " + package2);
          System.out.println("----------------------------------------------------------------------");
          System.out.println("Package 3: " + package3);
          System.out.println("----------------------------------------------------------------------");
          System.out.println("WIT: " + wit);
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

          // If the user has chosen the WIT student package ask the user for their student Id and accept user input
          if (equalsWit) {
            do {
              try {
                System.out.println("What is your student Id");
                studentId = input.nextInt();
              } catch (Exception e) {
                System.out.println("ERROR: Invalid student ID");
                invalid = true;
              }
            } while (invalid);
          }

          // If the user has not chosen a valid package print an invalid statement and set invalid to true
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

      Member newMember;
      // If the member has chosen the WIT student package create a student member
      if (chosenPackage.equalsIgnoreCase("wit")) {
        newMember = new StudentMember(email, name, address, gender, height, startWeight, chosenPackage, studentId, "WIT");
      }
      // Otherwise create a Premium Member
      else {
        newMember = new PremiumMember(email, name, address, gender, height, startWeight, chosenPackage);
      }
      // Add the member to the GymApi
      gymApi.addMember(newMember);

      // store the new member to the members-and-trainers.xml file
      try {
        gymApi.store();
      } catch (Exception e) {
        System.err.println("ERROR: Unable to register at this time");
      }
    }

    // Otherwise if it is a trainer that is registering ask the trainer their speciality
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

      // Create a trainer object with the user input and add the member to the GymApi
      Trainer newTrainer = new Trainer(email, name, address, gender, speciality);
      gymApi.addTrainer(newTrainer);
      // Store the new trainer in the members-and-trainers.xml file
      try {
        gymApi.store();
      } catch (Exception e) {
        System.out.println("ERROR: Unable to register at this time");
      }
      if (!trainerLoggedIn) {
        trainerLoggedIn = true;
      }
    }
    // return the members email address
    return email;
  }

  /*
  When a trainer is logged in show the following Menu and wait for user input of a number that corresponds to
  a menu option. Once a legitimate number has been entered return the number
  */
  private int trainerMenu() {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("\n--TRAINER MENU ----------------------------------------------------------------------\n");
        System.out.println("Enter the menu number you wish to select: ");
        System.out.println(" 1. Add a new member. ");
        System.out.println(" 2. List all members.");
        System.out.println(" 3. Search for a member by email.");
        System.out.println(" 4. Search for a member by name.");
        System.out.println(" 5. List all member with an ideal weight");
        System.out.println(" 6. List members in a certain BMI Category");
        System.out.println(" 7. List member details imperially and metrically");
        System.out.println(" 8. Assessment sub menu.");
        System.out.println(" 0. Exit");
        System.out.print("=>");

        invalid = false;
        option = input.nextInt();
        input.nextLine();

        if (option < 0 || option > 8) {
          System.out.println("Invalid input please enter a number between 0 and 8");
          invalid = true;
        }
      } catch (Exception e) {
        invalid = true;
        System.err.println("ERROR: your must enter a number between 0 and 8");
        input.nextLine();
      }
    } while (invalid);
    return option;
  }

  /*
  Method to run the trainer Menu and wait for user input to be returned. Complete the action that corresponds
  to the returned case number.
   */
  private void runTrainerMenu() {
    int option = trainerMenu();
    while (option != 0) {
      switch (option) {

        // Register a member
        case 1:
          register("m");
          break;

        // Print a list of members
        case 2:
          ArrayList<Member> members = gymApi.listMembers();

          for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            System.out.println(member);
            System.out.println("----------------------------------------------------" +
                "-------------------------------------------------------------------");
          }
          break;

        // search for a member using their email address and print their details if they exist
        case 3:
          System.out.println("Input the members email address:");
          String email = input.nextLine();
          Member member = gymApi.searchMembersByEmail(email);
          if (member != null) {
            System.out.println(member);
          } else {
            System.out.println("No member registered with email address: " + email);
          }
          break;

        // Search for a member using their name and print their details if they exist
        case 4:
          System.out.println("Input the members name:");
          String name = input.nextLine();
          ArrayList<String> memberByNames = gymApi.searchMembersByName(name);
          if(memberByNames.size()>0) {
            for (int i = 0; i < memberByNames.size(); i++) {
              String memberByName = memberByNames.get(i);
              System.out.println(memberByName);
            }
          }
          else
          {
            System.out.println("There are no members with this name");
          }
          break;

        // List members that have an ideal body weight
        case 5:
          ArrayList<Member> listOfMembersWithIdealWeight = gymApi.listMembersWithIdealWeight();
          if (listOfMembersWithIdealWeight.size() <= 0) {
            System.out.println("There are no members with an ideal body weight");
          } else {
            System.out.println(listOfMembersWithIdealWeight.size());
            for (int i = 0; i < listOfMembersWithIdealWeight.size(); i++) {
              System.out.println("-------------------------------------------------------------------------------------" +
                  "-------------------------------------------------------------------------------------");
              System.out.println(listOfMembersWithIdealWeight.get(i));

            }
          }
          break;

        // list members that belong to a certain BMI category
        case 6:
          System.out.println("Enter the name of the BMI category you wish to find " +
              "\n 'SEVERELY UNDERWEIGHT', 'UNDERWEIGHT', 'NORMAL', 'OVERWEIGHT', 'MODERATELY OBESE' or 'SEVERELY OBESE' " +
              "\n =>");
          String bmiCategory = input.nextLine();
          ArrayList<Member> listOfMembersInBmiCategory = gymApi.listMembersBySpecificBMICategory(bmiCategory);
          int numberOfMemberInBmiCategory = listOfMembersInBmiCategory.size();

          if (numberOfMemberInBmiCategory <= 0) {
            System.out.println("There is no members in that category");
          } else {
            for (int i = 0; i < numberOfMemberInBmiCategory; i++) {
              System.out.println(listOfMembersInBmiCategory.get(i));
            }
          }
          break;

        // Print a list of members and their weight and height imperially and metrically
        case 7:
          String imperialAndMetricDetails = gymApi.listMemberDetailsImperialAndMetric();
          System.out.println(imperialAndMetricDetails);
          break;

        // Run the the trainers assessment sub menu
        case 8:
          runTrainerAssessmentSubMenu();
          break;

        default:
          System.out.println("Invalid menu option");
      }

      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      option = trainerMenu();
    }
    exit();
  }

  /*
  When a member is logged in show the following Menu and wait for user input of a number that corresponds to
  a menu option. Once a legitimate number has been entered return the number
  */
  public int memberMenu() {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("\n--MEMBER MENU -----------------------------------------------------------------------\n");
        System.out.println("Enter the menu number you wish to select: ");
        System.out.println(" 1. View your Profile. ");
        System.out.println(" 2. Update your profile");
        System.out.println(" 3. Find a trainer by email address");
        System.out.println(" 4. Find a trainer by name");
        System.out.println(" 5. Progress sub menu");
        System.out.println(" 6. Gym Support System");
        System.out.println(" 0. Exit");
        System.out.print("=>");

        invalid = false;
        option = input.nextInt();
        input.nextLine();
        if (option < 0 && option > 6) {
          System.out.println("Invalid input please enter a number between 0 and 6");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: your must enter a number between 0 and 6");
      }
    } while (invalid);
    return option;
  }

  /*
  Method to run the Member Menu and wait for user input to be returned. Complete the action that corresponds
  to the returned number
   */
  public void runMemberMenu() {
    int option = memberMenu();
    while (option != 0) {
      switch (option) {

        // Print the members profile information
        case 1:
          try {
            System.out.println("YOUR PROFILE " + loggedInMember.getName().toUpperCase());
            System.out.println(loggedInMember);
          } catch (Exception e) {
            System.err.println("ERROR: Unable to view members profile.");
          }
          break;

        // Call the update profile method
        case 2:
          try {
            updateProfile();
          } catch (Exception e) {
            System.err.println("ERROR: Unable to update your profile");
          }
          break;

        // search for a trainer using their email address and print their details if they exist
        case 3:
          System.out.println("Input the trainers email address:");
          String email = input.nextLine();
          Trainer trainer = gymApi.searchTrainersByEmail(email);
          if (trainer != null) {
            System.out.println(trainer);
          } else {
            System.out.println("No trainer registered with email address: " + email);
          }
          break;

        // Search for a trainer using their name and print their details if they exist
        case 4:
          System.out.println("Input the trainers name:");
          String name = input.nextLine();
          ArrayList<String> trainersByName = gymApi.searchTrainersByName(name);
          if(trainersByName.size()>0) {
            for (int i = 0; i < trainersByName.size(); i++) {
              String trainerByName = trainersByName.get(i);
              System.out.println(trainerByName);
            }
          }
          else
          {
            System.out.println("There are no trainers with this name");
          }
          break;

        // Call the members progress sub menu
        case 5:
          runMemberProgressSubMenu();

          // Create a Support System and call the startSupport method
        case 6:
          SupportSystem support = new SupportSystem();
          support.startSupport();
          break;
        default:
          System.out.println("Invalid menu option");
      }

      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      option = memberMenu();
    }
    exit();
  }

  /*
  Update Profile method asks the user to update their profile (name, address, gender). The new information is then
  updated and stored for the logged in member
  */
  private void updateProfile() {

    String name = "";
    String address = "";
    String gender = "";
    boolean invalid = false;

    System.out.println("\n--UPDATE PROFILE ------------------------------------------------------------------------\n");
    // Ask the user for their name and accept user input
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

    // Ask the user for their address and accept user input
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

    // Ask the user for their gender and accept user input
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

    // Set the name, address and gender for the logged in member
    loggedInMember.setName(name);
    loggedInMember.setAddress(address);
    loggedInMember.setGender(gender);

    // Store the member with the new details into the members-and-trainers.xml file
    try {
      gymApi.store();

    } catch (Exception e) {
      System.err.println("ERROR: Unable to register at this time");
    }
  }

  /*
  Trainer Assessment Sub Menu, prints the menu and waits for a number input from the member between 0 and 2 and
  returns this number
  */
  private int trainerAssessmentSubMenu() {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("\n--ASSESSMENT SUB-MENU ---------------------------------------------------------------\n");
        System.out.println(" 1. Add an assessment for a member");
        System.out.println(" 2. Update comment on an assessment for a member ");
        System.out.println(" 0. Return to the main trainer menu");
        System.out.print("=>");
        invalid = false;
        option = input.nextInt();
        // read next line due to bug in scanner class
        input.nextLine();
        if (option < 0 || option > 2) {
          invalid = true;
          System.out.println("Invalid entry please enter a number between 0 and 2");
        }
      } catch (Exception e) {
        System.err.println("ERROR: Invalid entry please enter a number between 0 and 2");
      }
    } while (invalid);
    return option;
  }

  /*
  Run the Trainer Assessment sub menu and wait for a number to be returned.
  Action the case that corresponds to the number
   */
  private void runTrainerAssessmentSubMenu() {
    int option = trainerAssessmentSubMenu();

    while (option != 0) {
      switch (option) {

        // call the addAssessment method
        case 1:
          addAssessment();
          break;

        // call the updateAssessmentComment method
        case 2:
          updateAssessementComment();
          break;

        default:
          System.out.println("Invalid menu option");
      }

      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      runTrainerAssessmentSubMenu();

    }
    runTrainerMenu();
  }

  // Method to add an assessement to a member object
  public void addAssessment() {
    boolean invalid = false;
    String date = "00/00/00";
    String email = "";
    Member member = null;
    float weight = 0;
    float thigh = 0;
    float waist = 0;
    String comment = "";

    System.out.println("\n-- ADD ASSESSMENT -----------------------------------------------------------------------\n");

    /*
    Ask the trainer for  the members email address, search for the member in the members email address list if
    the members address is not valid ask the trainer for a valid members email address again
    */
    do {
      try {
        System.out.println("Enter the email address of the member");
        email = input.nextLine();
        member = gymApi.searchMembersByEmail(email);
        invalid = false;
        if (member == null) {
          System.out.println("No member exists with this email address. Try again");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: Invalid Email Address");
        invalid = true;
      }
    } while (invalid);

    /*
    Ask the trainer for the date of the assessment and accept user input. check if the date is a valid date.
    If the date is not valid ask the user for a valid date again
    */
    do {
      try {
        invalid = false;
        System.out.println("Input the date of assessment (In the format yy/mm/dd e.g. 18/05/04 ):");
        date = input.nextLine();
        boolean validDate = isValidDate(date);
        if (!validDate) {
          System.err.println("Invalid date format");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: Invalid Date.");
        invalid = true;
      }
    } while (invalid);

    // Ask for the members weight and accept input between 35 and 250 kgs
    do {
      try {
        invalid = false;
        System.out.println("Enter the members weight (kgs): ");
        weight = input.nextFloat();
        if (weight < 35 || weight > 250) {
          System.out.println("Invalid weight. The weight must be between 35 and 250 kgs");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: invalid weight in kgs");
        input.nextLine();
        invalid = true;
      }
    } while (invalid);

    // Ask the trainer to input the members thigh measurements and accept user input
    do {
      try {
        invalid = false;
        System.out.println("Enter the members thigh measurements (inches):");
        thigh = input.nextFloat();
      } catch (Exception e) {
        System.err.println("ERROR: Invalid thigh measurements:");
        input.nextLine();
        invalid = true;
      }
    } while (invalid);

    // Ask the trainer to input the members waist measurements and accept user input
    do {
      try {
        invalid = false;
        System.out.println("Enter the members waist measurements (inches):");
        waist = input.nextFloat();

      } catch (Exception e) {
        System.err.println("ERROR: Invalid waist measurements.");
        input.nextLine();
        invalid = true;
      }
    } while (invalid);

    // Prompt the trainer to input a comment and accept the users input
    do {
      try {
        input.nextLine();
        invalid = false;
        System.out.println("Trainers Comment: ");
        comment = input.nextLine();
      } catch (Exception e) {
        System.err.println("Error: Invalid comment");
        invalid = true;
      }
    } while (invalid);

    /*
    Find the member that the assessment is for using the members email address
    Create the assessment using the trainers input
    Set the trainer of the assessment to the logged in trainer
    Add the assessment to the member
    Store the updated member and assessment to the members-and-trainers.xml file
    */
    Member memberForAssessment;
    memberForAssessment = gymApi.searchMembersByEmail(email);
    Assessment assessment = new Assessment(weight, thigh, waist, comment);
    assessment.setTrainer(loggedInTrainer);
    memberForAssessment.addAssessment(date, assessment);
    try {
      gymApi.store();
    } catch (Exception e) {
      System.err.println("ERROR: Unable to add the assessment at this time");
    }
  }

  /*
  Method to check that the date is inputted correctly and is a valid date.
  The date should be in the form yy/mm/dd.
  The Pattern class was used to check the pattern is correct
  If the pattern is correct, use the SimpleDateFormat to check if the date is a valid date if it is a valid date
  return true otherwise return false
  I used the following websites to help with creating this method.

  http://www.java2s.com/Tutorial/Java/0120__Development/CheckifaStringisavaliddate.htm
  https://www.baeldung.com/java-date-regular-expressions
  */
  private boolean isValidDate(String date) {

    Pattern datePattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{2}$");

    if (datePattern.matcher(date).matches()) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
      // the format of the date is not to be lenient, so input from the user must match the format yy/MM/dd
      dateFormat.setLenient(false);
      try {
        dateFormat.parse(date.trim());
      } catch (ParseException pe) {
        return false;
      }
      return true;
    }
    return false;
  }


  // Method to update an assessments comment
  public void updateAssessementComment() {
    boolean invalid = false;
    Member memberToEdit = null;
    String email = "";
    String date = "00/00/00";
    String comment;
    HashMap<String, Assessment> assessments;
    Assessment assessmentToEdit;
    /*
    Prompt the trainer to input the members email address of the member who's assessment you want to update and
    accept user input
    */
    do {
      try {
        invalid = false;
        System.out.println("Enter the email address of the member whose assessment you wish to change:");
        email = input.nextLine();
        memberToEdit = gymApi.searchMembersByEmail(email);
        if (memberToEdit == null) {
          System.out.println("There is no member registered with this email address, try again\n");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: invalid email");
      }
    } while (invalid);

    /*
    List all dates of the members assessments and ask the trainer to input the date of the assessment they want to
    update.
    */
    if (memberToEdit.sortedAssessmentDates().size() > 0) {
      do {
        try {

          invalid = false;
          System.out.println("Below is a list of all assessment dates, input the date of the comment you wish to edit");
          System.out.println(memberToEdit.sortedAssessmentDates());
          System.out.println("=>");
          date = input.nextLine();
          // check if the input is a valid date
          boolean validDate = isValidDate(date);
          // if its not a valid date print an invalid message and ask the trainer to input a valid date again
          if (!validDate) {
            System.out.println("Invalid date please enter the date in the following format yy/mm/dd e.g. 18/05/04");
            invalid = true;
          }
          // Otherwise get the assessment using the date input as the key
          else {
            assessments = memberToEdit.getAssessments();
            assessmentToEdit = assessments.get(date);

          /* If there was not assessment associated with the inputted date print a no assessment message and ask
          the trainer to input a valid date
          */
            if (assessmentToEdit == null) {
              System.out.println("No assessment was entered for the date " + date + "\n");
              invalid = true;
            }
          /*
          If the date entered is a valid date and is associated with an assessment for that member. Print the
          assessments original comment and ask the trainer to enter a new comment. Accept the trainer input,
          set the comment in the assessment to the new comment and store the member and assessment to the
          members-and-trainers.yml file
           */
            else {
              System.out.println("Original Comment: " + assessmentToEdit.getComment());
              System.out.println("\n Enter New Comment: ");
              System.out.print("=>");
              comment = input.nextLine();
              assessmentToEdit.setComment(comment);
              try {
                gymApi.store();
              } catch (Exception e) {
                System.err.println("ERROR: Unable to save GymAPI before exit");
              }
            }
          }
        } catch (Exception e) {
          System.err.println("ERROR: Unable to update comment");
        }
      } while (invalid);
    } else {
      System.out.println("This member has no assessments");
    }
  }

  // Print the members progress sub menu and prompt the user to input a menu number. Accept user input
  private int memberProgressSubMenu() {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("\n--PROGRESS SUB-MENU -----------------------------------------------------------------\n");
        System.out.println(" 1. View progress weight");
        System.out.println(" 2. View progress by waist measurement");
        System.out.println(" 3. View progress by thigh measurement");
        System.out.println(" 4. View All Trainers comments");
        System.out.println(" 5. View Trainers latest comment");
        System.out.println(" 0. Return to the main member menu");
        System.out.print("=>");
        invalid = false;
        option = input.nextInt();
        if (option < 0 && option > 5) {
          invalid = true;
          System.out.println("Invalid entry please input a number between 0 and 5");
        }

      } catch (Exception e) {
        System.err.println("ERROR: Invalid entry please enter a number between 0 and 5");
      }
    } while (invalid);
    return option;
  }

  /* Control the members progress menu. Print the menu and wait for user input. Complete the action depending on
  case number returned
  */
  private void runMemberProgressSubMenu() {
    int option = memberProgressSubMenu();

    while (option != 0) {
      switch (option) {
        // get the members weight progress
        case 1:
          getWeightProgress();
          break;
        // get the members waist measurement progress
        case 2:
          getWaistProgress();
          break;
        // get the members thigh measurement progress
        case 3:
          getThighProgress();
          break;
        // get the trainers comments for all assessments
        case 4:
          viewAllTrainersComments();
          break;
        // get the trainers comment for the latest assessment
        case 5:
          viewLatestTrainersComments();
          break;
        // default if none of the above cases
        default:
          System.out.println("invalid menu option");
          break;
      }
      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      input.nextLine();
      option = memberProgressSubMenu();
    }
    runMemberMenu();
  }

  /*
  Method to get the members weight progress
  by getting the members starting weight and subtracting the members current weight.
  Print a message depending on if the member has gained or lost weight and print the figure
  */
  private void getWeightProgress() {
    float startWeight = loggedInMember.getStartWeight();
    Assessment latestAssessment = loggedInMember.latestAssessment();
    if(latestAssessment == null)
    {
      System.out.println("You have no assessments to base progress against. \nCurrent weight: "+startWeight+" kg");
    }
    else {
      float latestWeight = latestAssessment.getWeight();
      float weightProgress = startWeight - latestWeight;
      System.out.println("Your starting weight was: " + startWeight);
      System.out.println("Your latest weight is: " + latestWeight);
      if (weightProgress < 0) {
        System.out.println("You have gained: " + (weightProgress * -1) + " kg");
      } else {
        System.out.println("You have lost: " + weightProgress + " kg");
      }
    }
  }

  /*
  Method to get the members waist progress
  by getting the members starting waist measurement and subtracting the members current waist measurement.
  Print a message depending on if the member has increased or decreased in waist size and print the figure
  */
  private void getWaistProgress() {
    Assessment firstAssessment = loggedInMember.firstAssessment();
    Assessment latestAssessment = loggedInMember.latestAssessment();
    if(firstAssessment == null)
    {
      System.out.println("You have no assessments to base progress against.");
    }
    else if (firstAssessment == latestAssessment) {
      System.out.println("There is only one assessment so there will be no progress yet");
    }
    else {
      float firstWaist = firstAssessment.getWaist();
      float latestWaist = latestAssessment.getWaist();
      float waistProgress = firstWaist - latestWaist;
      System.out.println("Your starting waist measurement was: " + firstWaist + " inches");
      System.out.println("Your latest waist measurement is:    " + latestWaist + " inches");
      if (waistProgress < 0) {
        System.out.println("Your waist has increased by:         " + (waistProgress * -1) + " inches");
      } else {
        System.out.println("Your waist has decreased by:         " + waistProgress + " inches");
      }
    }
  }

  /*
  Method to get the members thigh progress
  by getting the members starting thigh measurement and subtracting the members current thigh measurement.
  Print a message depending on if the member has increased or decreased in thigh size and print the figure
  */
  private void getThighProgress() {
    Assessment firstAssessment = loggedInMember.firstAssessment();
    Assessment latestAssessment = loggedInMember.latestAssessment();

    if(firstAssessment == null)
    {
      System.out.println("You have no assessments to base progress against.");
    }
    else if (firstAssessment == latestAssessment) {
      System.out.println("There is only one assessment so there will be no progress yet");
    }
    else {
      float firstThigh = firstAssessment.getThigh();
      float latestThigh = latestAssessment.getThigh();
      float waistProgress = firstThigh - latestThigh;
      System.out.println("Your starting thigh measurement was:     " + firstThigh + " inches");
      System.out.println("Your latest thigh measurement is:        " + latestThigh + " inches");
      if (waistProgress < 0) {
        System.out.println("Your thigh measurement has increased by: " + (waistProgress * -1) + " inches");
      } else {
        System.out.println("Your thigh measurement has improved by:  " + waistProgress + " inches");
      }
    }
  }

  /*
  View all trainers comments for each of a members assessments
  Get a set of all assessment dates. Create an iterator to iterate through the assessment dates. for each date in the
  Set get the assessments comment and print the date and comment
   */
  private void viewAllTrainersComments() {
    SortedSet<String> assessmentDates = loggedInMember.sortedAssessmentDates();
    Iterator iterator = assessmentDates.iterator();
    HashMap<String, Assessment> assessments = loggedInMember.getAssessments();
    String date;
    String comment;
    Assessment assessment;
    if (assessments.size() > 0) {
      while (iterator.hasNext()) {
        date = (String) iterator.next();
        assessment = assessments.get(date);
        comment = assessment.getComment();
        System.out.println("Date: " + date + " , Comment: " + comment);
      }
    } else {
      System.out.println("You have no assessments yet");
    }
  }

  // View the latest trainers comment by getting the latest assessment and printing the date and comment
  private void viewLatestTrainersComments() {
    Assessment latestAssessment = loggedInMember.latestAssessment();
    SortedSet<String> assessmentDates = loggedInMember.sortedAssessmentDates();
    if (assessmentDates.size() > 0) {
      String date = (String) assessmentDates.last();
      String comment = latestAssessment.getComment();
      System.out.println("Date: " + date + ", Comment: " + comment);
    } else {
      System.out.println("You have no assessments");
    }
  }

  // exit the system - store the gymApi set the trainerLoggedIn to false and print a goodbye statement
  private void exit() {
    try {
      gymApi.store();
    } catch (Exception e) {
      System.err.println("ERROR: Unable to save GymAPI before exit");
    }
    trainerLoggedIn = false;
    System.out.println("Goodbye........");
    System.exit(0);

  }
}

