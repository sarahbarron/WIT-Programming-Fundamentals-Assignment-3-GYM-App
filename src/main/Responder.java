import java.util.*;

/*
 The responder class is used to generate an automatic response, based on user input in the terminal.
 The responder uses a HashMap to link words with a response. If any of the user input words is found in the HashMap
 the corresponding response is returned. If none of the inputted words are found in the HashMap,
 one of the default responses is randomly chosen.
*/
public class Responder {

  // Used to map key words to responses.
  private HashMap<String, String> responseMap;
  // Default responses to use if we don't recognise a word.
  private ArrayList<String> defaultResponses;
  private Random randomGenerator;

  // Responder Constructor method
  public Responder() {

    responseMap = new HashMap<String, String>();
    defaultResponses = new ArrayList<String>();
    fillResponseMap();
    fillDefaultResponses();
    randomGenerator = new Random();
  }

  /*
  A set of words are passed to the generateResponse method. for each word in the set check the HashMap
  to see if the word is a key to a response. If it is a key return the response string otherwise generate
  a random response
  */
  public String generateResponse(HashSet<String> words) {

    Iterator<String> it = words.iterator();
    while (it.hasNext()) {
      String word = it.next();
      String response = responseMap.get(word);
      if (response != null) {
        return response;
      }
    }
    // when none of the words are a key in the HashMap then pick a default response
    return pickDefaultResponse();
  }


  // A method to build a HashMap of keywords and associated responses
  private void fillResponseMap() {
    responseMap.put("gained",
        "I know you must feel frustrated by this gain but don't give up.\n" +
            "By sticking to the exercise plan we have created just for you and maintaining a healthy " +
            "lifestyle you will get the results you desire");
    responseMap.put("bmi",
        "Your BMI is calculated as follows:\n" +
            "weight divided by the square of the height.");
    responseMap.put("ideal",
        "Your ideal weight is calculated based on the Devine formula. \n" +
            "If you have not reached your ideal weight yet, don't worry with our help you will get there");
    responseMap.put("assessments",
        "Assessments are carried out weekly at the gym.\n Your personal trainer will set a day and time" +
            " every week for you to get weighed and take measurements.\n Your personal trainer will then " +
            "add your results as an assessment to your dashboard.\n This helps both you and your Personal " +
            "trainer to track your results over time and make sure you are on track to reach your goal");
    responseMap.put("profile",
        "If you need to update your profile at any stage you can do this from the main menu,\n" +
            "by entering the number 2.\n You can update your name, address or gender. To return to the main " +
            "menu type 'bye'");
    responseMap.put("unmotivated",
        "I'm sorry to hear you are unmotivated, but dont worry this happens to us all regularly.\n" +
            "Remember the reasons why you joined the gym in the first place. Remember your goals.\n" +
            "You won't always love the workout, but you will love the results.\n" +
            "Please pop into the gym as soon as you can so that we can help to get that motivation" +
            " back on track.");
    responseMap.put("bad",
        "I know you must feel frustrated by the recent bad results but don't give up.\n" +
            "By sticking to the exercise plan we have created just for you and maintaining a healthy " +
            "lifestyle you will get the results you desire");
    responseMap.put("1",
        "So you want some more information on Package 1.\n" +
            "With Package 1 you are allowed access anytime to gym.\n You have free access to all classes." +
            "\n and you have access to all changing areas including deluxe changing rooms.");
    responseMap.put("2",
        "So you want some more information on Package 2.\n With Package 2 you are allowed access anytime to gym.\n" +
            "You will have to pay €3 for all classes.\n You will have access to all changing areas " +
            "including deluxe changing rooms.");
    responseMap.put("3",
        "So you want more information on Package 3. \n With Package 3 you are allowed access to gym at off-peak " +
            "times.\n You will have to pay €5 for all classes. \n You will have no access to deluxe changing rooms.");
    responseMap.put("wit",
        "So you want to know more about our student package WIT. \n With the student WIT package you are allowed " +
            "access to gym during term time.\n You will have to pay €4 for all classes.  \nYou will have no access " +
            "to deluxe changing rooms.");
    responseMap.put("packages",
        "So you want more information about our packages: \n " +
            "Package 1: you are allowed access anytime to gym. You have free access to all classes." +
            "You have access to all changing areas including deluxe changing rooms.\n Package 2:  " +
            "you are allowed access anytime to gym. You will have to pay €3 for all classes. You will have access " +
            "to all changing areas including deluxe changing rooms. \n Package 3: you are allowed access to gym " +
            "at off-peak times. You will have to pay €5 for all classes. You will have no access to deluxe " +
            "changing rooms. \n Student Package 'WIT': you are allowed access to gym during term time. You will have to " +
            "pay €4 for all classes. You will have no access to deluxe changing rooms.");
    responseMap.put("next",
        "The next class to take place in the Gym is a Spinning class at 7pm tonight. \n We look forward to seeing you " +
            "there");
  }

  //A method to build a list of default responses used when there is no match in the responseMap HashMap.
  private void fillDefaultResponses() {
    defaultResponses.add("Could you describe that problem in more detail?");
    defaultResponses.add("I need a bit more information on that.");
    defaultResponses.add("Could you elaborate on that?");
    defaultResponses.add("Hmmm I am not sure what you are looking for could you please call the Gym on 051-4353734" +
        " and someone will be able to help you with that question");
  }

  // A method to randomly pick a default response, by randomly picking an index from the default response list
  private String pickDefaultResponse() {
    int index = randomGenerator.nextInt(defaultResponses.size());
    return defaultResponses.get(index);
  }
}
