public class Assessment {
  private float weight;
  private float thigh;
  private float waist;
  private String comment;
  private Trainer trainer;


  public Assessment(float weight, float thigh, float waist, String comment, Trainer trainer) {
    setWeight(weight);
    setThigh(thigh);
    setWaist(waist);
    setComment(comment);
    setTrainer(trainer);
  }

  public float getWeight() {
    return weight;
  }

  public void setWeight(float weight) {
    this.weight = weight;
  }

  public float getThigh() {
    return thigh;
  }

  public void setThigh(float thigh) {
    this.thigh = thigh;
  }

  public float getWaist() {
    return waist;
  }

  public void setWaist(float waist) {
    this.waist = waist;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Trainer getTrainer() {
    return trainer;
  }

  public void setTrainer(Trainer trainer) {
    this.trainer = trainer;
  }

}
