package com.example.admin;

public class check_order_dataset {
    String Token,Time,Date,order_id,status,delivery;
    int total_price;
    public check_order_dataset() {
    }

    public check_order_dataset(String token, String time, String date, String order_id, String status, String delivery, int total_price) {
        Token = token;
        Time = time;
        Date = date;
        this.order_id = order_id;
        this.status = status;
        this.delivery = delivery;
        this.total_price = total_price;
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

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }




}
