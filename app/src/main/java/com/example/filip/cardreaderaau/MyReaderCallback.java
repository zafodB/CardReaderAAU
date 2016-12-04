package com.example.filip.cardreaderaau;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

import com.example.filip.cardreaderaau.networking.CardDetails;
import com.example.filip.cardreaderaau.networking.MyRetrofitAPI;
import com.example.filip.cardreaderaau.networking.RestService;

import java.io.IOException;

import layout.WaitingFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by filip on 15/11/2016.
 */

public class MyReaderCallback implements NfcAdapter.ReaderCallback {

    private StartAnimationInterface mStartAnim;
    private MainActivity currentActivity;

    MyRetrofitAPI service;

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
        boolean readError = true;

        if (mIsoDep != null) {
            try {
                mIsoDep.connect();

                byte[] sendCommand = BuildSelectApdu(Constants.ACCESSSYSTEM_AID);

                byte[] reply = mIsoDep.transceive(sendCommand);

                String serialNo = new String(reply);

                Log.i(Constants.TAG, serialNo);
                readError = false;

                contactServer(serialNo);

            } catch (IOException e) {
                Log.i(Constants.TAG, "Caught following IOException");
                Log.e(Constants.TAG, e.getMessage());
//                Log.i(TAG, e.getStackTrace().toString());
                e.printStackTrace();
                readError = true;
            }
        } else if (mMifare != null) {
            try {

                mMifare.connect();

                byte[] tagData = mMifare.readPages(0);

                String serialNo = "";

                for (int i = 0; i <= 9; i++) {
                    serialNo = serialNo + (String.valueOf(tagData[i]));
                }

                Log.i(Constants.TAG, "Serial number is: " + serialNo);
                readError = false;

                contactServer(serialNo);


            } catch (IOException e) {
                Log.i(Constants.TAG, "IO exception");
                e.printStackTrace();
                readError = true;
            }
        }

        if (readError) {
            mStartAnim.notifyAnimation(Constants.STATUS_TAG_ERROR, currentActivity.getString(R.string.tag_error_msg));
        }
        else {
            mStartAnim.notifyAnimation(Constants.STATUS_TAG_DETECTED, currentActivity.getString(R.string.tag_found_msg));
        }
        //TODO add other status options (e.g. card found, but access revoked).
    }


    private boolean contactServer(String cardID){
        service = RestService.getInstance();

        WaitingFragment fragment = currentActivity.getFragment();
        int accessConst = (int) fragment.getmSpinner().getSelectedItemId();


        //TODO change access level selection.
        Call<Boolean> call = service.checkCard(wrapCardData(cardID, accessConst));
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                //TODO handle shit here
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

        return false;
    }

    private CardDetails wrapCardData(String cardID, int access){
        CardDetails card = new CardDetails();

        card.setCardID(cardID);
        card.setAccessLvl(access);

        return card;
    }

    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]

        return HexStringToByteArray(Constants.SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
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