package cmsc436.umd.edu.spiraltest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import edu.umd.cmsc436.sheets.Sheets;

import static cmsc436.umd.edu.spiraltest.ResultsFragment.newInstance;
import static cmsc436.umd.edu.spiraltest.SpiralTestFragment.newInstance;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getAppendage;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getDifficulty;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getPatientId;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialNum;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialOutOf;
import static edu.umd.cmsc436.sheets.Sheets.TestType;

public class SpiralTest extends FragmentActivity implements
        SpiralInstructionFragment.StartSpiralPracticeListener, Sheets.Host {
    public static final String RESULT_KEY = "RESULT_KEY";
    public static final String TOTAL_ROUND_KEY = "TOTAL_ROUND_KEY";
    public static final String ROUND_KEY = "ROUND_KEY";
    public static final String ID_KEY = "ID_KEY";
    public static final String SIDE_KEY = "SIDE_KEY";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final String MODE_KEY = "MODE_KEY";
    private static final String DEFAULT_SIDE = TestType.RH_SPIRAL.toId();
    private static final int DEFAULT_DIFFICULTY = 1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String side;
    private int difficulty;
    private int trialNum;
    private int totalTrials;
    private String patientId;
    private Sheets sheet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_test);
        Intent intent = getIntent();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }

        if (intent.getAction().equals("edu.umd.cmsc436.spiral.action.TRIAL")) {
            side = getAppendage(intent).toId();
            difficulty = getDifficulty(intent);
            trialNum = getTrialNum(intent);
            totalTrials = getTrialOutOf(intent);
            patientId = getPatientId(intent);
            sheet = new Sheets(this, this, getString(R.string.app_name),getString(R.string.class_sheet),
                    getString(R.string.trial_sheet));
            // run trial fragment
            SpiralTestFragment fragment = newInstance(false, side, difficulty, trialNum, totalTrials, patientId);
            transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
        } else if (intent.getAction().equals("edu.umd.cmsc436.spiral.action.HELP")) {
            // run instruction fragment then practice
            SpiralInstructionFragment fragment = new SpiralInstructionFragment();
            transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();

        } else {
            SpiralTestFragment fragment = newInstance(true, DEFAULT_SIDE, DEFAULT_DIFFICULTY, -1, -1, null);

            //Test TRIAL Mode
//            SpiralTestFragment fragment = newInstance(false, TestType.LH_SPIRAL.toId(), DEFAULT_DIFFICULTY, 1, 3, "user123");
//            sheet = new Sheets(this, this, getString(R.string.app_name),getString(R.string.class_sheet),
//                    getString(R.string.trial_sheet));

//            SpiralInstructionFragment fragment = new SpiralInstructionFragment(); // test HELP mode
            transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("score",(float)0);
        setResult(Activity.RESULT_CANCELED,data);
        finish();
    }

    public void sendToFrontEnd(float result) {
        Intent data = new Intent();
        data.putExtra("score",result);
        setResult(Activity.RESULT_OK,data);
    }

    public void startPractice() {
        SpiralTestFragment fragment = newInstance(true, DEFAULT_SIDE, DEFAULT_DIFFICULTY, -1, -1, null);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public int getRequestCode(Sheets.Action action) {
        switch (action) {
            case REQUEST_ACCOUNT_NAME:
                return 1;
            case REQUEST_AUTHORIZATION:
                return 2;
            case REQUEST_PERMISSIONS:
                return 3;
            case REQUEST_PLAY_SERVICES:
                return 4;
            case REQUEST_CONNECTION_RESOLUTION:
                return 5;
            default:
                return -1;
        }
    }
    @Override
    public void notifyFinished(Exception e) {
        if (e != null) {
            throw new RuntimeException(e);
        }
        Log.i(getClass().getSimpleName(), "Done");
        finish();
    }

    public void finishTest(float accuracy, float time, float score) {
        ResultsFragment fragment = newInstance(accuracy, time, score);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        this.sheet.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.sheet.onActivityResult(requestCode, resultCode, data);
    }

    public void sendToGroupSheet(String userId,String side, float[] trialData){
        if (side.indexOf("LH") != -1){
//            sheet.writeTrials(TestType.LH_SPIRAL,userId,trialData);
        }else {
//            sheet.writeTrials(TestType.RH_SPIRAL,userId,trialData);
        }


    }

    public void sendToDrive(String imageName,Bitmap bitmap) {
        sheet.uploadToDrive(getString(R.string.spiral_folder), imageName, bitmap);
    };

    //apparently we don't need to send anything to class for now.
//    public void sendToClassSheet(String userId,String side, float score){
//        if (side.indexOf("LH") != -1){
//            sheet.writeData(TestType.LH_SPIRAL, userId, score);
//        }else {
//            sheet.writeData(TestType.RH_SPIRAL, userId, score);
//        }
//    }

}
