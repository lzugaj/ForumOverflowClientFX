package sample.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by lzugaj on Thursday, March 2020
 */

public class Category {

    private Long id;

    private String name;

    private List<Post> posts;

    public Category() {
        // Default constructor
    }

    public Category(String name) {
        this.name = name;
        this.posts = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Category that = (Category) other;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(posts, that.posts);
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + name.hashCode();
        result = 31 * result + posts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ContentStatus { " +
                "id = " + id +
                ", name = " + name +
                ", posts = " + Arrays.toString(posts.toArray()) +
                "}";
    }
}
