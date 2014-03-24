package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import lbs.erasmus.touristanbul.DosAndDontsActivity;
import lbs.erasmus.touristanbul.HistoryActivity;
import lbs.erasmus.touristanbul.ImportantNumbersActivity;
import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.WeatherAndClimateActivity;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class InformationFragment extends Fragment implements View.OnClickListener{

    /**
     * Creating all dashboard information buttons instances
     * */
    private Button mHistory;
    private Button mNumbers;
    private Button mWeather;
    private Button mDosAndDonts;

    public InformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);

        // Dashboard History button
        mHistory = (Button) rootView.findViewById(R.id.button_history);
        // Dashboard Important Phone Numbers button
        mNumbers = (Button) rootView.findViewById(R.id.button_numbers);
        // Dashboard Weather button
        mWeather = (Button) rootView.findViewById(R.id.button_weather);
        // Dashboard Do\'s and Don\'ts button
        mDosAndDonts = (Button) rootView.findViewById(R.id.button_dos_and_donts);

        mHistory.setOnClickListener(this);
        mNumbers.setOnClickListener(this);
        mWeather.setOnClickListener(this);
        mDosAndDonts.setOnClickListener(this);

        return rootView;

    }

    /**
     * on Click events for the buttons
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_history:
                openHistory();
                break;
            case R.id.button_numbers:
                openImportantNumbers();
                break;
            case R.id.button_weather:
                openWeatherAndClimate();
                break;
            case R.id.button_dos_and_donts:
                openDosAndDonts();
                break;
        }
    }

    private void openDosAndDonts() {
        Intent i = new Intent(getActivity(), DosAndDontsActivity.class);
        startActivity(i);
    }

    private void openWeatherAndClimate() {
        Intent i = new Intent(getActivity(), WeatherAndClimateActivity.class);
        startActivity(i);
    }

    private void openImportantNumbers() {
        Intent i = new Intent(getActivity(), ImportantNumbersActivity.class);
        startActivity(i);
    }

    private void openHistory() {
        Intent i = new Intent(getActivity(), HistoryActivity.class);
        startActivity(i);
    }
}