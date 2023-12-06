package contacts_manager.dao;

import contacts_manager.models.Contact;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileContactsDAO implements ContactsDAO {

    private List<Contact> contactList; // List of contacts for reference throughout the program
    private Path filePath; // Path to file where contacts are stored

    public FileContactsDAO (String fileName) {
        filePath = Paths.get(fileName);
        contactList = fetchContacts();
    }

    public List<Contact> fetchContacts() {
        // Initialize list to return and list to read in lines of file
        List<Contact> retList = new ArrayList<Contact>();
        List<String> contactLines = null;

        // Read all lines of file
        try {
            contactLines = Files.readAllLines(filePath);
        } catch (IOException e) {
            System.out.printf("Error reading file %s\n", filePath.getFileName());
        }

        // Create a new contact for each line of the file and add it to the return list
        for (String contactline : contactLines) {
            String[] contactarr = contactline.split("\\|", 2);
            Contact contact = new Contact(contactarr[0].trim(), contactarr[1].trim());
            retList.add(contact);
        }
        return retList;
    }

    public long insertContact(Contact contact) {
        contactList.add(contact);
        writeFile();
        return 0;
    }

    public void deleteByName(String name) {
        for (Contact contact : contactList) {
            if (contact.getName().equalsIgnoreCase(name)) {
                contactList.remove(contact);
                writeFile();
                return;
            }
        }
        System.out.println("Contact not found.");
    }

    public List<Contact> searchContacts(String searchTerm) {

        List<Contact> retList = new ArrayList<>();
        for (Contact contact : contactList) {
            if (contact.getName().toLowerCase().startsWith(searchTerm)) {
                retList.add(contact);
            }
        }
        return retList;
    }

    public void open() {
        // No connection to open
    }

    public void close() {
        // No connection to close
    }

    // Update file based on List of contacts
    public void writeFile () {
        List<String> listToWrite = new ArrayList<>();

        // Build list of contacts from HashMap
        for (Contact contact : contactList) {
            listToWrite.add(contact.toString());
        }
        // Write list to file
        try {
            Files.write(filePath, listToWrite);
        } catch (IOException iox) {
            System.out.println("Error writing to file " + iox.getMessage());
        }
    }
}
