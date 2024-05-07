/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.model;

/**
 *
 * @author Tihara
 */
public class Patient extends Person {
    private String medicalHistory;
    private String healthStatus;
    
    public Patient() {
        
    }

    public Patient(String name, String contactInfo, String address, String medicalHistory, String healthStatus) {
        super(name, contactInfo, address);
        this.medicalHistory = medicalHistory;
        this.healthStatus = healthStatus;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    
}
