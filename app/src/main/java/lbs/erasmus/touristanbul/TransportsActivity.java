package lbs.erasmus.touristanbul;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TabHost;

/**
 * Created by HP on 03/04/2014.
 */
public class TransportsActivity extends Activity {

    private WebView tabBus = null;
    private WebView tabTram = null;
    private WebView tabFerry = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transports);

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

        WebView tabBus = (WebView)findViewById(R.id.busImage);
        WebView tabTram = (WebView)findViewById(R.id.tramImage);
        WebView tabFerry = (WebView)findViewById(R.id.ferryImage);

        tabBus.loadUrl("file:///android_asset/bus.html");
        tabBus.getSettings().setBuiltInZoomControls(true);
        tabBus.getSettings().setDisplayZoomControls(true);

        tabTram.loadUrl("file:///android_asset/tram.html");
        tabTram.getSettings().setBuiltInZoomControls(true);
        tabTram.getSettings().setDisplayZoomControls(true);

        tabFerry.loadUrl("file:///android_asset/ferry.html");
        tabFerry.getSettings().setBuiltInZoomControls(true);
        tabFerry.getSettings().setDisplayZoomControls(true);

    }
}
