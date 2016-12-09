package com.example.filip.cardreaderaau.networking;

import android.util.Log;

import com.example.filip.cardreaderaau.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by filip on 05/12/2016.
 */

public class Details {

    public static class UserDetails extends Details {

        @SerializedName("user_id") String cardID;
        @SerializedName("access_lvl") int accessLvl;

        public void setAccessLvl(int accessLvl) {
            this.accessLvl = accessLvl;
        }

        public void setCardID(String cardID) {
            this.cardID = cardID;
        }
    }

    public static class CardDetails extends Details{

        @SerializedName("card_id") String cardID;
        @SerializedName("access_lvl") int accessLvl;

        public void setAccessLvl(int accessLvl) {
            this.accessLvl = accessLvl;
        }

        public void setCardID(String cardID) {
            Log.i(Constants.TAG, "Setting cardID under card");
            this.cardID = cardID;
        }
    }
}


