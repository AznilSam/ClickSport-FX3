package com.mycompany.studentauthgui;

public class Student {
    private String matric;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String faculty;

    public Student(String matric, String password, String name, String email, String phone, String faculty) {
        this.matric = matric;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.faculty = faculty;
    }

    public String getMatric() { return matric; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getFaculty() { return faculty; }

    public String toCSV() {
        return String.join(",", matric, password, name, email, phone, faculty);
    }

    public static Student fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length < 6) return null;
        return new Student(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5]);
    }
}
