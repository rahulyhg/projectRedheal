package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 2.8.17.
 */

public class PackageLeadsItem {

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
    String sittingNumber;
    String sordId;

    public PackageLeadsItem(String packageLeadId, String patient_redhealId, String patientName, String doctor_redhealId, String clinic_redhealId, String clinicName, String packageId, String packageName, String bookingDateTime, String packageRefNo, String paymentMode, String status, String actualPrice, String discountPrice, String sittingNumber, String sordId) {
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
        this.sittingNumber = sittingNumber;
        this.sordId = sordId;
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

    public String getSittingNumber() {
        return sittingNumber;
    }

    public void setSittingNumber(String sittingNumber) {
        this.sittingNumber = sittingNumber;
    }

    public String getSordId() {
        return sordId;
    }

    public void setSordId(String sordId) {
        this.sordId = sordId;
    }
}
