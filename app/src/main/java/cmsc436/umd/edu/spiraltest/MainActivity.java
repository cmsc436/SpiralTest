package cmsc436.umd.edu.spiraltest;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private String side;
    private String difficulty;
    private RadioGroup side_rg;
    private RadioGroup difficulty_rg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        b.putString("side", side);
        b.putString("difficulty", difficulty);
        intent.putExtras(b);

        startActivity(intent);
        finish();
    }
}
