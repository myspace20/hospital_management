package com.hospital.models;

public class Patient {
    int patient_id;
    String first_name;
    String surname;
    String phone_number;
    int address_id;
    public Patient(String first_name, String surname, String phone_number){
        this.first_name = first_name;
        this.surname = surname;
        this.phone_number = phone_number;
    }

    public int getPatientId() {
        return patient_id;
    }

    public void setPatientId(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public int getAddressId() {
        return address_id;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public String getSurname() {
        return surname;
    }

    public void setAddressId(int address_id) {
        this.address_id = address_id;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }


    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patient_id +
                ", firstName='" + first_name + '\'' +
                ", lastName='" + surname + '\'' +
                ", phone='" + phone_number + '\'' +
                '}';
    }
}

