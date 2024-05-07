/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.resource;

/**
 *
 * @author Tihara
 */
import com.w1953194.dao.DoctorDAO;
import com.w1953194.dao.PrescriptionDAO;
import com.w1953194.dao.PatientDAO;
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.Doctor;
import com.w1953194.model.Prescription;
import com.w1953194.model.Patient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/prescriptions")
public class PrescriptionResource {

    private static final Logger LOGGER = Logger.getLogger(PrescriptionResource.class.getName());
    private final PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPrescriptions() {
        LOGGER.info("getAllPrescriptions called");
        List<Prescription> prescriptions = prescriptionDAO.getAllPrescriptions();
        LOGGER.log(Level.INFO, "Retrieved {0} prescriptions ", prescriptions.size());
        if (prescriptions.isEmpty()) {
            LOGGER.info("Prescriptions list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No prescriptions added")
                    .build();
        } else {
            LOGGER.info("Returning list of prescriptions");
            return Response.ok(prescriptions).build();
        }
    }

    @GET
    @Path("/{prescriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Prescription getPrescriptionById(@PathParam("prescriptionId") long prescriptionId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getPrescriptionById called with ID: {0}", prescriptionId);
        Prescription prescription = prescriptionDAO.getPrescriptionById(prescriptionId);
        if (prescription != null) {
            LOGGER.log(Level.INFO, "Prescription with ID {0} found.", prescriptionId);
            return prescription;
        } else {
            LOGGER.log(Level.INFO, "Prescription with ID {0} not found.", prescriptionId);
            throw new ResourceNotFoundException("Prescription with ID " + prescriptionId + " not found.");
        }
    }

    @GET
    @Path("/patient/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrescriptionsByPatientId(@PathParam("patientId") long patientId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getPrescriptionsByPatientId called with patient ID: {0}", patientId);
        List<Prescription> prescriptions = prescriptionDAO.getPrescriptionsByPatientId(patientId);
        if (prescriptions.isEmpty()) {
            LOGGER.log(Level.INFO, "No prescriptions found for patient with id: {0}", patientId);
            throw new ResourceNotFoundException("No prescriptions found for patient with id: " + patientId);
        }
        LOGGER.log(Level.INFO, "Retrieved {0} prescriptions for patient ID: {1}", new Object[]{prescriptions.size(), patientId});
        return Response.ok(prescriptions).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPrescription(Prescription prescription, @QueryParam("patientId") long patientId, @QueryParam("doctorId") long doctorId) {

        LOGGER.log(Level.INFO, "addPrescription called with prescription: {0}, patientId: {1}", new Object[]{prescription, patientId});

        // Get patient by ID
        PatientDAO patientDAO = new PatientDAO();
        Patient patient = patientDAO.getPatientById((int) patientId);
        if (patient == null) {
            LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Patient not found with id: " + patientId)
                    .build();
        }

        // Set patient to the prescription
        prescription.setPatient(patient);
        
        // Get doctor by ID
        DoctorDAO doctorDAO = new DoctorDAO();
        Doctor doctor = doctorDAO.getDoctorById((int) doctorId);
        if (doctor == null) {
            LOGGER.log(Level.INFO, "Doctor not found with id: {0}", doctorId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Doctor not found with id: " + doctorId)
                    .build();
        }

        // Set patient to the prescription
        prescription.setDoctor(doctor);


        // Validate and add the prescription
        String missingData = getMissingPrescriptionData(prescription);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid prescription data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid prescription data: Missing " + missingData)
                    .build();
        }

        prescriptionDAO.addPrescription(prescription);
        LOGGER.info("Prescription added successfully");
        return Response.status(Response.Status.CREATED).entity("Prescription added successfully").build();
    }

    @PUT
    @Path("/{prescriptionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePrescription(@PathParam("prescriptionId") long prescriptionId, Prescription updatedPrescription,
            @QueryParam("patientId") long patientId,
            @QueryParam("doctorId") long doctorId) throws ResourceNotFoundException {

        LOGGER.log(Level.INFO, "updatePrescription called with ID: {0}, patientId: {1}", new Object[]{prescriptionId, patientId});
        // Retrieve existing prescription
        Prescription existingPrescription = prescriptionDAO.getPrescriptionById(prescriptionId);
        if (existingPrescription != null) {
            // Get patient by ID
            PatientDAO patientDAO = new PatientDAO();
            Patient patient = patientDAO.getPatientById((int) patientId);
            if (patient == null) {
                LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Patient not found with id: " + patientId)
                        .build();
            }
            // Get doctor by ID
            DoctorDAO doctorDAO = new DoctorDAO();
            Doctor doctor = doctorDAO.getDoctorById((int) doctorId);
            if (doctor == null) {
                LOGGER.log(Level.INFO, "Doctor not found with id: {0}", doctorId);
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Doctor not found with id: " + doctorId)
                        .build();
            }

            // Update patient and other fields of existingPrescription from updatedPrescription object
            existingPrescription.setPatient(patient);
            existingPrescription.setDoctor(doctor);
            existingPrescription.setMedication(updatedPrescription.getMedication());
            existingPrescription.setDosage(updatedPrescription.getDosage());
            existingPrescription.setInstructions(updatedPrescription.getInstructions());
            existingPrescription.setDuration(updatedPrescription.getDuration());

            // Validate and update the prescription
            String missingData = getMissingPrescriptionData(existingPrescription); // Validate updated prescription
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid prescription data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid prescription data: Missing " + missingData)
                        .build();
            }

            prescriptionDAO.updatePrescription(existingPrescription);
            LOGGER.info("Prescription updated successfully");
            return Response.ok("Prescription updated successfully").build();

        } else {
            LOGGER.log(Level.INFO, "Prescription with ID {0} not found.", prescriptionId);
            throw new ResourceNotFoundException("Prescription with ID " + prescriptionId + " not found.");
        }
    }

    @DELETE
    @Path("/{prescriptionId}")
    public Response deletePrescription(@PathParam("prescriptionId") long prescriptionId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deletePrescription called with ID: {0}", prescriptionId);
        Prescription existingPrescription = prescriptionDAO.getPrescriptionById(prescriptionId);
        if (existingPrescription != null) {
            prescriptionDAO.deletePrescription(prescriptionId);
            LOGGER.info("Prescription deleted successfully");
            return Response.ok("Prescription deleted successfully").build();
        } else {
            LOGGER.log(Level.INFO, "Prescription with ID {0} not found.", prescriptionId);
            throw new ResourceNotFoundException("Prescription with ID " + prescriptionId + " not found.");
        }
    }

    private String getMissingPrescriptionData(Prescription prescription) {
        if (prescription == null) {
            return "prescription";
        } else if (prescription.getPatient() == null) {
            return "patient";
        }else if (prescription.getDoctor() == null) {
            return "doctor";
        } else if (prescription.getMedication() == null) {
            return "medication";
        } else if (prescription.getDosage() == null) {
            return "dosage";
        } else if (prescription.getInstructions() == null) {
            return "instructions";
        } else if (prescription.getDuration() == null) {
            return "duration";
        } else {
            return null;
        }
    }
}
