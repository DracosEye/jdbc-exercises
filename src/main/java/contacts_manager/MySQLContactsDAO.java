package contacts_manager;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;

import config.Config;
import dao.MySQLAlbumsException;

import java.util.ArrayList;
import java.util.List;

public class MySQLContactsDAO implements ContactsDAO {

    private Connection connection;

    public MySQLContactsDAO() {
        open();
    }

    public List<Contact> fetchContacts() {
        List<Contact> contacts = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from contacts");
            while (rs.next()) {
                contacts.add(new Contact(rs.getString("name"), rs.getString("phone")));
            }
        }
        catch (SQLException sqlx) {
            System.out.println("Error fetching contacts: " + sqlx.getMessage());
        }
        return contacts;
    }

    public long insertContact(Contact contact) {
        long id = 0;

        try {
            PreparedStatement statement = connection.prepareStatement("insert into contacts (name, phone) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getNumber());

            statement.executeUpdate();
            ResultSet res = statement.getGeneratedKeys();
            res.next();
            id = res.getLong(1);
        }
        catch (SQLException sqlx) {
            System.out.println("Error writing to database: " + sqlx.getMessage());
        }

        return id;
    }

    public void deleteByName(String name) {

    }

    public List<Contact> searchContacts(String searchTerm) {
        return null;
    }

    public void open() {
        System.out.print("Trying to connect... ");
        try {
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );

            System.out.println("Connection created.");
        } catch (SQLException e) {
            throw new MySQLAlbumsException("Connection failed!!!");
        }
    }

    public void close() {
        if(connection == null) {
            System.out.println("Connection aborted.");
            return;
        }
        try {
            connection.close();

            System.out.println("Connection closed.");
        } catch(SQLException e) {
            // ignore this
        }
    }
}
