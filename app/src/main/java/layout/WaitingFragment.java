package layout;

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

import com.example.filip.cardreaderaau.MyReaderCallback;
import com.example.filip.cardreaderaau.R;


public class WaitingFragment extends Fragment {

    public static final String TAG = "M_TAG";

    ImageView animationView;
    AnimationDrawable mAnimation;
    Animation animationFadeOut;


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

        View view = inflater.inflate(R.layout.fragment_waiting, container, false);

        animationView = (ImageView) view.findViewById(R.id.imageView);
        animationView.setBackgroundResource(R.drawable.spin_animation);

        mAnimation = (AnimationDrawable) animationView.getBackground();

        mAnimation.start();

        animationFadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        return view;


    }

    public void notifyAnim(){
        Log.i(TAG,"Animantion notified");
        mAnimation.stop();
//        mAnimation.setVisible(false,false);
//        animationView.startAnimation(animationFadeOut);
    }



}
