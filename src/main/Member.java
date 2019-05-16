import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;


/*
An abstract Member class that extends the Person class
Stores a member. Each member has a height, starting weight, a gym package and assessments
*/
public abstract class Member extends Person {

  private float height;
  private float startWeight;
  private String chosenPackage;
  private HashMap<String, Assessment> assessmentCollection;

  // Member empty constructor method
  public Member() {
  }

  // Constructor method with email name, address, gender, height (metres), start weight (kgs) and chosen package
  public Member(String email, String name, String address,
                String gender, float height, float startWeight, String chosenPackage) {

    super(email, name, address, gender);
    setHeight(height);
    setStartWeight(startWeight);
    setChosenPackage(chosenPackage);
    // Create a Collection to store Members Assessments
    assessmentCollection = new HashMap<String, Assessment>();
  }

  // Get the height of the member
  public float getHeight() {
    return height;
  }

  // Set the height of the member, if they enter between 1 and 3 store the input otherwise set the height to 0
  public void setHeight(float height) {
    if (height >= 1 && height <= 3.0) {
      this.height = height;
    } else {
      this.height = 0;
    }
  }

  // Get the members starting weight
  public float getStartWeight() {
    return startWeight;
  }

  /*
  Set the members starting weight, if the user input was between 35 and 250 store the user input otherwise
  store 0 as the starting weight
  */
  public void setStartWeight(float startWeight) {
    if (startWeight >= 35 && startWeight <= 250) {
      this.startWeight = startWeight;
    } else {
      this.startWeight = 0;
    }
  }


  // get the chosen package
  public String getChosenPackage() {
    return chosenPackage;
  }

  // set the chosen package
  public void setChosenPackage(String chosenPackage) {
    this.chosenPackage = chosenPackage;
  }

  /*
  Add an assessment to the members collection of assessments, with
  the date as the key and the assessment as the value
  */
  public void addAssessment(String date, Assessment assessment) {
    assessmentCollection.put(date, assessment);
  }

  /*
  Get the latest assessment by getting a sorted set of assessment dates,
  use the last date in the set as the key to finding the latest assessment and return the assessment
  */
  public Assessment latestAssessment() {

    SortedSet sortedSet = sortedAssessmentDates();
    Assessment lastAssessment = null;
    if (sortedSet.size() > 0) {
      String lastDate = (String) sortedSet.last();
      lastAssessment = assessmentCollection.get(lastDate);
      return lastAssessment;
    }

    return lastAssessment;
  }

  /*
  Return the first assessment by getting a sorted set of assessment dates. Use the first date in the set as the key
  to finding the first assessment. return this first assessment
  */
  public Assessment firstAssessment() {
    SortedSet sortedSet = sortedAssessmentDates();
    Assessment firstAssessment = null;
    if (sortedSet.size() > 0) {
      String firstDate = (String) sortedSet.first();
      firstAssessment = assessmentCollection.get(firstDate);
    }
    return firstAssessment;
  }

  /*
  A method to sort assessment dates, by creating a SortedSet and assigning a TreeSet to it,
  add all keys (dates) from the HashMap assessmentCollection to the SortedSet
  this will sort them automatically, return the SortedSet
  */
  public SortedSet sortedAssessmentDates() {
    SortedSet<String> sortedSet = new TreeSet();
    sortedSet.addAll(assessmentCollection.keySet());
    return sortedSet;
  }

  // Getter method to return the HashMap of assessments
  public HashMap<String, Assessment> getAssessments() {
    return assessmentCollection;
  }

  // Abstract method for chosen Package
  public abstract void chosenPackage(String chosenPackage);

  // toString method for Member class
  public String toString() {

    return super.toString() + "Height: " + height + " , Start Weight: " + startWeight
        + " , Chosen Package: " + chosenPackage;
  }
}

