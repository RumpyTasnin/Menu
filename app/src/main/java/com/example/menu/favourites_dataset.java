package com.example.menu;

public class favourites_dataset {
    String food_name,food_description,food_price,regular,large,regular_price,large_price,activity;
    int layout_no,food_image;
    boolean radio_visibility,add_ons_visibility;
    public favourites_dataset() {

    }
    public favourites_dataset(String food_name, String food_description, String food_price, String regular, String large, String regular_price,
                              String large_price, String activity, int layout_no, int food_image, boolean radio_visibility, boolean add_ons_visibility) {
        this.food_name = food_name;
        this.food_description = food_description;
        this.food_price = food_price;
        this.regular = regular;
        this.large = large;
        this.regular_price = regular_price;
        this.large_price = large_price;
        this.activity = activity;
        this.layout_no = layout_no;
        this.food_image = food_image;
        this.radio_visibility = radio_visibility;
        this.add_ons_visibility = add_ons_visibility;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public void setRegular_price(String regular_price) {
        this.regular_price = regular_price;
    }

    public String getLarge_price() {
        return large_price;
    }

    public void setLarge_price(String large_price) {
        this.large_price = large_price;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getLayout_no() {
        return layout_no;
    }

    public void setLayout_no(int layout_no) {
        this.layout_no = layout_no;
    }

    public int getFood_image() {
        return food_image;
    }

    public void setFood_image(int food_image) {
        this.food_image = food_image;
    }

    public boolean isRadio_visibility() {
        return radio_visibility;
    }

    public void setRadio_visibility(boolean radio_visibility) {
        this.radio_visibility = radio_visibility;
    }

    public boolean isAdd_ons_visibility() {
        return add_ons_visibility;
    }

    public void setAdd_ons_visibility(boolean add_ons_visibility) {
        this.add_ons_visibility = add_ons_visibility;
    }
}
