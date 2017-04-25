package cmsc436.umd.edu.spiraltest;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SpiralTestFragment extends Fragment{
    private OnFinishListener callback;
    public static final String HAND_KEY = "HAND_KEY";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final int EASY_TRACE_SIZE = 50;
    public static final int MEDIUM_TRACE_SIZE = 40;
    public static final int HARD_TRACE_SIZE = 30;

    private Activity activity;
    private Button button;
    private ImageView original;
    private View view;
    private String side;
    private String difficulty;
    private DrawingView drawView;
    private int timer_length;
    private CountDownTimer timer;
    private TextView text;
    private boolean started = false;

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

        text = (TextView) view.findViewById(R.id.roundText);
        drawView = (DrawingView) view.findViewById(R.id.drawView);
        original = (ImageView)view.findViewById(R.id.spiral);

        // Select spiral depending on difficulty
        switch(difficulty) {
            case "easy":
                timer_length = 10000;
                original.setImageResource(R.drawable.easy_spiral);
                drawView.setDrawPaintSize(EASY_TRACE_SIZE);
                break;
            case "hard":
                timer_length = 20000;
                original.setImageResource(R.drawable.hard_spiral);
                drawView.setDrawPaintSize(HARD_TRACE_SIZE);
                break;
            default:
                timer_length = 15000;
                original.setImageResource(R.drawable.medium_spiral);
                drawView.setDrawPaintSize(MEDIUM_TRACE_SIZE);
                break;
        }

        // flip the spiral horizontally if left handed
        if (side.equals("left")) {
            original.setScaleX(-1);
        }

        timer = new CountDownTimer(timer_length,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                text.setText("Timer: " + millisUntilFinished/1000);
            }

            // once timer is completed, user should not be able to draw anymore
            @Override
            public void onFinish() {
                text.setText("Time's up! Please click Finish.");
                drawView.pause();
            }
        };

        // starts timer when user begins drawing
        drawView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!started) {
                    started = true;
                    timer.start();
                }
                return false;
            }
        });

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



