package cmsc436.umd.edu.spiraltest;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import edu.umd.cmsc436.sheets.Sheets;


/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeMenuFragment extends Fragment {

    private String side;
    private int difficulty;

    public PracticeMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_practice_menu, container, false);
        Button startButton = (Button) view.findViewById(R.id.startButton);
        RadioGroup sideRadioGroup = (RadioGroup) view.findViewById(R.id.side);
        RadioGroup difficultyRadioGroup = (RadioGroup) view.findViewById(R.id.difficulty);
        side = null;
        difficulty = -1;

        sideRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                side = checkedId == R.id.left ? Sheets.TestType.LH_SPIRAL.toId() : Sheets.TestType.RH_SPIRAL.toId();
            }
        });

        difficultyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch(checkedId){
                    case R.id.easy:
                        difficulty = 1;
                        break;
                    case R.id.medium:
                        difficulty = 2;
                        break;
                    case R.id.hard:
                        difficulty = 3;
                        break;
                    default:
                        break;
                }
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (side != null && difficulty != -1) {
                    ((SpiralTest)getActivity()).startPractice(side, difficulty);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

}
