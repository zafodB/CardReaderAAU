package com.zafodB.cardreaderaau.networking;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by filip on 01/12/2016.
 */

public class RestService {

    public static final String SERVER_URL = "http://access-node.herokuapp.com";
    public static MyRetrofitAPI myService;

    public static MyRetrofitAPI getInstance(){
        if (myService == null){
            Retrofit myRetrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            myService = myRetrofit.create(MyRetrofitAPI.class);
            return myService;
        }
        else{
            return myService;
        }
    }
}
