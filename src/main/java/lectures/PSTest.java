package lectures;

import dao.MySQLUsersDAO;
import models.User;

public class PSTest {
    // play around with insert, update, delete using prepared statements
    public static void main(String[] args) {
        MySQLUsersDAO usersDAO = new MySQLUsersDAO();
        usersDAO.createConnection();

        long newUserId = usersDAO.addUser("bubba", "shrimp", "bubba@gump.com");
        System.out.println(newUserId);

        User user = usersDAO.fetchUserById(newUserId);
        System.out.println(user);

        user.setUsername("jimbo");
        usersDAO.updateUser(user);

        usersDAO.deleteUser(newUserId);

        usersDAO.closeConnection();
    }
}