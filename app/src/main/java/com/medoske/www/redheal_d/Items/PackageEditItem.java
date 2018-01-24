package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 19.5.17.
 */

public class PackageEditItem {

    String packageId;
    String packageName;
    String actualPrice;
    String discountPrice;
    String from;
    String to;
    String discription;
    String specializationId;
    String doctorRedhealId;
    String clinicId;
    String sittings;
    String period;
   // String image;


    public PackageEditItem(String packageId, String packageName, String actualPrice, String discountPrice, String from, String to, String discription, String specializationId, String doctorRedhealId, String clinicId, String sittings, String period) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.actualPrice = actualPrice;
        this.discountPrice = discountPrice;
        this.from = from;
        this.to = to;
        this.discription = discription;
        this.specializationId = specializationId;
        this.doctorRedhealId = doctorRedhealId;
        this.clinicId = clinicId;
        this.sittings = sittings;
        this.period = period;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getSpecializationId() {
        return specializationId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    public String getDoctorRedhealId() {
        return doctorRedhealId;
    }

    public void setDoctorRedhealId(String doctorRedhealId) {
        this.doctorRedhealId = doctorRedhealId;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getSittings() {
        return sittings;
    }

    public void setSittings(String sittings) {
        this.sittings = sittings;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
