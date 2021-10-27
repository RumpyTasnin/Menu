package com.example.admin;

public class orders_dataset {
    String extra_shot_price,iced_price,food_name,food_price,food_type,instructions,layout;
    int food_amount,extra_shot_amount,iced_amount;

    public orders_dataset() {
    }
    public orders_dataset(String extra_shot_price, String iced_price, String food_name,
                          String food_price, String food_type, String instructions, String layout, int food_amount, int extra_shot_amount, int iced_amount) {
        this.extra_shot_price = extra_shot_price;
        this.iced_price = iced_price;
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_type = food_type;
        this.instructions = instructions;
        this.layout = layout;
        this.food_amount = food_amount;
        this.extra_shot_amount = extra_shot_amount;
        this.iced_amount = iced_amount;
    }

    public String getExtra_shot_price() {
        return extra_shot_price;
    }

    public void setExtra_shot_price(String extra_shot_price) {
        this.extra_shot_price = extra_shot_price;
    }

    public String getIced_price() {
        return iced_price;
    }

    public void setIced_price(String iced_price) {
        this.iced_price = iced_price;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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
