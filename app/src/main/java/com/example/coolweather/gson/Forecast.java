package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    public Astro astro;
    public class Astro{
        public String mr;
        public String ms;
        public String sr;
        public String ss;//月升落，日升落
    }
    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt_d")
        public String info;
        @SerializedName("txt_n")
        public String night_info;
    }
    public String date;
    public String pcpn;//降水量
    public String pop;//降水概率
    public String pres;//大气压强
    public String uv;//紫外线强度指数
    public String vis;//能见度
    public String hum;//相对湿度
    @SerializedName("tmp")
    public Temperature temperature;
    public class Temperature{
        public String max;
        public String min;
    }
    public Wind wind;
    public class Wind{
        public String dir;//风向
        public String sc;//风力
        public String spd;//风速
    }
}
