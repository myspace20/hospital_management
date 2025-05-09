package com.hospital.infrastructure.dao;

import com.hospital.infrastructure.DatabaseSession;
import com.hospital.models.Address;
import com.hospital.models.base.DAO;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressDAO implements DAO<Address> {

    private static final Logger LOGGER = Logger.getLogger(AddressDAO.class.getName());

    @Override
    public Optional<Address> get(long id) {
        LOGGER.info(String.format("Fetching address with ID: %d", id));
        String query = "SELECT * FROM address WHERE address_id = ?";
        Optional<Address> address = Optional.empty();
        long startTime = System.nanoTime();

        try (DatabaseSession db = new DatabaseSession();
             PreparedStatement stmt = db.getConnection().prepareStatement(query)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                address = Optional.of(processRow(rs));
                LOGGER.info(String.format("Found address with ID: %d", id));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, String.format("Error fetching address with ID: %d", id), e);
            throw new RuntimeException("Error fetching address by ID", e);
        } finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for fetching address ID %d: %.3f ms", id, durationMs));
        }

        return address;
    }

    @Override
    public List<Address> getAll() {
        LOGGER.info("Fetching all addresses");
        List<Address> addresses = new ArrayList<>();
        String query = "SELECT * FROM address";
        long startTime = System.nanoTime();

        try (DatabaseSession db = new DatabaseSession();
             Statement stmt = db.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                addresses.add(processRow(rs));
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all addresses", e);
            throw new RuntimeException("Error fetching addresses", e);
        } finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for fetching all addresses: %.3f ms", durationMs));
        }

        return addresses;
    }

    private Address processRow(ResultSet rs) throws SQLException {
        Address address = new Address(
                rs.getString("street"),
                rs.getString("city"),
                rs.getString("country")
        );
        address.setAddressId(rs.getInt("address_id"));
        return address;
    }

    @Override
    public void save(Address address) {
        LOGGER.info("Inserting a new address");
        String query = "INSERT INTO address (street, city, country) VALUES (?, ?, ?)";
        long startTime = System.nanoTime();

        try (DatabaseSession db = new DatabaseSession();
             PreparedStatement stmt = db.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getCountry());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                address.setAddressId(keys.getInt(1));
            }
            LOGGER.info("Address insert operation completed");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting address", e);
            throw new RuntimeException("Error saving address", e);
        } finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for inserting address: %.3f ms", durationMs));
        }
    }

    @Override
    public void update(Address address, String[] params) {
        LOGGER.info(String.format("Updating address with ID: %d", address.getAddressId()));
        String query = "UPDATE address SET street = ?, city = ?, country = ? WHERE address_id = ?";
        long startTime = System.nanoTime();

        try (DatabaseSession db = new DatabaseSession();
             PreparedStatement stmt = db.getConnection().prepareStatement(query)) {

            stmt.setString(1, address.getStreet());
            stmt.setString(2, address.getCity());
            stmt.setString(3, address.getCountry());
            stmt.setLong(4, address.getAddressId());

            stmt.executeUpdate();
            LOGGER.info("Address update operation completed");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating address", e);
            throw new RuntimeException("Error updating address", e);
        } finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for updating address: %.3f ms", durationMs));
        }
    }

    @Override
    public void delete(Address address) {
        LOGGER.info(String.format("Deleting address with ID: %d", address.getAddressId()));
        String query = "DELETE FROM address WHERE address_id = ?";
        long startTime = System.nanoTime();

        try (DatabaseSession db = new DatabaseSession();
             PreparedStatement stmt = db.getConnection().prepareStatement(query)) {

            stmt.setLong(1, address.getAddressId());
            stmt.executeUpdate();
            LOGGER.info("Address delete operation completed");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting address", e);
            throw new RuntimeException("Error deleting address", e);
        } finally {
            long endTime = System.nanoTime();
            double durationMs = (endTime - startTime) / 1_000_000.0;
            LOGGER.info(String.format("Query execution time for deleting address: %.3f ms", durationMs));
        }
    }
}

