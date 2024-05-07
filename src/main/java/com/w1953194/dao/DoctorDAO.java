/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author Tihara
 */
import com.w1953194.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {

    // This list stores all the Doctor objects in memory
    private static final List<Doctor> doctors = new ArrayList<>();

    // Static block to initialize the list with some sample doctors for testing purposes
    static {
        // Initialize with some doctors
        Doctor d1 = new Doctor("Dr. Smith", "123456", "Hospital A", "Cardiology");
        Doctor d2 = new Doctor("Dr. Johnson", "987654", "Hospital B", "Pediatrics");
        // Set IDs separately as these don't run through addDoctor and get an ID generated
        d1.setId(1);
        d2.setId(2);
        doctors.add(d1);
        doctors.add(d2);
    }

    /**
     * Retrieves all doctors from the in-memory list.
     *
     * @return A list of all Doctor objects.
     */
    public List<Doctor> getAllDoctors() {
        // Return a copy of the internal list to avoid modification from outside the class
        return new ArrayList<>(doctors);
    }

    /**
     * Finds a doctor by their ID.
     *
     * @param id The ID of the doctor to search for.
     * @return The Doctor object with the matching ID, or null if not found.
     */
    public Doctor getDoctorById(int id) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    /**
     * Adds a new doctor to the in-memory list.
     *
     * @param doctor The Doctor object to add.
     */
    public void addDoctor(Doctor doctor) {
        int newDoctorId = getNextDoctorId();
        doctor.setId(newDoctorId);
        doctors.add(doctor);
    }

    /**
     * Updates an existing doctor in the list.
     *
     * @param updatedDoctor The Doctor object with the updated information.
     */
    public void updateDoctor(Doctor updatedDoctor) {
        for (int i = 0; i < doctors.size(); i++) {
            Doctor doctor = doctors.get(i);
            if (doctor.getId() == updatedDoctor.getId()) {
                doctors.set(i, updatedDoctor);
               
                return;
            }
        }
    }

    /**
     * Deletes a doctor from the list based on their ID.
     *
     * @param id The ID of the doctor to delete.
     */
    public void deleteDoctor(int id) {
        doctors.removeIf(doctor -> doctor.getId() == id);
    }

    /**
     * Finds the next available doctor ID by finding the maximum ID in the list
     * and incrementing it.
     *
     * @return The next available ID for a new doctor object.
     */
    private int getNextDoctorId() {
        // Initialize maxDoctorId with a value lower than any possible doctorId
        int maxDoctorId = 0;

        // Iterate through the list to find the maximum doctorId
        for (Doctor doctor : doctors) {
            int doctorId = (int) doctor.getId();
            if (doctorId > maxDoctorId) {
                maxDoctorId = doctorId;
            }
        }

        // Increment the maximum doctorId to get the next available doctorId
        return maxDoctorId + 1;
    }
}
