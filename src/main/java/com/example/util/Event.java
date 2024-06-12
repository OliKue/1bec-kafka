package com.example.util;

public class Event {
    private String station;
    private float weather;

    public Event(String station, float weather) {
        this.station = station;
        this.weather = weather;
    }

    public Event(String eventString) {
        int index = eventString.indexOf(",");
        this.station = eventString.substring(15,index-1);
        String weatherString = eventString.substring(index + 10, eventString.length()-1);
        this.weather = Float.parseFloat(weatherString);
    }

    @Override
    public String toString() {
        return "Event{station='" + station + "', weather=" + weather +'}';
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public float getWeather() {
        return weather;
    }

    public void setWeather(float weather) {
        this.weather = weather;
    }
}
