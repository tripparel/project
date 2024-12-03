package com.example.tripparel;

public class Store {
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private String category;

    // 생성자
    public Store(String name, double latitude, double longitude, String address, String category) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.category = category;
    }

    // getter 메소드들
    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }
}
