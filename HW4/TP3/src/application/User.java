package application;




import java.util.*; // For Map, List, HashMap, etc.


/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private String role;

    // Reviewer weightage mapping (reviewerName → weight)
    private final Map<String, Integer> reviewerWeightage = new HashMap<>();

    // Constructor to initialize a new User object with userName, password, and role.
    public User(String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Sets the role of the user.
    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // Set and get weightage for reviewers
    public void setReviewerWeight(String reviewerName, int weight) {
        reviewerWeightage.put(reviewerName, weight);
    }

    public int getReviewerWeight(String reviewerName) {
        return reviewerWeightage.getOrDefault(reviewerName, 0);
    }
}
