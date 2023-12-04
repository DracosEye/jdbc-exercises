import com.mysql.cj.jdbc.Driver;
import config.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuotesTest {

    public static List<String> getQuotes() {
        Connection connection = null;
        List<String> quotes = new ArrayList<>();
        try {
            // Register driver
            DriverManager.registerDriver(new Driver());
            // Establish connection
            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );
            // Create statement object
            Statement statement = connection.createStatement();

            // Execute statement
            ResultSet resultSet = statement.executeQuery("SELECT content FROM quotes");
            while (resultSet.next()) {
                quotes.add(resultSet.getString("content"));
            }

        }
        catch (SQLException sqlx) {
            System.out.println(sqlx.getMessage());
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException sqlx) {
                    System.out.println(sqlx.getMessage());
                }
            }
        }
        return quotes;
    }

    public static void main(String[] args) {
        List<String> quotesFromDb = getQuotes();
        for (String quote : quotesFromDb) {
            System.out.println(quote);
        }
    }
}
