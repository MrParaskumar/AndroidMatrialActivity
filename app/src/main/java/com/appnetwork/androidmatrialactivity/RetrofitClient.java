package com.appnetwork.androidmatrialactivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL="http://www.carricktechnologies.com/funday/";
    private static RetrofitClient mInstanse;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static synchronized RetrofitClient getInstance(){
        if (mInstanse==null){
            mInstanse=new RetrofitClient();
        }
        return mInstanse;
    }
    public Api getApi(){
        return retrofit.create(Api.class);
    }
}
