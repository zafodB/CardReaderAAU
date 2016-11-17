package layout;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filip.cardreaderaau.MyReaderCallback;
import com.example.filip.cardreaderaau.R;


public class WaitingFragment extends Fragment {

    public static final String TAG = "M_TAG";

    ImageView animationView;
    AnimationDrawable mAnimation;
    Animation animationFadeOut;
    TextView statusMessage;
    View view;


    public WaitingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_waiting, container, false);

        statusMessage = (TextView) view.findViewById(R.id.status_message);

        animationView = (ImageView) view.findViewById(R.id.imageView);
        animationView.setBackgroundResource(R.drawable.spin_animation);

        mAnimation = (AnimationDrawable) animationView.getBackground();

        mAnimation.start();


        return view;


    }

    public void notifyAnim() {
        Log.i(TAG, "Animantion notified");

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Ran on UI thread");
                mAnimation.stop();
                mAnimation.setVisible(false, false);

                animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                animationView.startAnimation(animationFadeOut);
                animationView.setVisibility(View.INVISIBLE);

                statusMessage.setText(R.string.tag_found_msg);

                view.setBackgroundColor(Color.GREEN);
            }
        });


    }

}



