package com.example.menu;

import android.widget.ImageView;

public class helperClass {

    int imageView;
    String title,description,price,price2,small,large;
            int order_id;

    Boolean add_ons_visibility,radio_groups_visibility;

    public helperClass(int imageView, String title, String description,String price,String price2,String small,String large,Boolean add_ons_visibility,Boolean radio_groups_visibility,int order_id) {
        this.imageView = imageView;
        this.title = title;
        this.description = description;
        this.price=price;
        this.price2=price2;
        this.small=small;
        this.large=large;
        this.add_ons_visibility=add_ons_visibility;
        this.radio_groups_visibility=radio_groups_visibility;
        this.order_id=order_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public Boolean getAdd_ons_visibility() {
        return add_ons_visibility;
    }

    public void setAdd_ons_visibility(Boolean add_ons_visibility) {
        this.add_ons_visibility = add_ons_visibility;
    }

    public Boolean getRadio_groups_visibility() {
        return radio_groups_visibility;
    }

    public void setRadio_groups_visibility(Boolean radio_groups_visibility) {
        this.radio_groups_visibility = radio_groups_visibility;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
