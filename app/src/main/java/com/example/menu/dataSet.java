package com.example.menu;

public class dataSet {
    public dataSet() {
    }
    String food_name,food_type,food_price,layout;
    int food_amount,extra_shot_amount,iced_amount;

    public dataSet(String food_name, String food_type, String food_price, String layout, int food_amount, int extra_shot_amount, int iced_amount) {
        this.food_name = food_name;
        this.food_type = food_type;
        this.food_price = food_price;
        this.layout = layout;
        this.food_amount = food_amount;
        this.extra_shot_amount = extra_shot_amount;
        this.iced_amount = iced_amount;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public int getFood_amount() {
        return food_amount;
    }

    public void setFood_amount(int food_amount) {
        this.food_amount = food_amount;
    }

    public int getExtra_shot_amount() {
        return extra_shot_amount;
    }

    public void setExtra_shot_amount(int extra_shot_amount) {
        this.extra_shot_amount = extra_shot_amount;
    }

    public int getIced_amount() {
        return iced_amount;
    }

    public void setIced_amount(int iced_amount) {
        this.iced_amount = iced_amount;
    }
}
