package com.example.cryptofuturestrader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    static Gson gson = new GsonBuilder().serializeNulls().create();
    public static Retrofit retrofit = null;



    public static BinanceAPI getRetrofitClient(){

        if(retrofit==null){
            HttpLoggingInterceptor loggingInterceptor =new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient =new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://fapi.binance.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(BinanceAPI.class);
    }
}

