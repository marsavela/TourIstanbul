package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.android.AndroidPreferences;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.layer.MyLocationOverlay;

import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.download.TileDownloadLayer;
import org.mapsforge.map.layer.download.tilesource.OpenStreetMapMapnik;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.common.PreferencesFacade;

import java.io.File;

import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.maps.Utils;
//hola
/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class MapFragment extends Fragment {

    private MapView mMapView;
    protected PreferencesFacade mPreferencesFacade;
    protected TileCache mTileCache;

    private MyLocationOverlay mMyLocationOverlay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Adding the map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        // Load last map position
        //SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getPersistableId(), Context.MODE_PRIVATE);
        //mPreferencesFacade = new AndroidPreferences(sharedPreferences);

        initialize();

        Button button = (Button) rootView.findViewById(R.id.mapfragment_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows user current location
                showCurrentPosition();
            }
        });

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
    public void onPause() {
        super.onPause();
        // Save current map position
        //mMapView.getModel().save(mPreferencesFacade);
        //mPreferencesFacade.save();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_example) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void addLayers(LayerManager layerManager, TileCache tileCache, MapViewPosition mapViewPosition) {
        layerManager.getLayers().add(Utils.createTileRendererLayer(tileCache, mapViewPosition, getMapFile()));
    }

    protected TileCache createTileCache() {
        return Utils.createExternalStorageTileCache(getActivity(), getPersistableId());
    }

    /**
     * @return center position of Istanbul (41.0096334, 28.9651646)
     */
    protected MapPosition getInitialPosition() {
        return new MapPosition(new LatLong(41.0096334, 28.9651646), (byte) 12);
    }

    /**
     * @return a map file
     */
    protected File getMapFile() {
        return new File(Environment.getExternalStorageDirectory(), getMapFileName());
    }

    /**
     * @return the map file name to be used
     */
    protected String getMapFileName() {
        //TODO: download istanbul map to a SD folder
        return "valenciamaps/spain.map";
    }

    /**
     * @return the id that is used to save this mapview
     */
    protected String getPersistableId() {
        return this.getClass().getSimpleName();
    }

    /**
     * Initializes the map view
     */
    protected void initialize() {
        initializeMapView(mMapView, mPreferencesFacade);
        mTileCache = createTileCache();
        MapViewPosition mapViewPosition = initializePosition(mMapView.getModel().mapViewPosition);

        //TODO: Change online maps for offline maps, add a download map dialog
        //addLayers(mMapView.getLayerManager(), mTileCache, mapViewPosition); // Offline map

        TileDownloadLayer downloadLayer = new TileDownloadLayer(mTileCache, mapViewPosition, OpenStreetMapMapnik.INSTANCE,
                AndroidGraphicFactory.INSTANCE);
        downloadLayer.start();
        mMapView.getLayerManager().getLayers().add(downloadLayer);

    }

    /**
     * Initializes the map view
     *
     * @param mapView
     */
    protected void initializeMapView(MapView mapView, PreferencesFacade preferences) {
        //mapView.getModel().init(preferences);
        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
    }

    /**
     * Initializes the map view position
     *
     * @param mapViewPosition
     *            the map view position to be set
     * @return the mapviewposition set
     */
    protected MapViewPosition initializePosition(MapViewPosition mapViewPosition) {
        LatLong center = mapViewPosition.getCenter();
        if (center.equals(new LatLong(0, 0))) {
            mapViewPosition.setMapPosition(getInitialPosition());
        }
        return mapViewPosition;
    }

    /**
     * Shows users location in the map
     */
    private void showCurrentPosition() {
        // a marker to show at the position
        Drawable drawable = getResources().getDrawable(R.drawable.ic_drawer);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        MapViewPosition mapViewPosition = mMapView.getModel().mapViewPosition;
        LayerManager layerManager = mMapView.getLayerManager();

        // create the overlay and tell it to follow the location
        mMyLocationOverlay = new MyLocationOverlay(getActivity(), mapViewPosition, bitmap);
        mMyLocationOverlay.setSnapToLocationEnabled(true);

        layerManager.getLayers().add(mMyLocationOverlay);
        mMyLocationOverlay.enableMyLocation(true);

    }

}