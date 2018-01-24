package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 27-03-2017.
 */
public class AddTipsItem {
    String doctor_redhealId;
    String tipName;
    String categoryId;
    String description;
    String categoryName;
    String  imagePath;

    public AddTipsItem() {
        this.doctor_redhealId = doctor_redhealId;
        this.tipName = tipName;
        this.categoryId = categoryId;
        this.description = description;
        this.categoryName = categoryName;
        this.imagePath = imagePath;
    }

    public String getDoctor_redhealId() {
        return doctor_redhealId;
    }

    public void setDoctor_redhealId(String doctor_redhealId) {
        this.doctor_redhealId = doctor_redhealId;
    }

    public String getTipName() {
        return tipName;
    }

    public void setTipName(String tipName) {
        this.tipName = tipName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
