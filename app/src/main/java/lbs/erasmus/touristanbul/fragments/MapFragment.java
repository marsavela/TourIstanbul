package lbs.erasmus.touristanbul.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
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

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.Downloader;
import com.graphhopper.util.Helper;
import com.graphhopper.util.ProgressListener;
import com.graphhopper.util.StopWatch;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.MapViewPosition;
import org.mapsforge.android.maps.overlay.ListOverlay;
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
import lbs.erasmus.touristanbul.maps.AndroidHelper;
import lbs.erasmus.touristanbul.maps.GHAsyncTask;
import lbs.erasmus.touristanbul.maps.Utils;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class MapFragment extends Fragment {

    private static final String DOWNLOAD_URL = "https://googledrive.com/host/0B-6BYv2-6JpCamxRMG9NLTZmd1E/istanbul.ghz";
    //private static final String MAP_URL = "https://googledrive.com/host/0B-6BYv2-6JpCamxRMG9NLTZmd1E/istanbul.map";
    private static final String APP_FOLDER = "touristanbul";
    private static final String AREA_NAME ="istanbul";
    private static final String MAP_FILENAME = "istanbul.map";
    private static final String ZIP_FILENAME = "istanbul.ghz";

    private static final String INTENT_BROADCAST_POSITION = "broadcast_gps";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ZOOM_LEVEL = "zoomLevel";
    private static final String PREFERENCES_FILE = "MapActivity";

    private static final int MIN_OBJECT_ZOOM = 18;


    private FileMapDownloader mFileMapDownloader;

    private MapView mMapView;
    private ListOverlay pathOverlay;
    private MyLocationOverlay mMyLocationOverlay;

    private GraphHopperAPI mHopper;
    private volatile boolean shortestPathRunning = false;
    private volatile boolean prepareInProgress = true;

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
                //showCurrentPosition();
                calculateRoute(new GeoPoint(40.983934, 28.820443), new GeoPoint(40.973210, 29.151750));
            }
        });

        // Check if the maps file exists, if not shows a download dialog
        mFileMapDownloader = null;
        File mapFile = getMapFile();
        if (!mapFile.exists()) {
            if (Utils.checkWifiConection(getActivity())) {
                mFileMapDownloader = new FileMapDownloader();
                mFileMapDownloader.execute();
            } else {
                Utils.showSimpleDialog(getActivity(),
                        R.string.mapfragment_advice_title,
                        R.string.mapfragment_advice_text);
            }
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
        return new File(getMapFileName());
    }

    private String getMapFileName() {
        //TODO: download istanbul map to a SD folder
        return getLocalFolder() + "-gh/" + MAP_FILENAME;
    }

    private String getLocalFolder() {
        //TODO: download istanbul map to a SD folder
        return Environment.getExternalStorageDirectory() + "/" + APP_FOLDER + "/" + AREA_NAME;
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

        pathOverlay = new ListOverlay();
        mapView.getOverlays().add(pathOverlay);

        loadGraphStorage();

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

    private void calculateRoute(GeoPoint startPoint, GeoPoint endPoint) {
        // Clear map marks
        pathOverlay.getOverlayItems().clear();

        calcPath(startPoint, endPoint);

        Marker marker = Utils.createMarker(getActivity(), startPoint, R.drawable.flag_green);
        pathOverlay.getOverlayItems().add(marker);

        marker = Utils.createMarker(getActivity(), endPoint, R.drawable.flag_red);
        pathOverlay.getOverlayItems().add(marker);
        mMapView.redraw();

    }

    private void loadGraphStorage() {
        new GHAsyncTask<Void, Void, Path>()
        {
            protected Path saveDoInBackground( Void... v ) throws Exception
            {
                GraphHopper tmpHopp = new GraphHopper().forMobile();
                tmpHopp.setCHShortcuts("fastest");
                tmpHopp.load(getLocalFolder()+"-gh");
                //tmpHopp.setEncodingManager(new EncodingManager("car,bike,foot"));
                mHopper = tmpHopp;
                return null;
            }

            protected void onPostExecute( Path o )
            {
                if (hasError()) {
                    Log.e(getClass().getName(),"An error happend while creating graph:" + getErrorMessage());
                } else {
                    Log.v(getClass().getName(), "Finished loading graph. Touch to route.");
                }

                prepareInProgress = false;
            }
        }.execute();
    }

    private void calcPath( final GeoPoint fromPoint, final GeoPoint toPoint) {
        new AsyncTask<Void, Void, GHResponse>() {
            float time;

            protected GHResponse doInBackground( Void... v ) {
                double fromLat = fromPoint.latitude;
                double fromLon = fromPoint.longitude;
                double toLat = toPoint.latitude;
                double toLon = toPoint.longitude;

                StopWatch sw = new StopWatch().start();
                GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon);
                req.setVehicle("car");
                req.setAlgorithm("dijkstrabi");
                req.putHint("instructions", false);

                GHResponse resp = mHopper.route(req);
                time = sw.stop().getSeconds();
                return resp;
            }

            protected void onPostExecute( GHResponse resp ) {
                if (!resp.hasErrors()) {
                    Utils.showRouteInfo(getActivity(), resp, time);
                    pathOverlay.getOverlayItems().add(Utils.createPolyline(resp));
                    mMapView.redraw();
                } else {
                    Utils.showSimpleDialog(getActivity(),
                            R.string.mapfragment_route_error_title,
                            R.string.mapfragment_route_error_text);
                    Log.e(getClass().getName(),resp.toString());
                }
                shortestPathRunning = false;
            }
        }.execute();
    }

    /**
     * Create a thread that downloads the map file and shows a dialog with the progress
     */
    private class FileMapDownloader extends AsyncTask<Void,Integer,File> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setMax(0);
            progressDialog.setTitle(getResources().getString(R.string.mapfragment_downloading_title));
            progressDialog.setMessage(getResources().getString(R.string.mapfragment_downloading_text_download));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

//            int orientation = getActivity().getRequestedOrientation();
//            getActivity().setRequestedOrientation(orientation);
        }

        @Override
        protected File doInBackground(Void... voids) {
            File outputFile = null;
            Log.d(getClass().getName(),"Starting download");
            try {
                URL url = new URL(DOWNLOAD_URL);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();

                String localfolder = getLocalFolder() + "-gh";

                // Check if the folder exists
                File path = new File(localfolder);
                if (!path.exists()) path.mkdirs();

                outputFile = new File(path,ZIP_FILENAME);
                if (outputFile.exists()) outputFile.delete();

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

                Log.d(getClass().getName(),"File downloaded");

                Utils.unzipFile(localfolder,ZIP_FILENAME);

                Log.d(getClass().getName(),"File uncompressed");

            } catch (Exception e) {
                Log.e(getClass().getName(), "Error while downloading: " + e);
            }
            return outputFile;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
            if(values[0]==100) {
                progressDialog.setMessage(getResources().getString(R.string.mapfragment_downloading_text_decompressing));
            }
        }

        @Override
        protected void onPostExecute(File outputFile) {
            super.onPostExecute(outputFile);
            progressDialog.dismiss();
            if(!isCancelled()){
                initializeMapView(mMapView);
            }

            if (outputFile.exists()) outputFile.delete();

            Log.d(getClass().getName(),"Changin orientation");
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            Log.d(getClass().getName(),"End");
        }
    }
}