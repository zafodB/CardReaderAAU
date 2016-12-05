package com.example.filip.cardreaderaau;

/**
 * Created by filip on 04/12/2016.
 */

public class Constants {
    public static final String TAG = "M_TAG";



    public static final int STATUS_TAG_ERROR = 0;
    public static final int STATUS_ACCESS_GRANTED = 1;
    public static final int STATUS_ACCESS_DENIED = 2;
    public static final int STATUS_SERVER_ERROR = 3;

    public static final int TAG_TYPE_USER = 0;
    public static final int TAG_TYPE_CARD = 1;


    static final String SELECT_APDU_HEADER = "00A40400";
    static final String ACCESSSYSTEM_AID = "F231120161";
}
