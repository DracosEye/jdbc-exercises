package dao;

public class MySQLAlbumsTest {
    public static void main(String[] args) {
        MySQLAlbumsDAO albumsDao = new MySQLAlbumsDAO();

        try {
            albumsDao.createConnection();

            System.out.println("Using the connection...");
            int numAlbums = albumsDao.getTotalAlbums();
            System.out.println("Total # of album records: " + numAlbums);
        } catch(MySQLAlbumsException e) {
            System.out.println(e.getMessage());
        } finally {
            albumsDao.closeConnection();
        }

    }
}
