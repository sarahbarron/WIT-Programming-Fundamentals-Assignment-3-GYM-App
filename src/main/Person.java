
// THIS CODE IS INCOMPLETE
//given
public class Person {
    //given
    private String name;
    //given
    private String email;
    //given
    private String address;
    //given
    private String gender;

    //given
    public void setName(String name) {

        if(name.length()>30)
        {
           this.name = name.trim().substring(0,30);
        }
        else {
            this.name = name;
        }
    }

    //given
    public void setAddress(String address) {
        this.address = address;
    }

    //given
    public void setGender(String gender) {
        if(gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"))
        {
            this.gender = gender;
        }
        else {
            this.gender = "Unspecified";
        }
    }

    //given
    public String getName() {
        return name;
    }

    //given
    public String getEmail() {
        return email;
    }

    //given
    public String getAddress() {
        return address;
    }

    //given
    public String getGender() {
        return gender;
    }

    //given
    public Person(String email, String name, String address, String gender) {
        //given
        setName(name);
        //given
        this.email = email;
        //given
        this.address = address;
        //given
        setGender(gender);
    }

    // toString method for Person class
    public String toString() {
        return "Person: "+name+"/nAddress: "+address+"/nEmail: "+email+"/nGender: "+gender;
    }
}
