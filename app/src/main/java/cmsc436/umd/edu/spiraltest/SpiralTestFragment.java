package cmsc436.umd.edu.spiraltest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static cmsc436.umd.edu.spiraltest.MainActivity.DIFFICULTY_KEY;
import static cmsc436.umd.edu.spiraltest.MainActivity.SIDE_KEY;

public class SpiralTestFragment extends Fragment{
    private OnFinishListener callback;
    private static final int PERMISSION_REQUEST_CODE = 1;
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
        args.putString(SIDE_KEY, side);
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
        side = getArguments().getString(SIDE_KEY);
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
                saveDrawing();
                // should redirect to the results page
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

    public void saveDrawing(){
        view = this.getView();
        if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        } else {
            String imgSaved = MediaStore.Images.Media.insertImage(
                    activity.getContentResolver(), screenShot(view),
                    UUID.randomUUID().toString() + ".png", "drawing");
            if (imgSaved != null) {
                Toast savedToast = Toast.makeText(activity.getApplicationContext(),
                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                savedToast.show();
            } else {
                Toast unsavedToast = Toast.makeText(activity.getApplicationContext(),
                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                unsavedToast.show();
            }
            drawView.destroyDrawingCache();
        }

        drawView.clear();
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}



