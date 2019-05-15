// Premium Member is a member that is any member that is not a student
public class PremiumMember extends Member {

  // Constructor method for a premium member
  public PremiumMember(String email, String name, String address,
                String gender, float height, float startWeight, String chosenPackage)
  {
    super(email,name,address,gender,height,startWeight,chosenPackage);
  }

  // Implementation of the abstract method chosenPackage in the Member class to set the chosen package
  @Override
    public void chosenPackage(String packageChoice)
  {
     super.setChosenPackage(packageChoice);
  }
}
