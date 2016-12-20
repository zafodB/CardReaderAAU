package com.zafodB.cardreaderaau.networking;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by filip on 23/11/2016.
 */

public interface MyRetrofitAPI {

    @Headers("Content-Type:application/json")
    @POST("/access/{inst}")
    Call<Access> checkCardCard(@Path("inst") String inst, @Body Details.CardDetails cardDetails);

    @Headers("Content-Type:application/json")
    @POST("/access/{inst}")
    Call<Access> checkCardHCE(@Path("inst") String inst, @Body Details.UserDetails cardDetails);
}
