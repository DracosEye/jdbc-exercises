package contacts_manager.dao;

import contacts_manager.models.Contact;

import java.util.List;

public interface ContactsDAO {

    List<Contact> fetchContacts();
    long insertContact(Contact contact);
    void deleteByName(String name);
    List<Contact> searchContacts(String searchTerm);
    void open();
    void close();
}
