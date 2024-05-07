/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author Tihara
 */
import com.w1953194.model.Prescription;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    // This list stores all the Prescription objects in memory (consider using a database for persistence in real applications)
    private static final List<Prescription> prescriptions = new ArrayList<>();

    /**
     * Retrieves all prescriptions from the in-memory list.
     *
     * @return A list of all Prescription objects.
     */
    public List<Prescription> getAllPrescriptions() {
        // Return a copy of the internal list to avoid modification from outside the class
        return new ArrayList<>(prescriptions);
    }

    /**
     * Finds a prescription by its ID.
     *
     * @param id The ID of the prescription to search for.
     * @return The Prescription object with the matching ID, or null if not
     * found.
     */
    public Prescription getPrescriptionById(long id) {
        for (Prescription prescription : prescriptions) {
            if (prescription.getId() == id) {
                return prescription;
            }
        }
        return null;
    }

    /**
     * Retrieves all prescriptions associated with a specific patient ID.
     *
     * @param patientId The ID of the patient to find prescriptions for.
     * @return A list of Prescription objects for the given patient, or an empty
     * list if none found.
     */
    public List<Prescription> getPrescriptionsByPatientId(long patientId) {
        List<Prescription> prescriptionsForPatient = new ArrayList<>();
        for (Prescription prescription : prescriptions) {
            if (patientId == prescription.getPatient().getId()) {
                prescriptionsForPatient.add(prescription);
            }
        }
        return prescriptionsForPatient;
    }

    /**
     * Adds a new prescription to the in-memory list.
     *
     * @param prescription The Prescription object to add.
     */
    public void addPrescription(Prescription prescription) {
        int newPrescriptionId = getNextPrescriptionId();
        prescription.setId(newPrescriptionId);
        prescriptions.add(prescription);
    }

    /**
     * Updates an existing prescription in the list.
     *
     * @param updatedPrescription The Prescription object with the updated
     * information.
     */
    public void updatePrescription(Prescription updatedPrescription) {
        for (int i = 0; i < prescriptions.size(); i++) {
            if (prescriptions.get(i).getId() == updatedPrescription.getId()) {
                prescriptions.set(i, updatedPrescription);
                return;
            }
        }
    }

    /**
     * Deletes a prescription from the list based on its ID.
     *
     * @param id The ID of the prescription to delete.
     */
    public void deletePrescription(long id) {
        prescriptions.removeIf(prescription -> prescription.getId() == id);
    }

    /**
     * Finds the next available prescription ID by finding the maximum ID in the
     * list and incrementing it.
     *
     * @return The next available ID for a new prescription object.
     */
    private int getNextPrescriptionId() {
        int maxPrescriptionId = 0;
        for (Prescription prescription : prescriptions) {
            int prescriptionId = (int) prescription.getId();
            if (prescriptionId > maxPrescriptionId) {
                maxPrescriptionId = prescriptionId;
            }
        }
        return maxPrescriptionId + 1;
    }
}
