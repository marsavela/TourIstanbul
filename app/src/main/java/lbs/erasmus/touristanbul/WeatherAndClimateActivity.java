package lbs.erasmus.touristanbul;




import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WeatherAndClimateActivity extends Activity {

    EditText city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);
        setContentView(R.layout.activity_weather_and_climate);
        getWeather(null);
    }

    public void getWeather(View v) {
        if (isConnected()) {
            new WeatherTask().execute();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
        }
    }

    private class WeatherTask extends AsyncTask<Void, Void, Void> {

        String temperature;
        String description;
        int weather;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
                list.add(new BasicNameValuePair("APPID", "12cad205e71e363f07afa9b738950c32"));
                list.add(new BasicNameValuePair("mode", "json"));
                list.add(new BasicNameValuePair("units", "metric"));
                list.add(new BasicNameValuePair("q", "istambul"));

				/* This is just for GET/DELETE operation */
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?" +
                        URLEncodedUtils.format(list, "UTF-8"));

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                //Json
                StringBuffer buffer = new StringBuffer();
                String s;
                while ((s = reader.readLine()) != null) {
                    buffer.append(s);
                }
                reader.close();
                JSONObject object = new JSONObject(buffer.toString());
                JSONObject mainObject = object.getJSONObject("main");
                temperature = String.valueOf(mainObject.getDouble("temp"));
                JSONArray jsonList = object.getJSONArray("weather");
                object = jsonList.getJSONObject(0);
                description = object.getString("description");
                weather = object.getInt("id");

                connection.disconnect();

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            ((TextView) findViewById(R.id.tvWeatherTemperature)).setText(temperature + " ·C");
            ((TextView) findViewById(R.id.tvWeatherDescription)).setText(description);
            Drawable icon = null;
            switch(weather) {
                case 200:
                case 201:
                case 202:
                case 210:
                case 211:
                case 212:
                case 221:
                case 230:
                case 231:
                case 232:
                    icon = getResources().getDrawable(R.drawable.w11d);
                    break;
                case 300:
                case 301:
                case 302:
                case 310:
                case 311:
                case 312:
                case 321:
                case 520:
                case 521:
                case 522:
                    icon = getResources().getDrawable(R.drawable.w09d);
                    break;
                case 500:
                case 501:
                case 502:
                case 503:
                case 504:
                    icon = getResources().getDrawable(R.drawable.w10d);
                    break;
                case 511:
                case 600:
                case 601:
                case 602:
                case 611:
                case 621:
                    icon = getResources().getDrawable(R.drawable.w13d);
                    break;
                case 701:
                case 711:
                case 721:
                case 731:
                case 741:
                    icon = getResources().getDrawable(R.drawable.w50d);
                    break;
                case 800:
                    icon = getResources().getDrawable(R.drawable.w01d);
                    break;
                case 801:
                    icon = getResources().getDrawable(R.drawable.w02d);
                    break;
                case 802:
                    icon = getResources().getDrawable(R.drawable.w03d);
                    break;
                case 803:
                    icon = getResources().getDrawable(R.drawable.w04d);
                    break;
                case 804:
                    icon = getResources().getDrawable(R.drawable.w04d);
                    break;
            }
            ((ImageView) findViewById(R.id.ivWeatherIcon)).setImageDrawable(icon);

            setProgressBarIndeterminateVisibility(false);
        }

    }

    public boolean isConnected() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        return ((info != null) && (info.isConnected()));
    }
}
