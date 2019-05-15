/*
Trainer class extends the Person class.
It stores the details of a trainer, their email address, name, address, gender, and trainer speciality.
 */
public class Trainer extends Person  {

  private String speciality;

  // Constructor method of a trainer
  public Trainer(String email, String name, String address, String gender, String speciality) {
    super(email, name, address, gender);
    setSpeciality(speciality);
  }

  // Get the trainers speciality
  public String getSpeciality() {
    return speciality;
  }

  // Set the trainers Speciality
  public void setSpeciality(String speciality) {
    this.speciality = speciality;
  }

  // toString method for Trainer class
  public String toString() {

    return super.toString()+" Speciality: "+speciality;
  }
}
