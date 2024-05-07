/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.w1953194.model;

/**
 *
 * @author Tihara
 */
public class Billing {

    private long id;
    private Patient patient;
    private Doctor doctor;
    private String invoiceDetails;
    private String paymentDetails;
    private String outstandingBalance; //string to include currency/ to validate if input is missing in post/put requests

    public Billing() {
    }

    public Billing(Patient patient, Doctor doctor, String invoiceDetails, String paymentDetails, String outstandingBalance) {
     
        this.patient = patient;
        this.doctor = doctor;
        this.invoiceDetails = invoiceDetails;
        this.paymentDetails = paymentDetails;
        this.outstandingBalance = outstandingBalance;
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(String invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getOutstandingBalance() {
        return outstandingBalance;
    }

    public void setOutstandingBalance(String outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }
}
