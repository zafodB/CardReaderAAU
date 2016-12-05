package com.example.filip.cardreaderaau.networking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by filip on 05/12/2016.
 */

public class Access {



    @SerializedName("message") String message;
    @SerializedName("success") boolean accessGranted;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAccessGranted() {
        return accessGranted;
    }

    public void setAccessGranted(boolean accessGranted) {
        this.accessGranted = accessGranted;
    }
}
