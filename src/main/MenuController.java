import com.sun.org.apache.xpath.internal.operations.Bool;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This class runs the application and handles the Product I/O
 *
 * @version 3.0
 */
public class MenuController<DateMatcher> {

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

  public static void main(String[] args) {
    MenuController c = new MenuController();
  }

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
            loggedInTrainer = gymApi.searchTrainersByEmail(inputEmail);
            trainerLoggedIn = true;
            System.out.println("\n************************************** WELCOME BACK "
                + loggedInTrainer.getName().toUpperCase()
                + "**************************************\n");
            runTrainerMenu();
          }
          if (loginValid && isMember) {
            loggedInMember = gymApi.searchMembersByEmail(inputEmail);
            System.out.println("\n************************************** WELCOME BACK "
                + loggedInMember.getName().toUpperCase()
                + "**************************************\n");
            runMemberMenu();
          }
        }
        break;

      case "r":
        memberOrTrainer = areYouAMemberOrTrainerMenu();
        boolean isAMember = memberOrTrainer.equalsIgnoreCase("m");
        boolean isATrainer = memberOrTrainer.equalsIgnoreCase("t");
        String registeredEmail = "";
        if (memberOrTrainer.equalsIgnoreCase("x")) {
          logout();
        } else if (isAMember || isATrainer) {
          registeredEmail = register(memberOrTrainer);
        }
        if (!registeredEmail.equals(null) && isAMember) {
          loggedInMember = gymApi.searchMembersByEmail(registeredEmail);
          System.out.println("\n************************************** WELCOME "
              + loggedInMember.getName().toUpperCase()
              + "**************************************\n");
          runMemberMenu();
        } else if (!registeredEmail.equals(null) && isATrainer) {
          loggedInTrainer = gymApi.searchTrainersByEmail(registeredEmail);
          System.out.println("\n************************************** WELCOME "
              + loggedInTrainer.getName().toUpperCase()
              + "**************************************\n");
          runTrainerMenu();
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
        System.out.println("Are your a member (m) or a trainer (t) (enter x to exit)");
        invalid = false;
        option = input.nextLine().trim();
        boolean isAMember = option.equalsIgnoreCase("m");
        boolean isATrainer = option.equalsIgnoreCase("t");
        boolean exit = option.equalsIgnoreCase("x");
        if (exit) {
          logout();
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
        return true;
      }
    } else if (personIsATrainer) {
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
    String collegeName = "";


    if (trainerLoggedIn) {
      System.out.println("\n--PLEASE ENTER THE NEW MEMBERS DETAILS BELOW  -----------------------------------------\n");
    } else {
      System.out.println("\n--PLEASE ENTER YOUR DETAILS BELOW: ----------------------------------------------------\n");
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

          try {
            if (equalsWit) {
              System.out.println("What is your student Id");
              studentId = input.nextInt();
            }
          } catch (Exception e) {
            System.out.println("ERROR: Invalid student ID");
          }

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
      if (chosenPackage.equalsIgnoreCase("wit")) {
        newMember = new StudentMember(email, name, address, gender, height, startWeight, chosenPackage, studentId, "WIT");
      } else {
        newMember = new PremiumMember(email, name, address, gender, height, startWeight, chosenPackage);
      }
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
      if (!trainerLoggedIn) {
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
        System.out.println("\n--TRAINER MENU ----------------------------------------------------------------------\n");
        System.out.println("Enter the menu number you wish to select: ");
        System.out.println(" 1. Add a new member. ");
        System.out.println(" 2. List all members.");
        System.out.println(" 3. Search for a member by email.");
        System.out.println(" 4. Search for a member by name.");
        System.out.println(" 5. List all member with an ideal weight");
        System.out.println(" 6. List members by their BMI Category");
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


  private void runTrainerMenu() {
    int option = trainerMenu();
    while (option != 0) {
      switch (option) {
        case 1:
          register("m");
          break;
        case 2:
          ArrayList<Member> members = gymApi.getMembers();

          for (int i = 0; i < members.size(); i++) {
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
          if (member != null) {
            System.out.println(member);
          } else {
            System.out.println("No member registered with email address: " + email);
          }
          break;
        case 4:
          System.out.println("Input the members name:");
          String name = input.nextLine();
          ArrayList<String> memberByNames = gymApi.searchMembersByName(name);
          for (int i = 0; i < memberByNames.size(); i++) {
            String memberByName = memberByNames.get(i);
            System.out.println(memberByName);
          }
          break;
        case 5:
          ArrayList<Member> listOfMembersWithIdealWeight = gymApi.listMembersWithIdealWeight();
          if (listOfMembersWithIdealWeight.size() <= 0) {
            System.out.println("There are no members with an ideal body weight");
          } else {
            for (int i = 0; i > listOfMembersWithIdealWeight.size(); i++) {
              System.out.println("-------------------------------------------------------------------------------------");
              System.out.println(listOfMembersWithIdealWeight.get(i));

            }
          }
          break;
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
        case 7:
          String imperialAndMetricDetails = gymApi.listMemberDetailsImperialAndMetric();
          System.out.println(imperialAndMetricDetails);
          break;
        case 8:
          runTrainerAssessmentSubMenu();
          break;
      }

      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      option = trainerMenu();
    }
    logout();
  }


  public int memberMenu() {
    boolean invalid = false;
    int option = -1;
    do {
      try {
        System.out.println("\n--MEMBER MENU -----------------------------------------------------------------------\n");
        System.out.println("Enter the menu number you wish to select: ");
        System.out.println(" 1. View your Profile. ");
        System.out.println(" 2. Update your profile");
        System.out.println(" 3. Progress sub menu");
        System.out.println(" 0. Exit");
        System.out.print("=>");

        invalid = false;
        option = input.nextInt();
        if (option < 0 && option > 3) {
          System.out.println("Invalid input please enter a number between 0 and 3");
          invalid = true;
        }
      } catch (Exception e) {
        invalid = true;
        System.err.println("ERROR: your must enter a number between 0 and 3");
      }
    } while (invalid);
    return option;
  }


  private void runMemberMenu() {
    int option = memberMenu();
    while (option != 0) {
      switch (option) {

        case 1:
          try {
            System.out.println("YOUR PROFILE " + loggedInMember.getName().toUpperCase());
            System.out.println(loggedInMember);
          } catch (Exception e) {
            System.err.println("ERROR: Unable to view members profile.");
          }
          break;

        case 2:
          System.out.println(option);
          try {
            updateProfile();
          } catch (Exception e) {
            System.err.println("ERROR: Unable to update your profile");
          }
          break;
        case 3:
          runMemberProgressSubMenu();
      }

      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      input.nextLine();
      option = memberMenu();
    }
    logout();
  }

  private void updateProfile() {

    String name = "";
    String address = "";
    String gender = "";
    boolean invalid = false;

//    to avoid but in the system.
    input.nextLine();
    System.out.println("\n--UPDATE PROFILE ------------------------------------------------------------------------\n");
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

    loggedInMember.setName(name);
    loggedInMember.setAddress(address);
    loggedInMember.setGender(gender);

    try {
      gymApi.store();

    } catch (Exception e) {
      System.err.println("ERROR: Unable to register at this time");
    }

  }

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

  private void runTrainerAssessmentSubMenu() {
    int option = trainerAssessmentSubMenu();

    while (option != 0) {
      switch (option) {
        case 1:
          addAssessment();
          break;
        case 2:
          updateAssessementComment();
          break;
      }

      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      runTrainerAssessmentSubMenu();

    }
    runTrainerMenu();
  }

  public void addAssessment() {
    boolean invalid = false;
    String date = "00/00/00";
    String email = "";
    Member member = null;
    Trainer trainer;
    float weight = 0;
    float thigh = 0;
    float waist = 0;
    String comment = "";

    System.out.println("\n-- ADD ASSESSMENT -----------------------------------------------------------------------\n");
    do {
      try {
        input.nextLine();
        System.out.println("Enter the email address of the member");
        email = input.nextLine();
        member = gymApi.searchMembersByEmail(email);
        invalid = false;
        if (member == null) {
          System.out.println("No member exists with this email address. Try again");
          input.nextLine();
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: Invalid Email Address");
        invalid = true;
      }
    } while (invalid);


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

    do {
      try {
        invalid = false;
        System.out.println("Enter the members weight (kgs): ");
        weight = input.nextFloat();
      } catch (Exception e) {
        System.err.println("ERROR: invalid weight in kgs");
        input.nextLine();
        invalid = true;
      }
    } while (invalid);

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

    Member memberForAssessment;
    memberForAssessment = gymApi.searchMembersByEmail(email);
    Assessment assessment = new Assessment(weight, thigh, waist, comment);
    assessment.setTrainer(loggedInTrainer);
    memberForAssessment.addAssessment(date, assessment);
    try {
      gymApi.store();

    } catch (Exception e) {
      System.err.println("ERROR: Unable to register at this time");
    }
  }

  //http://www.java2s.com/Tutorial/Java/0120__Development/CheckifaStringisavaliddate.htm
  // https://www.baeldung.com/java-date-regular-expressions
  private boolean isValidDate(String date) {

    Pattern datePattern = Pattern.compile(
        "^\\d{2}/\\d{2}/\\d{2}$");

    if (datePattern.matcher(date).matches()) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd");
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

  public void updateAssessementComment() {
    boolean invalid = false;
    Member memberToEdit = null;
    String email = "";
    String date = "00/00/00";
    String comment;
    HashMap<String, Assessment> assessments;
    Assessment assessmentToEdit;
    do {
      try {
        input.nextLine();
        invalid = false;
        System.out.println("Enter the email address of the member whose assessment you wish to change:");
        email = input.nextLine();
        memberToEdit = gymApi.searchMembersByEmail(email);
        if (memberToEdit == null) {
          System.err.println("There is no member registered with this email address, try again\n");
          invalid = true;
        }
      } catch (Exception e) {
        System.err.println("ERROR: invalid email");
      }
    } while (invalid);

    do {
      try {

        invalid = false;
        System.out.println("Below is a list of all assessment dates, input the date of the comment you wish to edit");
        System.out.println(memberToEdit.sortedAssessmentDates());
        System.out.println("=>");
        date = input.nextLine();
        boolean validDate = isValidDate(date);
        if (!validDate) {
          System.out.println("Invalid date please enter the date in the following format yy/mm/dd e.g. 18/05/04");
          invalid = true;
        } else {
          assessments = memberToEdit.getAssessments();
          assessmentToEdit = assessments.get(date);

          if (assessmentToEdit == null) {
            System.out.println("No assessment was entered for the date " + date + "\n");
            invalid = true;
          } else {
            System.out.println("Original Comment: " + assessmentToEdit.getComment());
            System.out.println("\n Enter New Comment: ");
            System.out.print("=>");
            comment = input.nextLine();
            assessmentToEdit.setComment(comment);
            try {
              gymApi.store();
            } catch (Exception e) {
              System.err.println("ERROR: Unable to save GymAPI before logout");
            }
          }
        }
      } catch (Exception e) {
        System.err.println("ERROR: Unable to update comment");
      }
    } while (invalid);
  }

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

  private void runMemberProgressSubMenu() {
    int option = memberProgressSubMenu();

    while (option != 0) {
      switch (option) {
        case 1:
          getWeightProgress();
          break;
        case 2:
          getWaistProgress();
          break;
        case 3:
          getThighProgress();
          break;
        case 4:
          viewAllTrainersComments();
          break;
        case 5:
          viewLatestTrainersComments();
          break;
        default:
          System.out.println("invalid input");
          break;
      }
      System.out.println("\nPress any key to continue.....................");
      input.nextLine();
      input.nextLine();
      option = memberProgressSubMenu();
    }
    runMemberMenu();
  }

  private void getWeightProgress() {
    float startWeight = loggedInMember.getStartWeight();
    Assessment latestAssessment = loggedInMember.latestAssessment();
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

  private void getWaistProgress() {
    Assessment firstAssessment = loggedInMember.firstAssessment();
    Assessment latestAssessment = loggedInMember.latestAssessment();
    if (firstAssessment == latestAssessment) {
      System.out.println("There is only one assessment so there will be no progress yet");
    }
    if (firstAssessment.getWaist() == -1) {
      System.out.println("You have not entered any assessments yet");
    } else {
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

  private void getThighProgress() {
    Assessment firstAssessment = loggedInMember.firstAssessment();
    Assessment latestAssessment = loggedInMember.latestAssessment();
    if (firstAssessment == latestAssessment) {
      System.out.println("There is only one assessment so there will be no progress yet");
    }
    if (firstAssessment.getWaist() == -1) {
      System.out.println("You have not entered any assessments yet");
    } else {
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
      System.out.println("You have not entered any assessments yet");
    }
  }

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

  private void logout() {
    try {
      gymApi.store();
    } catch (Exception e) {
      System.err.println("ERROR: Unable to save GymAPI before logout");
    }
    trainerLoggedIn = false;
    System.out.println("Goodbye........");
    System.exit(0);

  }
}

