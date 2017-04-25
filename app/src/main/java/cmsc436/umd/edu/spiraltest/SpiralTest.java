package cmsc436.umd.edu.spiraltest;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import static cmsc436.umd.edu.spiraltest.SpiralTestFragment.newInstance;

public class SpiralTest extends FragmentActivity {
    private static final String RESULT_KEY = "RESULT_KEY";
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private String side,difficulty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spiral_test);
        Bundle bundle = getIntent().getExtras();
        Log.i("Tag","side and difficulty: " + bundle.getString("side") + " " +
                bundle.getString("difficulty"));
        side = bundle.getString("side");
        difficulty = bundle.getString("difficulty");
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        SpiralTestFragment fragment = newInstance(side,difficulty);
        transaction.add(R.id.fragmentContainer,fragment).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        // disable back button
    }
}
