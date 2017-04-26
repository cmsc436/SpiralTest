package cmsc436.umd.edu.spiraltest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import static android.R.attr.fragment;
import static cmsc436.umd.edu.spiraltest.MainActivity.DIFFICULTY_KEY;
import static cmsc436.umd.edu.spiraltest.MainActivity.SIDE_KEY;
import static cmsc436.umd.edu.spiraltest.MainActivity.TEST_TYPE_HELP;
import static cmsc436.umd.edu.spiraltest.MainActivity.TEST_TYPE_KEY;
import static cmsc436.umd.edu.spiraltest.MainActivity.TEST_TYPE_PRACTICE;
import static cmsc436.umd.edu.spiraltest.MainActivity.TEST_TYPE_TRIAL;
import static cmsc436.umd.edu.spiraltest.SpiralTestFragment.newInstance;

public class SpiralTest extends FragmentActivity {
    private static final String RESULT_KEY = "RESULT_KEY";
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String side;
    private String difficulty;
    private String testType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_test);
        Bundle bundle = getIntent().getExtras();
//        Log.i("Tag","side and difficulty: " + bundle.getString(SIDE_KEY) + " " +
//                bundle.getString(DIFFICULTY_KEY));

        side = bundle.getString(SIDE_KEY);
        difficulty = bundle.getString(DIFFICULTY_KEY);
        testType = bundle.getString(TEST_TYPE_KEY);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        if (testType.equals(TEST_TYPE_TRIAL)) {
            // run trial
        } else if (testType.equals(TEST_TYPE_HELP)) {
            // show instruction then practice
        } else {
            // run unstructured trial; TODO: set params to remove time limit
            SpiralTestFragment fragment = newInstance(side,difficulty);
            transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
        }

    }

    @Override
    public void onBackPressed() {
        // disable back button
    }
}
