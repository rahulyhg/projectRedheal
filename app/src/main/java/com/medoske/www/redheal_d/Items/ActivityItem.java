package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 3/25/2017.
 */
public class ActivityItem {
    private String bookingTime;
    private String name;
    private String status;
    private String bookingId;
    private String bookingDate;
    private String patient_redhealId;
    private String doctor_redhealId;
    private String clinic_redhealId;
    private String clinicName;
    private String paymentMode;
    private String imagePath;
    private String appointmentRefNo;

    public ActivityItem(String bookingTime, String name, String status, String bookingId, String bookingDate, String patient_redhealId, String doctor_redhealId, String clinic_redhealId, String clinicName, String paymentMode, String imagePath, String appointmentRefNo) {
        this.bookingTime = bookingTime;
        this.name = name;
        this.status = status;
        this.bookingId = bookingId;
        this.bookingDate = bookingDate;
        this.patient_redhealId = patient_redhealId;
        this.doctor_redhealId = doctor_redhealId;
        this.clinic_redhealId = clinic_redhealId;
        this.clinicName = clinicName;
        this.paymentMode = paymentMode;
        this.imagePath = imagePath;
        this.appointmentRefNo = appointmentRefNo;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getPatient_redhealId() {
        return patient_redhealId;
    }

    public void setPatient_redhealId(String patient_redhealId) {
        this.patient_redhealId = patient_redhealId;
    }

    public String getDoctor_redhealId() {
        return doctor_redhealId;
    }

    public void setDoctor_redhealId(String doctor_redhealId) {
        this.doctor_redhealId = doctor_redhealId;
    }

    public String getClinic_redhealId() {
        return clinic_redhealId;
    }

    public void setClinic_redhealId(String clinic_redhealId) {
        this.clinic_redhealId = clinic_redhealId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAppointmentRefNo() {
        return appointmentRefNo;
    }

    public void setAppointmentRefNo(String appointmentRefNo) {
        this.appointmentRefNo = appointmentRefNo;
    }
}
