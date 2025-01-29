package com.udjaya.kasirudjay.model;

public class DeviceInfo {
    private String brand;
    private String model;
    private String os_version;

    public DeviceInfo(String deviceBrand, String model, String release) {
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs_version() {
        return os_version;
    }

    public void setOs_version(String os_version) {
        this.os_version = os_version;
    }
}
