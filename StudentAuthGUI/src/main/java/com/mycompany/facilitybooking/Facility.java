package com.mycompany.facilitybooking;

public class Facility {
    private int facilityNo;
    private String name;
    private int capacity;
    private float pricePerHour;
    private String openTime;
    private String closeTime;
    private boolean isAvailable;
    private String imagePath; //added for image path (gambar facility mainly)

    public Facility(int facilityNo, String name, int capacity, float pricePerHour, String openTime, String closeTime, boolean isAvailable, String imagePath) {
        this.facilityNo = facilityNo;
        this.name = name;
        this.capacity = capacity;
        this.pricePerHour = pricePerHour;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.isAvailable = isAvailable;
        this.imagePath = imagePath;
    }

    public int getFacilityNo() {
        return facilityNo;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public float getPricePerHour() {
        return pricePerHour;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getImagePath() {
        return imagePath;
    } //getter for image path (gambar)

    public String toCSV() {
        return facilityNo + "," + name + "," + capacity + "," + pricePerHour + "," + openTime + "," + closeTime + "," + isAvailable + "," + imagePath;
    }

    public static Facility fromCSV(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 8) return null;
        return new Facility(
                Integer.parseInt(parts[0]),
                parts[1],
                Integer.parseInt(parts[2]),
                Float.parseFloat(parts[3]),
                parts[4],
                parts[5],
                Boolean.parseBoolean(parts[6]),
                parts[7] //including imagePath (GAMBAR)
        );
    }

    @Override
    public String toString() {
        return "Facility No: " + facilityNo + " | " + name + " | RM" + pricePerHour + "/hr | Available: " + isAvailable;
    }
}
