package com.example.yannis.trainfriendsfinder.model;

/**
 * Created by yannis on 11/06/2018.
 */

public class Trein {
    String station;
    String vehicle;
    String platform;
    String time;

    @Override
    public String toString() {
        return "Country{" +
                "station='" + station + '\'' +
                ", vehicle='" + vehicle + '\'' +
                ", platform='" + platform + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public String getTime() {
        return time.substring(11,16);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getvehicle() {
        return vehicle;
    }

    public void setvehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getPlatform() {
        return platform;
    }

    public void setId(String platform) {
        this.platform = platform;
    }

    public String getstation() {
        return station;
    }

    public void setstation(String station) {
        this.station = station;
    }
}
