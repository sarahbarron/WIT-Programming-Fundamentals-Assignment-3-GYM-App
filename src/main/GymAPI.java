import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/*
This class operates between the model classes and the menu driver class it stores an array list
for members and an array list for trainers
*/
public class GymAPI {
  // Array list of members
  private ArrayList<Member> members;
  // Array list of Trainers
  private ArrayList<Trainer> trainers;

  // Constructor method for the GymAPI
  public GymAPI() {

    members = new ArrayList<Member>();
    trainers = new ArrayList<Trainer>();
  }

  // Returns a list of members
  public ArrayList<Member> getMembers() {
    return members;
  }

  // Returns an list of all trainers
  public ArrayList<Trainer> getTrainers() {
    return trainers;
  }

  // Adds a member to the array list
  public void addMember(Member member) {
    members.add(member);
  }

  // Adds a trainer to the array list
  public void addTrainer(Trainer trainer) {
    trainers.add(trainer);
  }

  // Returns the number of members in the array list
  public int numberOfMembers() {
    return members.size();
  }

  // Return the number of trainers in the array list
  public int numberOfTrainerss() {
    return trainers.size();
  }

  // Returns a boolean indicating if the index passed as a parameter is a valid index for the members array list
  public boolean isValidMemberIndex(int index) {
    if (index < numberOfMembers()) {
      return true;
    }
    return false;
  }

  // Returns a boolean indicating if the index passed as a parameter is a valid index for members array list
  public boolean isValidTrainerIndex(int index) {
    if (index < numberOfTrainerss()) {
      return true;
    }
    return false;
  }

  // Returns the member object that matches the email entered. If no member matches null is returned
  public Member searchMembersByEmail(String emailEntered) {
    members = getMembers();
    for (Member member : members) {
      if (member.getEmail().equals(emailEntered)) {
        return member;
      }
    }
    return null;
  }

  /*
  Returns a list of member names that partially or entirely matches the entered name. An empty array is returned
  when there are no matches
  */
  public ArrayList<String> searchMembersByName(String nameEntered) {
    members = getMembers();
    ArrayList<String> matchedlist = new ArrayList<>();

    for (Member member : members) {
      String memberName = member.getName();
      //Convert to lowercase as the .contains and .equals methods are case sensitive
      memberName = memberName.toLowerCase();
      nameEntered = nameEntered.toLowerCase();
      // check for an exact match of the name being search in the list of members
      if (memberName.equals(nameEntered)) {
        matchedlist.add(memberName);
      }
      // otherwise check if there is a partial match
      else if (memberName.contains(nameEntered)) {
        matchedlist.add(memberName);
      }
    }
    return matchedlist;
  }

  // Returns the trainer object that matches the email entered. If no trainer matches, return null
  public Trainer searchTrainersByEmail(String emailEntered) {
    trainers = getTrainers();
    for (Trainer trainer : trainers) {
      if (trainer.getEmail().equals(emailEntered)) {
        return trainer;
      }
    }
    return null;
  }

  /*
  Returns a list of trainers names that partially or entirely matches the entered name. An empty array is returned
  when there are no matches
 */
  public ArrayList<String> searchTrainersByName(String nameEntered) {
    trainers = getTrainers();
    ArrayList<String> matchedlist = new ArrayList<>();

    for (Trainer trainer : trainers) {
      // Convert to lower case names as the .contains and .equals method is case sensitive
      String trainerName = trainer.getName().toLowerCase();
      nameEntered = nameEntered.toLowerCase();
      // Check for an exact match of the name being search in the list of members
      if (trainerName.equals(nameEntered)) {
        matchedlist.add(trainerName);
      }
      // Otherwise check if there is a partial match
      else if (trainerName.contains(nameEntered)) {
        matchedlist.add(trainerName);
      }
    }
    return matchedlist;
  }

  // A method to returns a list containing all the members in the gym. returns an empty list if none found
  public ArrayList<Member> listMembers() {
    return members;
  }

  /*
  A method to return a list containing all the members details in the gym whose latest assessment weight is an ideal
  weight (based on the Devine Formula). Returns an empty list if none are found.
  */
  public ArrayList<Member> listMembersWithIdealWeight() {

    ArrayList<Member> listOfMembersWithIdealWeight = new ArrayList<>();
    for (Member member : members) {
      if (member.getAssessments().size() > 0) {
        Assessment latestAssessment = member.latestAssessment();
        if (GymUtility.isIdealBodyWeight(member, latestAssessment)) {
          listOfMembersWithIdealWeight.add(member);
        }
      }
    }
    return listOfMembersWithIdealWeight;
  }

  /*
  Returns a list of all members details in the gym whose BMI category (based on the latest assessment weight) partially
  or entirely matches the entered category. Returns an empty list if none found
  */
  public ArrayList<Member> listMembersBySpecificBMICategory(String category) {

    ArrayList<Member> listOfMembersInSpecifiedBMICategory = new ArrayList<>();
    category = category.trim().toUpperCase();
    Assessment latestAssessment;
    float bmi;
    String memberBmiCategory;
    String severelyUnderWeight = "SEVERELY UNDERWEIGHT";
    String underweight = "UNDERWEIGHT";
    String normal = "NORMAL";
    String overweight = "OVERWEIGHT";
    String moderatelyObese = "MODERATELY OBESE";
    String severlyObese = "SEVERELY OBESE";

    /*
    If the inputted string is an exact match to one of the BMI categories iterate through the list of members.
    get each members latest assessment, calculate their BMi and with their BMI rate calculate the BMI category they are
    in. If the member is in the category inputted by user add the member to an array. Return this array after all
    members categories have been calculated.
   */
    if (category.equals(severelyUnderWeight) || category.equals(underweight) || category.equals(normal)
        || category.equals(overweight) || category.equals(moderatelyObese) || category.equals(severlyObese)) {
      for (Member member : members) {
        latestAssessment = member.latestAssessment();
        bmi = (float) GymUtility.calculateBMI(member, latestAssessment);
        memberBmiCategory = GymUtility.determineBMICategory(bmi);
        if (memberBmiCategory.equals(category)) {
          listOfMembersInSpecifiedBMICategory.add(member);
        }
      }
    }

    /*
    Otherwise if the inputted string only partially matches a bmi category, get each members assessment, calculate
    their bmi using the latest weight. Calculate the bmi category using the bmi rate. If the members category partially
    matches the input from the user add the member to an array
    */
    else if (severelyUnderWeight.contains(category) || underweight.contains(category) || normal.contains(category)
        || overweight.contains(category) || moderatelyObese.contains("category") || severlyObese.contains(category)) {
      for (Member member : members) {
        latestAssessment = member.latestAssessment();
        bmi = (float) GymUtility.calculateBMI(member, latestAssessment);
        memberBmiCategory = GymUtility.determineBMICategory(bmi);
        if (memberBmiCategory.contains(category)) {
          listOfMembersInSpecifiedBMICategory.add(member);
        }
      }
    }
    // Otherwise print as statement to say there is no such category
    else {
      System.out.println("no such category");
    }

    // Return the array list of all members who matched the inputted category from the user.
    return listOfMembersInSpecifiedBMICategory;
  }

  /*
  List for each member, their latest Assessment weight and their height both imperially and metrically.
  format Joe Soap: xx kg (xxx lbs) x.x metres(xx inches)
  If there are no members in the gym return a message "No registered members"
  */
  public String listMemberDetailsImperialAndMetric() {
    if (members.size() > 0) {
      String memberName = "";
      int weightKg = 0;
      int weightLbs = 0;
      double heightMetres = 0;
      int heightInches = 0;
      String membersDetails = "Member Details Imperial and Metric: \n";
      Assessment latestAssessment;
      for (Member member : members) {
        memberName = member.getName();
        latestAssessment = member.latestAssessment();
        weightKg = (int) Math.round(latestAssessment.getWeight());
        weightLbs = (int) (weightKg * 2.2);
        heightMetres = (double) member.getHeight();
        heightInches = (int) Math.round((heightMetres * 39.37));
        heightMetres = toOneDecimalPlaces(heightMetres);
        membersDetails = membersDetails + memberName + ": " + weightKg
            + " kg (" + weightLbs + " lbs) " + heightMetres + " metres (" + heightInches + " inches) \n";
      }
      return membersDetails;
    } else {
      return "No registered members";
    }
  }

  // Returns a float with 2 decimal places
  private double toOneDecimalPlaces(double num) {
    return (int) (num * 10) / 10.0;
  }

  // Push the members and trainers array list out the associated XML file
  public void store() throws Exception {
    XStream xstream = new XStream(new DomDriver());
    ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("members-and-trainers.xml"));
    out.writeObject(members);
    out.writeObject(trainers);
    out.close();

  }

  // Pull the members, trainers and assessments from the associated XML file
  @SuppressWarnings("unchecked")
  public void load() throws Exception {
    XStream xstream = new XStream(new DomDriver());

    // ------------------ PREVENT SECURITY WARNINGS---------------------------
    Class<?>[] classes = new Class[]
        {Member.class, StudentMember.class, PremiumMember.class, Trainer.class, Assessment.class};
    XStream.setupDefaultSecurity(xstream);
    xstream.allowTypes(classes);
    // -----------------------------------------------------------------------

    ObjectInputStream is = xstream.createObjectInputStream(new FileReader("members-and-trainers.xml"));
    members = (ArrayList<Member>) is.readObject();
    trainers = (ArrayList<Trainer>) is.readObject();
    is.close();
  }
}
