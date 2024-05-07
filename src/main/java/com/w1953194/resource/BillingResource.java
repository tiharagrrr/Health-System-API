/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.resource;

/**
 *
 * @author Tihara
 */
import com.w1953194.dao.BillingDAO;
import com.w1953194.dao.DoctorDAO;
import com.w1953194.dao.PatientDAO;
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.Billing;
import com.w1953194.model.Doctor;
import com.w1953194.model.Patient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/billings")
public class BillingResource {

    private static final Logger LOGGER = Logger.getLogger(BillingResource.class.getName());
    private final BillingDAO billingDAO = new BillingDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBillings() {
        LOGGER.info("getAllBillings called");
        List<Billing> billings = billingDAO.getAllBillings();
        LOGGER.log(Level.INFO, "Retrieved {0} billings ", billings.size());
        if (billings.isEmpty()) {
            LOGGER.info("Billings list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No billings added")
                    .build();
        } else {
            LOGGER.info("Returning list of billings");
            return Response.ok(billings).build();
        }
    }

    @GET
    @Path("/{billingId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Billing getBillingById(@PathParam("billingId") long billingId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getBillingById called with ID: {0}", billingId);
        Billing billing = billingDAO.getBillingById(billingId);
        if (billing != null) {
            LOGGER.log(Level.INFO, "Billing with ID {0} found.", billingId);
            return billing;
        } else {
            LOGGER.log(Level.INFO, "Billing with ID {0} not found.", billingId);
            throw new ResourceNotFoundException("Billing with ID " + billingId + " not found.");
        }
    }

    @GET
    @Path("/patient/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillingsByPatientId(@PathParam("patientId") long patientId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getBillingsByPatientId called with patient ID: {0}", patientId);
        List<Billing> billings = billingDAO.getBillingsByPatientId(patientId);
        if (billings.isEmpty()) {
            LOGGER.log(Level.INFO, "No billings found for patient with id: {0}", patientId);
            throw new ResourceNotFoundException("No billings found for patient with id: " + patientId);
        }
        LOGGER.log(Level.INFO, "Retrieved {0} billings for patient ID: {1}", new Object[]{billings.size(), patientId});
        return Response.ok(billings).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addBilling(Billing billing, @QueryParam("patientId") long patientId, @QueryParam("doctorId") long doctorId) {

        LOGGER.log(Level.INFO, "addBilling called with billing: {0}, patientId: {1}", new Object[]{billing, patientId});

        // Get patient by ID
        PatientDAO patientDAO = new PatientDAO();
        Patient patient = patientDAO.getPatientById((int) patientId);
        if (patient == null) {
            LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Patient not found with id: " + patientId)
                    .build();
        }

        // Set patient to the billing
        billing.setPatient(patient);
        
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
        billing.setDoctor(doctor);

        // Validate and add the billing
        String missingData = getMissingBillingData(billing);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid billing data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid billing data: Missing " + missingData)
                    .build();
        }

        billingDAO.addBilling(billing);
        LOGGER.info("Billing added successfully");
        return Response.status(Response.Status.CREATED).entity("Billing added successfully").build();
    }

    @PUT
    @Path("/{billingId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBilling(@PathParam("billingId") long billingId, Billing updatedBilling,
            @QueryParam("patientId") long patientId,
            @QueryParam("doctorId") long doctorId) throws ResourceNotFoundException {

        LOGGER.log(Level.INFO, "updateBilling called with ID: {0}, patientId: {1}", new Object[]{billingId, patientId});
        // Retrieve existing billing
        Billing existingBilling = billingDAO.getBillingById(billingId);
        if (existingBilling != null) {
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

            // Update patient and other fields of existingBilling from updatedBilling object
            existingBilling.setPatient(patient);
            existingBilling.setDoctor(doctor);
            existingBilling.setInvoiceDetails(updatedBilling.getInvoiceDetails());
            existingBilling.setPaymentDetails(updatedBilling.getPaymentDetails());
            existingBilling.setOutstandingBalance(updatedBilling.getOutstandingBalance());

            // Validate and update the billing
            String missingData = getMissingBillingData(existingBilling); // Validate updated billing
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid billing data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid billing data: Missing " + missingData)
                        .build();
            }

            billingDAO.updateBilling(existingBilling);
            LOGGER.info("Billing updated successfully");
            return Response.ok("Billing updated successfully").build();

        } else {
            LOGGER.log(Level.INFO, "Billing with ID {0} not found.", billingId);
            throw new ResourceNotFoundException("Billing with ID " + billingId + " not found.");
        }
    }

    @DELETE
    @Path("/{billingId}")
    public Response deleteBilling(@PathParam("billingId") long billingId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deleteBilling called with ID: {0}", billingId);
        Billing existingBilling = billingDAO.getBillingById(billingId);
        if (existingBilling != null) {
            billingDAO.deleteBilling(billingId);
            LOGGER.info("Billing deleted successfully");
            return Response.ok("Billing deleted successfully").build();
        } else {
            LOGGER.log(Level.INFO, "Billing with ID {0} not found.", billingId);
            throw new ResourceNotFoundException("Billing with ID " + billingId + " not found.");
        }
    }

    private String getMissingBillingData(Billing billing) {
        if (billing == null) {
            return "billing";
        } else if (billing.getPatient() == null) {
            return "patient";
        } else if (billing.getDoctor() == null) {
            return "doctor";
        }else if (billing.getInvoiceDetails() == null) {
            return "invoice details";
        } else if (billing.getPaymentDetails() == null) {
            return "payment details";
        } else if (billing.getOutstandingBalance() == null) {
            return "outstanding balance";
        } else {
            return null;
        }
    }
}
