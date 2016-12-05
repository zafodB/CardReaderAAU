package com.example.filip.cardreaderaau.networking;

import android.util.Log;

import com.example.filip.cardreaderaau.Constants;
import com.google.gson.annotations.SerializedName;

/**
 * Created by filip on 05/12/2016.
 */

public class Details {

    /**
     * Created by filip on 04/12/2016.
     */

    public static class UserDetails extends Details {

        @SerializedName("user_id") String cardID;
        @SerializedName("access_lvl") int accessLvl;

        public int getAccessLvl() {
            return accessLvl;
        }

        public void setAccessLvl(int accessLvl) {
            this.accessLvl = accessLvl;
        }

        public String getCardID() {
            return cardID;
        }

        public void setCardID(String cardID) {
            Log.i(Constants.TAG, "Setting cardID under user");
            this.cardID = cardID;
        }
    }

    /**
     * Created by filip on 04/12/2016.
     */

    public static class CardDetails extends Details{

        @SerializedName("card_id") String cardID;
        @SerializedName("access_lvl") int accessLvl;

        public int getAccessLvl() {
            return accessLvl;
        }

        public void setAccessLvl(int accessLvl) {
            this.accessLvl = accessLvl;
        }

        public String getCardID() {
            return cardID;
        }

        public void setCardID(String cardID) {
            Log.i(Constants.TAG, "Setting cardID under card");
            this.cardID = cardID;
        }
    }
}


