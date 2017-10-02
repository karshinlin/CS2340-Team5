package edu.gatech.teamraid.ratastic.Model;

/**
 * Created by colby on 9/27/17.
 */

public class User {

    public enum UserType {
      ADMIN, USER;
    }

    public static User currentUser;

    private String name;
    private String username;
    private String email;
    private UserType userType;

    public User(String name, String username, String email, UserType userType) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.userType = userType;
    }

    public UserType getUserType() { return userType; }

    public void setUserType(UserType userType) { this.userType = userType; }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
