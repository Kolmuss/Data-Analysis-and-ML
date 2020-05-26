package com.company;

import java.sql.*;

public class Database {
    private Connection conn = null;

    public Database(String DB_URL, String USER, String PASS) {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("CONNECTION ESTABLISHED!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Database(Connection conn){
        this.conn = conn;
    }

    public void insertDepartments(int dep_id, String dep_name ){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Departments (dep_id, dep_name) VALUES(?, ?);");
            ps.setInt(1, dep_id);
            ps.setString(2, dep_name);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void insertFlights(String dep_airport_code, String arr_airport_code, double flight_time, double distance){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO FlightInfo " +
                    "(dep_airport_code, arr_airport_code, flight_time, distance) " +
                    "VALUES (?, ?, ?, ?);");
            ps.setString(1, dep_airport_code);
            ps.setString(2, arr_airport_code);
            ps.setDouble(3, flight_time);
            ps.setDouble(4, distance);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void insertTransactions(int traveler_id, String booking_num, String dep_airport_code, String arr_airport_code,
                                   Timestamp dep_time, Timestamp ret_time, Timestamp booking_time, String line){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Transactions(traveler_id, booking_num, " +
                    "dep_airport_code, arr_airport_code, dep_time, ret_time, booking_time, line)" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setInt(1, traveler_id);
            ps.setString(2, booking_num);
            ps.setString(3, dep_airport_code);
            ps.setString(4, arr_airport_code);
            ps.setTimestamp(5, dep_time);
            ps.setTimestamp(6, ret_time);
            ps.setTimestamp(7, booking_time);
            ps.setString(8, line);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void insertEmployees(int staff_number, String name, int gender, Date dob, Date hire, int status, int dep_id){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Employees (staff_number, name, gender, " +
                    "dob, hire, status, dep_id) VALUES(?,?,?,?,?,?,?)");

            ps.setInt(1, staff_number);
            ps.setString(2, name);
            ps.setInt(3, gender);
            ps.setDate(4, dob);
            ps.setDate(5, hire);
            ps.setInt(6, status);
            ps.setInt(7, dep_id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void insertBeneficiaries(int benef_id, String name, int gender, Date dob, int is_active, int type_id, int staff_number){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Beneficiaries(benef_id, " +
                    "name, gender, dob, is_active, type_id, staff_number) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?);");
            ps.setInt(1, benef_id);
            ps.setString(2, name);
            ps.setInt(3, gender);
            ps.setDate(4, dob);
            ps.setInt(5, is_active);
            ps.setInt(6, type_id);
            ps.setInt(7, staff_number);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void insertBTypes(int type_id, String type){
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO BenefTypes(type_id, type) VALUES(?, ?);");
            ps.setInt(1, type_id);
            ps.setString(2, type);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            conn.close();
            System.out.println("CONNECTION SUCCESSFULLY CLOSED!");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
