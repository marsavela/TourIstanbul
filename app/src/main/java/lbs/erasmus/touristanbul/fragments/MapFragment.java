package lbs.erasmus.touristanbul.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.GraphHopperAPI;
import com.graphhopper.routing.Path;
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
import java.util.ArrayList;
import java.util.List;

import lbs.erasmus.touristanbul.DAOAttractions;
import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.SettingsManager;
import lbs.erasmus.touristanbul.domain.Attraction;
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

    public static final String ZIP_FILENAME = "istanbul.ghz";

    private static final String INTENT_BROADCAST_POSITION = "broadcast_gps";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_ZOOM_LEVEL = "zoomLevel";
    private static final String PREFERENCES_FILE = "MapActivity";

    private static final int REFRESH_DISTANCE = 5000;

    private FileMapDownloader mFileMapDownloader;

    private MapView mMapView;
    private ListOverlay mPathOverlay;
    private ToggleButton mRouteButton;
    private ToggleButton mSnapToLocation;
    //private UserLocation mLocationOverlay;
    private MyLocationOverlay mLocationOverlay;

    private GraphHopperAPI mHopper;
    private volatile boolean prepareInProgress = true;

    private MapFragmentCommunication mCallback;
    List<Attraction> mAttractionList;

    private GHResponse mRouteResponse;
    private GeoPoint mStartPoint;
    private GeoPoint mEndPoint;

    /**
     * Interface for communicate activity with map fragment
     */
    public interface MapFragmentCommunication {
        public List<Attraction> getAttractionList();
        public void setUserLocation(Location location);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (MapFragmentCommunication) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Adding the map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int eventAction = motionEvent.getAction();
                if (eventAction == MotionEvent.ACTION_UP) {
                    //showAttractions();
                }
                return false;
            }
        });

        // Add route button
        mRouteButton = (ToggleButton) rootView.findViewById(R.id.mapfragment_button_route);
        mRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!prepareInProgress) {
                    //calculateRoute(new GeoPoint(40.983934, 28.820443), new GeoPoint(40.973210, 29.151750));
                    if (mRouteButton.isChecked()) {
                        showRouteDialog();
                    } else {
                        mRouteResponse = null;
                        mStartPoint = null;
                        mEndPoint = null;
                        showAttractions();
                    }
                } else {
                    Toast.makeText(getActivity(),R.string.mapfragment_route_graph_notloaded,Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add my location button
        mSnapToLocation = (ToggleButton) rootView.findViewById(R.id.mapfragment_button_location);
        mSnapToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSnapToLocation.isChecked()) {
                    showCurrentPosition();
                } else {
                    mLocationOverlay.disableMyLocation();
                    mCallback.setUserLocation(null);
                }
            }
        });

        /* Code to download map from server
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
        */

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
            initializeMapView();
            showAttractions();
        }

        // Get attractions from database and filter
        DAOAttractions daoAttractions = new DAOAttractions(this.getActivity());
        SettingsManager settingsManager = new SettingsManager(getActivity());

        mAttractionList = new ArrayList<Attraction>();
        mAttractionList.clear();
        for (Attraction a : daoAttractions.getAttractions()) {
            //if (interests.contains(a.getCategory()) || interests.contains(a.getInterest()))
            if (settingsManager.checkAttractionCategory(a) && settingsManager.checkAttractionInterest(a))
                mAttractionList.add(a);
        }

        mAttractionList = daoAttractions.getAttractions();
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

    public static File getMapFile() {
        return new File(getMapFileName());
    }

    private static String getMapFileName() {
        //TODO: download istanbul map to a SD folder
        return getLocalFolder() + "-gh/" + MAP_FILENAME;
    }

    public static String getLocalFolder() {
        //TODO: download istanbul map to a SD folder
        return Environment.getExternalStorageDirectory() + "/" + APP_FOLDER + "/" + AREA_NAME;
    }

    /**
     * Set initial map view parameters
     */
    public void initializeMapView() {
        mMapView.setClickable(true);
        mMapView.getMapScaleBar().setShowMapScaleBar(true);
        mMapView.setBuiltInZoomControls(false);
        //mapView.getFpsCounter().setFpsCounter(true);
        mMapView.setMapFile(getMapFile());

        mPathOverlay = new ListOverlay();
        mMapView.getOverlays().add(mPathOverlay);

        loadGraphStorage();

        restoreMapView(mMapView);
    }

    /**
     * Initializes the map view position
     *
     * @param mapViewPosition the map view position to be set
     * @return the MapViewPosition set
     */
    protected MapViewPosition initializePosition(MapViewPosition mapViewPosition) {
        mapViewPosition.setCenter(getInitialPosition());
        return mapViewPosition;
    }

    public void centerMap() {
        mMapView.getMapViewPosition().setCenter(getInitialPosition());
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
        //mLocationOverlay = new UserLocation(getActivity(), mMapView, drawable);
        mLocationOverlay = new MyLocationOverlay(getActivity(), mMapView, drawable);
        mLocationOverlay.enableMyLocation(true);
    }

    /**
     * Called from MainActivity to put the new attractions on the map
     */
    public void showAttractions() {
        //mAttractionList = mCallback.getAttractionList();
        SettingsManager settingsManager = new SettingsManager(getActivity().getApplicationContext());

        mPathOverlay.getOverlayItems().clear();

        if (mAttractionList != null) {
            // Save current location
            Location currentLocation = new Location("CurrentLocation");
            currentLocation.setLatitude( mMapView.getMapViewPosition().getCenter().latitude);
            currentLocation.setLongitude(mMapView.getMapViewPosition().getCenter().longitude);

            mPathOverlay.getOverlayItems().clear();
            Location location;
            Marker marker;
            GeoPoint position;
            int zoomLevel = mMapView.getMapViewPosition().getZoomLevel();
            //float distance;
            // Check all attractions on list
            //if (zoomLevel >= MIN_OBJECT_ZOOM) {
                for (Attraction attraction : mAttractionList) {
                    if (settingsManager.checkAttractionCategory(attraction) && settingsManager.checkAttractionInterest(attraction)) {
                        location = attraction.getLocation();
                        //distance = location.distanceTo(currentLocation);

                        position = new GeoPoint(location.getLatitude(), location.getLongitude());
                        //marker = Utils.createMarker(getActivity(), position, Utils.getCategoryDrawableId(attraction.getCategory()));
                        marker = Utils.createTextMarker(getActivity(), attraction.getTitle(), position, Utils.getCategoryDrawableId(attraction.getCategory()));
                        mPathOverlay.getOverlayItems().add(marker);
                    }
                }
            //}
        }

        if (mRouteResponse != null) {
            mPathOverlay.getOverlayItems().add(Utils.createPolyline(mRouteResponse));
            Marker marker = Utils.createMarker(getActivity(), mStartPoint, R.drawable.flag_green);
            mPathOverlay.getOverlayItems().add(marker);
            marker = Utils.createMarker(getActivity(), mEndPoint, R.drawable.flag_red);
            mPathOverlay.getOverlayItems().add(marker);
        }

        mMapView.redraw();
    }

    private void calculateRoute(GeoPoint startPoint, GeoPoint endPoint) {
        // Clear map marks
        mPathOverlay.getOverlayItems().clear();

        calcPath(startPoint, endPoint);
        mStartPoint = startPoint;
        mEndPoint = endPoint;

        Marker marker = Utils.createMarker(getActivity(), startPoint, R.drawable.flag_green);
        mPathOverlay.getOverlayItems().add(marker);
        marker = Utils.createMarker(getActivity(), endPoint, R.drawable.flag_red);
        mPathOverlay.getOverlayItems().add(marker);
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
                    Utils.showRouteInfo(getActivity(), resp, time);;
                    mRouteResponse = resp;
                    showAttractions();
                } else {
                    mRouteButton.setChecked(false);
                    mRouteResponse = null;
                    mStartPoint = null;
                    mEndPoint = null;
                    Utils.showSimpleDialog(getActivity(),
                            R.string.mapfragment_route_error_title,
                            R.string.mapfragment_route_error_text);
                    Log.e(getClass().getName(), resp.toString());
                }
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
                initializeMapView();
            }

            if (outputFile.exists()) outputFile.delete();

            Log.d(getClass().getName(),"Changin orientation");
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            Log.d(getClass().getName(),"End");
        }
    }

    public void showRouteDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder alertDialog;
        alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.mapfragment_route_dialog_title);
        View view = inflater.inflate(R.layout.fragment_map_dialog_route, null);
        alertDialog.setView(view);

        final Spinner from = (Spinner) view.findViewById(R.id.mapfragment_route_spinner_from);
        final Spinner to = (Spinner) view.findViewById(R.id.mapfragment_route_spinner_to);

        ArrayAdapter<Attraction> attractionAdapter = new ArrayAdapter<Attraction>(getActivity(),android.R.layout.simple_spinner_item, mAttractionList);
        attractionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        from.setAdapter(attractionAdapter);
        to.setAdapter(attractionAdapter);

        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Location locFrom = ((Attraction) from.getSelectedItem()).getLocation();
                Location locTo = ((Attraction) to.getSelectedItem()).getLocation();

                GeoPoint fromGeoPoint = new GeoPoint(locFrom.getLatitude(),locFrom.getLongitude());
                GeoPoint toGeoPoint = new GeoPoint(locTo.getLatitude(), locTo.getLongitude());

                calculateRoute(fromGeoPoint, toGeoPoint);
            }
        });

        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private class UserLocation extends MyLocationOverlay {

        public UserLocation(Context context, MapView mapView, Drawable drawable) {
            super(context, mapView, drawable);
        }

        @Override
        public void onLocationChanged(Location location) {
            mCallback.setUserLocation(location);
            super.onLocationChanged(location);
        }
    }
}