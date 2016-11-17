package layout;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

    ImageView myImageView;
    AnimationDrawable mAnimation;
    Animation animationFade;
    TextView statusMessage;
    View view;

    private String messagePasser;
    private int status;


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

        myImageView = (ImageView) view.findViewById(R.id.imageView);
        myImageView.setBackgroundResource(R.drawable.spin_animation);

//        myImageView.setBackgroundResource(R.drawable.checkmark);

        mAnimation = (AnimationDrawable) myImageView.getBackground();

        mAnimation.start();


        return view;


    }

    public void triggerAnim(final int status, String message) {
        Log.i(TAG, "Animantion notified");

        messagePasser = message;
        this.status = status;

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Ran on UI thread");
                mAnimation.stop();
                mAnimation.setVisible(false, false);

                if (status == MyReaderCallback.STATUS_TAG_DETECTED){
                    view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.transition_green));
                }
                else {
                    view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.transition_red));
                }
                TransitionDrawable transition = (TransitionDrawable) view.getBackground();
                transition.startTransition(300);


                animationFade = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                myImageView.startAnimation(animationFade);
//                myImageView.setVisibility(View.INVISIBLE);

                if (status == MyReaderCallback.STATUS_TAG_DETECTED){
                    myImageView.setBackgroundResource(R.drawable.checkmark);
                    animationFade = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                    myImageView.startAnimation(animationFade);
                }

                statusMessage.setText(messagePasser);


            }
        });


    }

}



