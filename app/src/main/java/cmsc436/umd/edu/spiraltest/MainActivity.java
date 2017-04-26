package cmsc436.umd.edu.spiraltest;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import static cmsc436.umd.edu.spiraltest.SpiralTestFragment.newInstance;

public class MainActivity extends AppCompatActivity {
    public static final String SIDE_KEY = "SIDE_KEY";
    public static final String DIFFICULTY_KEY = "DIFFICULTY_KEY";
    public static final String TEST_TYPE_KEY = "TEST_TYPE_KEY";
    public static final String TEST_TYPE_TRIAL = "TRIAL";
    public static final String TEST_TYPE_PRACTICE = "PRACTICE";
    public static final String TEST_TYPE_HELP = "HELP";

    private String side;
    private String difficulty;
    private RadioGroup side_rg;
    private RadioGroup difficulty_rg;
    private String testType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if (intent.getAction().equals("edu.umd.cmsc436.spiral.action.TRIAL")) {
            testType = TEST_TYPE_TRIAL;
        } else if (intent.getAction().equals("edu.umd.cmsc436.spiral.action.HELP")) {
            testType = TEST_TYPE_HELP;
        } else {
            testType = TEST_TYPE_PRACTICE;
        }

        side_rg = (RadioGroup) findViewById(R.id.side);
        difficulty_rg = (RadioGroup) findViewById(R.id.difficulty);

        side_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                side = checkedId == R.id.left ? "left" : "right";
            }
        });

        difficulty_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.easy:
                        difficulty = "easy";
                        break;
                    case R.id.medium:
                        difficulty = "medium";
                        break;
                    case R.id.hard:
                        difficulty = "hard";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void startTest(View view) {
        Intent intent = new Intent(this, SpiralTest.class);
        Bundle b = new Bundle();
        b.putString(SIDE_KEY, side);
        b.putString(DIFFICULTY_KEY, difficulty);
        b.putString(TEST_TYPE_KEY, testType);
        intent.putExtras(b);

        startActivity(intent);
        finish();
    }
}
