package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 3/23/2017.
 */
public class AppointmentPutItem {

    private String bookingDate;
    private String bookingTime;
    private String patientId;
    private String bookingId;
    private String doctorRhid;
    private String clinicRhid;
    private String status;

    public AppointmentPutItem() {
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.patientId = patientId;
        this.bookingId = bookingId;
        this.doctorRhid = doctorRhid;
        this.clinicRhid = clinicRhid;
        this.status = status;
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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getDoctorRhid() {
        return doctorRhid;
    }

    public void setDoctorRhid(String doctorRhid) {
        this.doctorRhid = doctorRhid;
    }

    public String getClinicRhid() {
        return clinicRhid;
    }

    public void setClinicRhid(String clinicRhid) {
        this.clinicRhid = clinicRhid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
