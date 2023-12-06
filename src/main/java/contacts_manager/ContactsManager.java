package contacts_manager;

import contacts_manager.dao.ContactsDAO;
import contacts_manager.dao.MySQLContactsDAO;
import contacts_manager.models.Contact;

import java.util.List;
import java.util.Scanner;

public class ContactsManager {

    private static Scanner scanner;

    // Standard output for displaying contacts
    public static void displayHeaders() {
        System.out.println("Name                | Phone number |");
        System.out.println("------------------------------------");
    }

    public static void displayContacts(List<Contact>contactList) {
        displayHeaders();
        for (Contact contact : contactList) {
            System.out.println(contact.toString() + "|");
        }
        System.out.println();
    }

    private static String formatNumber() {
        System.out.println("Please enter a phone number that is 7 or 10 digits long" +
                " without any - or ()");
        String phoneNumber = scanner.nextLine();

        // Ensure user entered all numerical digits
        try {
            long number = Long.parseLong(phoneNumber);
        } catch (NumberFormatException e) {
            System.out.println("Not a number try again");
            return formatNumber();
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
        return formatNumber();
    }

    public static void main(String[] args) {

        String PATH_TO_FILE = "data/contacts.txt"; // File used for file-based data storage
        scanner = new Scanner(System.in);
        String userResponse; // Menu choice
        boolean done = false; // Used to terminate menu loop

        ContactsDAO contactsDAO = new MySQLContactsDAO();

        System.out.println("Welcome to the Contacts Manager!\n");
        // Display menu with choices
        while (!done) {
            System.out.println("1. View contacts.");
            System.out.println("2. Add a new contact.");
            System.out.println("3. Search a contact by name.");
            System.out.println("4. Delete an existing contact.");
            System.out.println("5. Exit.\n");
            System.out.println("Enter an option (1-5): ");

            userResponse = scanner.nextLine();
            switch (userResponse) {
                case "1": // View contacts
                    displayContacts(contactsDAO.fetchContacts());
                    break;
                case "2": // Add a new contact
                    List<Contact> curContacts = contactsDAO.fetchContacts();
                    boolean duplicateContact;
                    String newName, overwrite;
                    do {
                        duplicateContact = false;
                        System.out.println("Enter name of new contact:");
                        newName = scanner.nextLine();
                        // For updating existing contacts
                        for (Contact curContact : curContacts) {
                            if (curContact.getName().equalsIgnoreCase(newName)) {
                                System.out.println("Contact already exists. Overwrite (y/n)?");
                                overwrite = scanner.nextLine();
                                // User does not wish to overwrite --> ask for another name
                                if (!overwrite.equalsIgnoreCase("y") && !overwrite.equalsIgnoreCase("yes")) {
                                    duplicateContact = true;
                                }
                                // User does wish to overwrite --> delete existing contact and create
                                // new one with the same name
                                else {
                                    contactsDAO.deleteByName(newName);
                                }
                                break;
                            }
                        }
                    } while (duplicateContact);
                    contactsDAO.insertContact(new Contact(newName, formatNumber()));
                    break;
                case "3": // Search a contact by name
                    System.out.println("Enter name you would like to search for");
                    String searchName = scanner.nextLine();
                    displayContacts(contactsDAO.searchContacts(searchName));
                    break;
                case "4": // Delete an existing contact
                    System.out.println("Enter name of contact you would like to delete");
                    String deleteName = scanner.nextLine();
                    contactsDAO.deleteByName(deleteName);
                    break;
                case "5": // Exit
                    done = true;
                    scanner.close();
                    contactsDAO.close();
                    break;
                default: // Entered something other than 1-5
                    System.out.println("Invalid response. Try again.\n");
            }
        }
    }
}
