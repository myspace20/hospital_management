package com.hospital.infrastructure.dao;

import com.hospital.models.Patient;
import com.hospital.infrastructure.DatabaseSession;
import com.hospital.models.base.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class PatientDAO implements DAO<Patient> {
    private static final Logger LOGGER = Logger.getLogger(PatientDAO.class.getName());


    @Override
    public Optional<Patient> get(long id) {
        LOGGER.info(String.format("Fetching patient with ID: %s", id));
        Optional<Patient> patient = Optional.empty();
        String query = "SELECT * FROM patient WHERE patient_id = ?";
        long startTime = System.nanoTime();

        try (DatabaseSession dbSession = new DatabaseSession();
             PreparedStatement stmt = dbSession.getConnection().prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                patient = Optional.of(processRow(rs));
                LOGGER.info(String.format("Found patient with ID: %s", id));
            }

        } catch (Exception e) {
            LOGGER.severe("Error fetching patient: " + e.getMessage());
            throw new RuntimeException("Error fetching patient", e);
        }finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for get patient ID %d: %.3f ms", id, durationMs));
        }

        return patient;
    }

    @Override
    public List<Patient> getAll() {
        LOGGER.info("Fetching all patients");
        List<Patient> list = new ArrayList<>();
        String query = "SELECT * FROM patient";
        long startTime = System.nanoTime();

        try (DatabaseSession dbSession = new DatabaseSession();
             Statement stmt = dbSession.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if(rs.next()){
                LOGGER.info("Patients found");
            }

            while (rs.next()) {
                list.add(processRow(rs));
            }

        } catch (Exception e) {
            LOGGER.severe("Error fetching patients: " + e.getMessage());
            throw new RuntimeException("Error fetching patients", e);
        }finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for get patients %.3f ms", durationMs));
        }

        return list;
    }

    private Patient processRow(ResultSet rs) throws SQLException {
        Patient patient = new Patient(
                rs.getString("first_name"),
                rs.getString("surname"),
                rs.getString("phone_number")
        );
        patient.setPatientId(rs.getInt("patient_id"));
        return  patient;
    }

    @Override
    public void save(Patient patient) {
        LOGGER.info("Inserting a patient");
        String query = "INSERT INTO patient (first_name, surname, phone_number, address_id) VALUES (?, ?, ?, ?)";
        long startTime = System.nanoTime();


        try (DatabaseSession dbSession = new DatabaseSession();
             PreparedStatement stmt = dbSession.getConnection().prepareStatement(query)) {

            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getSurname());
            stmt.setString(3, patient.getPhoneNumber());
            stmt.setInt(4, patient.getAddressId());

            stmt.executeUpdate();
            LOGGER.info("Inserting of patient completed");

        } catch (SQLException e) {
            LOGGER.info("Error while inserting patient");
            throw new RuntimeException("Error inserting patient", e);
        }finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for inserting patients %.3f ms", durationMs));
        }
    }

    @Override
    public void update(Patient patient, String[] params) {
        LOGGER.info("Patient update operation started");
        String query = "UPDATE patient SET first_name = ?, surname = ?, phone_number = ?, address_id = ? WHERE patient_id = ?";
        long startTime = System.nanoTime();


        try (DatabaseSession dbSession = new DatabaseSession();
             PreparedStatement stmt = dbSession.getConnection().prepareStatement(query)) {

            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getSurname());
            stmt.setString(3, patient.getPhoneNumber());
            stmt.setInt(4, patient.getAddressId());
            stmt.setLong(5, patient.getPatientId());

            stmt.executeUpdate();
            LOGGER.info("Patient update operation completed");

        } catch (SQLException e) {
            LOGGER.info("Error while updating patient");
            throw new RuntimeException("Error updating patient", e);
        }finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for updating patients %.3f ms", durationMs));
        }
    }

    @Override
    public void delete(Patient patient) {
        LOGGER.info("Patient update operation started");
        String query = "DELETE FROM patient WHERE patient_id = ?";
        long startTime = System.nanoTime();


        try (DatabaseSession dbSession = new DatabaseSession();
             PreparedStatement stmt = dbSession.getConnection().prepareStatement(query)) {

            stmt.setLong(1, patient.getPatientId());

            stmt.executeUpdate();
            LOGGER.info("Patient delete operation completed");

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting patient", e);
        }finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for updating patients %.3f ms", durationMs));
        }
    }
}

