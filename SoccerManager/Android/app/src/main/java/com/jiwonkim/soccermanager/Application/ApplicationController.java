package com.jiwonkim.soccermanager.Application;

import android.app.Application;

import com.jiwonkim.soccermanager.Network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by user on 2017-06-07.
 */

public class ApplicationController extends Application {
    private static ApplicationController instance;
    public static ApplicationController getInstance(){
        return instance;
    }
    private static String baseUrl = "http://210.123.255.158:3000";
    private NetworkService networkService;
    public NetworkService getNetworkService(){
        return networkService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;

        buildService();       //통신소스 완료 후 주석풀자.
    }

    public void buildService(){
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder.baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        networkService = retrofit.create(NetworkService.class);
    }

}
