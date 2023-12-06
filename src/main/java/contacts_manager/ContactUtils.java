package contacts_manager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ContactUtils {

    private HashMap<String, Contact> contactList; // List of contacts for reference throughout the program
    Path filePath; // Path to file where contacts are stored

    public ContactUtils (String fileName) {
        filePath = Paths.get(fileName);
        contactList = new HashMap<>();
    }

    // Build contact list (HashMap) from list of strings
    public void createContacts() {
        List<String> contactLines = readFile();
        for (String contactline : contactLines) {
            String[] contactarr = contactline.split("\\|",2);
            Contact contact = new Contact(contactarr[0].trim(), contactarr[1].trim());
            contactList.put(contactarr[0].trim(), contact);
        }
    }

    // Builds list from file
    public List<String> readFile() {
        List<String> linesInFile = new ArrayList<>();
        try {
            linesInFile = Files.readAllLines(filePath);
        } catch (IOException iox){
            iox.printStackTrace();
        }
        return linesInFile;
    }

    // Update file based on HashMap of contacts
    public void writeFile () {
        List<String> listToWrite = new ArrayList<>();

        // Build list of contacts from HashMap
        for (String name : contactList.keySet()) {
            listToWrite.add(contactList.get(name).toString());
        }
        // Write list to file
        try {
            Files.write(filePath, listToWrite);
        } catch (IOException iox) {
            System.out.println("Error writing to file " + iox.getMessage());
        }
    }

    public void addContact (Scanner scanner) {

        String newName = "";
        String newNumber = "";

        System.out.println("Enter name of new contact.");
        newName = scanner.nextLine();
        // If name is already in contacts, give the option of overwriting
        // Uses recursion
        if (getContact(newName) != null) {
            System.out.println("That name is already in your contacts. Overwrite (y/n)?");
            String overwrite = scanner.nextLine();
            // If user does not wish to overwrite, use recursion to continually prompt user for name
            if (!overwrite.equalsIgnoreCase("y") && !overwrite.equalsIgnoreCase("yes")) {
                addContact (scanner);
                return;
            }
            // If user does wish to overwrite, then update the appropriate contact in the hashmap
            else {
                newNumber = formatNumber(scanner);
                contactList.get(newName).setNumber(newNumber);
                return;
            }
        }
        // Create new contact and insert into hashmap
        newNumber = formatNumber(scanner);
        Contact newContact = new Contact(newName, newNumber);

        contactList.put(newContact.getName(), newContact);
    }

    public void delContact (String name) {
        contactList.remove(name);
    }

    // Standard output for displaying contacts
    public static void displayHeaders() {
        System.out.println("Name                | Phone number |");
        System.out.println("------------------------------------");
    }

    // Display names and numbers
    public void displayContacts() {
        displayHeaders();
        for (String name : contactList.keySet()) {
            System.out.println(contactList.get(name).toString() + "|");
        }
        System.out.println();
    }

    public Contact getContact(String name) {
        return contactList.get(name);
    }

    // Display the contacts that start with the specified search string (case insensitive)
    public void displaySimilarContacts (String searchName) {
        displayHeaders();

        // Build list of contacts whose names start with that specified by the user
        for (String name : contactList.keySet()) {
            if (name.toLowerCase().startsWith(searchName)) {
                System.out.println(contactList.get(name).toString());
            }
        }
        System.out.println();
    }

    // Confirm number is appropriate length (7 or 10 digits) and insert dashes
    public String formatNumber(Scanner scanner) {
        System.out.println("Please enter a phone number that is 7 or 10 digits long" +
                " without any - or ()");
        String phoneNumber = scanner.nextLine();

        // Ensure user entered all numerical digits
        try {
            long number = Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            System.out.println("Not a number try again");
            return formatNumber(scanner);
        }

        // Insert dashes if number is an acceptable length (7 or 10 digits)
        switch (phoneNumber.length()){
            case 10 :
                phoneNumber = phoneNumber.substring(0,6) + "-" + phoneNumber.substring(6);
            case 7 :
                return phoneNumber.substring(0,3) + "-" + phoneNumber.substring(3);
            default :
                System.out.println("invalid number please enter a valid number");
        }
        return formatNumber(scanner);
    }
}
