package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 19.4.17.
 */
public class MyPackageItem {
    String packageId;
    String speclizationId;
    String speclization;
    String packageName;
    String actualPrice;
    String discountPrice;
    String fromTime;
    String toTime;
    String image;
    String description;

    public MyPackageItem(String packageId, String speclizationId, String speclization, String packageName, String actualPrice, String discountPrice, String fromTime, String toTime, String image, String description) {
        this.packageId = packageId;
        this.speclizationId = speclizationId;
        this.speclization = speclization;
        this.packageName = packageName;
        this.actualPrice = actualPrice;
        this.discountPrice = discountPrice;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.image = image;
        this.description = description;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getSpeclizationId() {
        return speclizationId;
    }

    public void setSpeclizationId(String speclizationId) {
        this.speclizationId = speclizationId;
    }

    public String getSpeclization() {
        return speclization;
    }

    public void setSpeclization(String speclization) {
        this.speclization = speclization;
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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
