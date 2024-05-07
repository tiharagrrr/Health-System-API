/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author Tihara
 */
import com.w1953194.model.MedicalRecord;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordDAO {

    // This list stores all the MedicalRecord objects in memory (consider using a database for persistence in real applications)
    private static final List<MedicalRecord> medicalRecords = new ArrayList<>();

    /**
     * Retrieves all medical records from the in-memory list.
     *
     * @return A list of all MedicalRecord objects.
     */
    public List<MedicalRecord> getAllMedicalRecords() {
        // Return a copy of the internal list to avoid modification from outside the class
        return new ArrayList<>(medicalRecords);
    }

    /**
     * Finds a medical record by its ID.
     *
     * @param id The ID of the medical record to search for.
     * @return The MedicalRecord object with the matching ID, or null if not
     * found.
     */
    public MedicalRecord getMedicalRecordById(long id) {
        for (MedicalRecord record : medicalRecords) {
            if (record.getId() == id) {
                return record;
            }
        }
        return null;
    }

    /**
     * Retrieves all medical records associated with a specific patient ID.
     *
     * @param patientId The ID of the patient to find medical records for.
     * @return A list of MedicalRecord objects for the given patient, or an
     * empty list if none found.
     */
    public List<MedicalRecord> getMedicalRecordsByPatientId(long patientId) {
        List<MedicalRecord> recordsForPatient = new ArrayList<>();
        for (MedicalRecord record : medicalRecords) {
            if (patientId == record.getPatient().getId()) {
                recordsForPatient.add(record);
            }
        }
        return recordsForPatient;
    }

    /**
     * Adds a new medical record to the in-memory list.
     *
     * @param medicalRecord The MedicalRecord object to add.
     */
    public void addMedicalRecord(MedicalRecord medicalRecord) {
        int newRecordId = getNextMedicalRecordId();
        medicalRecord.setId(newRecordId);
        medicalRecords.add(medicalRecord);
    }

    /**
     * Updates an existing medical record in the list.
     *
     * @param updatedRecord The MedicalRecord object with the updated
     * information.
     */
    public void updateMedicalRecord(MedicalRecord updatedRecord) {
        for (int i = 0; i < medicalRecords.size(); i++) {
            if (medicalRecords.get(i).getId() == updatedRecord.getId()) {
                medicalRecords.set(i, updatedRecord);
                return;
            }
        }
    }

    /**
     * Deletes a medical record from the list based on its ID.
     *
     * @param id The ID of the medical record to delete.
     */
    public void deleteMedicalRecord(long id) {
        medicalRecords.removeIf(record -> record.getId() == id);
    }

    /**
     * Finds the next available medical record ID by finding the maximum ID in
     * the list and incrementing it.
     *
     * @return The next available ID for a new medical record object.
     */
    private int getNextMedicalRecordId() {
        // Initialize maxRecordId with a value lower than any possible recordId
        int maxRecordId = 0;

        // Iterate through the list to find the maximum recordId
        for (MedicalRecord record : medicalRecords) {
            int recordId = (int) record.getId();
            if (recordId > maxRecordId) {
                maxRecordId = recordId;
            }
        }

        // Increment the maximum recordId to get the next available recordId
        return maxRecordId + 1;
    }
}
