/*
StudentMember is a member that is a student.
A student member has a student id and a college name stored with the members details
 */
public class StudentMember extends Member {

  private int studentId;
  private String collegeName;

  // Constructor method for a StudentMember
  public StudentMember(String email, String name, String address, String gender, float height,
                       float startWeight, String chosenPackage, int studentId, String collegeName) {
    super(email, name, address, gender, height, startWeight, chosenPackage);
    this.studentId = studentId;
    this.collegeName = collegeName;
  }

  // Get the student Id
  public int getStudentId() {
    return studentId;
  }

  // Set the student Id
  public void setStudentId(int studentId) {
    this.studentId = studentId;
  }

  // Get the College name
  public String getCollegeName() {
    return collegeName;
  }

  // set the College name
  public void setCollegeName(String collegeName) {
    this.collegeName = collegeName;
  }

  // Implementation of the abstract method chosenPackage in the Member class to set the chosen package
  @Override
  public void chosenPackage(String packageChoice) {
    String collegeName = getCollegeName();
    if (collegeName.equals(packageChoice)) {
      super.setChosenPackage(packageChoice);
    } else {
      super.setChosenPackage("Package 3");
    }
  }

  // toString method for Student Member class
  public String toString() {

    return super.toString() + ", College Name: " + collegeName + " ,Student Id: " + studentId;
  }
}
