package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import lbs.erasmus.touristanbul.CurrencyConverterActivity;
import lbs.erasmus.touristanbul.PhrasebookActivity;
import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.TranslatorActivity;
import lbs.erasmus.touristanbul.TransportsActivity;

/**
 * Created by HP on 9/03/14.
 */
public class ToolsFragment extends Fragment implements View.OnClickListener {

    public ToolsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tools, container, false);

        /**
         Creating all dashboard information buttons instances
         */
        // Dashboard Currency Converter button
        Button mCurrencyConverter = (Button) rootView.findViewById(R.id.button_currency_converter);
        // Dashboard Transports button
        Button mTransports = (Button) rootView.findViewById(R.id.button_transports);
        // Dashboard Translator button
        Button mTranslator = (Button) rootView.findViewById(R.id.button_translator);
        // Dashboard Phrasebook button
        Button mPhrasebook = (Button) rootView.findViewById(R.id.button_phrasebook);

        mCurrencyConverter.setOnClickListener(this);
        mTransports.setOnClickListener(this);
        mTranslator.setOnClickListener(this);
        mPhrasebook.setOnClickListener(this);

        return rootView;

    }

    /**
     * on Click events for the buttons
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_currency_converter:
                openCurrencyConverter();
                break;
            case R.id.button_transports:
                openTransports();
                break;
            case R.id.button_translator:
                openTranslator();
                break;
            case R.id.button_phrasebook:
                openPhrasebook();
                break;
        }
    }

    private void openCurrencyConverter() {
        Intent i = new Intent(getActivity(), CurrencyConverterActivity.class);
        startActivity(i);
    }

    private void openTransports() {
        Intent i = new Intent(getActivity(), TransportsActivity.class);
        startActivity(i);
    }

    private void openTranslator() {
        Intent i = new Intent(getActivity(), TranslatorActivity.class);
        startActivity(i);
    }

    private void openPhrasebook() {
        Intent i = new Intent(getActivity(), PhrasebookActivity.class);
        startActivity(i);
    }

}