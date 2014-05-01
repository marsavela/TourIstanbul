package lbs.erasmus.touristanbul;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by HP on 03/04/2014.
 */
public class CurrencyConverterActivity extends BaseGameActivity implements AdapterView.OnItemSelectedListener {

        private EditText entry;
        private TextView exit, from, to;
        private TextView destinyCurrency;
        private boolean changeCurrency;
        private Button changeCurrencyButton, converter;
        private String currency2, currencyAcronym, previousCurrencyAcronym, previousCurrencyAcronymSpinner;
        private Spinner currencyOri;
        private SharedPreferences _prefs;
        private SharedPreferences.Editor _prefsEditor;
        private Long currencySelected, defaultEUR, defaultUSD, defaultSEK, defaultNOK, defaultJPY, defaultCNY;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_currency_converter);
            entry = (EditText) findViewById(R.id.entry);
            exit = (TextView) findViewById(R.id.exit);
            from = (TextView) findViewById(R.id.from);
            to = (TextView) findViewById(R.id.to);
            changeCurrencyButton = (Button) findViewById(R.id.changeCurrency);
            destinyCurrency = (TextView) findViewById(R.id.destinyCurrency);
            converter =  (Button) findViewById(R.id.converter);
            currency2 = "TRY";
            _prefs = this.getSharedPreferences("myPreferences", this.MODE_PRIVATE);
            //By default, the spinner is the source currency
            changeCurrency = false;


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            // Currency Origin
            currencyOri = (Spinner) findViewById(R.id.currencyOri);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.currency_array,
                    R.layout.spinner_item);

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            currencyOri.setAdapter(adapter);
        //    Log.v("VERBOSE", "Paso: " + currencyOri.getCount());



            currencyOri.setOnItemSelectedListener(this);
        //    Log.v("VERBOSE", "Paso: " + changeCurrency);

            //Call WebServices
            getAllCurrency();

            //Change currency Botton
            changeCurrencyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!changeCurrency) {
                        //Change the currency of origin by destination (Spinner to TextView)
                        changeCurrency = true;
                        currencyOri.setY(getPixels(140));
                        destinyCurrency.setY(getPixels(0));
                    } else {
                        //Change the currency of destiny by destination (TextView to Spinner  )
                        changeCurrency = false;
                        currencyOri.setY(getPixels(0));
                        destinyCurrency.setY(getPixels(140));
                     }
                }
            });

            converter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClicked(v);
                }
            });
        }
        private int getPixels(int dipValue){
            Resources r = getResources();
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
            return px;
        }

        // Management spinner
        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                   long id) {
         currencyAcronym = parent.getItemAtPosition(pos).toString().substring(parent.getItemAtPosition(pos).toString().length() - 3, parent.getItemAtPosition(pos).toString().length());

            if(currencyAcronym.equals("EUR")){
                //previousCurrencyAcronymSpinner = currencyAcronym;
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), defaultEUR);
            } else if (currencyAcronym.equals("USD")){
                //previousCurrencyAcronymSpinner = currencyAcronym;
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), defaultUSD);
            } else if (currencyAcronym.equals("SEK")){
                //previousCurrencyAcronymSpinner = currencyAcronym;
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), defaultSEK);
            } else if (currencyAcronym.equals("NOK")){
                //previousCurrencyAcronymSpinner = currencyAcronym;
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), defaultNOK);
            } else if (currencyAcronym.equals("JPY")){
               // previousCurrencyAcronymSpinner = currencyAcronym;
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), defaultJPY);
            } else if (currencyAcronym.equals("CNY")){
               // previousCurrencyAcronymSpinner = currencyAcronym;
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), defaultCNY);
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }


        // button converter
        public void isClicked(View view) {
            Resources res = this.getResources();
            String[] monetaryCurrencySybolList = res.getStringArray(R.array.monetary_symbol_array);
            try {
                if (!changeCurrency){
                    if(currencyAcronym.equals("EUR")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    } else if (currencyAcronym.equals("USD")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    } else if (currencyAcronym.equals("SEK")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    } else if (currencyAcronym.equals("NOK")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    } else if (currencyAcronym.equals("JPY")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    } else if (currencyAcronym.equals("CNY")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    }
                } else {
                    if(currencyAcronym.equals("EUR")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[0]);
                    } else if (currencyAcronym.equals("USD")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[1]);
                    } else if (currencyAcronym.equals("SEK")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[2]);
                    } else if (currencyAcronym.equals("NOK")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[3]);
                    } else if (currencyAcronym.equals("JPY")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[4]);
                    } else if (currencyAcronym.equals("CNY")){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[5]);
                    } else if (currencyAcronym.equals(currency2)){
                        exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)) + " " + monetaryCurrencySybolList[6]);
                    }
                }
                if (isSignedIn())
                    Games.Achievements.unlock(getApiClient(),  getResources().getString(R.string.achievement_currency_converter));

            } catch (Exception e) {
                exit.append("Connection error\n");
                e.printStackTrace();
            }

        }

        private void saveCurrency( String currency, double value){
            if(_prefs==null) return;
            _prefsEditor = _prefs.edit();
            if(_prefsEditor==null) return;

            _prefsEditor.putLong(currency, Double.doubleToRawLongBits(value));
            _prefsEditor.putLong("currencyUpdated", System.currentTimeMillis());
            _prefsEditor.commit();
        }

        // connect Web Service to get currencies
        public void getAllCurrency() {
            if(isConnectingToInternet() && (System.currentTimeMillis() - _prefs.getLong("currencyUpdated", 0L)) > 86400000) {
                CurrencyTask currencyTask = new CurrencyTask();
                currencyTask.execute();
                defaultEUR =  _prefs.getLong("EUR", Double.doubleToRawLongBits(3.0632));
                defaultUSD =  _prefs.getLong("USD", Double.doubleToRawLongBits(2.2155));
                defaultSEK =  _prefs.getLong("SEK", Double.doubleToRawLongBits(0.3461));
                defaultNOK =  _prefs.getLong("NOK", Double.doubleToRawLongBits(0.3677));
                defaultJPY =  _prefs.getLong("JPY", Double.doubleToRawLongBits(0.0217));
                defaultCNY =  _prefs.getLong("CNY", Double.doubleToRawLongBits(0.3572));
            } else {
                defaultEUR =  _prefs.getLong("EUR", Double.doubleToRawLongBits(3.0632));
                defaultUSD =  _prefs.getLong("USD", Double.doubleToRawLongBits(2.2155));
                defaultSEK =  _prefs.getLong("SEK", Double.doubleToRawLongBits(0.3461));
                defaultNOK =  _prefs.getLong("NOK", Double.doubleToRawLongBits(0.3677));
                defaultJPY =  _prefs.getLong("JPY", Double.doubleToRawLongBits(0.0217));
                defaultCNY =  _prefs.getLong("CNY", Double.doubleToRawLongBits(0.3572));
            }
        }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }

    private class CurrencyTask extends AsyncTask<Void, Void, Void> {


            @Override
            protected Void doInBackground(Void... params) {
                // TODO Auto-generated method stub
                Long timeStart = System.currentTimeMillis();
                // Web Service

                try {
                    SAXParserFactory parserFac = SAXParserFactory.newInstance();
                    SAXParser parser = parserFac.newSAXParser();
                    XMLReader reader = parser.getXMLReader();
                    HandlerXML handlerXML = new HandlerXML();
                    String[] currencyArray = getResources().getStringArray(R.array.currency_array);
                    URL url = null;
                    double value = 0.0;
                    String currency = "";

                    for (int i = 0; i < currencyArray.length; i++) {
                        currency = currencyArray[i];
                        Log.v("VERBOSE", " currency " + currency.substring(currency.length() - 3, currency.length()));
                        url = new URL(
                                "http://www.webservicex.net/CurrencyConvertor.asmx/ConversionRate?FromCurrency="
                                        + URLEncoder.encode(currency.substring(currency.length() - 3, currency.length()), "UTF-8")
                                        + "&ToCurrency=TRY"
                        );
                        reader.setContentHandler(handlerXML);
                        reader.parse(new InputSource(url.openStream()));
                        value = handlerXML.getTotalResults();
                        Log.v("VERBOSE", " currency " + currency +  " value " + value);
                        saveCurrency(currency, value);
                        Log.v("VERBOSE", " variable guardada " + _prefs.getLong(currency, Double.doubleToRawLongBits(0.0)));
                    }

                } catch (Exception e) {
                    Toast toast = Toast.makeText(getApplicationContext(), "This service needs web connectivity, please active your network", Toast.LENGTH_SHORT);
                    toast.show();
                }
                Log.v("VERBOSE","tiempo total del hilo: "+ (System.currentTimeMillis() - timeStart));
                return null;
            }

            /**   @Override
            protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            }

             @Override
             protected void onPreExecute() {
             // TODO Auto-generated method stub
             super.onPreExecute();

             }

             @Override
             protected void onProgressUpdate(Void... values) {
             // TODO Auto-generated method stub
             super.onProgressUpdate(values);

             } */

        }

    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

}
