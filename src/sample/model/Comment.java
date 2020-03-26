package sample.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by lzugaj on Thursday, March 2020
 */

public class Comment {

    private Long id;

    private String description;

    private LocalDateTime createdDate;

    private ContentStatus contentStatus;

    private User user;

    private Post post;

    public Comment() {
        // Default constructor
    }

    public Comment(String description, LocalDateTime createdDate,
            ContentStatus contentStatus, User user, Post post) {
        this.description = description;
        this.createdDate = createdDate;
        this.contentStatus = contentStatus;
        this.user = user;
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Comment that = (Comment) other;
        return Objects.equals(id, that.id)
                && Objects.equals(description, that.description)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(contentStatus, that.contentStatus)
                && Objects.equals(user, that.user)
                && Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + description.hashCode();
        result = 31 * result + createdDate.hashCode();
        result = 31 * result + contentStatus.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + post.hashCode();
        return result;
    }

    @Override public String toString() {
        return "Comment { " +
                "id = " + id +
                ", description = " + description +
                ", createdDate = " + createdDate +
                ", contentStatus = " + contentStatus +
                ", user = " + user +
                ", post = " + post +
                "}";
    }
}
