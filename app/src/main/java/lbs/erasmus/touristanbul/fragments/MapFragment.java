package lbs.erasmus.touristanbul.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.graphhopper.GraphHopper;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.overlay.Marker;
import org.mapsforge.android.maps.overlay.MyLocationOverlay;
import org.mapsforge.core.model.GeoPoint;
import org.mapsforge.core.model.MapPosition;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.domain.Attraction;
import lbs.erasmus.touristanbul.maps.Utils;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class MapFragment extends Fragment {

    private static final String MAP_URL = "https://googledrive.com/host/0B-6BYv2-6JpCamxRMG9NLTZmd1E/istanbul.map";
    private static final String MAP_FOLDER = "touristanbul/";
    private static final String FILENAME = "istanbul.map";

    private static final String INTENT_BROADCAST_POSITION = "broadcast_gps";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ZOOM_LEVEL = "zoomLevel";
    private static final String PREFERENCES_FILE = "MapActivity";

    private static final int MIN_OBJECT_ZOOM = 18;


    private FileMapDownloader mFileMapDownloader;

    private MapView mMapView;
    private MyLocationOverlay mMyLocationOverlay;
    private ToggleButton mSnapToLocation;

    List<Attraction> attractionList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Adding the map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        // Add my location button
        Button buttonLocation = (Button) rootView.findViewById(R.id.mapfragment_button_location);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows user current location
                showCurrentPosition();
            }
        });

        // Check if the maps file exists, if not shows a download dialog
        mFileMapDownloader = null;
        File mapFile = getMapFile();
        if (!mapFile.exists() && Utils.checkWifiConection(getActivity())) {
            mFileMapDownloader = new FileMapDownloader();
            mFileMapDownloader.execute();
        } else if (!Utils.checkWifiConection(getActivity())) {
            AlertDialog.Builder alertDialog;
            alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle(getResources().getString(R.string.mapfragment_advice_title));
            alertDialog.setMessage(getResources().getString(R.string.mapfragment_advice_text));
            alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        }


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
        // if there is not downloading the file,
        File mapFile = getMapFile();
        if (mapFile.exists() && (mFileMapDownloader == null
                || mFileMapDownloader.getStatus() == AsyncTask.Status.FINISHED)) {
            initializeMapView(mMapView);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
        // Save last map position
        saveMapView(mMapView);
        // Calls download thread if it is running
        if (mFileMapDownloader != null && mFileMapDownloader.getStatus() == AsyncTask.Status.RUNNING) {
            mFileMapDownloader.cancel(true);
        }
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.destroy();
        }
    }

    /**
     * @return center position of Istanbul (41.0096334, 28.9651646)
     */
    private GeoPoint getInitialPosition() {
        return new GeoPoint(41.0096334, 28.9651646);
    }

    private File getMapFile() {
        return new File(Environment.getExternalStorageDirectory(), getMapFileName());
    }

    private String getMapFileName() {
        //TODO: download istanbul map to a SD folder
        return MAP_FOLDER + FILENAME;
    }

    /**
     * Set initial map view parameters
     *
     * @param mapView
     */
    private void initializeMapView(MapView mapView) {
        mapView.setClickable(true);
        mapView.getMapScaleBar().setShowMapScaleBar(true);
        mapView.setBuiltInZoomControls(false);
        //mapView.getFpsCounter().setFpsCounter(true);

        mapView.setMapFile(getMapFile());

        restoreMapView(mapView);
    }

    /**
     * Initializes the map view position
     *
     * @param mapViewPosition the map view position to be set
     * @return the MapViewPosition set
     */
    protected MapViewPosition initializePosition(MapViewPosition mapViewPosition) {
        GeoPoint center = mapViewPosition.getCenter();
        if (center.equals(new GeoPoint(0, 0))) {
            mapViewPosition.setCenter(getInitialPosition());
        }
        return mapViewPosition;
    }

    /**
     * Save the mapview position and zoom
     *
     * @param mapView to be saved
     */
    private void saveMapView(MapView mapView) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE).edit();
        editor.clear();
        MapPosition mapPosition = mMapView.getMapViewPosition().getMapPosition();
        GeoPoint geoPoint = mapPosition.geoPoint;
        editor.putFloat(KEY_LATITUDE, (float) geoPoint.latitude);
        editor.putFloat(KEY_LONGITUDE, (float) geoPoint.longitude);
        editor.putInt(KEY_ZOOM_LEVEL, mapPosition.zoomLevel);
        editor.commit();
    }

    /**
     * Restore the last mapview position
     *
     * @param mapView to be restored
     */
    private void restoreMapView(MapView mapView) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(KEY_LATITUDE) && sharedPreferences.contains(KEY_LONGITUDE)
                && sharedPreferences.contains(KEY_ZOOM_LEVEL)) {

            // get and set the map position and zoom level
            float latitude = sharedPreferences.getFloat(KEY_LATITUDE, 0);
            float longitude = sharedPreferences.getFloat(KEY_LONGITUDE, 0);
            int zoomLevel = sharedPreferences.getInt(KEY_ZOOM_LEVEL, -1);

            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            MapPosition mapPosition = new MapPosition(geoPoint, (byte) zoomLevel);
            mapView.getMapViewPosition().setMapPosition(mapPosition);
        } else {
            initializePosition(mapView.getMapViewPosition());
        }
    }

    /**
     * Shows user location in the map
     */
    private void showCurrentPosition() {
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_menu_compass);
        drawable = Marker.boundCenter(drawable);
        mMyLocationOverlay = new MyLocationOverlay(getActivity(), mMapView, drawable);
        mMyLocationOverlay.enableMyLocation(true);
    }

    /**
     * Called from MainActivity to put the new attractions on the map
     */
    public void updateAttractionsList(List<Attraction> attractionList) {

    }


    /**
     * Create a thread that downloads the map file and shows a dialog with the progress
     */
    private class FileMapDownloader extends AsyncTask<Void,Integer,Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setMax(0);
            progressDialog.setTitle(getResources().getString(R.string.mapfragment_downloading_title));
            progressDialog.setMessage(getResources().getString(R.string.mapfragment_downloading_text));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL(MAP_URL);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();

                // Check if the folder exists
                File path = new File(Environment.getExternalStorageDirectory() + "/" + MAP_FOLDER);
                if (!path.exists()) path.mkdirs();
                File outputFile = new File(path,FILENAME);

                InputStream inputStream = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(outputFile);

                byte buffer[] = new byte[1024];
                long total = 0;
                int count = 0;
                while ((count = inputStream.read(buffer)) != -1) {
                    total += count;
                    // Set progress
                    publishProgress((int) ((total*100)/fileLength));
                    output.write(buffer, 0, count);
                }
                output.flush();
                output.close();
                inputStream.close();

            } catch (Exception e) {
                Log.e(getClass().getName(), "Error while downloading");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(!isCancelled()){
                initializeMapView(mMapView);
            }
        }
    }
}