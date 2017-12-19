package com.yiful.couriorprojectfirebase.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yifu on 12/16/2017.
 */

public class MyParcel {
    private String userId;
    private String name;
    private String pickupAddress;
    private Date pickupTime;

    private String deliveryAddress;
    private Date deliveryTime;

    private String vendor;
    private String status;
    private String location;
    private String mBitmapString;

    public MyParcel(){

    }
    public MyParcel(String userId, String name, String pickupAddress, Date pickupTime, String deliveryAddress,
                    Date deliveryTime, String vendor, String status, String location) {
        this.userId = userId;
        this.name = name;
        this.pickupAddress = pickupAddress;
        this.pickupTime = pickupTime;
        this.deliveryAddress = deliveryAddress;
        this.deliveryTime = deliveryTime;
        this.vendor = vendor;
        this.status = status;
        this.location = location;
        //mBitmapString=bitMapString;
    }


    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Date pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }



    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }


    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public Map<String, Object>  toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("userId", this.getUserId());
        map.put("name", this.getName());
        map.put("pickupAddress", this.getPickupAddress());
        map.put("pickupTime", this.getPickupTime());
        map.put("deliveryAddress", this.getDeliveryAddress());
        map.put("deliveryTime", this.getDeliveryTime());
        map.put("vendor", this.getVendor());
        map.put("location", this.getLocation());
        return map;
    }
}
