package com.example.coolweather.gson;

public class AQI {
    public AQICity city;
    public class AQICity{
        public String aqi;//空气质量指数
        public String co;
        public String no2;
        public String o3;
        public String pm10;
        public String pm25;
        public String qlty;//空气质量（优/良/轻度污染/中度污染/重度污染/严重污染）
        public String so2;
    }
}
