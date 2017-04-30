package cmsc436.umd.edu.spiraltest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import static android.R.attr.fragment;

import static cmsc436.umd.edu.spiraltest.SpiralTestFragment.newInstance;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getAppendage;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getDifficulty;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getPatientId;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialNum;
import static edu.umd.cmsc436.frontendhelper.TrialMode.getTrialOutOf;
import static edu.umd.cmsc436.sheets.Sheets.TestType;

public class SpiralTest extends FragmentActivity {
    public static final String RESULT_KEY = "RESULT_KEY";
    public static final String TOTAL_ROUND_KEY = "TOTAL_ROUND_KEY";
    public static final String ROUND_KEY = "ROUND_KEY";
    public static final String ID_KEY = "ID_KEY";
    public static final String SIDE_KEY = "SIDE_KEY";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final String TEST_TYPE_KEY = "TEST_TYPE_KEY";
    public static final String TEST_TYPE_TRIAL = "TRIAL";
    public static final String TEST_TYPE_PRACTICE = "PRACTICE";
    public static final String TEST_TYPE_HELP = "HELP";
    public static final String MODE_KEY = "MODE_KEY";
    private static final String DEFAULT_SIDE = TestType.RH_SPIRAL.toId();
    private static final int DEFAULT_DIFFICULTY = 2;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String side;
    private int difficulty;
    private String testType;
    private int trialNum;
    private int totalTrials;
    private String patientId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_test);

        Intent intent = getIntent();
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (intent.getAction().equals("edu.umd.cmsc436.spiral.action.TRIAL")) {
            testType = TEST_TYPE_TRIAL;
            side = getAppendage(intent).toId();
            difficulty = getDifficulty(intent);
            trialNum = getTrialNum(intent);
            totalTrials = getTrialOutOf(intent);
            patientId = getPatientId(intent);
            // run trial fragment
            SpiralTestFragment fragment = newInstance(false, side, difficulty, trialNum, totalTrials, patientId);
            transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
        } else if (intent.getAction().equals("edu.umd.cmsc436.spiral.action.HELP")) {
            testType = TEST_TYPE_HELP;
            // run instruction fragment then practice

        } else {
            testType = TEST_TYPE_PRACTICE;
            SpiralTestFragment fragment = newInstance(true, DEFAULT_SIDE, DEFAULT_DIFFICULTY, null, null, null);
            transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
        }


    }

    @Override
    public void onBackPressed() {
        // disable back button
    }
}
