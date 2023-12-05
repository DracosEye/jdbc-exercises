package lectures;

import dao.MySQLUsersDAO;

import java.util.Scanner;

public class UserInputTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        MySQLUsersDAO usersDAO = new MySQLUsersDAO();
        usersDAO.createConnection();

        // get a login and pw from the user
        System.out.print("Enter login: ");
        String login = input.nextLine();

        System.out.print("Enter password: ");
        String pw = input.nextLine();

        // see if that user exists in the users db table
        if(usersDAO.checkCredentialsSafe(login, pw) == true) {
            System.out.println("You are logged in!");
        } else {
            System.out.println("Invalid credentials!!! Calling cops");
        }

        usersDAO.closeConnection();

        input.close();
    }
}