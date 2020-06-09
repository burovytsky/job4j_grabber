package ru.job4j.html;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post {
    private String url;
    private String name;
    private String description;
    private LocalDateTime created;

    public Post(String url, String name, String description, LocalDateTime created) {
        this.url = url;
        this.name = name;
        this.description = description;
        this.created = created;
    }

    public String getId() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(url, post.url) &&
                Objects.equals(name, post.name) &&
                Objects.equals(description, post.description) &&
                Objects.equals(created, post.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, name, description, created);
    }

    @Override
    public String toString() {
        return "Post{" +
                "url=" + url +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", created=" + created +
                '}';
    }
}
