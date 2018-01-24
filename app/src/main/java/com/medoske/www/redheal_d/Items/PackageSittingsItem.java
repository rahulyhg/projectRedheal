package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 3.8.17.
 */

public class PackageSittingsItem {

    String packageLeadId;
    String patient_redhealId;
    String patientName;
    String doctor_redhealId;
    String clinic_redhealId;
    String clinicName;
    String packageId;
    String packageName;
    String bookingDateTime;
    String packageRefNo;
    String paymentMode;
    String status;
    String actualPrice;
    String discountPrice;
    String sittingN0;
    String sittingStatus;
    String report;

    public PackageSittingsItem(String packageLeadId, String patient_redhealId, String patientName, String doctor_redhealId, String clinic_redhealId, String clinicName, String packageId, String packageName, String bookingDateTime, String packageRefNo, String paymentMode, String status, String actualPrice, String discountPrice, String sittingN0, String sittingStatus, String report) {
        this.packageLeadId = packageLeadId;
        this.patient_redhealId = patient_redhealId;
        this.patientName = patientName;
        this.doctor_redhealId = doctor_redhealId;
        this.clinic_redhealId = clinic_redhealId;
        this.clinicName = clinicName;
        this.packageId = packageId;
        this.packageName = packageName;
        this.bookingDateTime = bookingDateTime;
        this.packageRefNo = packageRefNo;
        this.paymentMode = paymentMode;
        this.status = status;
        this.actualPrice = actualPrice;
        this.discountPrice = discountPrice;
        this.sittingN0 = sittingN0;
        this.sittingStatus = sittingStatus;
        this.report = report;
    }

    public String getPackageLeadId() {
        return packageLeadId;
    }

    public void setPackageLeadId(String packageLeadId) {
        this.packageLeadId = packageLeadId;
    }

    public String getPatient_redhealId() {
        return patient_redhealId;
    }

    public void setPatient_redhealId(String patient_redhealId) {
        this.patient_redhealId = patient_redhealId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getBookingDateTime() {
        return bookingDateTime;
    }

    public void setBookingDateTime(String bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }

    public String getPackageRefNo() {
        return packageRefNo;
    }

    public void setPackageRefNo(String packageRefNo) {
        this.packageRefNo = packageRefNo;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getSittingN0() {
        return sittingN0;
    }

    public void setSittingN0(String sittingN0) {
        this.sittingN0 = sittingN0;
    }

    public String getSittingStatus() {
        return sittingStatus;
    }

    public void setSittingStatus(String sittingStatus) {
        this.sittingStatus = sittingStatus;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
