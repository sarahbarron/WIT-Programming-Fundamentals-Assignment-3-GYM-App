import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/* This class operates between the model classes and the menu driver class it stores an array list
for members and an array list for trainers */
public class GymAPI {
    // Array list of members
    private ArrayList<Member> members;
    // Array list of Trainers
    private ArrayList<Trainer> trainers;

    // Constructor method for the GymAPI
    public GymAPI() {

        this.members = new ArrayList<Member>();
        this.trainers = new ArrayList<Trainer>();
    }

    // Returns a list of members
    public ArrayList<Member> getMembers() {
        return members;
    }

    public ArrayList<Trainer> getTrainers()
    {
        return trainers;
    }

    // Adds a member
    public void addMember(Member member) {

        this.members.add(member);
    }

    // Adds a trainer
    public void addTrainer(Trainer trainer) {
        this.trainers.add(trainer);
    }

    // Returns the number of members
    public int numberOfMembers() {

        return members.size();
    }

    // Return the number of trainers
    public int numberOfTrainerss() {

        return trainers.size();
    }

    // Returns a boolean indicating if the index passed as a parameter is a valid index for the members array list
    public boolean isValidMemberIndex(int index) {
        if(index<numberOfMembers())
        {
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

//    Returns the member object that matches the email entered. If no member matches null is returned
    public Member searchMembersByEmail(String emailEntered)
    {
        members = getMembers();
        for(Member member:members)
        {
            if(member.getEmail().equals(emailEntered))
            {
                return member;
            }
        }
        return null;
    }

/*  Returns a list of member names that partially or entirely matches the entered name. An empty array is returned
    when there are no matches
 */
    public ArrayList<String> searchMembersByName(String nameEntered)
    {
        members = getMembers();
        ArrayList<String> matchedlist = new ArrayList<>();

        for (Member member:members)
        {
            String memberName = member.getName();
            int nameLength = nameEntered.length();


            // check for an exact match of the name being search in the list of members
            if(memberName.equalsIgnoreCase(nameEntered))
            {
                matchedlist.add(memberName);
            }

            /* otherwise check if there is a partial match if a members name up the 1st 3 letters matching or
            if the name entered is less than 3 characters long does it start with the 1st letter entered */
            else
            {
                String check;
                if(nameLength<3&&nameLength>1)
                {
                    check = nameEntered.substring(0,1);

                }
                else
                {
                    check = nameEntered.substring(0,2);
                }
                if(memberName.startsWith(check))
                {
                    matchedlist.add(memberName);
                }
            }
        }
        return matchedlist;
    }

//    Returns the trainer object that matches the email entered. If no trainer matches, return null
    public Trainer searchTrainersByEmail(String emailEntered)
    {
        trainers = getTrainers();
        for(Trainer trainer:trainers)
        {
            if(trainer.getEmail().equals(emailEntered))
            {
                return trainer;
            }
        }
        return null;
    }

//    Returns a list containing all the members in the gym. returns an empty list if none found
    public ArrayList<Member> listMembers()
    {
        return members;
    }

/* Returns a list containing all the members details in the gym whose latest assessment weight is an ideal
weight (based on the devine formula). Returns an empty list if none are found.
 */
    public ArrayList<Member> listMembersWithIdealWeight()
    {
        ArrayList<Member> listOfMembersWithIdealWeight = new ArrayList<>();
        for(Member member:members)
        {
//            TODO work out the calculation for ideal weight
            listOfMembersWithIdealWeight.add(member);
        }
        return listOfMembersWithIdealWeight;
    }

/* Returns a list of all members details in the gym whose BMI category (based on the latest assessment weight) partially
or entirely matches the entered category. Returns an empty list if none found */

    public ArrayList<Member> listMembersBySpecificBMICategory(String category)
    {
        ArrayList<Member> listOfMembersInSpecifiedBMICategory = new ArrayList<>();
        for(Member member:members)
        {
//            TODO work out the calculation for BMI Category
            listOfMembersInSpecifiedBMICategory.add(member);
        }
        return listOfMembersInSpecifiedBMICategory;
    }

/*    List for each member, their latest Assessment weight and their height both imperially and metrically.
  format Joe Soap: xx kg (xxx lbs) x.x metres(xx inches)
  If there are no members in the gym return a message "No registered members"  */
    public String listMemberDetailsImperialAndMetric()
    {
        if(members.size()>0) {
            String memberName = "";
            int weightKg = 0;
            int weightLbs = 0;
            double heightMetres = 0;
            int heightInches = 0;

            for (Member member : members) {
                memberName = member.getName();
                weightKg = (int)member.getStartWeight();
                weightLbs = (int)(weightKg / 0.45359237);
                heightMetres = (double)member.getHeight();
                heightMetres =  toTwoDecimalPlaces(heightMetres);
                heightInches = (int)(heightMetres / 0.0254);

            }

            return (memberName + ": " + weightKg + " kg (" + weightLbs + " lbs) " + heightMetres + " metres (" + heightInches + " inches)");
        }
        else
        {
            return "No registered members";
        }
    }

    // returns a float with 2 decimal places
    private double toTwoDecimalPlaces(double num){
        return (int) (num *100 ) /100.0;
    }

// Push the members and trainers array list out the associated XML file
    public void store() throws Exception
    {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter("members-and-trainers.xml"));
        out.writeObject(members);
        out.writeObject(trainers);
        out.close();

    }

//    Pull the members and trainers Array lists from the associated XML file
    @SuppressWarnings("unchecked")
    public void load() throws Exception
    {
        XStream xstream = new XStream(new DomDriver());

        // ------------------ PREVENT SECURITY WARNINGS---------------------------
        Class<?>[] classes = new Class[] { Member.class, Trainer.class};
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        // -----------------------------------------------------------------------

        ObjectInputStream is = xstream.createObjectInputStream(new FileReader("members-and-trainers.xml"));
        members = (ArrayList<Member>) is.readObject();
        trainers = (ArrayList<Trainer>) is.readObject();
        is.close();
    }
}
