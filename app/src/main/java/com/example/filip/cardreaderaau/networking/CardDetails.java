package com.example.filip.cardreaderaau.networking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by filip on 04/12/2016.
 */

public class CardDetails {

    //TODO input serialized names!

    @SerializedName("") String cardID;
    @SerializedName("") int accessLvl;

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
        this.cardID = cardID;
    }
}
