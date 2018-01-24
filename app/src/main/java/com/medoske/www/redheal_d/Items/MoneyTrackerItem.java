package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 20.7.17.
 */

public class MoneyTrackerItem {

    String clinicName;
    String clinic_redhealId;
    String year;
    String month;
    String patients;
    String amount;
    String online;
    String offline;


    public MoneyTrackerItem(String clinicName, String clinic_redhealId, String year, String month, String patients, String amount, String online, String offline) {
        this.clinicName = clinicName;
        this.clinic_redhealId = clinic_redhealId;
        this.year = year;
        this.month = month;
        this.patients = patients;
        this.amount = amount;
        this.online = online;
        this.offline = offline;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinic_redhealId() {
        return clinic_redhealId;
    }

    public void setClinic_redhealId(String clinic_redhealId) {
        this.clinic_redhealId = clinic_redhealId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPatients() {
        return patients;
    }

    public void setPatients(String patients) {
        this.patients = patients;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getOffline() {
        return offline;
    }

    public void setOffline(String offline) {
        this.offline = offline;
    }
}
