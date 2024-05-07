/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.resource;

/**
 *
 * @author Tihara
 */


import com.w1953194.dao.PatientDAO;
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.Patient;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/patients")
public class PatientResource {

    private static final Logger LOGGER = Logger.getLogger(PatientResource.class.getName());
    private final PatientDAO patientDAO = new PatientDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPatients() {
        LOGGER.info("getAllPatients called");
        List<Patient> patients = patientDAO.getAllPatients();
        LOGGER.log(Level.INFO, "Retrieved {0} patients ", patients.size());
        if (patients.isEmpty()) {
            LOGGER.info("Patients list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No patients added")
                    .build();
        } else {
            LOGGER.info("Returning list of patients");
            return Response.ok(patients).build();
        }
    }

    @GET
    @Path("/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Patient getPatientById(@PathParam("patientId") int patientId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getPatientById called with ID: {0}", patientId);
        Patient patient = patientDAO.getPatientById(patientId);
        if (patient != null) {
            LOGGER.log(Level.INFO, "Patient with ID {0} found.", patientId);
            return patient;
        } else {
            LOGGER.log(Level.INFO, "Patient with ID {0} not found.", patientId);
            throw new ResourceNotFoundException("Patient with ID " + patientId + " not found.");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPatient(Patient patient) {
        LOGGER.log(Level.INFO, "addPatient called with patient: {0}", patient);
        String missingData = getMissingPatientData(patient);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid patient data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid patient data: Missing " + missingData)
                    .build();
        }
        patientDAO.addPatient(patient);
        LOGGER.log(Level.INFO, "Patient with name {0} added successfully", patient.getName());
        return Response.ok()
                .entity("Patient with name " + patient.getName() + " created successfully.")
                .build();
    }

    @PUT
    @Path("/{patientId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePatient(@PathParam("patientId") int patientId, Patient updatedPatient) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "updatePatient called for ID: {0}", patientId);
        Patient existingPatient = patientDAO.getPatientById(patientId);
        if (existingPatient != null) {
            String missingData = getMissingPatientData(updatedPatient);
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid patient data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid patient data: Missing " + missingData)
                        .build();
            }
            updatedPatient.setId(patientId);
            patientDAO.updatePatient(updatedPatient);
            LOGGER.log(Level.INFO, "Patient with ID {0} updated successfully", patientId);
            return Response.ok()
                    .entity("Patient with ID " + patientId + " updated successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Patient with ID {0} not found to update.", patientId);
            throw new ResourceNotFoundException("Patient with ID " + patientId + " not found.");
        }
    }

    @DELETE
    @Path("/{patientId}")
    public Response deletePatient(@PathParam("patientId") int patientId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deletePatient called for ID: {0}", patientId);
        Patient deletePatient = patientDAO.getPatientById(patientId);
        if (deletePatient != null) {
            patientDAO.deletePatient(patientId);
            LOGGER.log(Level.INFO, "Patient with ID {0} deleted successfully.", patientId);
            return Response.ok()
                    .entity("Patient with ID " + patientId + " deleted successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Patient with ID {0} not found for deletion.", patientId);
            throw new ResourceNotFoundException("Patient with ID " + patientId + " not found.");
        }
    }

    private String getMissingPatientData(Patient patient) {
        if (patient == null) {
            return "patient";
        } else if (patient.getName() == null) {
            return "name";
        } else if (patient.getContactInfo() == null) {
            return "contact info";
        } else if (patient.getAddress() == null) {
            return "address";
        } else if (patient.getMedicalHistory() == null) {
            return "medical history";
        } else if (patient.getHealthStatus() == null) {
            return "health status";
        } else {
            return null;
        }
    }
}
