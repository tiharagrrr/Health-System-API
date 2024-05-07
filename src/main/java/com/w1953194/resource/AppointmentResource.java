/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.resource;

/**
 *
 * @author Tihara
 */
import com.w1953194.dao.AppointmentDAO;
import com.w1953194.dao.DoctorDAO;
import com.w1953194.dao.PatientDAO;
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.Appointment;
import com.w1953194.model.Doctor;
import com.w1953194.model.Patient;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/appointments")
public class AppointmentResource {

    private static final Logger LOGGER = Logger.getLogger(AppointmentResource.class.getName());
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();

    /**
     * Retrieves all appointments.
     *
     * @return Response containing the list of appointments.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAppointments() {
        LOGGER.info("getAllAppointments called");
        List<Appointment> appointments = appointmentDAO.getAllAppointments();
        LOGGER.log(Level.INFO, "Retrieved {0} appointments ", appointments.size());
        if (appointments.isEmpty()) {
            LOGGER.info("Appointments list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No appointments added")
                    .build();
        } else {
            LOGGER.info("Returning list of appointments");
            return Response.ok(appointments).build();
        }
    }

    @GET
    @Path("/{appointmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Appointment getAppointmentById(@PathParam("appointmentId") int appointmentId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getAppointmentById called with ID: {0}", appointmentId);
        Appointment appointment = appointmentDAO.getAppointmentById(appointmentId);
        if (appointment != null) {
            LOGGER.log(Level.INFO, "Appointment with ID {0} found.", appointmentId);
            return appointment;
        } else {
            LOGGER.log(Level.INFO, "Appointment with ID {0} not found.", appointmentId);
            throw new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found.");
        }
    }
    
    @GET
    @Path("/date/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointmentsByDate(@PathParam("date") String date) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getAppointmentsByDate called with date: {0}", date);
        List<Appointment> appointments = appointmentDAO.getAppointmentsByDate(date);
        if (appointments.isEmpty()) {
            LOGGER.log(Level.INFO, "No appointments found on date: {0}", date);
            throw new ResourceNotFoundException("No appointments found on date: " + date);
        }
        LOGGER.log(Level.INFO, "Retrieved {0}  appointments for date: {1}", new Object[]{appointments.size(), date});
        return Response.ok(appointments).build();
    }

    @GET
    @Path("/patient/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointmentsByPatientId(@PathParam("patientId") String patientId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getAppointmentsByPatientId called with patient ID: {0}", patientId);
        List<Appointment> appointments = appointmentDAO.getAppointmentsByPatientId(Long.parseLong(patientId)); // Assuming patientId is a string representation of long
        if (appointments.isEmpty()) {
            LOGGER.log(Level.INFO, "No appointments found for patient with id: {0}", patientId);
            throw new ResourceNotFoundException("No appointments found for patient with id: " + patientId);
        }
        LOGGER.log(Level.INFO, "Retrieved {0} appointments for patient ID: {1}", new Object[]{appointments.size(), patientId});
        return Response.ok(appointments).build();
    }
    
    @GET
    @Path("/doctor/{doctorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointmentsByDoctorId(@PathParam("doctorId") String doctorId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getAppointmentsByDoctorId called with doctor ID: {0}", doctorId);
        List<Appointment> appointments = appointmentDAO.getAppointmentsByDoctorId(Long.parseLong(doctorId)); // Assuming doctorId is a string representation of long
        if (appointments.isEmpty()) {
            LOGGER.log(Level.INFO, "No appointments found for doctor with id: {0}", doctorId);
            throw new ResourceNotFoundException("No appointments found for doctor with id: " + doctorId);
        }
        LOGGER.log(Level.INFO, "Retrieved {0}  doctor ID: {1}", new Object[]{appointments.size(), doctorId});
     
        return Response.ok(appointments).build();
    }

    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response scheduleAppointment(Appointment appointment, @QueryParam("doctorId") long doctorId,
                                    @QueryParam("patientId") long patientId) {

        LOGGER.log(Level.INFO, "scheduleAppointment called with appointment: {0}, doctorId: {1}, patientId: {2}", new Object[]{appointment, doctorId, patientId});

        DoctorDAO doctorDAO = new DoctorDAO();
        Doctor doctor = doctorDAO.getDoctorById((int) doctorId);
        if (doctor == null) {
            LOGGER.log(Level.INFO, "Doctor not found with id: {0}", doctorId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Doctor not found with id: " + doctorId)
                    .build();
        }

        PatientDAO patientDAO = new PatientDAO();
        Patient patient = patientDAO.getPatientById((int) patientId);
        if (patient == null) {
            LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Patient not found with id: " + patientId)
                    .build();
        }
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        
        String missingData = getMissingAppointmentData(appointment);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid appointment data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid appointment data: Missing " + missingData)
                    .build();
        }

        

        appointmentDAO.addAppointment(appointment);
        LOGGER.log(Level.INFO, "Appointment scheduled successfully");
        return Response.status(Response.Status.CREATED)
                .entity("Appointment scheduled successfully")
                .build();
    }

    
    @PUT
    @Path("/{appointmentId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAppointment(@PathParam("appointmentId") int appointmentId,
            Appointment updatedAppointment,
            @QueryParam("doctorId") String doctorIdString,
            @QueryParam("patientId") String patientIdString) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "updateAppointment called for ID: {0}", appointmentId);
        Appointment existingAppointment = appointmentDAO.getAppointmentById(appointmentId);
        if (existingAppointment != null) {
            // Similar to scheduleAppointment, handle doctor and patient updates if IDs provided
            DoctorDAO doctorDAO = new DoctorDAO();
            if (doctorIdString != null) {
                try {
                    long doctorId = Long.parseLong(doctorIdString);
                    Doctor doctor = doctorDAO.getDoctorById((int) doctorId);
                    if (doctor == null) {
                        LOGGER.log(Level.INFO, "Doctor not found with id: {0}", doctorId);
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity("Doctor not found with id: " + doctorId)
                                .build();
                    }
                    updatedAppointment.setDoctor(doctor);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Invalid doctor ID format", e);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Invalid doctor ID format. Please provide a valid long number.")
                            .build();
                }
            }

            PatientDAO patientDAO = new PatientDAO();
            if (patientIdString != null) {
                try {
                    long patientId = Long.parseLong(patientIdString);
                    Patient patient = patientDAO.getPatientById((int) patientId);
                    if (patient == null) {
                        LOGGER.log(Level.INFO, "Patient not found with id: {0}", patientId);
                        return Response.status(Response.Status.NOT_FOUND)
                                .entity("Patient not found with id: " + patientId)
                                .build();
                    }
                    updatedAppointment.setPatient(patient);
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.INFO, "Invalid patient ID format", e);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Invalid patient ID format. Please provide a valid long number.")
                            .build();
                }
            }
            String missingData = getMissingAppointmentData(updatedAppointment);
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid appointment data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid appointment data: Missing " + missingData)
                        .build();
            }

            // Update the existing appointment with potentially modified data
            updatedAppointment.setId(appointmentId); // Ensure ID remains the same
            appointmentDAO.updateAppointment(updatedAppointment);
            LOGGER.log(Level.INFO, "Appointment with ID {0} updated successfully", appointmentId);
            return Response.ok()
                    .entity("Appointment with ID " + appointmentId + " updated successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Appointment with ID {0} not found to update.", appointmentId);
            throw new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found.");
        }
    }


    @DELETE
    @Path("/{appointmentId}")
    public Response deleteAppointment(@PathParam("appointmentId") int appointmentId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deleteAppointment called for ID: {0}", appointmentId);
        Appointment deleteAppointment = appointmentDAO.getAppointmentById(appointmentId);
        if (deleteAppointment != null) {
            appointmentDAO.deleteAppointment(appointmentId);
            LOGGER.log(Level.INFO, "Appointment with ID {0} deleted successfully.", appointmentId);
            return Response.ok()
                    .entity("Appointment with ID " + appointmentId + " deleted successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Appointment with ID {0} not found for deletion.", appointmentId);
            throw new ResourceNotFoundException("Appointment with ID " + appointmentId + " not found.");
        }
    }

    private String getMissingAppointmentData(Appointment appointment) {
        if (appointment == null) {
            return "appointment";
        } else if (appointment.getDate() == null) {
            return "date";
        } else if (appointment.getTime() == null) {
            return "time";
        } else if (appointment.getPatient() == null) {
            return "patient";
        } else if (appointment.getDoctor() == null) {
            return "doctor";
        } else {
            return null;
        }
    }
}
