package cmsc436.umd.edu.spiraltest;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultsFragment extends Fragment {
    private static final String ACCURACY = "ACCURACY";
    private static final String TIME = "TIME";
    private static final String SCORE = "SCORE";


    private float accuracy;
    private float time;
    private float score;


    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(float acc, float time, float score) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putFloat(ACCURACY, acc);
        args.putFloat(TIME, time);
        args.putFloat(SCORE, score);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accuracy = getArguments().getFloat(ACCURACY);
            time = getArguments().getFloat(TIME);
            score = getArguments().getFloat(SCORE);
//            Log.d("results:", String.valueOf(time));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_results, container, false);

        //assign scores
        TextView acc = (TextView) v.findViewById(R.id.accuracy);
        TextView timeText = (TextView) v.findViewById(R.id.time);
        TextView scoreText = (TextView) v.findViewById(R.id.score);

        acc.setText("Accuracy: " + accuracy + "%");
        timeText.setText("Time Spent: " + time/1000 + "seconds");
        scoreText.setText("Score: " + score);

        return v;
    }
}
