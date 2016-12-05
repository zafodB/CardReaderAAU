package com.example.filip.cardreaderaau;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

import com.example.filip.cardreaderaau.networking.Access;
import com.example.filip.cardreaderaau.networking.Details;
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

    private int status;

    interface StartAnimationInterface {
        void notifyAnimation(int status);
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

        if (mIsoDep != null) {
            try {
                mIsoDep.connect();

                byte[] sendCommand = BuildSelectApdu(Constants.ACCESSSYSTEM_AID);

                byte[] reply = mIsoDep.transceive(sendCommand);

                String serialNo = new String(reply);

                Log.i(Constants.TAG, serialNo);

                contactServer(serialNo, Constants.TAG_TYPE_USER);

            } catch (IOException e) {
                Log.i(Constants.TAG, "Caught following IOException");
                Log.e(Constants.TAG, e.getMessage());

                status = Constants.STATUS_TAG_ERROR;
                notifyAnim(status);
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

                contactServer(serialNo, Constants.TAG_TYPE_CARD);

            } catch (IOException e) {
                Log.i(Constants.TAG, e.getMessage());

                status = Constants.STATUS_TAG_ERROR;
                notifyAnim(status);
            }
        }
    }


    private void contactServer(String cardID, int tagType){
        WaitingFragment fragment = currentActivity.getFragment();
        final int accessConst = (int) fragment.getmSpinner().getSelectedItemId();
        //TODO change access level selection ^^^

        MyRetrofitAPI service = RestService.getInstance();

        //TODO remake so route is not hardcoded
        Call<Access> call = null;
        switch (tagType){
            case Constants.TAG_TYPE_CARD:
                call = service.checkCardCard("aau", (Details.CardDetails) wrapCardData(tagType, cardID, accessConst));
                break;
            case Constants.TAG_TYPE_USER:
                call = service.checkCardHCE("aau", (Details.UserDetails) wrapCardData(tagType, cardID, accessConst));
                break;
        }


        Log.i(Constants.TAG, "Access level is: " + String.valueOf(accessConst));
        call.enqueue(new Callback<Access>() {
            @Override
            public void onResponse(Call<Access> call, Response<Access> response) {
//                TODO handle shit here

//                call.request().body()
                Log.i(Constants.TAG, "The body of the call is: " + call.request().body().toString());

                boolean accessGranted = false;
                try {
                    Log.i(Constants.TAG, "The message is: " + response.body().getMessage());
                    accessGranted = response.body().isAccessGranted();
                }
                catch (NullPointerException e){
                    Log.i(Constants.TAG, "Caught null-pointer exception with following message:" + e.getMessage());
                }

                if(accessGranted){
                    Log.i(Constants.TAG, "Hurray, access granted");
                    notifyAnim(Constants.STATUS_ACCESS_GRANTED);
                }
                else {
                    Log.i(Constants.TAG, "Naay, access denied");
                    notifyAnim(Constants.STATUS_ACCESS_DENIED);
                }
            }

            @Override
            public void onFailure(Call<Access> call, Throwable t) {
                Log.i(Constants.TAG,"On Failure called with following message: " + t.getMessage());
//                notifyAnim(Constants.STATUS_SERVER_ERROR);
            }
        });
    }

    private Details wrapCardData(int tagType, String cardID, int access){
        switch (tagType){
            case Constants.TAG_TYPE_CARD:
                Details.CardDetails card = new Details.CardDetails();
                card.setCardID(cardID);
                card.setAccessLvl(access);

                Log.i(Constants.TAG, "wrapping data card");
                return card;

            case Constants.TAG_TYPE_USER:
                Details.UserDetails user = new Details.UserDetails();
                user.setCardID(cardID);
                user.setAccessLvl(access);
                Log.i(Constants.TAG, "wrapping data user");
                return user;

            default:
                return null;
        }
    }

    private void notifyAnim(int status){
        mStartAnim.notifyAnimation(status);
    }

    private static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]

        return HexStringToByteArray(Constants.SELECT_APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

//    /**
//     * Utility class to convert a byte array to a hexadecimal string.
//     *
//     * @param bytes Bytes to convert
//     * @return String, containing hexadecimal representation.
//     */
//    public static String ByteArrayToHexString(byte[] bytes) {
//        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
//        char[] hexChars = new char[bytes.length * 2];
//        int v;
//        for (int j = 0; j < bytes.length; j++) {
//            v = bytes[j] & 0xFF;
//            hexChars[j * 2] = hexArray[v >>> 4];
//            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
//        }
//        return new String(hexChars);
//    }

    /**
     * Utility class to convert a hexadecimal string to a byte string.
     * <p>
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     */
    private static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}