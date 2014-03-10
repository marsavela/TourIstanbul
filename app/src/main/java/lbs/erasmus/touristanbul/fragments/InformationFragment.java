package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lbs.erasmus.touristanbul.R;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class InformationFragment extends Fragment {

    public InformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Information");
        return rootView;
    }
}