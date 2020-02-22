package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;
import com.example.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static boolean handleProvinceResponse(String response){
       if(!TextUtils.isEmpty(response)){
           try{
               JSONArray allProvinces = new JSONArray(response);
               for(int i = 0;i < allProvinces.length();i++){
                   JSONObject provincesObject = allProvinces.getJSONObject(i);//从allprovinces中取出的元素是JSONObject对象
                   //取出对象中的数据 并将其组装成实体类对象
                   Province province = new Province();
                   province.setProvinceName(provincesObject.getString("name"));
                   province.setProvinceCode(provincesObject.getInt("id"));
                    province.save();
               }
               return true;
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }
       return false;
    }
    //处理市级数据
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCities = new JSONArray(response);
                for(int i=1;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try{
                JSONArray allCounties = new JSONArray(response);
                for(int i=1;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County County = new County();
                    County.setCountyName(countyObject.getString("name"));
                    County.setCityId(cityId);
                    County.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleWeatherResponse(String response)  {
        try{
            //将天气信息的主题内容解析出来
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            //将JSON数据转换魏weather对象
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
