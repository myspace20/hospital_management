package com.hospital;

import com.hospital.infrastructure.dao.PatientDAO;
import com.hospital.models.Patient;

import java.util.List;


public class Main {
    public static void main(String[] args) {

        PatientDAO patientDAO = new PatientDAO();


        Patient newPatient = new Patient("Jane", "Doe", "08123456789");
        newPatient.setAddressId(1);
        patientDAO.save(newPatient);
        System.out.println("Patient created.");


        List<Patient> patients = patientDAO.getAll();
        System.out.println("\nAll Patients:");
        for (Patient p : patients) {
            System.out.println(p);
        }


        if (!patients.isEmpty()) {
            Patient patientToUpdate = patients.get(0);
            patientToUpdate.setPhoneNumber("09000000000");
            patientToUpdate.setFirstName("John");
            patientToUpdate.setAddressId(1);

            String[] params = {
                    patientToUpdate.getFirstName(),
                    patientToUpdate.getSurname(),
                    patientToUpdate.getPhoneNumber()
            };

            patientDAO.update(patientToUpdate, params);
            System.out.println("\nPatient updated.");
        }

        if (!patients.isEmpty()) {
            Patient patientToDelete = patients.get(patients.size() - 1);
            patientDAO.delete(patientToDelete);
            System.out.println("\nPatient deleted.");
        }

        patientDAO.getAll().forEach(System.out::println);


    }
}