package ru.job4j.grabber;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement st =
                     cnn.prepareStatement(
                             "insert into posts (link, name, description, created) values (?, ?, ?, ?) on conflict (link) do nothing");
        ) {
            st.setString(1, post.getUrl());
            st.setString(2, post.getName());
            st.setString(3, post.getDescription());
            st.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> itemList = new ArrayList<>();
        try (Statement st = cnn.createStatement();
             ResultSet rs = st.executeQuery("select * from posts")) {
            while (rs.next()) {
                String link = rs.getString("link");
                String name = rs.getString("name");
                String desc = rs.getString("description");
                LocalDateTime date = rs.getTimestamp("created").toLocalDateTime();
                itemList.add(new Post(link, name, desc, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    @Override
    public Post findById(String id) {
        Post rsl = null;
        try (PreparedStatement st = cnn.prepareStatement("select * from posts WHERE id = ?")) {
            st.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    String link = rs.getString("link");
                    String name = rs.getString("name");
                    String desc = rs.getString("description");
                    LocalDateTime date = rs.getTimestamp("created").toLocalDateTime();
                    rsl = new Post(link, name, desc, date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

}
