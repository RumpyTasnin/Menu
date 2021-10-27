package com.example.menu;

public class navOrderDataSet {
String Token,Time,Date,order_id,status;
        int total_price;
boolean Confirmed;
    public navOrderDataSet() {

    }

    public navOrderDataSet(String token, String time, String date, int total_price, String order_id, String status, boolean confirmed) {
        Token = token;
        Time = time;
        Date = date;
        this.total_price = total_price;
        this.order_id = order_id;
        this.status = status;
        Confirmed = confirmed;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isConfirmed() {
        return Confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        Confirmed = confirmed;
    }
}

