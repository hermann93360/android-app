package com.example.dacontrolagent.domain.model;

public class Delivery {
    private final String emailOfDeliver;
    private final String packageNumber;
    private final String name;
    private final String salutation;
    private final String address;
    private final float longitude;
    private final float latitude;
    private byte[] photoOfPackage;
    private boolean delivered;


    public Delivery(String emailOfDeliver, String packageNumber, String name, String salutation, String address, float longitude, float latitude, byte[] photoOfPackage, boolean delivered) {
        this.emailOfDeliver = emailOfDeliver;
        this.packageNumber = packageNumber;
        this.name = name;
        this.salutation = salutation;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.photoOfPackage = photoOfPackage;
        this.delivered = delivered;
    }

    public Delivery(String emailOfDeliver, String packageNumber, String name, String salutation, String address, float longitude, float latitude, int delivered) {
        this.emailOfDeliver = emailOfDeliver;
        this.packageNumber = packageNumber;
        this.name = name;
        this.salutation = salutation;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.delivered = delivered == 1;
    }

    public String getEmailOfDeliver() {
        return emailOfDeliver;
    }

    public String getPackageNumber() {
        return packageNumber;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public String getName() {
        return name;
    }

    public String getSalutation() {
        return salutation;
    }

    public String getAddress() {
        return address;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public byte[] getPhotoOfPackage() {
        return photoOfPackage;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setPhotoOfPackage(byte[] photoOfPackage) {
        this.photoOfPackage = photoOfPackage;
    }

    public void update(Delivery deliveryToUpdate) {
        delivered = deliveryToUpdate.delivered;
        photoOfPackage = deliveryToUpdate.photoOfPackage;
    }
}
