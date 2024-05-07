/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.model;

/**
 *
 * @author Tihara
 */
public class MedicalRecord {

    private long id;
    private Patient patient;
    private String diagnoses;
    private String treatments;

    public MedicalRecord() {
    }

    public MedicalRecord(Patient patient, String diagnoses, String treatments) {
     
        this.patient = patient;
        this.diagnoses = diagnoses;
        this.treatments = treatments;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {    
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
    }

    public String getTreatments() {
        return treatments;
    }

    public void setTreatments(String treatments) {
        this.treatments = treatments;
    }
}
