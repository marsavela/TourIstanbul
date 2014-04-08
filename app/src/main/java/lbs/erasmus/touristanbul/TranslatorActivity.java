package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.os.Bundle;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;


/**
 * Created by HP on 03/04/2014.
 */

public class TranslatorActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
    }

    public static void main(String[] args) throws Exception {
        //CLIENT_ID_HERE
        Translate.setClientId("erasmus-lbs-spain");
        //CLIENT_SECRET_HERE
        Translate.setClientSecret("eVRrtV6YIJNNqVxdbyobEPZQJI+dUoXd9O6C56TU2Ok=");

        // Translate an english string to turkish
        String englishString = "Hello World!";
        String turkishTranslation = Translate.execute(englishString, Language.TURKISH);

        System.out.println("Original english phrase: " + englishString);
        System.out.println("Translated turkish phrase: " + turkishTranslation);
    /*
    OUTPUT Example:
    Original english phrase: Hello World!
    Translated spanish phrase: dünya merhaba!
    */
    }
}
