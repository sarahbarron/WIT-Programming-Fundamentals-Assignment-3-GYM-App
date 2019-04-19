import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

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

    //given
    public Member(String email, String name, String address,
                  String gender, float height, float startWeight, String chosenPackage) {
        //given
        super(email, name, address, gender);
        //given
        setHeight(height);
        //given
        setStartWeight(startWeight);
        //given
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

      SortedSet assessments = sortedAssessmentDates();
      String lastDate = assessments.getlast();
      Assessment lastAssessment = assessmentCollection.get(lastDate);
      return lastAssessment;
   }

   public SortedSet sortedAssessmentDates(){
          SortedSet sortedAssessmentDates = new SortedSet(assessmentCollection);
          sortedAssessmentDates.getSortedSet();
          return sortedAssessmentDates;
    }

//    public abstract void chosenPackage(String chosenPackage)
//    {
//          // TODO the concrete implementation of this method will be completed in Member subclasses (pg3)
//    }



    // toString method for Member class
    public String toString() {

        return super.toString()+"/nHeight: "+height+"/nStart Weight: "+startWeight+"/nChosen Package: "+chosenPackage;
    }
}

