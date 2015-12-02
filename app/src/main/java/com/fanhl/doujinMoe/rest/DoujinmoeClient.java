package com.fanhl.doujinMoe.rest;

import com.fanhl.doujinMoe.rest.service.HomeService;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by fanhl on 15/12/1.
 */
public class DoujinmoeClient {

    public static final String BASE_URL = "http://m.doujin-moe.us";
    private final HomeService homeService;

    public DoujinmoeClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        homeService = retrofit.create(HomeService.class);
    }

    public HomeService getHomeService() {
        return homeService;
    }
}
