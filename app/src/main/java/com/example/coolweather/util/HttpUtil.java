package com.example.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    //和服务器进行数据交互
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();//创建一个实例
        Request request = new Request.Builder().url(address).build();//发起http请求，通过url设置目标的网络地址
        client.newCall(request).enqueue(callback);//创建一个call对象并加入调度队伍
    }

}
