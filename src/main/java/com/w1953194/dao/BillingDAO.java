/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author TIhara
 */
import com.w1953194.model.Billing;
import java.util.ArrayList;
import java.util.List;

public class BillingDAO {

    // This list stores all the billing objects in memory
    private static final List<Billing> billings = new ArrayList<>();

    /**
     * Retrieves all billings from the in-memory list.
     *
     * @return A list of all Billing objects.
     */
    public List<Billing> getAllBillings() {
        // Return a copy of the internal list to avoid modification from outside the class
        return new ArrayList<>(billings);
    }

    /**
     * Finds a billing by its ID.
     *
     * @param id The ID of the billing to search for.
     * @return The Billing object with the matching ID, or null if not found.
     */
    public Billing getBillingById(long id) {
        for (Billing billing : billings) {
            if (billing.getId() == id) {
                return billing;
            }
        }
        return null;
    }

    /**
     * Retrieves all billings associated with a specific patient ID.
     *
     * @param patientId The ID of the patient to find billings for.
     * @return A list of Billing objects for the given patient, or an empty list
     * if none found.
     */
    public List<Billing> getBillingsByPatientId(long patientId) {
        List<Billing> billingsForPatient = new ArrayList<>();
        for (Billing billing : billings) {
            if (patientId == billing.getPatient().getId()) {
                billingsForPatient.add(billing);
            }
        }
        return billingsForPatient;
    }

    /**
     * Adds a new billing to the in-memory list.
     *
     * @param billing The Billing object to add.
     */
    public void addBilling(Billing billing) {
        int newBillingId = getNextBillingId();
        billing.setId(newBillingId);
        billings.add(billing);
    }

    /**
     * Updates an existing billing in the list.
     *
     * @param updatedBilling The Billing object with the updated information.
     */
    public void updateBilling(Billing updatedBilling) {
        for (int i = 0; i < billings.size(); i++) {
            if (billings.get(i).getId() == updatedBilling.getId()) {
                billings.set(i, updatedBilling);
                return;
            }
        }
    }

    /**
     * Deletes a billing from the list based on its ID.
     *
     * @param id The ID of the billing to delete.
     */
    public void deleteBilling(long id) {
        billings.removeIf(billing -> billing.getId() == id);
    }

    /**
     * Finds the next available billing ID by finding the maximum ID in the list
     * and incrementing it.
     *
     * @return The next available ID for a new billing object.
     */
    private int getNextBillingId() {
        // Initialize maxBillingId with 0
        int maxBillingId = 0;

        // Iterate through the list to find the maximum billingId
        for (Billing billing : billings) {
            int billingId = (int) billing.getId();
            if (billingId > maxBillingId) {
                maxBillingId = billingId;
            }
        }

        // Increment the maximum billingId to get the next available billingId
        return maxBillingId + 1;
    }
}
