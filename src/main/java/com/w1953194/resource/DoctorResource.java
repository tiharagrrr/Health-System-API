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
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.Doctor;

import javax.ws.rs.core.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/doctors")
public class DoctorResource {

    private static final Logger LOGGER = Logger.getLogger(DoctorResource.class.getName());
    private DoctorDAO doctorDAO = new DoctorDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDoctors() {
        LOGGER.info("getAllDoctors called");
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        LOGGER.log(Level.INFO, "Retrieved {0} doctors ", doctors.size());
        if (doctors.isEmpty()) {
            LOGGER.info("Doctors list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No doctors added")
                    .build();
        } else {
            LOGGER.info("Returning list of doctors");
            return Response.ok(doctors).build();
        }
    }

    @GET
    @Path("/{doctorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Doctor getDoctorById(@PathParam("doctorId") int doctorId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getDoctorById called with ID: {0}", doctorId);
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        if (doctor != null) {
            LOGGER.log(Level.INFO, "Doctor with ID {0} found.", doctorId);
            return doctor;
        } else {
            LOGGER.log(Level.INFO, "Doctor with ID {0} not found.", doctorId);
            throw new ResourceNotFoundException("Doctor with ID " + doctorId + " not found.");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDoctor(Doctor doctor) {
        LOGGER.log(Level.INFO, "addDoctor called with doctor: {0}", doctor);
        String missingData = getMissingDoctorData(doctor);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid doctor data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid doctor data: Missing " + missingData)
                    .build();
        }
        doctorDAO.addDoctor(doctor);
        LOGGER.log(Level.INFO, "Doctor with name {0} added successfully", doctor.getName());
        return Response.ok()
                .entity("Doctor with name " + doctor.getName() + " created successfully.")
                .build();
    }

    @PUT
    @Path("/{doctorId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDoctor(@PathParam("doctorId") int doctorId, Doctor updatedDoctor) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "updateDoctor called for ID: {0}", doctorId);
        Doctor existingDoctor = doctorDAO.getDoctorById(doctorId);
        if (existingDoctor != null) {
            String missingData = getMissingDoctorData(updatedDoctor);
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid doctor data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid doctor data: Missing " + missingData)
                        .build();
            }
            updatedDoctor.setId(doctorId);
            doctorDAO.updateDoctor(updatedDoctor);
            LOGGER.log(Level.INFO, "Doctor with ID {0} updated successfully", doctorId);
            return Response.ok()
                    .entity("Doctor with ID " + doctorId + " updated successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Doctor with ID {0} not found to update.", doctorId);
            throw new ResourceNotFoundException("Doctor with ID " + doctorId + " not found.");
        }
    }

    @DELETE
    @Path("/{doctorId}")
    public Response deleteDoctor(@PathParam("doctorId") int doctorId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deleteDoctor called for ID: {0}", doctorId);
        Doctor deleteDoctor = doctorDAO.getDoctorById(doctorId);
        if (deleteDoctor != null) {
            doctorDAO.deleteDoctor(doctorId);
            LOGGER.log(Level.INFO, "Doctor with ID {0} deleted successfully.", doctorId);
            return Response.ok()
                    .entity("Doctor with ID " + doctorId + " deleted successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Doctor with ID {0} not found for deletion.", doctorId);
            throw new ResourceNotFoundException("Doctor with ID " + doctorId + " not found.");
        }
    }

    private String getMissingDoctorData(Doctor doctor) {
        if (doctor == null) {
            return "doctor";
        } else if (doctor.getName() == null) {
            return "name";
        } else if (doctor.getContactInfo() == null) {
            return "contact info";
        } else if (doctor.getAddress() == null) {
            return "address";
        } else if (doctor.getSpecialization() == null){
            return "specialization";
        }
        else {
            return null;
        }
    }
}
