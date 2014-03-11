package lbs.erasmus.touristanbul.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutiteq.MapView;
import com.nutiteq.components.Components;
import com.nutiteq.log.Log;
import com.nutiteq.projections.EPSG3857;
import com.nutiteq.rasterdatasources.HTTPRasterDataSource;
import com.nutiteq.rasterdatasources.RasterDataSource;
import com.nutiteq.rasterlayers.RasterLayer;

import lbs.erasmus.touristanbul.R;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class MapFragment extends Fragment {

    private MapView mapView;

    public MapFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.mapView);

        // Restore map state during device rotation,
        // it is saved in onRetainNonConfigurationInstance() below
        Components retainObject = (Components) getActivity().getLastNonConfigurationInstance();
        if (retainObject != null) {
            // just restore configuration and update listener, skip other initializations
            mapView.setComponents(retainObject);
            return rootView;
        } else {
            // 2. create and set MapView components - mandatory
            mapView.setComponents(new Components());
        }

        // Define base layer. Here we use MapQuest open tiles which are free to use
        // Almost all online maps use EPSG3857 projection.
        // We use online data source for the tiles and the URL is given as template.
        RasterDataSource dataSource = new HTTPRasterDataSource(new EPSG3857(), 11, 22, "http://otile1.mqcdn.com/tiles/1.0.0/osm/{zoom}/{x}/{y}.png");
        RasterLayer mapLayer = new RasterLayer(dataSource, 0);
        mapView.getLayers().setBaseLayer(mapLayer);
        adjustMapDpi();

        // Show performance indicator
    //    mapView.getOptions().setFPSIndicator(true);

        // Increase raster tile download speed by doing 4 downloads in parallel
        mapView.getOptions().setRasterTaskPoolSize(4);

        // set initial map view camera - optional. "World view" is default
        // Location: Istanbul
        // NB! it must be in base layer projection (EPSG3857), so we convert it from lat and long
        mapView.setFocusPoint(mapView.getLayers().getBaseLayer().getProjection().fromWgs84(28.9651646, 41.0096334));
        // rotation - 0 = north-up
        mapView.setMapRotation(0f);
        // zoom - 0 = world, like on most web maps
        mapView.setZoom(11.0f);
        // tilt means perspective view. Default is 90 degrees for "normal" 2D map view, minimum allowed is 30 degrees.
        //mapView.setTilt(65.0f);



        // Activate some mapview options to make it smoother
        mapView.getOptions().setPreloading(true);
        mapView.getOptions().setSeamlessHorizontalPan(true);
        mapView.getOptions().setTileFading(true);
        mapView.getOptions().setKineticPanning(true);
        mapView.getOptions().setDoubleClickZoomIn(true);
        mapView.getOptions().setDualClickZoomOut(true);

        // configure texture caching
        mapView.getOptions().setTextureMemoryCacheSize(40 * 1024 * 1024);
        mapView.getOptions().setCompressedMemoryCacheSize(8 * 1024 * 1024);

        //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        //textView.setText("Map");
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Start the map - mandatory
        mapView.startMapping();
    }

    @Override
    public void onStop() {
        //Stop the map - mandatory to avoid problems with app restart
        mapView.stopMapping();
        super.onStop();
    }

    // adjust zooming to DPI, so texts on rasters will be not too small
    // useful for non-retina rasters, they would look like "digitally zoomed"
    private void adjustMapDpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float dpi = metrics.densityDpi;
        // following is equal to -log2(dpi / DEFAULT_DPI)
        float adjustment = (float) - (Math.log(dpi / DisplayMetrics.DENSITY_HIGH) / Math.log(2));
        Log.debug("adjust DPI = " + dpi + " as zoom adjustment = " + adjustment);
        mapView.getOptions().setTileZoomLevelBias(adjustment / 2.0f);
    }
}