package dao;

import com.mysql.cj.jdbc.Driver;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import config.Config;
import models.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLAlbumsDAO extends MySQLDAO{
    // initialize the connection to null so we know whether or not to close it when done

    public void createConnection() throws MySQLAlbumsException {
        System.out.print("Trying to connect... ");
        try {
            //TODO: create the connection and assign it to the instance variable
            DriverManager.registerDriver(new Driver());
            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );

            System.out.println("connection created.");
        } catch (SQLException e) {
            throw new MySQLAlbumsException("connection failed!!!");
        }
    }

    protected int getTotalAlbums() throws MySQLAlbumsException {
        int count = 0;
        try {
            //TODO: fetch the total number of albums from the albums table and assign it to the local variable
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM albums");
            while (resultSet.next()) {
                count++;
            }

        } catch (SQLException e) {
            throw new MySQLAlbumsException("Error executing query: " + e.getMessage() + "!!!");
        }
        return count;
    }

    public void closeConnection() {
        if(connection == null) {
            System.out.println("Connection aborted.");
            return;
        }
        try {
            //TODO: close the connection
            connection.close();

            System.out.println("Connection closed.");
        } catch(SQLException e) {
            // ignore this
        }
    }

    public List<Album> fetchAlbums() {
        List<Album> albums = new ArrayList<>();

        // TODO: write your code here
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from albums");
            while (rs.next()) {
                albums.add(new Album(rs.getInt("id"), rs.getString("artist"), rs.getString("name"), rs.getInt("release_date"), rs.getDouble("sales"), rs.getString("genre")));
            }
        } catch (SQLException sqlx) {
            System.out.println("Error fetching albums: " + sqlx.getMessage());
        }


        return albums;
    }

    public Album fetchAlbumById(long id) {
        Album album = null;

        // TODO: write your code here
        try {
            PreparedStatement statement = connection.prepareStatement("select * from albums where id = ?");
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            album = new Album(rs.getInt("id"), rs.getString("artist"), rs.getString("name"), rs.getInt("release_date"), rs.getDouble("sales"), rs.getString("genre"));
        }
        catch (SQLException sqlx) {
            System.out.println("Error fetching record: " + sqlx.getMessage());
        }

        return album;
    }

    // Note that insertAlbum should return the id that MySQL creates for the new inserted album record
    public long insertAlbum(Album album) throws MySQLAlbumsException {
        long id = 0;

        // TODO: write your code here
        try {
            // Prepare record for insertion
            PreparedStatement statement = connection.prepareStatement("insert into albums ( artist, name, release_date, sales, genre) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, album.getArtist());
            statement.setString(2, album.getName());
            statement.setInt(3, album.getReleaseDate());
            statement.setDouble(4, album.getSales());
            statement.setString(5, album.getGenre());

            // Insert record and fetch ID
            statement.executeUpdate();
            ResultSet res = statement.getGeneratedKeys();
            res.next();
            id = res.getLong(1);

        }
        catch (SQLException sqlx) {
            System.out.println("Failed to insert new album: " + sqlx.getMessage());
        }

        return id;
    }

    public void updateAlbum(Album album) throws MySQLAlbumsException {

        try {
            PreparedStatement statement = connection.prepareStatement("update albums " +
                    "set artist = ? " +
                    ", name = ? " +
                    ", release_date = ? " +
                    ", sales = ? " +
                    ", genre = ? " +
                    "where id = ?");
            statement.setString(1, album.getArtist());
            statement.setString(2, album.getName());
            statement.setInt(3, album.getReleaseDate());
            statement.setDouble(4, album.getSales());
            statement.setString(5, album.getGenre());
            statement.setLong(6, album.getId());
            statement.executeUpdate();
        }
        catch (SQLException sqlx) {
            System.out.println("Error updating record: " + sqlx.getMessage());
        }

    }

    public void deleteAlbumById(long id) throws MySQLAlbumsException {

        try {
            PreparedStatement statement = connection.prepareStatement("delete from albums " +
                    "where id = ?");
            statement.setLong(1, id);
            statement.executeUpdate();
        }
        catch (SQLException sqlx) {
            System.out.println("Error deleting album: " + sqlx.getMessage());
        }

    }

    public static void main(String[] args) {
        MySQLAlbumsDAO albumsDao = new MySQLAlbumsDAO();

        try {
            albumsDao.createConnection();

            System.out.println("Using the connection...");
            int numAlbums = albumsDao.getTotalAlbums();
            System.out.println("Total # of album records: " + numAlbums);

            // fetch all albums and print the size
            // this should equal the above Total # of album records:
            List<Album> albums = albumsDao.fetchAlbums();
            System.out.println("Number of album records: " + albums.size());

            // fetch and print a single album (the album with id 1 in the db)
            Album album = albumsDao.fetchAlbumById(2L);
            System.out.println("Album with id 2: " + album);

            // insert a new album
            Album newAlbum = new Album();
            newAlbum.setName("Wish You Were Hear");
            newAlbum.setArtist("Pink Floyd");
            newAlbum.setReleaseDate(1975);
            newAlbum.setGenre("Progressive Rock");
            newAlbum.setSales(9.2);
            long newId = albumsDao.insertAlbum(newAlbum);
            System.out.println("Id for new album: " + newId);
            newAlbum.setId(newId);

            // fix a mistake in the new album
            newAlbum.setName("Wish You Were Here");
            albumsDao.updateAlbum(newAlbum);

            // let's confirm that the update worked
            album = albumsDao.fetchAlbumById(newId);
            System.out.println("Fixed album: " + album);

            // delete the album
            albumsDao.deleteAlbumById(newId);
            System.out.println("Album with id " + newId + " deleted");

        } catch(MySQLAlbumsException e) {
            System.out.println(e.getMessage());
        } finally {
            albumsDao.closeConnection();
        }

    }

}