package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

import java.util.ArrayList;

import lbs.erasmus.touristanbul.R;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class MapFragment extends Fragment {

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Adding the map
        MapView mapView = (MapView) rootView.findViewById(R.id.mapView);

        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(11);
        mapView.setMaxZoomLevel(18);
        mapView.setMinZoomLevel(11);
        // Location: Istanbul
        mapView.getController().setCenter(new GeoPoint(41.0096334, 28.9651646));
        //mapView.setUseDataConnection(false); //keeps the mapView from loading online tiles using network connection.

        // Add tiles layer with custom tile source
        final MapTileProviderBasic tileProvider = new MapTileProviderBasic(getActivity().getApplicationContext());
        // Mapnik map tiles
        final ITileSource tileSource = new XYTileSource("Mapnik", ResourceProxy.string.mapnik, 1, 18, 256, ".png", new String[] { "http://tile.openstreetmap.org/"});
        // MapQuest map tiles
        //final ITileSource tileSource = new XYTileSource("MapQuest", ResourceProxy.string.mapquest_osm, 1, 21, 256, ".png", new String[] {"http://otile1.mqcdn.com/tiles/1.0.0/osm/","http://otile2.mqcdn.com/tiles/1.0.0/osm/"});
        tileProvider.setTileSource(tileSource);
        final TilesOverlay tilesOverlay = new TilesOverlay(tileProvider, getActivity().getBaseContext());
        tilesOverlay.setLoadingBackgroundColor(Color.TRANSPARENT);
        mapView.getOverlays().add(tilesOverlay);

        // Adding a mark
        Marker startMarker = new Marker(mapView);
        startMarker.setPosition(new GeoPoint(40.97244, 29.15236));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_compass));
        startMarker.setTitle("Yeditepe University");
        startMarker.setDraggable(true);
        mapView.getOverlays().add(startMarker);

        // Adding a route
        RoadManager roadManager = new OSRMRoadManager();
        //RoadManager roadManager = new MapQuestRoadManager("YOUR_API_KEY");
        //roadManager.addRequestOption("routeType=bicycle");
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(new GeoPoint(40.98196, 28.82070));
        waypoints.add(new GeoPoint(40.97244, 29.15236)); //end point
        Road road = roadManager.getRoad(waypoints);
        // Build a Polyline with the route shape
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road, getActivity().getApplicationContext());
        // Add this Polyline to the overlays
        mapView.getOverlays().add(roadOverlay);

        // Refresh map
        mapView.invalidate();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_example) {
            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}