package dao;

import models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLUsersDAO extends MySQLDAO {

    public boolean checkCredentials(String login, String pw) {
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select count(*) as user_count " +
                    "from users " +
                    "where username = '" + login + "' " +
                    "and password = '" + pw + "'");
            rs.next();
            int userCount = rs.getInt("user_count");
            if(userCount > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean checkCredentialsSafe(String login, String pw) {
        try {
            PreparedStatement st = connection.prepareStatement("select count(*) as user_count " +
                    "from users " +
                    "where username = ? " +
                    "and password = ? ");
            st.setString(1, login);
            st.setString(2, pw);
            ResultSet rs = st.executeQuery();
            rs.next();
            int userCount = rs.getInt("user_count");
            if(userCount > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    // returns the new user's id if successful or throws exception if anything bad happens
    public long addUser(String username, String pw, String email) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("insert into blog_users " +
                    " (email, password, username) " +
                    " values(?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, email);
            st.setString(2, pw);
            st.setString(3, username);

            int numInserted = st.executeUpdate();

            ResultSet keys = st.getGeneratedKeys();
            keys.next();

            long newId = keys.getLong(1);
            return newId;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void deleteUser(long id) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement("delete from blog_users " +
                    " where id = ? ");
            st.setLong(1, id);

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User fetchUserById(long id) {
        User user = null;
        try {
            PreparedStatement st = connection.prepareStatement("select * from blog_users " +
                    " where id = ? ");
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            rs.next();

            user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return user;
    }

    public void updateUser(User user) {
        try {
            PreparedStatement st = connection.prepareStatement("update blog_users " +
                    " set username = ? " +
                    " , password = ? " +
                    " , email = ? " +
                    " where id = ? ");
            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getEmail());
            st.setLong(4, user.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}