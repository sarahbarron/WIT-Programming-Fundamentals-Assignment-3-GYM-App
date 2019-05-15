/*
Person class - is the super class for a member, student member, premium member and trainer.
It stores a name, email address, address and gender.
*/
public class Person {
    private String name;
    private String email;
    private String address;
    private String gender;

    // Set the persons name with a max length of 30 characters
    public void setName(String name) {

        if(name.length()>30)
        {
           this.name = name.trim().substring(0,30);
        }
        else {
            this.name = name;
        }
    }

    // Set the persons address
    public void setAddress(String address) {
        this.address = address;
    }

    // Set the persons gender to m for male, f for female or unspecified if neither
    public void setGender(String gender) {
        if(gender.equalsIgnoreCase("M") || gender.equalsIgnoreCase("F"))
        {
            this.gender = gender;
        }
        else {
            this.gender = "Unspecified";
        }
    }

    // Get the persons name
    public String getName() {
        return name;
    }

    // Get the persons email address
    public String getEmail() {
        return email;
    }

    // Get the persons address
    public String getAddress() {
        return address;
    }

    // Get the persons gender
    public String getGender() {
        return gender;
    }

    // Constructor method for a person with email address, name, address and gender
    public Person(String email, String name, String address, String gender) {
        setName(name);
        this.email = email;
        this.address = address;
        setGender(gender);
    }

    // empty constructor method
    public Person(){

    }

    // toString method for Person class
    public String toString() {
        return "Name: "+name+ ", Address: "+address+", Email: "+email+", Gender: "+gender +" , ";
    }
}
