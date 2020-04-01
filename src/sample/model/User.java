package sample.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by lzugaj on Thursday, March 2020
 */

public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private int blockerCounter;

    private UserStatus userStatus;

    private List<Post> posts;

    private List<Comment> comments;

    private List<Role> roles;

    public User() {
        // Default constructor
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String firstName, String lastName,
            String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.blockerCounter = 0;
        this.posts = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBlockerCounter() {
        return blockerCounter;
    }

    public void setBlockerCounter(int blockerCounter) {
        this.blockerCounter = blockerCounter;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        User that = (User) other;
        return Objects.equals(id, that.id)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(username, that.username)
                && Objects.equals(email, that.email)
                && Objects.equals(password, that.password)
                && Objects.equals(blockerCounter, that.blockerCounter)
                && Objects.equals(userStatus, that.userStatus)
                && Objects.equals(posts, that.posts)
                && Objects.equals(comments, that.comments)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + String.valueOf(blockerCounter).hashCode();
        result = 31 * result + userStatus.hashCode();
        result = 31 * result + posts.hashCode();
        result = 31 * result + comments.hashCode();
        result = 31 * result + roles.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User { " +
                "id = " + id +
                ", firstName = " + firstName +
                ", lastName = " + lastName +
                ", username = " + username +
                ", email = " + email  +
                ", password = " + password +
                ", blockerCounter = " + blockerCounter +
                "}";
    }
}
