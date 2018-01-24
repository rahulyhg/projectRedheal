package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 13.7.17.
 */

public class ClinicListItem {

    String clinic_redhealId;
    String clinicName;

    public ClinicListItem(String clinic_redhealId, String clinicName) {
        this.clinic_redhealId = clinic_redhealId;
        this.clinicName = clinicName;
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
}
