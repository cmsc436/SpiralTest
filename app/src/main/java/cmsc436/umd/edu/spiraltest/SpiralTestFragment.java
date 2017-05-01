package cmsc436.umd.edu.spiraltest;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import edu.umd.cmsc436.sheets.Sheets;

import static cmsc436.umd.edu.spiraltest.SpiralTest.DIFFICULTY_KEY;
import static cmsc436.umd.edu.spiraltest.SpiralTest.MODE_KEY;
import static cmsc436.umd.edu.spiraltest.SpiralTest.SIDE_KEY;
import static cmsc436.umd.edu.spiraltest.SpiralTest.ID_KEY;
import static cmsc436.umd.edu.spiraltest.SpiralTest.ROUND_KEY;
import static cmsc436.umd.edu.spiraltest.SpiralTest.TOTAL_ROUND_KEY;

public class SpiralTestFragment extends Fragment{
    private OnFinishListener callback;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String HAND_KEY = "HAND_KEY";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final int EASY_TRACE_SIZE = 60;
    public static final int MEDIUM_TRACE_SIZE = 50;
    public static final int HARD_TRACE_SIZE = 40;


    private Activity activity;
    private Button button;
    private ImageView original;
    private View view;
    private String side;
    private int difficulty;
    private DrawingView drawView;
    private CountDownTimer timer;
    private TextView text;
    private boolean started = false;
    private boolean isPractice;
    private int round;
    private int totalRounds;
    private TextView roundText;

    // [0] = time allotted
    // [1] = time spent
    // [2] = time remaining
    private long[] time;

    // [0] = overall score
    // [1] = correctly drawn/total drawn
    // [2] = pixels missed on original spiral
    // [3] = duration
    private Float[] results;

    public interface OnFinishListener{
        //do nothing right now
    }


    /*
    * For whoever is doing the sheets:
    * >> Should be sending the results array to the trial sheet
    * >> For the centralized shared google sheet, send results[0]
    * >> gluck with figuring out how to send the images.
    *       probably a good idea to change the file name to include the date
    * */


    public static SpiralTestFragment newInstance(boolean isPractice, String side, int difficulty, int round, int totalRound, String patientId) {
        SpiralTestFragment fragment = new SpiralTestFragment();
        Bundle args = new Bundle();
        args.putString(SIDE_KEY, side);
        args.putInt(DIFFICULTY_KEY, difficulty);
        args.putInt(ROUND_KEY, round);
        args.putInt(TOTAL_ROUND_KEY, totalRound);
        args.putString(ID_KEY, patientId);
        args.putBoolean(MODE_KEY, isPractice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        activity = getActivity();
        view = inflater.inflate(R.layout.fragment_spiral_test, container, false);
        button = (Button)view.findViewById(R.id.finish);
        side = getArguments().getString(SIDE_KEY);
        difficulty = getArguments().getInt(DIFFICULTY_KEY);

        isPractice = getArguments().getBoolean(MODE_KEY);
        round = getArguments().getInt(ROUND_KEY);
        totalRounds = getArguments().getInt(TOTAL_ROUND_KEY);

        time = new long[3];
        results = new Float[6];

        roundText = (TextView)view.findViewById(R.id.roundText);
        text = (TextView) view.findViewById(R.id.timerText);

        drawView = (DrawingView) view.findViewById(R.id.drawView);
        original = (ImageView)view.findViewById(R.id.spiral);


        // Select spiral depending on difficulty
        switch (difficulty) {
            case 1:
                time[0] = 10000;
                original.setImageResource(R.drawable.easy_spiral);
                drawView.setDrawPaintSize(EASY_TRACE_SIZE);
                break;
            case 3:
                time[0] = 20000;
                original.setImageResource(R.drawable.hard_spiral);
                drawView.setDrawPaintSize(HARD_TRACE_SIZE);
                break;
            default:
                time[0] = 15000;
                original.setImageResource(R.drawable.medium_spiral);
                drawView.setDrawPaintSize(MEDIUM_TRACE_SIZE);
                break;
        }

        // flip the spiral horizontally if left handed
        if (side.equals(Sheets.TestType.LH_SPIRAL.toId())) {
            original.setScaleX(-1);
        }

        // if not in practice mode, allow timer and drawview listener to be set up
        if (!isPractice) {
            roundText.setText("Round " + round + " of " + totalRounds);
            timer = new CountDownTimer(time[0], 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    text.setText("Timer: " + millisUntilFinished / 1000);
                    time[1] = time[0] - millisUntilFinished;
                    time[2] = millisUntilFinished;
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
                    if (!started) {
                        started = true;
                        timer.start();
                    }
                    return false;
                }
            });

            // User finishes the test
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();

                    results[1] = computeAccuracy(); // accuracy/missed
                    results[3] = (float)time[1]; // duration
                    results[0] = computeScore(); // overall score

                    // display the overall score on the bottom of the screen so its included in screenshot
                    drawView.displayScore(results[0]);

                    text.setText("Round Complete!");
                    saveDrawing();
                    // TODO in trial mode: redirect to the results page
                }
            });
        } else {
            roundText.setText("Practice Round");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // in practice mode: redirect to spiral test main activity
                    activity.finish();
                }
            });
        }

        return view;

    }

    // 100% accuracy + 20% extra time remaining
    // initially, accuracy = 50% part of original spiral that is drawn over + 50% accurate pixels among all pixels drawn
    // but having some issues with the accuracy of portion of original spiral that is covered
    public float computeScore() {
        return (float)(results[1] + (time[2]/time[0])*20);
//        return (float)(results[1]*.5 + (100-results[2])*.5 + (time[2]/time[0])*20);
    }

    public float computeAccuracy() {
        // convert original ImageView into a Bitmap
        original.setDrawingCacheEnabled(true);
        Bitmap origbit = original.getDrawingCache();
        drawView.setDrawingCacheEnabled(true);
        Bitmap drawbit = drawView.getDrawingCache();

        // Retrieve pixel data in the form of an array for the original spiral and drawn spiral
        int width = origbit.getWidth();
        int height = origbit.getHeight();
        int[] origpixels = new int[width*height];
        int[] drawpixels = new int[width*height];
        origbit.getPixels(origpixels,0,width,1,1,width-1,height-1);
        drawbit.getPixels(drawpixels,0,width,1,1,width-1,height-1);

        float totalDrawn = 0;
        float totalAccurate = 0;
        float missed = 0;
        float totalOrig = 0;
        float score;

        // calculates accuracy and tracks non-traced parts of original
        for(int i = 0; i < width*height; i++) {
            if(drawpixels[i] != 0) {
                if(origpixels[i] != 0) {
                    totalOrig++;
                    totalAccurate++;
                }
                totalDrawn++;
            } else if (origpixels[i] != 0) {
                missed++;
                totalOrig++;
            }
        }

        if (totalDrawn == 0) {
            score = 0;
        } else {
            score = totalAccurate*100/totalDrawn;
            results[2] = missed*100/totalOrig;
        }

        return score;
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



