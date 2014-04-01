package lbs.erasmus.touristanbul.fragments;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.android.AndroidPreferences;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.layer.MyLocationOverlay;

import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.common.PreferencesFacade;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import lbs.erasmus.touristanbul.R;
import lbs.erasmus.touristanbul.maps.Utils;

/**
 * Created by SergiuDaniel on 9/03/14.
 */
public class MapFragment extends Fragment {

    private final static String MAP_URL = "https://googledrive.com/host/0B-6BYv2-6JpCamxRMG9NLTZmd1E/istanbul.map";
    private final static String MAP_FOLDER = "touristanbul/";
    private final static String FILENAME = "istanbul.map";

    private MapView mMapView;
    protected PreferencesFacade mPreferencesFacade;
    protected TileCache mTileCache;

    private MyLocationOverlay mMyLocationOverlay;

    private FileMapDownloader mFileMapDownloader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        // Adding the map
        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        // Load last map position
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getPersistableId(), Context.MODE_PRIVATE);
        mPreferencesFacade = new AndroidPreferences(sharedPreferences);

        // Add buttons functionality

        Button buttonIn = (Button) rootView.findViewById(R.id.mapfragment_button_zoomIn);
        buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows user current location
                mMapView.getModel().mapViewPosition.zoomIn();
            }
        });

        Button buttonOut = (Button) rootView.findViewById(R.id.mapfragment_button_zoomOut);
        buttonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows user current location
                mMapView.getModel().mapViewPosition.zoomOut();
            }
        });

        Button buttonLocation = (Button) rootView.findViewById(R.id.mapfragment_button_location);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //shows user current location
                showCurrentPosition();
            }
        });

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

            if (mMapView != null) {
                mMapView.destroy();
            }
        }

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFileMapDownloader == null || mFileMapDownloader.getStatus() == AsyncTask.Status.FINISHED) {
            initialize();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save current map position
        mMapView.getModel().save(mPreferencesFacade);
        mPreferencesFacade.save();

        if (mMapView != null) {
            mMapView.destroy();
        }

        if (mFileMapDownloader != null && mFileMapDownloader.getStatus() == AsyncTask.Status.RUNNING) {
            mFileMapDownloader.cancel(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
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
        return MAP_FOLDER + FILENAME;
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
        addLayers(mMapView.getLayerManager(), mTileCache, mapViewPosition); // Offline map

        /*
        TileDownloadLayer downloadLayer = new TileDownloadLayer(mTileCache, mapViewPosition, OpenStreetMapMapnik.INSTANCE,
                AndroidGraphicFactory.INSTANCE);
        downloadLayer.start();
        mMapView.getLayerManager().getLayers().add(downloadLayer);
        */
    }

    /**
     * Initializes the map view
     *
     * @param mapView
     */
    protected void initializeMapView(MapView mapView, PreferencesFacade preferences) {
        mapView.getModel().init(preferences);
        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);

        //mMapView.getLayerManager().getLayers().add(new TileGridLayer(AndroidGraphicFactory.INSTANCE));
        //mMapView.getLayerManager().getLayers().add(new TileCoordinatesLayer(AndroidGraphicFactory.INSTANCE));
        mMapView.getFpsCounter().setVisible(true);


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

    class FileMapDownloader extends AsyncTask<Void,Integer,Void> {

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
                initialize();
            }
        }
    }
}