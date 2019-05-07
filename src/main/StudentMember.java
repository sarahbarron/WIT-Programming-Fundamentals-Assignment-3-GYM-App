public class StudentMember extends Member  {

  private int studentId;
  private String collegeName;

  public StudentMember(String email, String name, String address, String gender, float height,
                       float startWeight, String chosenPackage, int studentId, String collegeName) {
    super(email, name, address, gender, height, startWeight, chosenPackage);
    this.studentId = studentId;
    this.collegeName = collegeName;

  }

  public int getStudentId() {
    return studentId;
  }

  public void setStudentId(int studentId){
    this.studentId = studentId;
  }

  public String getCollegeName() {
    return collegeName;
  }

  @Override
  public void chosenPackage(String packageChoice)
  {
    String collegeName = getCollegeName();
    if(collegeName.equals(packageChoice))
    {
      super.setChosenPackage(packageChoice);
    }
    else
    {
      super.setChosenPackage("Package 3");
    }
  }
  // toString method for Student Member class
  public String toString() {

    return super.toString()+", College Name: "+collegeName+" ,Student Id: "+studentId;
  }
}
