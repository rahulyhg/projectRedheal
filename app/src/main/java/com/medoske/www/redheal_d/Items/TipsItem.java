package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 27-03-2017.
 */
public class TipsItem {
    // private final int imageId;
    private final String categoryId;
    private final String doctor_redhealId;
    private final String tipName;
    private final String description;
    private final String imagePath;
    private final String id;
    private final String categoryName;


    public TipsItem(String categoryId, String doctor_redhealId, String tipName, String description, String imagePath, String id, String categoryName) {
        this.categoryId = categoryId;
        this.doctor_redhealId = doctor_redhealId;
        this.tipName = tipName;
        this.description = description;
        this.imagePath = imagePath;
        this.id = id;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getDoctor_redhealId() {
        return doctor_redhealId;
    }

    public String getTipName() {
        return tipName;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
