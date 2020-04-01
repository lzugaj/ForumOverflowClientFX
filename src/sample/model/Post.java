package sample.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by lzugaj on Thursday, March 2020
 */

public class Post {

    private Long id;

    private String title;

    private String description;

    private Date createdDate;

    private ContentStatus contentStatus;

    private User user;

    private Category category;

    private List<Comment> comments;

    public Post() {
        // Default constructor
    }

    public Post(String title, String description, Date createdDate,
            ContentStatus contentStatus, User user, Category category) {
        this.title = title;
        this.description = description;
        this.createdDate = createdDate;
        this.contentStatus = contentStatus;
        this.user = user;
        this.category = category;
        this.comments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Post that = (Post) other;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(contentStatus, that.contentStatus)
                && Objects.equals(user, that.user)
                && Objects.equals(category, that.category)
                && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + createdDate.hashCode();
        result = 31 * result + contentStatus.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + category.hashCode();
        result = 31 * result + comments.hashCode();
        return result;
    }

    @Override public String toString() {
        return "Post { " +
                "id = " + id +
                ", title = " + title +
                ", description = " + description +
                ", createdDate = " + createdDate +
                "}";
    }
}
