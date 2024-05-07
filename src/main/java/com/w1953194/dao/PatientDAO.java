/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author Tihara
 */


import java.util.ArrayList;
import java.util.List;

import com.w1953194.model.Patient;

public class PatientDAO {

    // This list stores all the Patient objects in memory (consider using a database for persistence in real applications)
    private static final List<Patient> patients = new ArrayList<>();

    // Static block to initialize the list with some sample patients for testing purposes
    static {
        // Initialize with some patients
        Patient pat1 = new Patient("John Doe", "123456", "no12", "medicalHistory1", "currentHealthStatus1");
        Patient pat2 = new Patient("Jane Smith", "654321", "no34", "medicalHistory2", "currentHealthStatus2");
        // Set IDs separately as these don't run through addPatient and get an ID generated
        pat1.setId(1);
        pat2.setId(2);
        patients.add(pat1);
        patients.add(pat2);
    }

    /**
     * Retrieves all patients from the in-memory list.
     *
     * @return A list of all Patient objects.
     */
    public List<Patient> getAllPatients() {
      
        return new ArrayList<>(patients);
    }

    /**
     * Finds a patient by their ID.
     *
     * @param id The ID of the patient to search for.
     * @return The Patient object with the matching ID, or null if not found.
     */
    public Patient getPatientById(int id) {
        for (Patient patient : patients) {
            if (patient.getId() == id) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Adds a new patient to the in-memory list.
     *
     * @param patient The Patient object to add.
     */
    public void addPatient(Patient patient) {
        int newUserId = getNextUserId();
        patient.setId(newUserId);
        patients.add(patient);
    }

    /**
     * Updates an existing patient in the list.
     *
     * @param updatedPatient The Patient object with the updated information.
     */
    public void updatePatient(Patient updatedPatient) {
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            if (patient.getId() == updatedPatient.getId()) {
                patients.set(i, updatedPatient);
                // Consider removing this for production use (prints to console)
                System.out.println("updated: " + updatedPatient);
                return;
            }
        }
    }

    /**
     * Deletes a patient from the list based on their ID.
     *
     * @param id The ID of the patient to delete.
     */
    public void deletePatient(int id) {
        patients.removeIf(patient -> patient.getId() == id);
    }

    /**
     * Finds the next available patient ID by finding the maximum ID in the list
     * and incrementing it.
     *
     * @return The next available ID for a new patient object.
     */
    private int getNextUserId() {
        int maxUserId = 0;
        for (Patient patient : patients) {
            int userId = (int) patient.getId();
            if (userId > maxUserId) {
                maxUserId = userId;
            }
        }
        return maxUserId + 1;
    }
}
