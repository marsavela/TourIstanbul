package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
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

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * Created by HP on 03/04/2014.
 */
public class CurrencyConverterActivity extends Activity implements AdapterView.OnItemSelectedListener {

        private EditText entry;
        private TextView exit;
        private TextView destinyCurrency;
        private boolean changeCurrency;
        private Button changeCurrencyButton, converter;
        private String currency2;
        private Spinner currencyOri;
        private SharedPreferences _prefs;
        private SharedPreferences.Editor _prefsEditor;
        private Long currencySelected;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_currency_converter);
            entry = (EditText) findViewById(R.id.entry);
            exit = (TextView) findViewById(R.id.exit);
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
                    android.R.layout.simple_spinner_item);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            currencyOri.setAdapter(adapter);
            Log.v("VERBOSE", "Paso: " + currencyOri.getCount());



            currencyOri.setOnItemSelectedListener(this);
            Log.v("VERBOSE", "Paso: " + changeCurrency);

            //Call WebServices
            getAllCurrency();

            //Change currency Botton
            changeCurrencyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!changeCurrency) {
                        //Change the currency of origin by destination (Spinner to TextView)
                        changeCurrency = true;
                        currencyOri.setY(getPixels(120));
                        destinyCurrency.setY(getPixels(0));
                    } else {
                        //Change the currency of destiny by destination (TextView to Spinner  )
                        changeCurrency = false;
                        currencyOri.setY(getPixels(0));
                        destinyCurrency.setY(getPixels(120));
                        // destinyCurrency.setY(valuey);
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
                ((TextView) currencyOri.getChildAt(pos)).setTextColor(Color.BLACK);

            String currencyAcronym = parent.getItemAtPosition(pos).toString().substring(parent.getItemAtPosition(pos).toString().length() - 3, parent.getItemAtPosition(pos).toString().length());

            if(currencyAcronym.equals("EUR")){
                Log.v("VERBOSE", "Valor del texto: " + " preferences " +_prefs.getFloat(currencyAcronym.toString(), Float.parseFloat(String.valueOf(3.0632))));
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), Double.doubleToRawLongBits(3.0632));
            } else if (currencyAcronym.equals("USD")){
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), Double.doubleToRawLongBits(2.2155));
            } else if (currencyAcronym.equals("SEK")){
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), Double.doubleToRawLongBits(0.3461));
            } else if (currencyAcronym.equals("NOK")){
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), Double.doubleToRawLongBits(0.3677));
            } else if (currencyAcronym.equals("JPY")){
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), Double.doubleToRawLongBits(0.0217));
            } else if (currencyAcronym.equals("CNY")){
                currencySelected =  _prefs.getLong(currencyAcronym.toString(), Double.doubleToRawLongBits(0.3572));
            }
        }

        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }


        // button converter
        public void isClicked(View view) {

            try {
                if (!changeCurrency){
                    exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) * Double.longBitsToDouble(currencySelected)));
                } else {
                    exit.setText(String.format("%.2f", Double.parseDouble(entry.getText().toString()) / Double.longBitsToDouble(currencySelected)));
                }

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
            if((System.currentTimeMillis() - _prefs.getLong("currencyUpdated", 0L)) > 86400000) {
                CurrencyTask currencyTask = new CurrencyTask();
                currencyTask.execute();
            }
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





}
