package com.example.filip.cardreaderaau;

import android.graphics.drawable.AnimationDrawable;
import android.nfc.NfcAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import layout.WaitingFragment;

public class MainActivity extends AppCompatActivity implements MyReaderCallback.StartAnimationInterface{

    public static final String TAG = "M_TAG";
    public static final String FRAGMENT_TAG = "readerFragment";

    private WaitingFragment fragment;

    public static int READER_FLAGS =
            NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = new WaitingFragment();
            transaction.replace(R.id.waiting_fragment_placeholder, fragment, FRAGMENT_TAG);
            transaction.commit();
        }

        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Log.i(TAG, "Created NFC adapter");

        MyReaderCallback mreadercallback = new MyReaderCallback(this);

        mNfcAdapter.enableReaderMode(this, mreadercallback, READER_FLAGS, null);
    }

    @Override
    public void notifyAnimation() {
        Log.i(TAG, "I hear you");
        if (fragment == null){
            Log.i(TAG, "Fragment not ready yet");
        }
        else
            Log.i(TAG, "Notifying animation");
            fragment.notifyAnim();
    }
}
