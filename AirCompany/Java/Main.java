package com.company;

public class Main {
    static final String DIR = "...";
    static final String DB_URL = "...";
    static final String USER = "...";
    static final String PASS = "...";

    public static void main(String[] args) {
        Database db = new Database(DB_URL, USER, PASS);
        FileReader reader = new FileReader(DIR, db);
        reader.flightsRead("flightinfo.xlsx");
        reader.transactionsRead("transactions.csv");
        reader.departmentsRead("departments.csv");
        reader.employeesRead("employees");
        reader.beneficiariesRead("beneficiaries.xml");
        db.closeConnection();
    }
}
