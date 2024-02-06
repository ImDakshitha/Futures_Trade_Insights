package com.example.cryptofuturestrader;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Url;

public interface BinanceAPI {

    @GET
    Call<List<Trades>> getTrades(@Header("X-MBX-APIKEY") String header, @Url String url);

    @GET
    Call<List<Balances>> getBalance(@Header("X-MBX-APIKEY") String header, @Url String url);

}
