package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 19.4.17.
 */
public class AddPackageItem {
    String doctorId;
    String packageId;
    String packageName;
    String actualPrice;
    String discountPrice;
    String from;
    String to;
    String discription;
    String clinicId;
    String sittings;
    String period;

    public AddPackageItem() {
        this.doctorId = doctorId;
        this.packageId = packageId;
        this.packageName = packageName;
        this.actualPrice = actualPrice;
        this.discountPrice = discountPrice;
        this.from = from;
        this.to = to;
        this.discription = discription;
        this.clinicId = clinicId;
        this.sittings = sittings;
        this.period = period;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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
