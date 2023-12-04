import dao.MySQLQuotesDAO;
import models.Quote;

import java.util.List;

public class QuotesTest {

    public static void main(String[] args) {
        MySQLQuotesDAO quotesDAO = new MySQLQuotesDAO();
        quotesDAO.createConnection();
        List<Quote> quotesFromDb = quotesDAO.getQuotes();
        for (Quote quote : quotesFromDb) {
            System.out.print(quote.getAuthor() + " said: ");
            System.out.println(quote.getContent());
        }
        quotesDAO.closeConnection();
    }
}
