package edu.gatech.teamraid.ratastic.Model;


/**
 * Class that stores user information for the user that is currently logged in through
 *  Firebase auth.

 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 * 10/1/17:  CTOBIN   Created.
 * 10/9/17:  KLIN     Added getUserType method. Added comments
 *
 */

public class User {
    /**
     * Enum for the two different userTypes that are possible
     */
    public enum UserType {
      ADMIN, USER;

        /**
         * Method that matches string usertype with the Enum type
         * @param usertype String to be converted
         * @return  matched Enum type
         */
        public static UserType getUserType(String usertype) {
            if (usertype.equalsIgnoreCase("user")) {
                return UserType.USER;
            } else if (usertype.equalsIgnoreCase("admin")) {
                return UserType.ADMIN;
            }
            return null;
        }
    }

    /**
     * Singleton reference for the user that is currently logged in
     */
    public static User currentUser;

    /**
     * The name of the user. Also shown as Display Name in Firebase
     */
    private String name;

    /**
     * Username of the user. Currently his or her email
     */
    private String username;

    /**
     * Email of the user.
     */
    private String email;

    /**
     * Usertype of the user. Enum is above
     */
    private UserType userType;

    /**
     * Constructor for a user
     * @param name name of user
     * @param username username provided
     * @param email email provided
     * @param userType type of user indicated
     */
    public User(String name, String username, String email, UserType userType) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.userType = userType;
        //mDatabase = FirebaseDatabase.getInstance().getReference("users");

    }

    /**
     * Retrieves usertype
     * @return the usertype
     */
    public UserType getUserType() { return userType; }

    /**
     * Gets user's display name
     * @return display name
     */
    public String getName() {
        return name;
    }


    /**
     * Set display name
     * @param name name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
