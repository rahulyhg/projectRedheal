package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 12.7.17.
 */

public class CatagoeryItem {
    private String categoryId;
    private String categoryName;

    public CatagoeryItem(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
