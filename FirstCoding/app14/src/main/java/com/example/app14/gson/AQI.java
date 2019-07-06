package com.example.app14.gson;

/**
 * AQI信息类
 */
public class AQI {
    public AQICity city;

    public class AQICity {
        public String aqi;
        public String pm25;
    }
}
