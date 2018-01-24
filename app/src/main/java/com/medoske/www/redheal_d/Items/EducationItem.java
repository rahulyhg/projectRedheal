package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 17.4.17.
 */
public class EducationItem {

    String qualification;
    String university;
    String id;
    String redhealId;


    public EducationItem(String qualification, String university, String id, String redhealId) {
        this.qualification = qualification;
        this.university = university;
        this.id = id;
        this.redhealId = redhealId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRedhealId() {
        return redhealId;
    }

    public void setRedhealId(String redhealId) {
        this.redhealId = redhealId;
    }
}
