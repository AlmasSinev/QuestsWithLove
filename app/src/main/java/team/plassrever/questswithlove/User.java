package team.plassrever.questswithlove;

public class User {
    public int id;
    public String firstname, lastName, email, phoneNumber, password;

    public User(int id, String firstname, String lastName, String email, String phoneNumber, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public User(){

    }
}
