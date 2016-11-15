package com.example.filip.cardreaderaau;

import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "M_TAG";

    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Log.i(TAG, "Created NFC adapter");

        MyReaderCallback mreadercallback = new MyReaderCallback();

        mNfcAdapter.enableReaderMode(this, mreadercallback, READER_FLAGS, null);

    }
}
