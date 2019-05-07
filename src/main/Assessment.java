/*
An Assessment stores weight, thigh, waist, comment and a trainer that entered the members assessment
 */
public class Assessment {
  private float weight;
  private float thigh;
  private float waist;
  private String comment;
  private Trainer trainer;

  //  Constructor method for an assessment with weight, thigh, waist and comment
  public Assessment(float weight, float thigh, float waist, String comment) {
    setWeight(weight);
    setThigh(thigh);
    setWaist(waist);
    setComment(comment);
  }

  //  Getter method to get the members weight for this assessment
  public float getWeight() {
    return weight;
  }

  //  Setter method to set the members weight for this assessment
  public void setWeight(float weight) {
    this.weight = weight;
  }

  //  Getter method to get the members thigh measurement for this assessment
  public float getThigh() {
    return thigh;
  }

  //  Setter method to set the members thigh measurement for this assessment
  public void setThigh(float thigh) {
    this.thigh = thigh;
  }

  //  Getter method to get the members waist measurement for this assessment
  public float getWaist() {
    return waist;
  }

  //  Setter method to set the members waist measurement for this assessment
  public void setWaist(float waist) {
    this.waist = waist;
  }

  //  Getter method to get the trainers comment for this assessment
  public String getComment() {
    return comment;
  }

  //  Setter method to set the trainers comment for this assessment
  public void setComment(String comment) {
    this.comment = comment;
  }

  //  Getter method to get the trainer who added the assessment to the members account
  public Trainer getTrainer() {
    return trainer;
  }

  //  Setter method to set the trainer who added the assessmen to the members account
  public void setTrainer(Trainer trainer) {
    this.trainer = trainer;
  }

  //  To string method for Assessment
  public String toString() {
    return "Assessment : weight: " + weight + " , waist: " + waist + ", thigh: " + thigh + " , comment: " + comment + " , trainer: "
        + trainer;
  }
}
