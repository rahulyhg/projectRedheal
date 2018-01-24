package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 1.4.17.
 */
public class MyClinicItem {
    private String ClinicName;
    private String password;
    private String primeryNo;
    private String secondaryNo;
    private String morningTime;
    private String morningDays;
    private String afternoonTime;
    private String afternoonDays;
    private String eveningTime;
    private String eveningDays;
    private String clinicRedhealId;
    private String imagePath;
    private String street;
    private String landmark;
    private String areaName;
    private String cityName;
    private String pincode;


    public MyClinicItem(String clinicName, String password, String primeryNo, String secondaryNo, String morningTime, String morningDays, String afternoonTime, String afternoonDays, String eveningTime, String eveningDays, String clinicRedhealId, String imagePath, String street, String landmark, String areaName, String cityName, String pincode) {
        ClinicName = clinicName;
        this.password = password;
        this.primeryNo = primeryNo;
        this.secondaryNo = secondaryNo;
        this.morningTime = morningTime;
        this.morningDays = morningDays;
        this.afternoonTime = afternoonTime;
        this.afternoonDays = afternoonDays;
        this.eveningTime = eveningTime;
        this.eveningDays = eveningDays;
        this.clinicRedhealId = clinicRedhealId;
        this.imagePath = imagePath;
        this.street = street;
        this.landmark = landmark;
        this.areaName = areaName;
        this.cityName = cityName;
        this.pincode = pincode;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrimeryNo() {
        return primeryNo;
    }

    public void setPrimeryNo(String primeryNo) {
        this.primeryNo = primeryNo;
    }

    public String getSecondaryNo() {
        return secondaryNo;
    }

    public void setSecondaryNo(String secondaryNo) {
        this.secondaryNo = secondaryNo;
    }

    public String getMorningTime() {
        return morningTime;
    }

    public void setMorningTime(String morningTime) {
        this.morningTime = morningTime;
    }

    public String getMorningDays() {
        return morningDays;
    }

    public void setMorningDays(String morningDays) {
        this.morningDays = morningDays;
    }

    public String getAfternoonTime() {
        return afternoonTime;
    }

    public void setAfternoonTime(String afternoonTime) {
        this.afternoonTime = afternoonTime;
    }

    public String getAfternoonDays() {
        return afternoonDays;
    }

    public void setAfternoonDays(String afternoonDays) {
        this.afternoonDays = afternoonDays;
    }

    public String getEveningTime() {
        return eveningTime;
    }

    public void setEveningTime(String eveningTime) {
        this.eveningTime = eveningTime;
    }

    public String getEveningDays() {
        return eveningDays;
    }

    public void setEveningDays(String eveningDays) {
        this.eveningDays = eveningDays;
    }

    public String getClinicRedhealId() {
        return clinicRedhealId;
    }

    public void setClinicRedhealId(String clinicRedhealId) {
        this.clinicRedhealId = clinicRedhealId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


}
