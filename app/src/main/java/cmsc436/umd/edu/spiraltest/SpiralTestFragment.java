package cmsc436.umd.edu.spiraltest;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class SpiralTestFragment extends Fragment{
    private OnFinishListener callback;
    public static final String HAND_KEY = "HAND_KEY";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";

    private Activity activity;
    private Button button;
    private ImageView original;
    private View view;
    private String side;
    private String difficulty;

    public interface OnFinishListener{
        //do nothing right now
    }

    public static SpiralTestFragment newInstance(String side, String difficulty){
        SpiralTestFragment fragment = new SpiralTestFragment();
        Bundle args = new Bundle();
        args.putString(HAND_KEY, side);
        args.putString(DIFFICULTY_KEY, difficulty);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        activity = getActivity();
        view = inflater.inflate(R.layout.fragment_spiral_test, container, false);
        button = (Button)view.findViewById(R.id.finish);
        Bundle bundle = getArguments();
        side = getArguments().getString(HAND_KEY);
        difficulty = getArguments().getString(DIFFICULTY_KEY);
        //drawview stuff
        //drawView = (DrawingView)view.findViewById(R.id.drawing);
        original = (ImageView)view.findViewById(R.id.spiral);
        if (difficulty.equals("medium")) {
            if (side.equals("left")){
                original.setImageResource(R.drawable.med_l);
            } else {
                original.setImageResource(R.drawable.med_r);
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //should go back to activity, maybe?
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            callback = (OnFinishListener) activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



