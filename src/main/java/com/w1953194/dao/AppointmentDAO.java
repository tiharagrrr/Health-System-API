/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.dao;

/**
 *
 * @author Tihara
 */

import com.w1953194.model.Appointment;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {

    // List to store all appointments
    private static List<Appointment> appointments = new ArrayList<>();

    // Method to return all appointments
    public List<Appointment> getAllAppointments() {
        return appointments;
    }

    // Method to return an appointment by its ID
    public Appointment getAppointmentById(long id) {
        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    // Method to return all appointments on a specific date
    public List<Appointment> getAppointmentsByDate(String date) {
        List<Appointment> appointmentsOnDate = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDate().equals(date)) {
                appointmentsOnDate.add(appointment);
            }
        }
        return appointmentsOnDate;
    }

    // Method to return all appointments for a specific patient
    public List<Appointment> getAppointmentsByPatientId(long patientId) {
        List<Appointment> appointmentsForPatient = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (patientId == appointment.getPatient().getId()) {
                appointmentsForPatient.add(appointment);
            }
        }
        return appointmentsForPatient;
    }

    // Method to return all appointments for a specific doctor
    public List<Appointment> getAppointmentsByDoctorId(long doctorId) {
        List<Appointment> appointmentsForDoctor = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctor().getId() == doctorId) {
                appointmentsForDoctor.add(appointment);
            }
        }
        return appointmentsForDoctor;
    }

    // Method to add a new appointment
    public void addAppointment(Appointment appointment) {
        int newAppointmentId = getNextAppointmentId();
        appointment.setId(newAppointmentId);
        appointments.add(appointment);
    }

    // Method to update an existing appointment
    public void updateAppointment(Appointment updatedAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == updatedAppointment.getId()) {
                appointments.set(i, updatedAppointment);
                return;
            }
        }
    }

    // Method to delete an appointment by its ID
    public void deleteAppointment(long id) {
        appointments.removeIf(appointment -> appointment.getId() == id);
    }

    // Method to generate the next ID for a new appointment
    public int getNextAppointmentId() {
        // Initialize maxAppointmentId with a value lower than any possible appointmentId
        int maxAppointmentId = 0;

        // Iterate through the list to find the maximum appointmentId
        for (Appointment appointment : appointments) {
            int appointmentId = (int) appointment.getId();
            if (appointmentId > maxAppointmentId) {
                maxAppointmentId = appointmentId;
            }
        }

        // Increment the maximum appointmentId to get the next available appointmentId
        return maxAppointmentId + 1;
    }
}
