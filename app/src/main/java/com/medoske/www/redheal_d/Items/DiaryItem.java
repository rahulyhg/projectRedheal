package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 7.4.17.
 */
public class DiaryItem {

    private String patientId;
    private String patientName;
    private String bookingDate;
    private String bookingTime;
    private String status;
    private String clinicName;
    private String colorCode;
    private String paymentMode;


    public DiaryItem(String patientId, String patientName, String bookingDate, String bookingTime, String status, String clinicName, String colorCode, String paymentMode) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.status = status;
        this.clinicName = clinicName;
        this.colorCode = colorCode;
        this.paymentMode = paymentMode;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
