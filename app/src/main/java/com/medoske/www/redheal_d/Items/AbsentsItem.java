package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 15.5.17.
 */

public class AbsentsItem {
    String doctorId;
    String fromDate;
    String toDate;
    String status;
    String id;

    public AbsentsItem(String doctorId, String fromDate, String toDate, String status, String id) {
        this.doctorId = doctorId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.id = id;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
