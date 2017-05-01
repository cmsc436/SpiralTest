package cmsc436.umd.edu.spiraltest;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpiralInstructionFragment extends Fragment {

    private Button startButton;
    private StartSpiralPracticeListener callback;

    public interface StartSpiralPracticeListener {
        public void startPractice();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_spiral_instruction, container, false);
        startButton = (Button)view.findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.startPractice();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            callback = (StartSpiralPracticeListener)activity;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
