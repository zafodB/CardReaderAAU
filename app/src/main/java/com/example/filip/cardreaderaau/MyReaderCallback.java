package com.example.filip.cardreaderaau;

import android.app.Activity;
import android.app.Fragment;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.util.Log;
import android.webkit.WebViewFragment;

import java.io.IOException;
import java.util.Objects;

import layout.WaitingFragment;

/**
 * Created by filip on 15/11/2016.
 */

public class MyReaderCallback implements NfcAdapter.ReaderCallback {

    public static final String TAG = "M_TAG";

    public static final int STATUS_TAG_DETECTED = 1;
    public static final int STATUS_TAG_ERROR = 0;

    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final String ACCESSSYSTEM_AID = "F231120161";


    private StartAnimationInterface mStartAnim;
    private MainActivity currentActivity;


    interface StartAnimationInterface {
        void notifyAnimation(int status, String message);
    }

    MyReaderCallback(MainActivity activity) {
        currentActivity = activity;
        try {
            mStartAnim = activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep mIsoDep = IsoDep.get(tag);
        MifareUltralight mMifare = MifareUltralight.get(tag);

//        Log.i(TAG, "Tag discovered");

        if (mIsoDep != null) {
            try {
                mIsoDep.connect();

                byte[] sendCommand = BuildSelectApdu(ACCESSSYSTEM_AID);

                byte[] reply = mIsoDep.transceive(sendCommand);

                String output = new String(reply);

                Log.i(TAG, output);

                mStartAnim.notifyAnimation(STATUS_TAG_DETECTED, currentActivity.getString(R.string.tag_found_msg));


            } catch (IOException e) {
                Log.i(TAG, "Caught following IOException");
                Log.e(TAG, e.getMessage());
//                Log.i(TAG, e.getStackTrace().toString());
                e.printStackTrace();
            }
        } else if (mMifare != null){
            try {

                mMifare.connect();

                byte[] tagData = mMifare.readPages(0);

                String serialNo = "";

                for (int i = 0; i <= 9 ; i++){
                    serialNo = serialNo + (String.valueOf(tagData[i]));
                }

                Log.i(TAG, "Serial number is: " + serialNo);

                mStartAnim.notifyAnimation(STATUS_TAG_DETECTED, currentActivity.getString(R.string.tag_found_msg));

            } catch (IOException e) {
                Log.i(TAG, "IO exception");
                e.printStackTrace();
            }
        }else {
            mStartAnim.notifyAnimation(STATUS_TAG_ERROR, currentActivity.getString(R.string.tag_error_msg));
        }
    }


    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]

        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    /**
     * Utility class to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     * <p>
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}