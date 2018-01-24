package com.medoske.www.redheal_d.Items;

/**
 * Created by USER on 25.5.17.
 */

public class ReportItem {

    String doctorName;
    String doctorRedhealId;
    String clinicName;
    String preconsultationImage;
    String prescriptionImage;
    String diagnosticImage;
    String referenceNo;

    public ReportItem(String doctorName, String doctorRedhealId, String clinicName, String preconsultationImage, String prescriptionImage, String diagnosticImage) {
        this.doctorName = doctorName;
        this.doctorRedhealId = doctorRedhealId;
        this.clinicName = clinicName;
        this.preconsultationImage = preconsultationImage;
        this.prescriptionImage = prescriptionImage;
        this.diagnosticImage = diagnosticImage;
        this.referenceNo = referenceNo;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorRedhealId() {
        return doctorRedhealId;
    }

    public void setDoctorRedhealId(String doctorRedhealId) {
        this.doctorRedhealId = doctorRedhealId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPreconsultationImage() {
        return preconsultationImage;
    }

    public void setPreconsultationImage(String preconsultationImage) {
        this.preconsultationImage = preconsultationImage;
    }

    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public String getDiagnosticImage() {
        return diagnosticImage;
    }

    public void setDiagnosticImage(String diagnosticImage) {
        this.diagnosticImage = diagnosticImage;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
}
