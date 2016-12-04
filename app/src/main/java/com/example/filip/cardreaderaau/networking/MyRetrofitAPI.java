package com.example.filip.cardreaderaau.networking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by filip on 23/11/2016.
 */

public interface MyRetrofitAPI {

    @Headers("Content-Type:application/json")
    @POST("")
    Call<Boolean> checkCard(@Body CardDetails cardDetails);
}
