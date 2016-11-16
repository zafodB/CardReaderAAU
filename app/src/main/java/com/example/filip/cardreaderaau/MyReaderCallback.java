package com.example.filip.cardreaderaau;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Objects;

import layout.WaitingFragment;

/**
 * Created by filip on 15/11/2016.
 */

public class MyReaderCallback implements NfcAdapter.ReaderCallback {

    public static final String TAG = "M_TAG";

    private static final String SELECT_APDU_HEADER = "00A40400";
    private static final String AAU_ACCESSSYSTEM_AID = "F222222222";

    StartAnimationInterface mStartAnim;


    public interface StartAnimationInterface{
        void notifyAnimation();
    }

    MyReaderCallback(Activity activity){
        try {
            mStartAnim = (StartAnimationInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep mIsoDep = IsoDep.get(tag);

        Log.i(TAG, "Tag discovered");

        if (mIsoDep != null){
            try {
                mIsoDep.connect();

                byte[] sendCommand = BuildSelectApdu(AAU_ACCESSSYSTEM_AID);

                byte[] reply1 = mIsoDep.transceive(sendCommand);

                String output = "reply1 is: ";

                for (byte b:reply1){
                    output = output.concat(Objects.toString(b) + " ");
                }

                Log.i(TAG, output);

                Log.i(TAG, "called interface");
                mStartAnim.notifyAnimation();


            } catch (IOException e) {
                Log.i(TAG, "Cought following IOException");
                Log.e(TAG, e.getMessage());
//                Log.i(TAG, e.getStackTrace().toString());
                e.printStackTrace();
            }
        }else {
            Log.i(TAG, "IsoDep is null");
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
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     *
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
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}