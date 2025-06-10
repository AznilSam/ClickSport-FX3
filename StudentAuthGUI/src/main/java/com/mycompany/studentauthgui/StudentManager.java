package com.mycompany.studentauthgui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private final String filename = "students.txt";

    public boolean registerStudent(Student student) {
        //check for duplicate matric number
        if (isMatricExists(student.getMatric())) {
            return false; //if matric number already exists
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            bw.write(student.toCSV());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Student loginStudent(String matric, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromCSV(line);
                if (s != null && s.getMatric().equals(matric) && s.getPassword().equals(password)) {
                    return s;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isMatricExists(String matric) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Student s = Student.fromCSV(line);
                if (s != null && s.getMatric().equalsIgnoreCase(matric)) {
                    return true;
                }
            }
        } catch (IOException e) {
            //file might not exist yet - no problem
        }
        return false;
    }
}