/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.resource;

/**
 *
 * @author Tihara
 */
import com.w1953194.dao.MedicalRecordDAO;
import com.w1953194.dao.PatientDAO;
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.MedicalRecord;
import com.w1953194.model.Patient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/medicalRecords")
public class MedicalRecordResource {

    private static final Logger LOGGER = Logger.getLogger(MedicalRecordResource.class.getName());
    private final MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMedicalRecords() {
        LOGGER.info("getAllMedicalRecords called");
        List<MedicalRecord> records = medicalRecordDAO.getAllMedicalRecords();
        LOGGER.log(Level.INFO, "Retrieved {0} medical records ", records.size());
        if (records.isEmpty()) {
            LOGGER.info("Medical records list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No medical records added")
                    .build();
        } else {
            LOGGER.info("Returning list of medical records");
            return Response.ok(records).build();
        }
    }

    @GET
    @Path("/{recordId}")
    @Produces(MediaType.APPLICATION_JSON)
    public MedicalRecord getMedicalRecordById(@PathParam("recordId") long recordId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getMedicalRecordById called with ID: {0}", recordId);
        MedicalRecord record = medicalRecordDAO.getMedicalRecordById(recordId);
        if (record != null) {
            LOGGER.log(Level.INFO, "Medical record with ID {0} found.", recordId);
            return record;
        } else {
            LOGGER.log(Level.INFO, "Medical record with ID {0} not found.", recordId);
            throw new ResourceNotFoundException("Medical record with ID " + recordId + " not found.");
        }
    }

    @GET
    @Path("/patient/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMedicalRecordsByPatientId(@PathParam("patientId") long patientId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getMedicalRecordsByPatientId called with patient ID: {0}", patientId);
        List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByPatientId(patientId);
        if (records.isEmpty()) {
            LOGGER.log(Level.INFO, "No medical records found for patient with id: {0}", patientId);
            throw new ResourceNotFoundException("No medical records found for patient with id: " + patientId);
        }
        LOGGER.log(Level.INFO, "Retrieved {0} medical records for patient ID: {1}", new Object[]{records.size(), patientId});
        return Response.ok(records).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMedicalRecord(MedicalRecord record, @QueryParam("patientId") long patientId) {

        LOGGER.log(Level.INFO, "addMedicalRecord called with record: {0}, patientId: {1}", new Object[]{record, patientId});

        // Get patient by ID
        PatientDAO patientDAO = new PatientDAO();
        Patient patient = patientDAO.getPatientById((int) patientId);
        if (patient == null) {
            LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Patient not found with id: " + patientId)
                    .build();
        }

        // Set patient to the medical record
        record.setPatient(patient);

        // Validate and add the medical record
        String missingData = getMissingMedicalRecordData(record);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid medical record data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid medical record data: Missing " + missingData)
                    .build();
        }

        medicalRecordDAO.addMedicalRecord(record);
        LOGGER.info("Medical record added successfully");
        return Response.status(Response.Status.CREATED).entity("Medical record added successfully").build();
    }


    @PUT
    @Path("/{recordId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMedicalRecord(@PathParam("recordId") long recordId, MedicalRecord updatedRecord,
            @QueryParam("patientId") long patientId) throws ResourceNotFoundException {

        LOGGER.log(Level.INFO, "updateMedicalRecord called with ID: {0}, patientId: {1}", new Object[]{recordId, patientId});
        // Retrieve existing record
        MedicalRecord existingRecord = medicalRecordDAO.getMedicalRecordById(recordId);
        if (existingRecord != null) {
            // Get patient by ID
            PatientDAO patientDAO = new PatientDAO();
            Patient patient = patientDAO.getPatientById((int) patientId);
            if (patient == null) {
                LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Patient not found with id: " + patientId)
                        .build();
            }

            // Update patient and other fields of existingRecord from updatedRecord object
            existingRecord.setPatient(patient);
            existingRecord.setDiagnoses(updatedRecord.getDiagnoses());
            existingRecord.setTreatments(updatedRecord.getTreatments());

            // Validate and update the medical record
            String missingData = getMissingMedicalRecordData(existingRecord); // Validate updated record
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid medical record data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid medical record data: Missing " + missingData)
                        .build();
            }

            medicalRecordDAO.updateMedicalRecord(existingRecord);
            LOGGER.info("Medical record updated successfully");
            return Response.ok("Medical record updated successfully").build();

        } else {
            LOGGER.log(Level.INFO, "Medical record with ID {0} not found.", recordId);
            throw new ResourceNotFoundException("Medical record with ID " + recordId + " not found.");
        }
    }


    @DELETE
    @Path("/{recordId}")
    public Response deleteMedicalRecord(@PathParam("recordId") long recordId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deleteMedicalRecord called with ID: {0}", recordId);
        MedicalRecord existingRecord = medicalRecordDAO.getMedicalRecordById(recordId);
        if (existingRecord != null) {
            medicalRecordDAO.deleteMedicalRecord(recordId);
            LOGGER.info("Medical record deleted successfully");
            return Response.ok("Medical record deleted successfully").build();
        } else {
            LOGGER.log(Level.INFO, "Medical record with ID {0} not found.", recordId);
            throw new ResourceNotFoundException("Medical record with ID " + recordId + " not found.");
        }
    }
    
    private String getMissingMedicalRecordData(MedicalRecord record) {
        if (record == null) {
            return "medical record";
        } else if (record.getPatient() == null) {
            return "patient";
        } else if (record.getDiagnoses() == null) {
            return "diagnoses";
        } else if (record.getTreatments() == null) {
            return "treatments";
        } else {
            return null;
        }
    }
}
    