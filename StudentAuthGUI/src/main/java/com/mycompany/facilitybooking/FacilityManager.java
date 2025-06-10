package com.mycompany.facilitybooking;

import java.io.*;
import java.util.*;

public class FacilityManager {
    private final String filename = "facilities.txt";

    public List<Facility> loadFacilities() {
        List<Facility> facilities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Facility facility = Facility.fromCSV(line);
                if (facility != null) {
                    facilities.add(facility);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return facilities;
    }

    public void saveFacilities(List<Facility> facilities) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Facility facility : facilities) {
                bw.write(facility.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeDefaultFacilities() {
        File file = new File(filename);
        if (!file.exists()) {
            List<Facility> defaults = List.of(
            new Facility(1, "Futsal Court", 10, 5.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/futsaloutdoor1.jpeg"),
            new Facility(2, "Futsal Court 2", 10, 4.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/futsaloutdoor2.jpeg"),
            new Facility(3, "Futsal Court 3", 10, 4.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/futsaloutdoor3.jpeg"),
            new Facility(4, "Futsal Court 4 (Indoor)", 10, 8.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/futsal.jpg"),
            new Facility(5, "Badminton Court", 4, 4.0f, "09:00", "21:00", true, "com/mycompany/facilitybooking/images/badmintonoutdoor.jpg"),
            new Facility(6, "Badminton Court (Indoor)", 4, 7.0f, "09:00", "21:00", true, "com/mycompany/facilitybooking/images/badminton.jpeg"),
            new Facility(7, "Badminton Court 2 (Indoor)", 4, 7.0f, "09:00", "21:00", true, "com/mycompany/facilitybooking/images/badminton2.jpg"),
            new Facility(8, "Swimming Pool", 20, 10.0f, "06:00", "22:00", true, "com/mycompany/facilitybooking/images/swimmingpool.jpg"),
            new Facility(9, "Volleyball Court (Indoor)", 12, 8.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/volleyball.jpg"),
            new Facility(10, "Netball Court (Indoor)", 14, 6.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/netballcourtindoor.jpg"),
            new Facility(11, "Netball Court 2 (Indoor)", 14, 6.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/netballcourtindoor2.jpg"),
            new Facility(12, "Netball Court 3", 14, 4.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/netballcourt.jpg"),
            new Facility(13, "Sepak Takraw Court", 6, 4.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/sepaktakraw.jpg"),
            new Facility(14, "Sepak Takraw Court 2", 6, 4.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/sepaktakraw2.jpg"),
            new Facility(15, "Handball Court", 14, 4.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/handball.jpg"),
            new Facility(16, "Hockey Turf", 50, 10.0f, "06:00", "22:00", true, "com/mycompany/facilitybooking/images/hockey.jpg"),
            new Facility(17, "Football Turf", 50, 10.0f, "06:00", "22:00", true, "com/mycompany/facilitybooking/images/football.jpg"),
            new Facility(18, "Rugby Field", 50, 10.0f, "06:00", "22:00", true, "com/mycompany/facilitybooking/images/rugby.jpg"),
            new Facility(19, "Basketball Court", 12, 7.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/basketball.jpeg"),
            new Facility(20, "Tennis Court", 2, 8.0f, "08:00", "22:00", true, "com/mycompany/facilitybooking/images/tennis.jpg")
        );

            saveFacilities(defaults);
        }
    }
}
