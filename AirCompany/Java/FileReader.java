package com.company;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Scanner;

public class FileReader {
    HashMap<String, Integer> departments;
    String directory;
    Database db;

    public FileReader(String directory, Database db) {
        this.directory = directory;
        this.db = db;
    }

    public void flightsRead(String filename) {
        try {
            FileInputStream fis = new FileInputStream(directory + filename);
            Workbook wb = new XSSFWorkbook(fis);
            Sheet main = wb.getSheet("Sheet1");
            int n = main.getLastRowNum() + 1;
            for (int i = 1; i < n; i++) {
                String dep_airport_code = main.getRow(i).getCell(1).getStringCellValue();
                String arr_airport_code = main.getRow(i).getCell(2).getStringCellValue();
                double flight_time = main.getRow(i).getCell(3).getNumericCellValue();
                double dist = main.getRow(i).getCell(4).getNumericCellValue();
                db.insertFlights(dep_airport_code, arr_airport_code, flight_time, dist);
            }
            fis.close();
            System.out.println(filename + " IS OK!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(filename + " FAILED!");

        }
    }

    public void transactionsRead(String filename) {
        try {
            File myObj = new File(directory + filename);
            Scanner myReader = new Scanner(myObj);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] line = data.split(";");
                int traveler_id = Integer.parseInt(line[0]);
                String booking_num = line[1];
                String dep_airport_code = line[2];
                String arr_airport_code = line[3];
                Timestamp dep_time = Timestamp.valueOf(line[4]);
                Timestamp ret_time;
                if(line[5].equals("NULL")){
                    ret_time = null;
                }else{
                    ret_time = Timestamp.valueOf(line[5]);
                }
                Timestamp booking_time = Timestamp.valueOf(line[7]);


                String airline;
                if (line[6].equals("1")) {
                    airline = "Fly";
                } else {
                    airline = "Wings";
                    ;
                }
                db.insertTransactions(traveler_id, booking_num, dep_airport_code, arr_airport_code,
                        dep_time, ret_time, booking_time, airline);
            }
            myReader.close();
            System.out.println(filename + " IS OK!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(filename + " FAILED!");
        }
    }

    public void departmentsRead(String filename) {
        departments = new HashMap<>();
        try {
            File myObj = new File(directory + filename);
            Scanner myReader = new Scanner(myObj);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] line = data.replace("\"", "").replace("\n",
                        "").split(";");
                int dep_id = line[1].hashCode(); // s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
                String dep_name = line[0].strip();
                db.insertDepartments(dep_id, dep_name);
                departments.putIfAbsent(dep_name, dep_id);
            }
            myReader.close();
            System.out.println(filename + " IS OK!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(filename + " FAILED!");
        }
    }

    public void employeesRead(String filename) {
        String[] mon = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        HashMap<String, Integer> months = new HashMap<>();
        for (int i = 0; i < mon.length; i++) {
            months.putIfAbsent(mon[i], i + 1);
        }
        try {
            File myObj = new File(directory + filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                int staff_number = Integer.parseInt(data.substring(0, 9).strip());
                String name = data.substring(9, 45).strip();
                int gender = Integer.parseInt(data.substring(45, 46));
                Date dob = Date.valueOf(data.substring(106, 110) + "-" + months.get(data.substring(99, 102)) + "-"
                        + data.substring(103, 105).replace(" ", ""));
                Date hire = Date.valueOf(data.substring(118, 122) + "-" + months.get(data.substring(114, 117)) + "-"
                        + data.substring(111, 113).replace(" ", ""));
                int status = Integer.parseInt(data.substring(46, 47));
                int dep_id = departments.get(data.substring(48, 99).strip());
                db.insertEmployees(staff_number, name, gender, dob, hire, status, dep_id);
            }
            myReader.close();
            System.out.println(filename + " IS OK!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(filename + " FAILED!");
        }
    }

    public void beneficiariesRead(String filename){
        String[] types = {"Spouse", "Child", "Parent", "Sibling", "Custodian"};
        for (int i = 0; i < types.length; i++) {
            db.insertBTypes(i+1, types[i]);
        }
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(directory + filename);
            Node root = document.getDocumentElement();
            NodeList data = root.getChildNodes();

            for (int i = 0; i < data.getLength(); i++) {
                Node t = data.item(i);
                if(t.hasAttributes()){
                    NamedNodeMap t_attrs = t.getAttributes();
                    Node b = t.getChildNodes().item(1);
                    NamedNodeMap b_attrs = b.getAttributes();
                    int benef_id = Integer.parseInt(t_attrs.item(3).getTextContent());

                    String name = t_attrs.item(4).getTextContent();
                    int gender = Integer.parseInt(t_attrs.item(2).getTextContent());
                    Date dob = Date.valueOf(t_attrs.item(0).getTextContent().substring(0, 10));
                    int is_active;
                    int type_id;
                    if(b_attrs.getLength() == 2) {
                        is_active = Integer.parseInt(b_attrs.item(0).getTextContent());
                        type_id = Integer.parseInt(b_attrs.item(1).getTextContent());
                    }else {
                        is_active = Integer.parseInt(b_attrs.item(1).getTextContent());
                        type_id = Integer.parseInt(b_attrs.item(2).getTextContent());
                    }
                    int staff_number = Integer.parseInt(t_attrs.item(1).getTextContent());

                    db.insertBeneficiaries(benef_id, name, gender, dob, is_active, type_id, staff_number);
                }
            }

            System.out.println(filename + " IS OK!");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace(System.out);
            System.out.println(filename + " FAILED!");
        }
    }
}
