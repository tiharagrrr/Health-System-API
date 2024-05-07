/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author Tihara
 */

import com.w1953194.model.Person;
import java.util.ArrayList;
import java.util.List;


public class PersonDAO {

    // This list stores all the Person objects in memory (consider using a database for persistence in real applications)
    private static final List<Person> persons = new ArrayList<>();

    // Static block to initialize the list with some sample people for testing purposes
    static {
        // Initialize with some people
        Person p1 = new Person("Tee", "091992", "no28");
        Person p2 = new Person("Jane Doe", "2234", "no23");
        // Set IDs separately as these don't run through addPerson and get an ID generated
        p1.setId(1);
        p2.setId(2);
        persons.add(p1);
        persons.add(p2);
    }

    /**
     * Retrieves all people from the in-memory list.
     *
     * @return A list of all Person objects.
     */
    public List<Person> getAllPersons() {
        // Return a copy of the internal list to avoid modification from outside the class
        return new ArrayList<>(persons);
    }

    /**
     * Finds a person by their ID.
     *
     * @param id The ID of the person to search for.
     * @return The Person object with the matching ID, or null if not found.
     */
    public Person getPersonById(int id) {
        for (Person person : persons) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }

    /**
     * Adds a new person to the in-memory list.
     *
     * @param person The Person object to add.
     */
    public void addPerson(Person person) {
        int newUserId = getNextUserId();
        person.setId(newUserId);
        persons.add(person);
    }

    /**
     * Updates an existing person in the list.
     *
     * @param updatedPerson The Person object with the updated information.
     */
    public void updatePerson(Person updatedPerson) {
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person.getId() == updatedPerson.getId()) {
                persons.set(i, updatedPerson);
                // Consider removing this for production use (prints to console)
                System.out.println("Updated: " + updatedPerson);
                return;
            }
        }
    }

    /**
     * Deletes a person from the list based on their ID.
     *
     * @param id The ID of the person to delete.
     */
    public void deletePerson(int id) {
        persons.removeIf(person -> person.getId() == id);
    }

    /**
     * Finds the next available person ID by finding the maximum ID in the list
     * and incrementing it.
     *
     * @return The next available ID for a new person object.
     */
    private int getNextUserId() {
        int maxUserId = 0;
        for (Person person : persons) {
            int userId = (int) person.getId();
            if (userId > maxUserId) {
                maxUserId = userId;
            }
        }
        return maxUserId + 1;
    }
}
