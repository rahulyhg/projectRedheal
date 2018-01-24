package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 3/23/2017.
 */
public class AppointmentItem {
    private String patientName;
    private String date;
    private String time;
    private String location;
    private String clinicName;
    private String patientId;
    private String appointmentId;
    private String doctorRhid;
    private String clinicRhid;
    private String paymentMode;
    private String imagePath;
    private String status;
    private String appointmentRefNo;


    public AppointmentItem(String patientName, String date, String time, String location, String clinicName, String patientId, String appointmentId, String doctorRhid, String clinicRhid, String paymentMode, String imagePath, String status, String appointmentRefNo) {
        this.patientName = patientName;
        this.date = date;
        this.time = time;
        this.location = location;
        this.clinicName = clinicName;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.doctorRhid = doctorRhid;
        this.clinicRhid = clinicRhid;
        this.paymentMode = paymentMode;
        this.imagePath = imagePath;
        this.status = status;
        this.appointmentRefNo = appointmentRefNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppointmentRefNo() {
        return appointmentRefNo;
    }

    public void setAppointmentRefNo(String appointmentRefNo) {
        this.appointmentRefNo = appointmentRefNo;
    }
}
