package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 3.4.17.
 */
public class PrescriptionItem {
    String prescription;
    String name;
    String url;
    String redhealId;
    String date;


    public PrescriptionItem(String prescription, String name, String url, String redhealId, String date) {
        this.prescription = prescription;
        this.name = name;
        this.url = url;
        this.redhealId = redhealId;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRedhealId() {
        return redhealId;
    }

    public void setRedhealId(String redhealId) {
        this.redhealId = redhealId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}
