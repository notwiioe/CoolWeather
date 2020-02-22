package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.Utility;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    private Button navButton;
    public SwipeRefreshLayout swipeRefresh;
    private String mWeatherId;
    private ScrollView weatherLayout;//滚动视图对象
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView titleLat;
    private TextView titleLon;
    private TextView weatherInfoText;
    private TextView flText;
    private TextView humText;
    private TextView pcpnText;
    private TextView presText;
    private TextView degreeText;
    private TextView visText;
    private TextView dirText;
    private TextView scText;
    private TextView spdText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView coText;
    private TextView no2Text;
    private TextView o3Text;
    private TextView pm10Text;
    private TextView pm25Text;
    private TextView qltyText;
    private TextView so2Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView drsgText;
    private TextView fluText;
    private TextView sportText;
    private TextView travText;
    private TextView uvText;
    private ImageView bingPicImg;//背景图片
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        //初始化各控件
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime=(TextView)findViewById(R.id.title_update_time);
        titleLat=(TextView)findViewById(R.id.lat_text);
        titleLat=(TextView)findViewById(R.id.lon_text);
        weatherInfoText=(TextView)findViewById(R.id.weather_info_text);
        flText=(TextView)findViewById(R.id.fl_text);
        humText=(TextView)findViewById(R.id.hum_text);
        pcpnText=(TextView)findViewById(R.id.pcpn_text);
        presText = (TextView) findViewById(R.id.pres_text);
        degreeText = (TextView) findViewById(R.id.degree_text);
        visText = (TextView) findViewById(R.id.vis_text);
        dirText = (TextView) findViewById(R.id.dir_text);
        scText = (TextView)findViewById(R.id.sc_text);
        spdText = (TextView) findViewById(R.id.spd_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        coText = (TextView) findViewById(R.id.co_text);
        no2Text = (TextView) findViewById(R.id.no2_text);
        o3Text = (TextView) findViewById(R.id.o3_text);
        pm10Text = (TextView) findViewById(R.id.pm10_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        qltyText = (TextView) findViewById(R.id.qlty_text);
        so2Text = (TextView) findViewById(R.id.so2_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carWashText = (TextView) findViewById(R.id.car_wash_text);
        drsgText = (TextView) findViewById(R.id.drsg_text);
        fluText = (TextView) findViewById(R.id.flu_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        travText = (TextView) findViewById(R.id.trav_text);
        uvText = (TextView) findViewById(R.id.uv_text);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navButton = (Button) findViewById(R.id.nav_button);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if(weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        }else{
            //无缓存去服务器查
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        //下拉监听器
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic !=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else{
            loadBingPic();
        }
        //请求新选择城市的天气信息
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

    void requestWeather(final String weatherId) {
        //向组装好的地址发送请求，服务器会将相应城市的天气信息以JSON（）格式返回
        String weatherUrl="http://guolin.tech/api/weather?cityid="+ weatherId +"&key=9cc0b6e01eec4595b58a90dd0cb0160b";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
                loadBingPic();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId = weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
                loadBingPic();
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = "更新时间"+weather.basic.update.updateTime.split(" ")[1];
        String lat = "经度"+weather.basic.cityLat;
        String lon = "纬度"+weather.basic.cityLon;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        titleLat.setText(lat);
        titleLon.setText(lon);
        String weatherInfo = weather.now.more.info;
        String flInfo = weather.now.fl+"C";
        String humInfo = weather.now.hum;
        String pcpnInfo = weather.now.pcpn + "mm";
        String presInfo = weather.now.pres + "Pa";
        String degree = weather.now.temperature + "℃";
        String visInfo = weather.now.vis;
        String dirInfo = weather.now.wind.dir;
        String scInfo = weather.now.wind.sc;
        String spdInfo = weather.now.wind.spd + "m/s";
        weatherInfoText.setText(weatherInfo);
        flText.setText(flInfo);
        humText.setText(humInfo);
        pcpnText.setText(pcpnInfo);
        presText.setText(presInfo);
        degreeText.setText(degree);
        visText.setText(visInfo);
        dirText.setText(dirInfo);
        scText.setText(scInfo);
        spdText.setText(spdInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText = (TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            TextView dirText = (TextView)view.findViewById(R.id.dir_text);
            TextView scText = (TextView)view.findViewById(R.id.sc_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            dirText.setText(forecast.wind.dir);
            scText.setText(forecast.wind.sc);
            forecastLayout.addView(view);
        }
        if(weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            coText.setText(weather.aqi.city.co);
            no2Text.setText(weather.aqi.city.no2);
            o3Text.setText(weather.aqi.city.o3);
            pm10Text.setText(weather.aqi.city.pm10);
            pm25Text.setText(weather.aqi.city.pm25);
            qltyText.setText(weather.aqi.city.qlty);
            so2Text.setText(weather.aqi.city.so2);
        }

        String comfort = "舒适度:"+weather.suggestion.comfort.info;
        String carWash = "洗车指数:"+weather.suggestion.carWash.info;
        String drsg = "穿衣指数:"+weather.suggestion.drsg.info;
        String flu = "感冒指数:"+weather.suggestion.flu.info;
        String sport = "运动建议:"+weather.suggestion.sport.info;
        String trav = "旅游指数:"+weather.suggestion.trav.info;
        String uv = "紫外线指数:"+weather.suggestion.uv.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        drsgText.setText(drsg);
        fluText.setText(flu);
        sportText.setText(sport);
        travText.setText(trav);
        uvText.setText(uv);
        //在设置完所有数据后，再将ScrollView设为可见
        weatherLayout.setVisibility(View.VISIBLE);
        //激活AutoUpdateService这个服务，只要选中了某个城市并成功更新天气之后，
        // AutoUpdateService就会一直在后台运行，并保证每8个小时更新一次天气
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
