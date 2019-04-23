import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;


// THIS CODE IS INCOMPLETE
//given
public class Member extends Person {
    //given
    private float height;
    //given
    private float startWeight;
    //given
    private String chosenPackage;

    private HashMap<String, Assessment> assessmentCollection;

    // height = metres, weight = kgs
    public Member(String email, String name, String address,
                  String gender, float height, float startWeight, String chosenPackage) {

        super(email, name, address, gender);
        setHeight(height);
        setStartWeight(startWeight);
        setChosenPackage(chosenPackage);
//        create a Collection to store Members Assessments
        assessmentCollection = new HashMap<String , Assessment>();
    }

    //given
    public float getHeight() {
        return height;
    }

    //given
    public void setHeight(float height) {
        if (height >= 1 && height <= 3.0) {
            this.height = height;
        } else {
            this.height = 0;
        }
    }

    //given
    public float getStartWeight() {
        return startWeight;
    }

    //given
    public void setStartWeight(float startWeight) {
        if (startWeight >= 35 && startWeight <= 250) {
            this.startWeight = startWeight;
        } else {
            this.startWeight = 0;
        }
    }


    //given
    public String getChosenPackage() {
        return chosenPackage;
    }

    //given
    public void setChosenPackage(String chosenPackage) {
        this.chosenPackage = chosenPackage;
    }

//    add an assessment to the members collection of assessments
    public void addAssessment(Assessment assessment)
    {
        Date dateObject = new Date();
        String date = formatDate(dateObject);
        assessmentCollection.put(date, assessment);
    }

//  format the date to a string YY/MM/DD eg 18/05/04
    public String formatDate(Date dateObject) {
        Format formatDate = new SimpleDateFormat("yy/mm/dd");
        return formatDate.format(dateObject);
    }

    public Assessment latestAssessment(){

      SortedSet sortedSet = sortedAssessmentDates();
      String lastDate = (String)sortedSet.last();
      Assessment lastAssessment = assessmentCollection.get(lastDate);
      return lastAssessment;
   }

   public SortedSet sortedAssessmentDates(){

        SortedSet<String> sortedSet = new TreeSet();
        sortedSet.addAll(assessmentCollection.keySet());
        return sortedSet;
    }

//    public abstract void chosenPackage(String chosenPackage)
//    {
//          // TODO the concrete implementation of this method will be completed in Member subclasses (pg3)
//    }


    public HashMap<String, Assessment> getAssessments() {
        return assessmentCollection;
    }


    // toString method for Member class
    public String toString() {

        return super.toString()+"Height: "+height+" , Start Weight: "+startWeight+" , Chosen Package: "+chosenPackage;
    }
}

