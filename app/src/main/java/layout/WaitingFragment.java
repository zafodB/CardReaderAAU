package layout;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.filip.cardreaderaau.Constants;
import com.example.filip.cardreaderaau.R;

import java.util.Timer;
import java.util.TimerTask;


public class WaitingFragment extends Fragment {

    public static final String TAG = "M_TAG";

    ImageView myImageView;
    AnimationDrawable mAnimation;
    Animation animationFade;
    TextView statusMessage;
    TransitionDrawable transition;
    View view;

    private String messagePasser;
    Spinner mSpinner;

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

        mSpinner = (Spinner) view.findViewById(R.id.spinner);

        runAnimation();


        return view;


    }

    public void triggerAnim(final int status, String message) {

        messagePasser = message;
        this.status = status;

        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mAnimation.stop();

                if (status == Constants.STATUS_TAG_DETECTED) {
                    view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.transition_green));
                } else if (status == Constants.STATUS_TAG_ERROR) {
                    view.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.transition_red));
                } else {
                }

                transition = (TransitionDrawable) view.getBackground();
                transition.startTransition(300);

                animationFade = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
                myImageView.startAnimation(animationFade);
//                myImageView.setVisibility(View.INVISIBLE);

                if (status == Constants.STATUS_TAG_DETECTED) {
                    myImageView.setBackgroundResource(R.drawable.checkmark);
                } else if (status == Constants.STATUS_TAG_ERROR) {
                    myImageView.setBackgroundResource(R.drawable.crossmark);
                }

                animationFade = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
                myImageView.startAnimation(animationFade);
                statusMessage.setText(messagePasser);

                Timer mTimer = new Timer();
                TimerTask mTimerTask = new TimerTask() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myImageView.clearAnimation();
                                transition.reverseTransition(300);
                                statusMessage.setText(getResources().getText(R.string.status_message_text));
                                runAnimation();
                            }
                        });
                    }
                };
                mTimer.schedule(mTimerTask, 3000);
            }
        });


    }

    void runAnimation() {
        if (view != null) {
            if (myImageView == null) {
                myImageView = (ImageView) view.findViewById(R.id.imageView);
            }
            myImageView.setBackgroundResource(R.drawable.spin_animation);

            mAnimation = null;
            mAnimation = (AnimationDrawable) myImageView.getBackground();
            mAnimation.start();

        }
    }

    public Spinner getmSpinner() {
        return mSpinner;
    }
}



