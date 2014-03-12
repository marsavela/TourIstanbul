package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.MapTileProviderBasic;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.TilesOverlay;

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

        MapView mapView = (MapView) rootView.findViewById(R.id.mapView);

        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(14);
        mapView.setMaxZoomLevel(18);
        mapView.setMinZoomLevel(14);
        // Location: Istanbul
        mapView.getController().setCenter(new GeoPoint(41.0096334, 28.9651646));

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

}