package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

import java.util.Locale;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * Created by HP on 03/04/2014.
 */

public class TranslatorActivity extends Activity implements OnInitListener {

    private TextToSpeech mTts;
    private Spinner translatedFrom;
    private String selected;
    private final static int MY_DATA_CHECK_CODE = 0;

/*    public void onSaveInstanceState (Bundle SavedInstanceState){
        super .onSaveInstanceState(SavedInstanceState); {
            String savedText = ;
            savedInstanceState.putString("Key", savedText);
        }
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        //LanguageFrom Spinner Adapter
        translatedFrom = (Spinner) findViewById(R.id.languageFrom);
        ArrayAdapter<CharSequence> adapterLanguageFrom = ArrayAdapter.createFromResource(getApplicationContext(), R.array.supported_languages,
                R.layout.spinner_item);

        adapterLanguageFrom.setDropDownViewResource(R.layout.spinner_dropdown_item);
        translatedFrom.setAdapter(adapterLanguageFrom);
        Log.v("VERBOSE", "LanguageFrom: " + translatedFrom.getCount());

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        ((ImageButton) findViewById(R.id.speechButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                readTranslation(((TextView) findViewById(R.id.outputText)).getText().toString());
            }
        });

        translatedFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                selected = translatedFrom.getSelectedItem().toString();
                System.out.println(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        ((Button) findViewById(R.id.translateButton)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                class bgStuff extends AsyncTask<Void, Void, Void>{
                    String translatedText = "";

                    @Override
                    protected Void doInBackground(Void... params) {
                        // TODO Auto-generated method stub
                        try {
                            String text = ((EditText) findViewById(R.id.inputText)).getText().toString();
                            translatedText = translate(text);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            translatedText = e.toString();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        // TODO Auto-generated method stub
                        ((TextView) findViewById(R.id.outputText)).setText(translatedText);
                        super.onPostExecute(result);
                    }

                }
                new bgStuff().execute();
            }
        });
    }

    public String translate(String text) throws Exception{
        //CLIENT_ID_HERE
        Translate.setClientId("erasmus-lbs-spain");
        //CLIENT_SECRET_HERE
        Translate.setClientSecret("eVRrtV6YIJNNqVxdbyobEPZQJI+dUoXd9O6C56TU2Ok=");
        String translatedText = Translate.execute(text, Language.TURKISH);
        return translatedText;
    }

    private void readTranslation(String text) {
        mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int status) {
        // TODO Auto-generated method stub
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                // readTranslation("Oh yeah. It works");
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case MY_DATA_CHECK_CODE :
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    mTts = new TextToSpeech(this, this);
                }
                else {
                    Intent installTTSIntent = new Intent();
                    installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installTTSIntent);
                }
                break;
        }
    }

    protected void onDestroy() {
        //Close the Text to Speech Library
        if(mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

}
