package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 27-03-2017.
 */
public class PatientInfoItem {
    private String firstName;
    private String age;
    private String redhealId;
    private String gender;
    private String image;
    private String mobileNumber;
    private String email;
    private String bloodGroup;
    private String dateOfBirth;
    private String address;
    private String referenceNo;


    public PatientInfoItem(String firstName, String age, String redhealId, String gender, String image, String mobileNumber, String email, String bloodGroup, String dateOfBirth, String address, String referenceNo) {
        this.firstName = firstName;
        this.age = age;
        this.redhealId = redhealId;
        this.gender = gender;
        this.image = image;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.referenceNo = referenceNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getRedhealId() {
        return redhealId;
    }

    public void setRedhealId(String redhealId) {
        this.redhealId = redhealId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
}
