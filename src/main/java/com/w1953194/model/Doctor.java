/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.model;

/**
 *
 * @author Tihara
 */
public class Doctor extends Person {
    private String specialization;
    
    public Doctor() {
        
    }

    public Doctor(String name, String contactInfo, String address, String specialization) {
        super(name, contactInfo, address);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    
}
    