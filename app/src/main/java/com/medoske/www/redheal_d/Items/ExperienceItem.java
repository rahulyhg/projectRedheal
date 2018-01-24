package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 17.4.17.
 */
public class ExperienceItem {

    String experience;
    String experienceId;
    String experienceRedhealId;

    public ExperienceItem(String experience, String experienceId, String experienceRedhealId) {
        this.experience = experience;
        this.experienceId = experienceId;
        this.experienceRedhealId = experienceRedhealId;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
    }

    public String getExperienceRedhealId() {
        return experienceRedhealId;
    }

    public void setExperienceRedhealId(String experienceRedhealId) {
        this.experienceRedhealId = experienceRedhealId;
    }
}
