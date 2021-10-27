package com.example.menu;

public class home_delivery_dataSet {
    String location,remarks,type,name;


    public home_delivery_dataSet() {
    }

    public home_delivery_dataSet(String location, String remarks, String type, String name) {
        this.location = location;
        this.remarks = remarks;
        this.type = type;
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

