package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by HP on 03/04/2014.
 */
public class TransportsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transports);

        // Control TabHost
        TabHost host = (TabHost) findViewById(R.id.tabhost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("BUS");
        spec.setContent(R.id.bus);
        spec.setIndicator(getString(R.string.bus));
        host.addTab(spec);

        spec = host.newTabSpec("TRAM");
        spec.setContent(R.id.tram);
        spec.setIndicator(getString(R.string.tram));
        host.addTab(spec);

        spec = host.newTabSpec("FERRY");
        spec.setContent(R.id.ferry);
        spec.setIndicator(getString(R.string.ferry));
        host.addTab(spec);

        host.setCurrentTabByTag("BUS");

    }
}
