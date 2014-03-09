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
public class AttractionsFragment extends Fragment {

    public AttractionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Attractions");
        return rootView;
    }
}