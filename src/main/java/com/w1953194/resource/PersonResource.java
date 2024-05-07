/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.resource;

import com.w1953194.dao.PersonDAO;
import com.w1953194.exception.ResourceNotFoundException;
import com.w1953194.model.Person;

import javax.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



/**
 *
 * @author Tihara
 */
@Path("/persons")
public class PersonResource {

    private static final Logger LOGGER = Logger.getLogger(PersonResource.class.getName());
    private final PersonDAO personDAO = new PersonDAO();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPersons() {
        LOGGER.info("getAllPersons called");
        List<Person> persons = personDAO.getAllPersons();
        LOGGER.log(Level.INFO, "Retrieved {0} persons ", persons.size());
        if (persons.isEmpty()) {
            LOGGER.info("Persons list is empty");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No users added")
                    .build();
        } else {
            LOGGER.info("Returning list of persons");
            return Response.ok(persons).build();
        }
    }

    @GET
    @Path("/{personId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getpersonById(@PathParam("personId") int personId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "getpersonById called with id: {0}", personId);
        Person person = personDAO.getPersonById(personId);
        if (person != null) {
            LOGGER.log(Level.INFO, "Person with ID {0} found.", personId);
            return person;
        } else {
            
            LOGGER.log(Level.INFO, "Person with ID {0} not found.", personId);
            throw new ResourceNotFoundException("person with ID " + personId + " not found.");
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(Person person) {
        LOGGER.log(Level.INFO, "addPerson called with person: {0}", person);
        String missingData = getMissingPersonData(person);
        if (missingData != null) {
            LOGGER.log(Level.INFO, "Invalid person data provided: Missing {0}", missingData);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Incomplete or invalid person data: Missing " + missingData)
                    .build();
        }
        personDAO.addPerson(person);
        LOGGER.log(Level.INFO, "Person with name {0} added successfully", person.getName());
        return Response.ok()
                .entity("Person with name " + person.getName() + " created successfully.")
                .build();
    }

    @PUT
    @Path("/{personId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(@PathParam("personId") int personId, Person updatedPerson) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "updatePerson called for ID: {0}", personId);
        Person existingPerson = personDAO.getPersonById(personId);
        if (existingPerson != null) {
            String missingData = getMissingPersonData(updatedPerson);
            if (missingData != null) {
                LOGGER.log(Level.INFO, "Invalid person data provided: Missing {0}", missingData);
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Incomplete or invalid person data: Missing " + missingData)
                        .build();
            }
            updatedPerson.setId(personId);
            personDAO.updatePerson(updatedPerson);
            LOGGER.log(Level.INFO, "Person with ID {0} updated successfully", personId);
            return Response.ok()
                    .entity("Person with ID " + personId + " updated successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Person with ID {0} not found to update.", personId);
            throw new ResourceNotFoundException("Person with ID " + personId + " not found.");
        }
    }

    @DELETE
    @Path("/{personId}")
    public Response deleteperson(@PathParam("personId") int personId) throws ResourceNotFoundException {
        LOGGER.log(Level.INFO, "deleteperson called for ID: {0}", personId);
        Person deletePerson = personDAO.getPersonById(personId);
        if (deletePerson != null) {
            personDAO.deletePerson(personId);
            LOGGER.log(Level.INFO, "Person with ID {0} deleted successfully.", personId);
            return Response.ok()
                    .entity("Person with ID " + personId + " deleted successfully.")
                    .build();
        } else {
            LOGGER.log(Level.INFO, "Person with ID {0} not found for deletion.", personId);
            throw new ResourceNotFoundException("person with ID " + personId + " not found.");
        }
    }
    
    private String getMissingPersonData(Person person) {
        if (person == null) {
            return "person";
        } else if (person.getName() == null) {
            return "name";
        } else if (person.getContactInfo() == null) {
            return "contact info";
        } else if (person.getAddress() == null) {
            return "address";
        } else {
            return null;
        }
    }
}

